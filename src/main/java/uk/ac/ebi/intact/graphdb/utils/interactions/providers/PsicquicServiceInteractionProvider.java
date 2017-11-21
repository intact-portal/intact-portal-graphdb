package uk.ac.ebi.intact.graphdb.utils.interactions.providers;

import au.com.bytecode.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.tab.model.Confidence;
import psidev.psi.mi.tab.model.CrossReference;
import uk.ac.ebi.enfin.mi.cluster.Encore2Binary;
import uk.ac.ebi.enfin.mi.cluster.EncoreInteraction;
import uk.ac.ebi.enfin.mi.cluster.score.InteractionClusterScore;
import uk.ac.ebi.intact.graphdb.error.GraphDbException;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.Protein;
import uk.ac.ebi.intact.graphdb.utils.InteractionProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static uk.ac.ebi.intact.graphdb.utils.interactions.providers.mitab.MiTabUtils.joinCrossReferencStyleCollection;


/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 11/11/2012
 * Time: 18:08
 */
@Component
public class PsicquicServiceInteractionProvider implements InteractionProvider {

    private static final Logger log = LoggerFactory.getLogger(PsicquicServiceInteractionProvider.class);

    private boolean enableRecording;
    private List<String> querySources;
    private String dirName;
    private String miqlQueryFilters;

    public PsicquicServiceInteractionProvider() {

        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHMM");
        dirName = df.format(new Date());
    }

    @Override
    public List<Interaction> getInteractions() throws GraphDbException {
        return null;
    }

