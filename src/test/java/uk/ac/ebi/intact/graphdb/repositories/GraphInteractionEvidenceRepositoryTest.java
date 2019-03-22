package uk.ac.ebi.intact.graphdb.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.AnnotationUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import uk.ac.ebi.intact.graphdb.model.nodes.*;
import uk.ac.ebi.intact.graphdb.utils.Constants;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by anjali on 07/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class GraphInteractionEvidenceRepositoryTest {

    @Autowired
    private GraphInteractionEvidenceRepository graphInteractionEvidenceRepository;

    @Test
    public void getInteractionEvidenceByAc() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10000974";
        int depth = 2;
        Page<GraphInteractionEvidence> page = graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0, 1),
                depth);
        if (page != null && !page.getContent().isEmpty()) {
            graphInteractionEvidence = page.getContent().get(0);
        }

        Assert.assertNotNull("Interaction is null", graphInteractionEvidence);
        Assert.assertEquals("Interaction is incorrect", ac, graphInteractionEvidence.getAc());

        //test for depth coverage

        Assert.assertNotNull("Experiment not present", graphInteractionEvidence.getExperiment());
        Assert.assertNotNull("Publication not present", graphInteractionEvidence.getExperiment().getPublication());
        Assert.assertEquals("Publication shortlabel not present", "25314077",
                ((GraphPublication) graphInteractionEvidence.getExperiment().getPublication()).getPubmedIdStr());
    }

    @Test
    public void findByInteractionAcForMiJson() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac1 = "EBI-10052707";
        Optional<GraphInteractionEvidence> graphInteractionEvidenceOptional1 = graphInteractionEvidenceRepository.
                findByInteractionAcForMiJson(ac1);

        Assert.assertTrue("GraphInteractionEvidence is not present ", graphInteractionEvidenceOptional1.isPresent());

        // interaction...
        graphInteractionEvidence = graphInteractionEvidenceOptional1.get();
        Assert.assertEquals("GraphInteractionEvidence is not correct", ac1, graphInteractionEvidence.getAc());
        Assert.assertNotNull("Interaction type is null", graphInteractionEvidence.getInteractionType());
        Assert.assertEquals("Interaction type is wrong", "direct interaction", graphInteractionEvidence.getInteractionType().
                getShortName());
        Assert.assertEquals("Interaction type Id is wrong", "MI:0407", graphInteractionEvidence.getInteractionType().
                getMIIdentifier());
        Assert.assertNotNull("Interaction identifiers is null", graphInteractionEvidence.getIdentifiers());
        Assert.assertNotNull("Interaction xrefs is null", graphInteractionEvidence.getXrefs());

        String imexIdOfInteraction = "IM-23527-10";
        String intactIdOfInteraction = "EBI-10052707";
        Collection<Xref> ieImexIdXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(graphInteractionEvidence.getXrefs(), Xref.IMEX_MI, "imex", "IM-23527-10");
        Assert.assertNotNull("Interaction Imex Id :" + imexIdOfInteraction + " Identifier not present", ieImexIdXrefs);
        Assert.assertEquals("Interaction Imex Id count is wrong", 1, ieImexIdXrefs.size());

        Collection<Xref> ieIntactXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(graphInteractionEvidence.getIdentifiers(), "MI:0469", Constants.INTACT_DB, "EBI-10052707");
        Assert.assertNotNull("Interaction Intact Id :" + intactIdOfInteraction + " Identifier not present", ieIntactXrefs);
        Assert.assertEquals("Interaction Intact Id count is wrong", 1, ieIntactXrefs.size());
        Assert.assertNotNull("Interaction Annotations is null", graphInteractionEvidence.getAnnotations());

        Collection<Annotation> figures = AnnotationUtils.collectAllAnnotationsHavingTopic(graphInteractionEvidence.getAnnotations(),
                Annotation.FIGURE_LEGEND_MI, Annotation.FIGURE_LEGEND);
        Assert.assertNotNull("Figures is null", figures);
        Assert.assertEquals("Figure count is wrong", 1, figures.size());

        Assert.assertNotNull("Participants is null", graphInteractionEvidence.getParticipants());
        Assert.assertEquals("Number of Participants is wrong", 2, graphInteractionEvidence.getParticipants().size());

        //participants...
        GraphParticipantEvidence graphParticipantEvidence_P53010 = null;
        GraphParticipantEvidence graphParticipantEvidence_P36102 = null;

        String participant_P53010Ac = "EBI-10052711";
        String participant_P36102Ac = "EBI-10052709";
        for (GraphParticipantEvidence participantEvidence : graphInteractionEvidence.getParticipants()) {
            if (participantEvidence.getAc().equals(participant_P53010Ac)) {
                graphParticipantEvidence_P53010 = participantEvidence;
            } else if (participantEvidence.getAc().equals(participant_P36102Ac)) {
                graphParticipantEvidence_P36102 = participantEvidence;
            }
        }
        Assert.assertNotNull("GraphParticipantEvidence with ac :" + participant_P53010Ac + " not present ",
                graphParticipantEvidence_P53010);
        Assert.assertNotNull("GraphParticipantEvidence with ac :" + graphParticipantEvidence_P36102 + " not present ",
                graphParticipantEvidence_P36102);


        Assert.assertNotNull("Stoichiometry is null", graphParticipantEvidence_P53010.getStoichiometry());
        Assert.assertEquals("Stoichiometry is null", 1, graphParticipantEvidence_P53010.getStoichiometry().getMaxValue());
        Assert.assertNotNull("Biological Role is null", graphParticipantEvidence_P53010.getBiologicalRole());
        Assert.assertEquals("Biological Role text seems wrong", "unspecified role", graphParticipantEvidence_P53010.
                getBiologicalRole().getShortName());
        Assert.assertEquals("Biological Role Mi Identifier is wrong", "MI:0499", graphParticipantEvidence_P53010.
                getBiologicalRole().getMIIdentifier());
        Assert.assertNotNull("Experimental Role is null", graphParticipantEvidence_P53010.getExperimentalRole());
        Assert.assertEquals("Experimental Role text seems wrong", "prey", graphParticipantEvidence_P53010.
                getExperimentalRole().getShortName());
        Assert.assertEquals("Experimental Role Mi Identifier is wrong", "MI:0498", graphParticipantEvidence_P53010.
                getExperimentalRole().getMIIdentifier());
        Assert.assertNotNull("Identification Method is null", graphParticipantEvidence_P53010.getIdentificationMethods());
        Assert.assertEquals("Identification Methods count is wrong", 1, graphParticipantEvidence_P53010.getIdentificationMethods().size());

        GraphCvTerm identificationMethod = graphParticipantEvidence_P53010.getIdentificationMethods().iterator().next();
        Assert.assertEquals("IdentificationMethod short name is wrong", "molecular weight estimation by coomasie staining",
                identificationMethod.getFullName());
        Assert.assertEquals("IdentificationMethod", "MI:0818", identificationMethod.getMIIdentifier());
        Assert.assertNotNull("Features is null", graphParticipantEvidence_P36102.getFeatures());
        Assert.assertEquals("Features Count is wrong", 1, graphParticipantEvidence_P36102.getFeatures().size());

        //features...
        GraphFeatureEvidence graphFeatureEvidence = graphParticipantEvidence_P36102.getFeatures().iterator().next();
        Assert.assertEquals("Feature has wrong name", "strep tag", graphFeatureEvidence.getShortName());
        Assert.assertNotNull("Feature Type is null", graphFeatureEvidence.getType());
        Assert.assertEquals("Feature Type short name is wrong", "strep ii tag", graphFeatureEvidence.getType().getShortName());
        Assert.assertEquals("Feature Type Mi Identifier is wrong", "MI:0962", graphFeatureEvidence.getType().getMIIdentifier());
        Assert.assertNotNull("Feature Ranges are null", graphFeatureEvidence.getRanges());
        Assert.assertEquals("Feature Ranges Count is wrong", 1, graphFeatureEvidence.getRanges().size());

        GraphRange graphRange = graphFeatureEvidence.getRanges().iterator().next();
        Assert.assertEquals("Feature Ranges Count is wrong", "c-c", graphRange.getRangeString());
        Assert.assertNotNull("Interactor is null", graphParticipantEvidence_P53010.getInteractor());

        //interactor...
        Interactor interactor = graphParticipantEvidence_P53010.getInteractor();
        Assert.assertNotNull("Interactor Preferred Identifier is null", interactor.getPreferredIdentifier());
        Assert.assertEquals("Interactor Preferred Identifier is wrong", "P53010", interactor.getPreferredIdentifier().getId());
        Assert.assertEquals("Not a Polymer", true, interactor instanceof GraphPolymer);
        Assert.assertNotNull("Polymer does not have sequence", ((GraphPolymer) interactor).getSequence());
        Assert.assertNotNull("Interactor Type is null", interactor.getInteractorType());
        Assert.assertEquals("Interactor Type name is wrong", "protein", interactor.getInteractorType().getShortName());
        Assert.assertEquals("Interactor Type MI Identifier is wrong", "MI:0326", interactor.getInteractorType().getMIIdentifier());
        Assert.assertNotNull("Organism is null", interactor.getOrganism());
        Assert.assertEquals("Organism tax id is wrong", 559292, interactor.getOrganism().getTaxId());
        Assert.assertEquals("Organism Scientific name is wrong", "Saccharomyces cerevisiae", interactor.getOrganism().getScientificName());

        //Experiment...
        Experiment experiment = graphInteractionEvidence.getExperiment();
        Assert.assertNotNull("Experiment is null", experiment);
        Assert.assertNotNull("Experiment Detection Method is null", experiment.getInteractionDetectionMethod());
        Assert.assertEquals("Experiment Detection method has wrong name", "pull down", experiment.getInteractionDetectionMethod().
                getShortName());
        Assert.assertEquals("Experiment Detection method has wrong Mi Identifier", "MI:0096", experiment.getInteractionDetectionMethod().
                getMIIdentifier());

        Assert.assertNotNull("Host Organism is null", experiment.getHostOrganism());
        Assert.assertEquals("Host Organism tax id is wrong", -1, experiment.getHostOrganism().getTaxId());
        Assert.assertEquals("Organism Scientific name is wrong", "In vitro", experiment.getHostOrganism().getScientificName());

        //publication...
        GraphPublication publication = (GraphPublication) experiment.getPublication();
        Assert.assertNotNull("Publication is null", publication);
        Assert.assertNotNull("Publication Identifiers is null", publication.getIdentifiers());
        Assert.assertEquals("Publication Identifiers count is wrong", 2, publication.getIdentifiers().size());

        Collection<Xref> pubmedXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(publication.getIdentifiers(), Xref.PUBMED_MI, Constants.PUBMED_DB, "24872509");
        Assert.assertNotNull("Publication pubmed identifier is null", pubmedXrefs);
        Assert.assertEquals("Publication pubmed identifier count is wrong", 1, pubmedXrefs.size());

        Collection<Xref> intactXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(publication.getIdentifiers(), "MI:0469", Constants.INTACT_DB, "EBI-9836453");
        Assert.assertNotNull("Publication intact identifier is null", intactXrefs);
        Assert.assertEquals("Publication intact identifier count is wrong", 1, intactXrefs.size());
        Assert.assertEquals("Publication Imex id is wrong", "IM-23527", publication.getImexId());
        Assert.assertNotNull("Publication Source is null", publication.getSource());
        Assert.assertEquals("Publication Source is wrong", "MINT, Dpt of Biology, University of Rome Tor Vergata", publication.getSource().getFullName());
        Assert.assertEquals("Publication Source Mi Identifier is wrong", "MI:0471", publication.getSource().getMIIdentifier());

        // for interaction parameters checking
        String ac2 = "EBI-10048599";
        Optional<GraphInteractionEvidence> graphInteractionEvidenceOptional2 = graphInteractionEvidenceRepository.
                findByInteractionAcForMiJson(ac2);

        Assert.assertTrue("GraphInteractionEvidence is not present ", graphInteractionEvidenceOptional2.isPresent());

        GraphInteractionEvidence graphInteractionEvidence2 = graphInteractionEvidenceOptional2.get();
        Assert.assertEquals("GraphInteractionEvidence is not correct", ac2, graphInteractionEvidence2.getAc());
        Assert.assertNotNull("Interaction Evidence Parameters is null", graphInteractionEvidence2.getParameters());
        Assert.assertEquals("Interaction Evidence Parameters count is wrong", 2, graphInteractionEvidence2.getParameters().size());

        String parameterAc = "EBI-10048604";
        GraphParameter ieParameter = null;
        for (GraphParameter parameter : graphInteractionEvidence2.getParameters()) {
            if (parameter.getAc() != null && parameter.getAc().equals(parameterAc)) {
                ieParameter = parameter;
            }
        }
        Assert.assertNotNull("GraphParameter with ac :" + parameterAc + " not present ", ieParameter);
        Assert.assertNotNull("GraphParameter Unit is null", ieParameter.getUnit());
        Assert.assertEquals("GraphParameter Unit name is wrong", "molar", ieParameter.getUnit().getShortName());
        Assert.assertNotNull("Graph Parameter Type is null", ieParameter.getType());
        Assert.assertEquals("Graph Parameter Type name is wrong", "ic50", ieParameter.getType().getShortName());

        GraphParameterValue ieParameterValue = (GraphParameterValue) ieParameter.getValue();
        Assert.assertNotNull("Interaction Evidence Parameter Value is null", ieParameterValue);
        Assert.assertEquals("Interaction Evidence Parameter Value - Net Value - is wrong", "521x10^(-9)", ieParameterValue.toString());


    }
}
