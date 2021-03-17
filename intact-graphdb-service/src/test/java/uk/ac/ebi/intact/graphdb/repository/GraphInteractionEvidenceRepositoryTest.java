package uk.ac.ebi.intact.graphdb.repository;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anjali on 07/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class GraphInteractionEvidenceRepositoryTest {

    @Autowired
    private GraphInteractionEvidenceRepository graphInteractionEvidenceRepository;

    @Test
    public void testGraphInteractionEvidenceRepositoryPagination() {
        int pageNumber = 0;
        int totalElements = 0;
        int pageSize = 10;
        int depth = 0;
        Page<GraphInteractionEvidence> page;

        do {
            page = graphInteractionEvidenceRepository.findAll(PageRequest.of(pageNumber, pageSize), depth);
            Assert.assertNotNull("Page is Null", page);
            totalElements = totalElements + page.getNumberOfElements();
            pageNumber++;
        } while (page.hasNext());

        Assert.assertEquals(pageNumber, page.getTotalPages());
        Assert.assertEquals(totalElements, page.getTotalElements());
        Assert.assertEquals(1000, totalElements);
        Assert.assertTrue(pageNumber > 1);
    }

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
    public void getInteractionEvidenceByAcs() {
        int pageNumber = 0;
        int totalElements = 0;
        int pageSize = 2;
        int depth = 0;
        Page<GraphInteractionEvidence> page;

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications
        interactionAcsToTest.add("EBI-10052707");// duplicated interaction on purpose
        interactionAcsToTest.add("EBI-10052707b");// non existing interaction on purpose

        do {
            page = graphInteractionEvidenceRepository.findByAcIn(interactionAcsToTest, PageRequest.of(pageNumber, pageSize), depth);
            Assert.assertNotNull("Page is Null", page);
            totalElements = totalElements + page.getNumberOfElements();
            pageNumber++;
        } while (page.hasNext());

        Assert.assertEquals(pageNumber, page.getTotalPages());
        Assert.assertEquals(totalElements, page.getTotalElements());
        Assert.assertEquals(7, totalElements);
        Assert.assertTrue(pageNumber > 1);

    }

    @Test
    public void getInteractionEvidenceBy1000Acs() {
        int pageNumber = 0;
        int totalElements = 1000;
        int pageSize = 1000;
        int depth = 0;
        Page<GraphInteractionEvidence> page;

        List<String> interactionAcsToTest = graphInteractionEvidenceRepository.findAllInteractionAcs();
        Assert.assertNotNull("Interaction ac list is Null", interactionAcsToTest);

        page = graphInteractionEvidenceRepository.findByAcIn(interactionAcsToTest, PageRequest.of(pageNumber, pageSize), depth);
        Assert.assertNotNull("Page is Null", page);


        Assert.assertEquals(pageNumber, page.getTotalPages()-1);
        Assert.assertEquals(totalElements, page.getTotalElements());
        Assert.assertEquals(1000, totalElements);
    }

    @Test
    public void getInteractionEvidenceByAcForDetailsPage() {
        String ac = "EBI-10000974";
        GraphInteractionEvidence graphInteractionEvidence = null;
        int depth = 2;
        Page<GraphInteractionEvidence> page = graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0, 1),
                depth);
        if (page != null && !page.getContent().isEmpty()) {
            graphInteractionEvidence = page.getContent().get(0);
        }

        // interaction
        Assert.assertNotNull("Interaction:" + ac + " was expected here" + graphInteractionEvidence);
        Assert.assertEquals("GraphInteractionEvidence is not correct", ac, graphInteractionEvidence.getAc());
        Assert.assertNotNull("Interaction type is null", graphInteractionEvidence.getInteractionType());
        Assert.assertEquals("Interaction type is wrong", "physical association", graphInteractionEvidence.getInteractionType().
                getShortName());

        //xrefs
        Assert.assertNotNull("Interaction xrefs is null", graphInteractionEvidence.getXrefs());
        Assert.assertEquals("Interaction xref count is wrong", 1, graphInteractionEvidence.getXrefs().size());

        String imexIdOfInteraction = "IM-23546-1";
        Collection<Xref> ieImexIdXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(graphInteractionEvidence.getXrefs(),
                Xref.IMEX_MI, "imex", imexIdOfInteraction);
        Assert.assertNotNull("Interaction Imex Id :" + imexIdOfInteraction + " Identifier not present", ieImexIdXrefs);
        Assert.assertEquals("Interaction Imex Id count is wrong", 1, ieImexIdXrefs.size());

        //annotations
        Assert.assertNotNull("Interaction Annotations is null", graphInteractionEvidence.getAnnotations());
        Assert.assertEquals("Interaction annotations count is wrong", 1, graphInteractionEvidence.getAnnotations().size());

        String figure_legend = "S5D";
        Annotation figures = AnnotationUtils.collectFirstAnnotationWithTopicAndValue(graphInteractionEvidence.getAnnotations(),
                Annotation.FIGURE_LEGEND_MI, Annotation.FIGURE_LEGEND, "S5D");
        Assert.assertNotNull("Figure Legend :" + figure_legend + " not present", figures);

        //parameters
        Assert.assertNotNull("Interaction parameters is null", graphInteractionEvidence.getParameters());
        Assert.assertEquals("Interaction parameters count is wrong", 1, graphInteractionEvidence.getParameters().size());

        GraphParameter graphParameter = graphInteractionEvidence.getParameters().iterator().next();
        Assert.assertNotNull("GraphParameter Unit is null", graphParameter.getUnit());
        Assert.assertEquals("GraphParameter Unit name is wrong", "molar", graphParameter.getUnit().getShortName());
        Assert.assertNotNull("Graph Parameter Type is null", graphParameter.getType());
        Assert.assertEquals("Graph Parameter Type name is wrong", "ic50", graphParameter.getType().getShortName());

        GraphParameterValue ieParameterValue = (GraphParameterValue) graphParameter.getValue();
        Assert.assertNotNull("Interaction Evidence Parameter Value is null", ieParameterValue);
        Assert.assertEquals("Interaction Evidence Parameter Value - Net Value - is wrong", "0.1499999999999999944488848768742172978818416595458984375x10^(-9)", ieParameterValue.toString());

        //confidences
        Assert.assertTrue("Confidences should be empty", graphInteractionEvidence.getConfidences().isEmpty());


    }

    @Test
    public void findByInteractionAcForMiJson() {
        String ac = "EBI-10052707";
        GraphInteractionEvidence graphInteractionEvidence = null;
        graphInteractionEvidence = graphInteractionEvidenceRepository.findByAc(ac);
//        if (page != null && !page.getContent().isEmpty()) {
//            graphInteractionEvidence = page.getContent().get(0);
//        }

        // interaction
        Assert.assertNotNull("Interaction:" + ac + " was expected here" + graphInteractionEvidence);
        Assert.assertEquals("GraphInteractionEvidence is not correct", ac, graphInteractionEvidence.getAc());
        Assert.assertNotNull("Interaction type is null", graphInteractionEvidence.getInteractionType());
        Assert.assertEquals("Interaction type is wrong", "direct interaction", graphInteractionEvidence.getInteractionType().
                getShortName());
        Assert.assertEquals("Interaction type Id is wrong", "MI:0407", graphInteractionEvidence.getInteractionType().
                getMIIdentifier());
        Assert.assertNotNull("Interaction identifiers is null", graphInteractionEvidence.getIdentifiers());
        Assert.assertNotNull("Interaction xrefs is null", graphInteractionEvidence.getXrefs());

        String imexIdOfInteraction = "IM-23527-10";
        String intactIdOfInteraction = "EBI-10052707";
        Collection<Xref> ieImexIdXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(graphInteractionEvidence.getXrefs(),
                Xref.IMEX_MI, "imex", "IM-23527-10");
        Assert.assertNotNull("Interaction Imex Id :" + imexIdOfInteraction + " Identifier not present", ieImexIdXrefs);
        Assert.assertEquals("Interaction Imex Id count is wrong", 1, ieImexIdXrefs.size());

        Collection<Xref> ieIntactXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(graphInteractionEvidence.getIdentifiers(),
                "MI:0469", Constants.INTACT_DB, "EBI-10052707");
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
        GraphFeature graphFeatureEvidence = graphParticipantEvidence_P36102.getFeatures().iterator().next();
        Assert.assertEquals("Feature has wrong name", "strep tag", graphFeatureEvidence.getShortName());
        Assert.assertNotNull("Feature Type is null", graphFeatureEvidence.getType());
        Assert.assertEquals("Feature Type short name is wrong", "strep ii tag", graphFeatureEvidence.getType().getShortName());
        Assert.assertEquals("Feature Type Mi Identifier is wrong", "MI:0962", graphFeatureEvidence.getType().getMIIdentifier());
        Assert.assertNotNull("Feature Ranges are null", graphFeatureEvidence.getRanges());
        Assert.assertEquals("Feature Ranges Count is wrong", 1, graphFeatureEvidence.getRanges().size());

        GraphRange graphRange = (GraphRange) graphFeatureEvidence.getRanges().iterator().next();
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
        Assert.assertNotNull("Interaction Detection Method is null", experiment.getInteractionDetectionMethod());
        Assert.assertEquals("Interaction Detection method has wrong name", "pull down", experiment.getInteractionDetectionMethod().
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

        Collection<Xref> pubmedXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(publication.getIdentifiers(),
                Xref.PUBMED_MI, Constants.PUBMED_DB, "24872509");
        Assert.assertNotNull("Publication pubmed identifier is null", pubmedXrefs);
        Assert.assertEquals("Publication pubmed identifier count is wrong", 1, pubmedXrefs.size());

        Collection<Xref> intactXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(publication.getIdentifiers(),
                "MI:0469", Constants.INTACT_DB, "EBI-9836453");
        Assert.assertNotNull("Publication intact identifier is null", intactXrefs);
        Assert.assertEquals("Publication intact identifier count is wrong", 1, intactXrefs.size());
        Assert.assertEquals("Publication Imex id is wrong", "IM-23527", publication.getImexId());
        Assert.assertNotNull("Publication Source is null", publication.getSource());
        Assert.assertEquals("Publication Source is wrong", "MINT, Dpt of Biology, University of Rome Tor Vergata",
                publication.getSource().getFullName());
        Assert.assertEquals("Publication Source Mi Identifier is wrong", "MI:0471", publication.getSource().getMIIdentifier());

        // for interaction parameters checking
        String ac2 = "EBI-10048599";
        GraphInteractionEvidence graphInteractionEvidence2 = null;
        Page<GraphInteractionEvidence> page2 = graphInteractionEvidenceRepository.findTopByAc(ac2, PageRequest.of(0, 1), 0);
        if (page2 != null && !page2.getContent().isEmpty()) {
            graphInteractionEvidence2 = page2.getContent().get(0);
        }

        Assert.assertNotNull("Interaction:" + ac2 + " was expected here" + graphInteractionEvidence2);
        Assert.assertEquals("GraphInteractionEvidence 2 is not correct", ac2, graphInteractionEvidence2.getAc());
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

        //for experimentalModifications Checking and expressed In
        String ac3 = "EBI-1004945";
        GraphInteractionEvidence graphInteractionEvidence3 = null;
        Page<GraphInteractionEvidence> page3 = graphInteractionEvidenceRepository.findTopByAc(ac3, PageRequest.of(0, 1), 0);
        if (page3 != null && !page3.getContent().isEmpty()) {
            graphInteractionEvidence3 = page3.getContent().get(0);
        }

        Assert.assertNotNull("Interaction:" + ac3 + " was expected here" + graphInteractionEvidence3);
        Assert.assertEquals("GraphInteractionEvidence 3 is not correct", ac3, graphInteractionEvidence3.getAc());
        Assert.assertNotNull("GraphInteractionEvidence 3 Experiment is null", graphInteractionEvidence3.getExperiment());
        Assert.assertNotNull("GraphInteractionEvidence 3 Experiment Annotations is null", graphInteractionEvidence3.getExperiment());

        Collection<Annotation> expModifications = AnnotationUtils.collectAllAnnotationsHavingTopic(
                graphInteractionEvidence3.getExperiment().getAnnotations(),
                Annotation.EXP_MODIFICATION_MI, Annotation.EXP_MODIFICATION);
        Assert.assertNotNull("GraphInteractionEvidence 3 Experiment Experimental Modifications is null", expModifications);
        Assert.assertEquals("GraphInteractionEvidence 3 Experiment Experimental Modifications count is wrong", 1, expModifications.size());

        // expressed in ...
        Assert.assertNotNull("GraphInteractionEvidence 3 Participants is null", graphInteractionEvidence3.getParticipants());
        Assert.assertEquals("GraphInteractionEvidence 3 Participants count is wrong", 1, graphInteractionEvidence3.getParticipants().size());

        GraphParticipantEvidence graphParticipantEvidence3 = graphInteractionEvidence3.getParticipants().iterator().next();
        Assert.assertNotNull("GraphParticipantEvidence 3 expressedIn is null ", graphParticipantEvidence3.getExpressedInOrganism());
        Assert.assertEquals("Host Organism tax id is wrong", 83333, graphParticipantEvidence3.getExpressedInOrganism().getTaxId());
        Assert.assertEquals("Organism Scientific name is wrong", "Escherichia coli (strain K12)",
                graphParticipantEvidence3.getExpressedInOrganism().getScientificName());

        // for interpro
        String ac4 = "EBI-1005174";
        GraphInteractionEvidence graphInteractionEvidence4 = null;
        Page<GraphInteractionEvidence> page4 = graphInteractionEvidenceRepository.findTopByAc(ac4, PageRequest.of(0, 1), 0);
        if (page4 != null && !page4.getContent().isEmpty()) {
            graphInteractionEvidence4 = page4.getContent().get(0);
        }

        Assert.assertNotNull("Interaction:" + ac4 + " was expected here" + graphInteractionEvidence4);
        Assert.assertEquals("GraphInteractionEvidence 4 is not correct", ac4, graphInteractionEvidence4.getAc());
        Assert.assertNotNull("GraphInteractionEvidence 4 participants is null", graphInteractionEvidence4.getParticipants());

        GraphFeature graphFeatureEvidenceWithInterpro = null;
        String interproId = "IPR001012";

        try {
            for (GraphParticipantEvidence graphParticipantEvidence : graphInteractionEvidence4.getParticipants()) {
                for (GraphFeature graphFeatureEvidence1 : graphParticipantEvidence.getFeatures()) {
                    if (graphFeatureEvidence1.getInterpro() != null && graphFeatureEvidence1.getInterpro().equals(interproId)) {
                        graphFeatureEvidenceWithInterpro = graphFeatureEvidence1;
                    }
                }
            }
        } catch (Exception e) {
        }
        Assert.assertNotNull("GraphParticipantEvidence does not contain feature with Interpro Id : " + interproId,
                graphFeatureEvidenceWithInterpro);

        // for linked features 1
        String ac5 = "EBI-10054743";
        GraphInteractionEvidence graphInteractionEvidence5 = null;
        Page<GraphInteractionEvidence> page5 = graphInteractionEvidenceRepository.findTopByAc(ac5, PageRequest.of(0, 1), 0);
        if (page5 != null && !page5.getContent().isEmpty()) {
            graphInteractionEvidence5 = page5.getContent().get(0);
        }

        Assert.assertNotNull("Interaction:" + ac5 + " was expected here" + graphInteractionEvidence5);

        HashMap<String, Collection<GraphFeature>> linkedFeatures = new HashMap<>();
        String featureAc1_withLinkedFeature = "EBI-10055168";
        String featureAc2_withLinkedFeature = "EBI-10055163";
        try {
            for (GraphParticipantEvidence graphParticipantEvidence : graphInteractionEvidence5.getParticipants()) {
                for (GraphFeature graphFeatureEvidence1 : graphParticipantEvidence.getFeatures()) {
                    if (graphFeatureEvidence1.getLinkedFeatures() != null) {
                        linkedFeatures.put(graphFeatureEvidence1.getAc(), graphFeatureEvidence1.getLinkedFeatures());
                    }
                }
            }
        } catch (Exception e) {
        }

        Assert.assertEquals("GraphInteractionEvidence 5 Participant Feature :" + featureAc1_withLinkedFeature +
                " has wrong number of linked features", 1, linkedFeatures.get(featureAc1_withLinkedFeature).size());
        Assert.assertEquals("GraphInteractionEvidence 5 Participant Feature :" + featureAc1_withLinkedFeature +
                        " has wrong linked feature", featureAc2_withLinkedFeature,
                linkedFeatures.get(featureAc1_withLinkedFeature).iterator().next().getAc());
        Assert.assertEquals("GraphInteractionEvidence 5 Participant Feature :" + featureAc2_withLinkedFeature +
                " has wrong number of linked features", 1, linkedFeatures.get(featureAc2_withLinkedFeature).size());
        Assert.assertEquals("GraphInteractionEvidence 5 Participant Feature :" + featureAc2_withLinkedFeature +
                        " has wrong linked feature", featureAc1_withLinkedFeature,
                linkedFeatures.get(featureAc2_withLinkedFeature).iterator().next().getAc());

        //linked features 2
        String ac6 = "EBI-10042058";
        GraphInteractionEvidence graphInteractionEvidence6 = null;
        Page<GraphInteractionEvidence> page6 = graphInteractionEvidenceRepository.findTopByAc(ac6, PageRequest.of(0, 1), 0);
        if (page6 != null && !page6.getContent().isEmpty()) {
            graphInteractionEvidence6 = page6.getContent().get(0);
        }

        Assert.assertNotNull("Interaction:" + ac6 + " was expected here" + graphInteractionEvidence6);

        HashMap<String, Collection<GraphFeature>> linkedFeatures2 = new HashMap<>();
        String featureAc1_withLinkedFeature_2 = "EBI-10042083";
        String featureAc2_withLinkedFeature_2 = "EBI-10042078";

        try {
            for (GraphParticipantEvidence graphParticipantEvidence : graphInteractionEvidence6.getParticipants()) {
                for (GraphFeature graphFeatureEvidence1 : graphParticipantEvidence.getFeatures()) {
                    if (graphFeatureEvidence1.getLinkedFeatures() != null) {
                        linkedFeatures2.put(graphFeatureEvidence1.getAc(), graphFeatureEvidence1.getLinkedFeatures());
                    }
                }
            }
        } catch (Exception e) {
        }

        Assert.assertEquals("GraphInteractionEvidence 6 Participant Feature :" + featureAc1_withLinkedFeature_2 +
                " has wrong number of linked features", 1, linkedFeatures2.get(featureAc1_withLinkedFeature_2).size());
        Assert.assertEquals("GraphInteractionEvidence 6 Participant Feature :" + featureAc1_withLinkedFeature +
                        " has wrong linked feature", featureAc2_withLinkedFeature_2,
                linkedFeatures2.get(featureAc1_withLinkedFeature_2).iterator().next().getAc());
        Assert.assertEquals("GraphInteractionEvidence 6 Participant Feature :" + featureAc2_withLinkedFeature_2 +
                " has wrong number of linked features", 1, linkedFeatures2.get(featureAc2_withLinkedFeature_2).size());
        Assert.assertEquals("GraphInteractionEvidence 6 Participant Feature :" + featureAc2_withLinkedFeature_2 +
                        " has wrong linked feature", featureAc1_withLinkedFeature_2,
                linkedFeatures2.get(featureAc2_withLinkedFeature_2).iterator().next().getAc());

    }

    @Test
    public void getExperimentByInteractionAc() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10052707";
        int depth = 2;
        Page<GraphInteractionEvidence> page = graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0, 1),
                depth);
        if (page != null && !page.getContent().isEmpty()) {
            graphInteractionEvidence = page.getContent().get(0);
        }

        Assert.assertNotNull("Interaction is null", graphInteractionEvidence);
        Assert.assertEquals("Interaction is incorrect", ac, graphInteractionEvidence.getAc());

        GraphExperiment graphExperiment = (GraphExperiment) graphInteractionEvidence.getExperiment();
        Assert.assertNotNull("Experiment was expected here", graphExperiment);
        Assert.assertEquals("GraphExperiment is not correct", "EBI-9837129", graphExperiment.getAc());
        Assert.assertNotNull("Host Organism is null", graphExperiment.getHostOrganism());
        Assert.assertEquals("Host Organism is incorrect", "in vitro", graphExperiment.getHostOrganism().getCommonName());
        Assert.assertNotNull("Interaction Detection method is null", graphExperiment.getInteractionDetectionMethod());
        Assert.assertEquals("Interaction Detection Method is incorrect", "pull down",
                graphExperiment.getInteractionDetectionMethod().getShortName());
        Assert.assertNotNull("Participant Detection method is null", graphExperiment.getParticipantDetectionMethod());
        Assert.assertEquals("Participant Detection Method is incorrect", "weight by comassie",
                graphExperiment.getParticipantDetectionMethod().getShortName());
        Assert.assertNotNull("Experiment Xrefs is null", graphExperiment.getXrefs());
        Assert.assertEquals("Experiment Xrefs count is wrong", 2, graphExperiment.getXrefs().size());

        GraphXref experimentXref = null;
        String imexIdOfExp = "IM-23527";
        for (GraphXref xref : graphExperiment.getXrefs()) {
            if (xref.getId() != null && xref.getId().equals(imexIdOfExp)) {
                experimentXref = xref;
            }
        }
        Assert.assertNotNull("Experiment Imex Id :" + imexIdOfExp + " Xref not present", experimentXref);

        Assert.assertNotNull("Experiment Annotations is null", graphExperiment.getAnnotations());
        Assert.assertEquals("Experiment Annotations count is wrong", 7, graphExperiment.getAnnotations().size());

        GraphPublication graphPublication = (GraphPublication) graphExperiment.getPublication();

        Assert.assertNotNull("GraphPublication is not present", graphPublication);
        Assert.assertEquals("GraphPublication is not correct", "EBI-9836453", graphPublication.getAc());
        Assert.assertEquals("pubmedid is not correct", "24872509", graphPublication.getPubmedIdStr());
        Assert.assertEquals("Publication Title is wrong", "Structural basis for Pan3 binding to Pan2 and its function" +
                " in mRNA recruitment and deadenylation.", graphPublication.getTitle());
        Assert.assertEquals("Journal is wrong", "EMBO J. (0261-4189)", graphPublication.getJournal());
        try {
            Assert.assertEquals("Publication Date is wrong", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2014"),
                    graphPublication.getPublicationDate());
        } catch (Exception parseException) {
            parseException.printStackTrace();
        }

        Assert.assertNotNull("Publication Xrefs is null", graphPublication.getXrefs());
        Assert.assertEquals("Publication Xrefs count is wrong", 1, graphPublication.getXrefs().size());

        GraphXref publicationXref = null;
        String imexIdOfPub = "IM-23527";
        for (GraphXref xref : graphPublication.getXrefs()) {
            if (xref.getId() != null && xref.getId().equals(imexIdOfPub)) {
                publicationXref = xref;
            }
        }

        Assert.assertNotNull("Publication Imex Id :" + imexIdOfPub + " Xref not present", publicationXref);

        Assert.assertNotNull("Publication Annotations is null", graphPublication.getAnnotations());
        Assert.assertEquals("Publication Annotations count is wrong", 3, graphPublication.getAnnotations().size());

    }
}
