package uk.ac.ebi.intact.graphdb.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.ols.CachedOlsOntologyTermFetcher;
import psidev.psi.mi.jami.datasource.InteractionWriter;
import psidev.psi.mi.jami.factory.InteractionWriterFactory;
import psidev.psi.mi.jami.json.InteractionViewerJson;
import psidev.psi.mi.jami.json.MIJsonOptionFactory;
import psidev.psi.mi.jami.json.MIJsonType;
import psidev.psi.mi.jami.model.InteractionCategory;
import psidev.psi.mi.jami.model.InteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.service.GraphInteractionService;
import uk.ac.ebi.intact.graphdb.ws.controller.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController
@RequestMapping("/interaction")
public class InteractionController {

    private GraphInteractionService graphInteractionService;

    @Autowired
    public InteractionController(GraphInteractionService graphInteractionService) {
        this.graphInteractionService = graphInteractionService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/details/{ac}", produces = {APPLICATION_JSON_VALUE})
    public InteractionDetails getInteractionDetails(
            @PathVariable String ac) {

        GraphInteractionEvidence graphInteractionEvidence = graphInteractionService.findByInteractionAc(ac);

        return createInteractionDetails(graphInteractionEvidence);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/detailsOld",
            params = {"ac"},
            produces = {APPLICATION_JSON_VALUE})
    public GraphInteractionEvidence getDetailsOld(
            @RequestParam(value = "ac") String ac,
            @RequestParam(value = "depth", defaultValue = "2", required = false) int depth) {
        return graphInteractionService.findByInteractionAc(ac);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/export/{ac}")
    public ResponseEntity<String> exportInteraction(@PathVariable String ac,
                                                    @RequestParam(value = "format", defaultValue = "json", required = false) String format,
                                                    HttpServletResponse response) throws Exception {
        Boolean exportAsFile = false;

        InteractionEvidence interactionEvidence = graphInteractionService.findByInteractionAcForMiJson(ac);

        ResponseEntity<String> responseEntity = null;
        if (interactionEvidence != null) {
            InteractionWriterFactory writerFactory = InteractionWriterFactory.getInstance();
            switch (InteractionExportFormat.formatOf(format)) {
                case JSON:
                default:
                    responseEntity = createJsonResponse(interactionEvidence, writerFactory);
                    break;
            }
            return responseEntity;
        }
        throw new Exception("Export failed " + ac + ". No Interaction result");
    }

    /**
     * CONVERTS from GraphInteractionEvidence and GraphExperiment to InteractionDetails model
     **/
    private InteractionDetails createInteractionDetails(GraphInteractionEvidence graphInteractionEvidence) {
        String ac = graphInteractionEvidence.getAc();

        String interactionType = graphInteractionEvidence.getInteractionType().getShortName();

        String shortLabel = graphInteractionEvidence.getShortName();

        Set<Xref> xrefs = new HashSet<>();
        graphInteractionEvidence.getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            xrefs.add(new Xref(term, xref.getId()));
        });

        Set<Annotation> annotations = new HashSet<>();
        graphInteractionEvidence.getAnnotations().forEach(annotation -> {
            CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());
            annotations.add(new Annotation(term, annotation.getValue()));
        });

        List<Parameter> parameters = new ArrayList<>();
        graphInteractionEvidence.getParameters().forEach(parameter -> {
            CvTerm paramType = new CvTerm(parameter.getType().getShortName(), parameter.getType().getMIIdentifier());
            CvTerm paramUnit = new CvTerm(parameter.getUnit().getShortName(), parameter.getUnit().getMIIdentifier());
            parameters.add(new Parameter(paramType, paramUnit, parameter.getValue().toString()));

        });

        List<Confidence> confidences = new ArrayList<>();
        graphInteractionEvidence.getConfidences().forEach(confidence -> {
            CvTerm cvTerm = new CvTerm(confidence.getType().getShortName(), confidence.getType().getMIIdentifier());
            confidences.add(new Confidence(cvTerm, confidence.getValue()));
        });

        GraphExperiment graphExperiment = (GraphExperiment) graphInteractionEvidence.getExperiment();

        ExperimentDetails experimentDetails = createExperimentDetails(graphExperiment);
        PublicationDetails publicationDetails = createPublicationDetails(graphExperiment);
        InteractionDetails interactionDetails = new InteractionDetails(ac, interactionType, shortLabel, xrefs, annotations, parameters, confidences, experimentDetails, publicationDetails);

