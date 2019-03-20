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
import psidev.psi.mi.jami.model.ParticipantEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.*;

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
    public void findByInteractionAcForMiJson(){
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10052707";
        Optional<GraphInteractionEvidence> graphInteractionEvidenceOptional= graphInteractionEvidenceRepository.
                findByInteractionAcForMiJson(ac);

        Assert.assertTrue("GraphInteractionEvidence is not present ", graphInteractionEvidenceOptional.isPresent());

        graphInteractionEvidence = graphInteractionEvidenceOptional.get();
        Assert.assertEquals("GraphInteractionEvidence is not correct", ac, graphInteractionEvidence.getAc());
        Assert.assertNotNull("Interaction type is null",graphInteractionEvidence.getInteractionType());
        Assert.assertEquals("Interaction type is wrong","direct interaction",graphInteractionEvidence.getInteractionType().
                getShortName());
        Assert.assertEquals("Interaction type Id is wrong","MI:0407",graphInteractionEvidence.getInteractionType().
                getMIIdentifier());
        Assert.assertNotNull("Interaction identifiers is null",graphInteractionEvidence.getIdentifiers());
        Assert.assertNotNull("Interaction xrefs is null",graphInteractionEvidence.getXrefs());

        GraphXref interactionImexXref= null;
        GraphXref interactionIntactIdentifier = null;
        String imexIdOfInteraction = "IM-23527-10";
        String intactIdOfInteraction= "EBI-10052707";
        for (GraphXref xref : graphInteractionEvidence.getXrefs()) {
            if (xref.getId() != null && xref.getId().equals(imexIdOfInteraction)) {
                interactionImexXref = xref;
            }
        }
        for (GraphXref xref : graphInteractionEvidence.getIdentifiers()) {
            if (xref.getId() != null && xref.getId().equals(intactIdOfInteraction)) {
                interactionIntactIdentifier = xref;
            }
        }
        Assert.assertNotNull("Interaction Imex Id :" + imexIdOfInteraction + " Identifier not present", interactionImexXref);
        Assert.assertNotNull("Interaction Intact Id :" + intactIdOfInteraction + " Identifier not present",
                interactionIntactIdentifier);

        Assert.assertNotNull("Participants is null",graphInteractionEvidence.getParticipants());
        Assert.assertEquals("Number of Participants is wrong",2,graphInteractionEvidence.getParticipants().size());

        GraphParticipantEvidence graphParticipantEvidence_P53010= null;
        GraphParticipantEvidence graphParticipantEvidence_P36102= null;

        String participant_P53010Ac="EBI-10052711";
        String participant_P36102Ac="EBI-10052709";
        for (GraphParticipantEvidence participantEvidence : graphInteractionEvidence.getParticipants()) {
            if (participantEvidence.getAc().equals(participant_P53010Ac)) {
                graphParticipantEvidence_P53010 = participantEvidence;
            } else if(participantEvidence.getAc().equals(participant_P36102Ac)) {
                graphParticipantEvidence_P36102= participantEvidence;
            }
        }
        Assert.assertNotNull("GraphParticipantEvidence with ac :" + participant_P53010Ac + " not present ",
                graphParticipantEvidence_P53010);
        Assert.assertNotNull("GraphParticipantEvidence with ac :" + graphParticipantEvidence_P36102 + " not present ",
                graphParticipantEvidence_P36102);


        Assert.assertNotNull("Stoichiometry is null",graphParticipantEvidence_P53010.getStoichiometry());
        Assert.assertEquals("Stoichiometry is null",1,graphParticipantEvidence_P53010.getStoichiometry().getMaxValue());
        Assert.assertNotNull("Biological Role is null",graphParticipantEvidence_P53010.getBiologicalRole());
        Assert.assertEquals("Biological Role text seems wrong","unspecified role",graphParticipantEvidence_P53010.
                getBiologicalRole().getShortName());
        Assert.assertEquals("Biological Role Mi Identifier is wrong","MI:0499",graphParticipantEvidence_P53010.
                getBiologicalRole().getMIIdentifier());
        Assert.assertNotNull("Experimental Role is null",graphParticipantEvidence_P53010.getExperimentalRole());
        Assert.assertEquals("Experimental Role text seems wrong","prey",graphParticipantEvidence_P53010.
                getExperimentalRole().getShortName());
        Assert.assertEquals("Experimental Role Mi Identifier is wrong","MI:0498",graphParticipantEvidence_P53010.
                getExperimentalRole().getMIIdentifier());
        Assert.assertNotNull("Identification Method is null",graphParticipantEvidence_P53010.getIdentificationMethods());
        Assert.assertEquals("Identification Methods count is wrong",1,graphParticipantEvidence_P53010.getIdentificationMethods().size());

        GraphCvTerm identificationMethod=graphParticipantEvidence_P53010.getIdentificationMethods().iterator().next();
        Assert.assertEquals("IdentificationMethod short name is wrong","molecular weight estimation by coomasie staining",
                identificationMethod.getFullName());
        Assert.assertEquals("IdentificationMethod","MI:0818",identificationMethod.getMIIdentifier());
        Assert.assertNotNull("Features is null",graphParticipantEvidence_P36102.getFeatures());
        Assert.assertEquals("Features Count is wrong",1,graphParticipantEvidence_P36102.getFeatures().size());

        GraphFeatureEvidence graphFeatureEvidence=graphParticipantEvidence_P36102.getFeatures().iterator().next();
        Assert.assertEquals("Feature has wrong name","strep tag",graphFeatureEvidence.getShortName());
        Assert.assertNotNull("Feature Type is null",graphFeatureEvidence.getType());
        Assert.assertEquals("Feature Type short name is wrong","strep ii tag",graphFeatureEvidence.getType().getShortName());
        Assert.assertEquals("Feature Type Mi Identifier is wrong","MI:0962",graphFeatureEvidence.getType().getMIIdentifier());
        Assert.assertNotNull("Feature Ranges are null",graphFeatureEvidence.getRanges());
    }
}