    public List<Interaction> getInteractions(Set<Protein> proteins, String species) throws GraphDbException {

        List<Interaction> interactions = null;

        log.info("Number of proteins: " + proteins.size());
        String query;

        int i = 0;
        for (Protein protein : proteins) {

            query = "identifier:" + protein.getAccession() +
                    " taxidA:" + species +
                    " AND " +
                    " taxidB:" + species +
                    " AND " + miqlQueryFilters;
            log.info("Query " + query + " number: " + ++i);


            InteractionClusterScore interactionClusterScore = new InteractionClusterScore();
            /* sources to query */


            //iC.setQuerySourcesFromPsicquicRegistry();
            interactionClusterScore.setQuerySources(querySources);


			/* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
            interactionClusterScore.setMappingIdDbNames("uniprotkb");

			/* Query one or more IDs */
            interactionClusterScore.setMIQLQueries(Collections.singletonList(query));


            interactionClusterScore.setScoreName("proteInferactScore");

			/* Run clustering service */
            try {
                interactionClusterScore.runService();

                Map<Integer, EncoreInteraction> interactionMapping = interactionClusterScore.getInteractionMapping();
                String idDBNames = interactionClusterScore.getMappingIdDbNames();

				/* Cluster results */
                if (!interactionMapping.isEmpty()) {

                    interactions = getBasicInteraction(interactionMapping, idDBNames, Integer.getInteger(species));

                    if (isEnableRecording()) {
                        writeResults(species + "_" + protein.getAccession(), interactions);
                    }
                }
            } catch (Exception e) {
                throw new GraphDbException("Impossible retrieving the interactions in this moment.");
            }

        }
        return interactions;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Interaction> getInteractome(String species) throws GraphDbException {

        List<Interaction> interactions = null;
        String query = "taxidA:" + species + " AND " + "taxidB:" + species + " AND " + miqlQueryFilters;

        log.info("Interactome query " + query);


        InteractionClusterScore interactionClusterScore = new InteractionClusterScore();
        /* sources to query */


        //iC.setQuerySourcesFromPsicquicRegistry();
        interactionClusterScore.setQuerySources(querySources);


		/* Set priority for molecule accession mapping (find more database names in the MI Ontology, MI:0473) */
        interactionClusterScore.setMappingIdDbNames("uniprotkb");

		/* Query one or more IDs */
        interactionClusterScore.setMIQLQueries(Collections.singletonList(query));


        interactionClusterScore.setScoreName("proteInferactScore");

		/* Run clustering service */
        try {
            interactionClusterScore.runService();

            Map<Integer, EncoreInteraction> interactionMapping = interactionClusterScore.getInteractionMapping();
            String idDBNames = interactionClusterScore.getMappingIdDbNames();

			/* Cluster results */
            if (!interactionMapping.isEmpty()) {

                interactions = getBasicInteraction(interactionMapping, idDBNames, Integer.getInteger(species));

                if (isEnableRecording()) {
                    writeResults(species, interactions);
                }
            }
        } catch (Exception e) {
            throw new GraphDbException("Impossible retrieving the interactions in this moment.");
        }

        return interactions;
    }

    private List<Interaction> getBasicInteraction(Map<Integer, EncoreInteraction> interactionMapping, String idDBNames, Integer taxId) {

        String A = null;
        String B = null;

        List<Interaction> interactions = new ArrayList<Interaction>();

        EncoreInteraction eI;
        BinaryInteraction bI;

        Encore2Binary iConverter;

        for (int mappingId : interactionMapping.keySet()) {
            EncoreInteraction aux = interactionMapping.get(mappingId);

            if (aux != null) {

                A = aux.getInteractorAccsA().get("uniprotkb");
                B = aux.getInteractorAccsB().get("uniprotkb");

                if (A == null || B == null) {
                    log.error("Intra molecular interaction or interaction without UniProt identifier A: " + A + " - B: " + B);
                } else {
                    List<Confidence> scores = aux.getConfidenceValues();

                    log.info("Interaction A: " + A + " - B: " + B);

                    String miScore = "0";
                    for (Confidence proteInferactScore : scores) {
                        //TODO Review the score type
                        if (proteInferactScore.getType().contains("proteInferactScore")) {
                            //En el peor caso, que tengas varios nos quedamos con el último
                            miScore = proteInferactScore.getValue();
                        }
                    }
                    iConverter = new Encore2Binary(idDBNames);

                    bI = iConverter.getBinaryInteraction(aux);

                    //Convert interaction
                    Set<GraphCvTerm> piPublications = new HashSet<>();
                    for (Object o : bI.getPublications()) {
                        if (o instanceof CrossReference) {
                            CrossReference cr = (CrossReference) o;
                            piPublications.add(new GraphCvTerm(cr.getDatabase(), cr.getIdentifier(), cr.getText(), null));
                        }
                    }

                    Set<GraphCvTerm> piDetectionMethods = new HashSet<GraphCvTerm>();
                    for (Object o : bI.getDetectionMethods()) {
                        if (o instanceof CrossReference) {
                            CrossReference cr = (CrossReference) o;
                            piDetectionMethods.add(new GraphCvTerm(cr.getDatabase(), cr.getIdentifier(), cr.getText(), null));
                        }
                    }

                    Set<GraphCvTerm> piInteractionTypes = new HashSet<GraphCvTerm>();
                    for (Object o : bI.getInteractionTypes()) {
                        if (o instanceof CrossReference) {
                            CrossReference cr = (CrossReference) o;
                            piInteractionTypes.add(new GraphCvTerm(cr.getDatabase(), cr.getIdentifier(), cr.getText(), null));
                        }
                    }

                    interactions.add(new Interaction(
                            new Interactor(A, taxId),
                            new Interactor(B, taxId),
                            Double.parseDouble(miScore),
                            piPublications,
                            piDetectionMethods,
                            piInteractionTypes));
                }
            }
        }

        return interactions;

    }


    /* We write one file per protein but maybe some of the interactions are redundant */
    private void writeResults(String name, List<Interaction> interactions) {

        File file;
        String fileName;
        FileWriter fileWriter = null;

        File dir = new File(getDirName());
        dir.mkdir();

        fileName = "interaction_" + name + ".tsv";
        file = new File(dir, fileName);

        // the fields to bind do in your JavaBean
        String[] columns = new String[]{"IdA", "IdB", "score", "publications", "detectionMethods", "interactionTypes"};

        CSVWriter writer = null;
        // feed in your array (or convert your data to an array)


        try {

            fileWriter = new FileWriter(file);
            writer = new CSVWriter(fileWriter, '\t', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);
            writer.writeNext(columns);

            for (Interaction interaction : interactions) {

                String A = interaction.getInteractorA().getAccession();
                String B = interaction.getInteractorB().getAccession();
                String miScore = interaction.getScore().toString();
                String publications = joinCrossReferencStyleCollection(interaction.getPublications());
                String detMethods = joinCrossReferencStyleCollection(interaction.getDetectionMethods());
                String interactionTypes = joinCrossReferencStyleCollection(interaction.getInteractionTypes());

                writer.writeNext(new String[]{A, B, miScore, publications, detMethods, interactionTypes});
            }
        } catch (IOException e) {
            log.error("The interaction can not be written in the file");
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    log.error("The file can not be closed");
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("The file can not be closed");
                }
            }
        }
    }

    public boolean isEnableRecording() {
        return enableRecording;
    }

    public void setEnableRecording(boolean enableRecording) {
        this.enableRecording = enableRecording;
    }

    public List<String> getQuerySources() {
        return querySources;
    }

    public void setQuerySources(List<String> querySources) {
        this.querySources = querySources;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getMiqlQueryFilters() {
        return miqlQueryFilters;
    }

    public void setMiqlQueryFilters(String miqlQueryFilters) {
        this.miqlQueryFilters = miqlQueryFilters;
    }


}