        shuffleDataBetweenModels(experimentDetails, publicationDetails, interactionDetails);
        return interactionDetails;
    }

    private ExperimentDetails createExperimentDetails(GraphExperiment graphExperiment) {
        String experimentAc = graphExperiment.getAc();
        String hostOrganism = graphExperiment.getHostOrganism().getScientificName();
        String interactionDetMethod = graphExperiment.getInteractionDetectionMethod().getShortName();
        String participantDetMethod = null;
        if (graphExperiment.getParticipantDetectionMethod() != null) {
            participantDetMethod = graphExperiment.getParticipantDetectionMethod().getShortName();
        }

        Set<Xref> experimentXrefs = new HashSet<>();
        graphExperiment.getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            experimentXrefs.add(new Xref(term, xref.getId()));
        });

        Set<Annotation> experimentAnnotations = new HashSet<>();
        graphExperiment.getAnnotations().forEach(annotation -> {
            CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());

            experimentAnnotations.add(new Annotation(term, annotation.getValue()));
        });

        return new ExperimentDetails(experimentAc, interactionDetMethod, participantDetMethod, hostOrganism, experimentXrefs, experimentAnnotations);
    }

    private PublicationDetails createPublicationDetails(GraphExperiment graphExperiment) {

        GraphPublication graphPublication = (GraphPublication) graphExperiment.getPublication();
        String pubmedId = graphPublication.getPubmedIdStr(); //TODO: WHEN FIXED extract from the pubmedId method
        String title = graphExperiment.getPublication().getTitle();
        String journal = graphExperiment.getPublication().getJournal();
        List<String> authors = graphExperiment.getPublication().getAuthors();
        //TODO...Check if we can fix this from graph db side, difficult to do as WFH
        String publicationDate = null;
        if (graphExperiment.getPublication().getPublicationDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            publicationDate = dateFormat.format(graphExperiment.getPublication().getPublicationDate());
        }

        Set<Xref> publicationXrefs = new HashSet<>();
        graphExperiment.getPublication().getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            publicationXrefs.add(new Xref(term, xref.getId()));
        });

        Set<Annotation> publicationAnnotation = new HashSet<>();
        graphExperiment.getPublication().getAnnotations().forEach(annotation -> {
            CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());
            publicationAnnotation.add(new Annotation(term, annotation.getValue()));
        });

        return new PublicationDetails(pubmedId, title, journal, authors, publicationDate, publicationXrefs, publicationAnnotation);
    }

    /*
     * For better user experience it was required to shuffle experiment and publication data between
     * publication and interaction data
     * */
    public void shuffleDataBetweenModels(ExperimentDetails experimentDetails, PublicationDetails publicationDetails, InteractionDetails interactionDetails) {

        //add experiment data to interaction data
        interactionDetails.getXrefs().addAll(experimentDetails.getExperimentXrefs());
        interactionDetails.getAnnotations().addAll(experimentDetails.getExperimentAnnotations());

        //add publication data to interaction data (some entries need to be at interaction level)
        interactionDetails.getAnnotations().addAll(publicationDetails.getPublicationAnnotations());

        //retain specific pub annotations
        List<Annotation> annotationsToRetain = new ArrayList<>(); // as already in publication fields
        annotationsToRetain.addAll(publicationDetails.getPublicationAnnotations().stream().filter(annotation ->
                annotation.getTopic().getIdentifier().equals("MI:0634") || //contact-email
                        annotation.getTopic().getIdentifier().equals("MI:0878") || //author-submitted
                        annotation.getTopic().getIdentifier().equals("MI:0612") || //comments
                        annotation.getTopic().getIdentifier().equals("MI:0618") || //cautions
                        annotation.getTopic().getIdentifier().equals("MI:0614")   //urls
        ).collect(Collectors.toList()));
        publicationDetails.getPublicationAnnotations().retainAll(annotationsToRetain);


        //remove publication data from interaction data (will remove duplication on page)
        interactionDetails.getXrefs().removeAll(publicationDetails.getPublicationXrefs());
        interactionDetails.getAnnotations().removeAll(publicationDetails.getPublicationAnnotations());

        //remove some unwanted xref and annotations from details page
        List<Annotation> interactionAnnotationsToDelete = new ArrayList<>();
        interactionAnnotationsToDelete.addAll(interactionDetails.getAnnotations().stream().filter(annotation ->
                annotation.getTopic().getIdentifier().equals("MI:0957") ||         //full-coverage
                        annotation.getTopic().getIdentifier().equals("MI:0959") || // imex curation
                        annotation.getTopic().getIdentifier().equals("MI:0886") ||         //publication year
                        annotation.getTopic().getIdentifier().equals("MI:0885") || //journal
                        annotation.getTopic().getIdentifier().equals("MI:0636")    //authors
        ).collect(Collectors.toList()));
        interactionDetails.getAnnotations().removeAll(interactionAnnotationsToDelete);

        List<Xref> xrefsToRemove = new ArrayList<>(); // as already in publication fields
        xrefsToRemove.addAll(interactionDetails.getXrefs().stream().filter(xref ->
                xref.getDatabase().getIdentifier().equals("MI:0446")// pubmed id
        ).collect(Collectors.toList()));
        interactionDetails.getXrefs().removeAll(xrefsToRemove);
    }


    private ResponseEntity<String> createJsonResponse(InteractionEvidence interactionEvidence,
                                                      InteractionWriterFactory writerFactory) {

        InteractionViewerJson.initialiseAllMIJsonWriters();
        MIJsonOptionFactory optionFactory = MIJsonOptionFactory.getInstance();
        StringWriter answer = new StringWriter();
        InteractionWriter writer = null;
        Map<String, Object> options = null;
        try {
            options = optionFactory.getJsonOptions(answer, InteractionCategory.evidence, null,
                    MIJsonType.n_ary_only, new CachedOlsOntologyTermFetcher(), null);
            writer = writerFactory.getInteractionWriterWith(options);
        } catch (BridgeFailedException e) {
            options = optionFactory.getJsonOptions(answer, InteractionCategory.evidence, null,
                    MIJsonType.n_ary_only, null, null);

        }
        writer = writerFactory.getInteractionWriterWith(options);

        try {
            writer.start();
            writer.write(interactionEvidence);
            writer.end();
        } finally {
            writer.close();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("X-Clacks-Overhead", "GNU Terry Pratchett"); //In memory of Sir Terry Pratchett
        return new ResponseEntity<String>(answer.toString(), httpHeaders, HttpStatus.OK);
    }

}
