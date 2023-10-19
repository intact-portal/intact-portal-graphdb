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
import uk.ac.ebi.intact.graphdb.model.nodes.*;

import java.util.List;

/**
 * Created by anjali on 12/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class GraphParticipantEvidenceRepositoryTest {

    @Autowired
    private GraphParticipantRepository graphParticipantEvidenceRepository;

    @Test
    public void getParticipantEvidencesByInteractionAc() {
        String interactionAc = "EBI-10052707";
        int pageNumber = 0;
        int pageSize = 10;
        Page<GraphParticipantEvidence> page = graphParticipantEvidenceRepository.findByInteractionAc(interactionAc,
                PageRequest.of(pageNumber, pageSize));

        Assert.assertNotNull("Page is Null", page);

        List<GraphParticipantEvidence> graphParticipantEvidenceList = page.getContent();
        Assert.assertNotNull("graphParticipantEvidenceList is null ", graphParticipantEvidenceList);
        Assert.assertEquals("Participant Count is wrong ", 2, graphParticipantEvidenceList.size());

        GraphParticipantEvidence graphParticipantEvidence = null;
        String participantAc = "EBI-10052709";
        for (GraphParticipantEvidence graphParticipantEvidence1 : graphParticipantEvidenceList) {
            if (graphParticipantEvidence1.getAc().equals(participantAc)) {
                graphParticipantEvidence = graphParticipantEvidence1;
            }
        }
        Assert.assertNotNull(participantAc + " Ac is not present", graphParticipantEvidence);

        Assert.assertNotNull("Experiment Role is null ", graphParticipantEvidence.getExperimentalRole());
        Assert.assertEquals("Experiment Role is wrong ", "bait",
                graphParticipantEvidence.getExperimentalRole().getShortName());
        Assert.assertEquals("Biological Role is wrong ", "unspecified role",
                graphParticipantEvidence.getBiologicalRole().getShortName());
        Assert.assertNotNull("Detection Methods is null ", graphParticipantEvidence.getIdentificationMethods());
        Assert.assertEquals("Detection Methods size is incorrect ", 1,
                graphParticipantEvidence.getIdentificationMethods().size());
        Assert.assertEquals("Detection Method is wrong ", "weight by comassie",
                graphParticipantEvidence.getIdentificationMethods().iterator().next().getShortName());
        Assert.assertNotNull("Experimental Preparation is null ", graphParticipantEvidence.getExperimentalPreparations());
        Assert.assertEquals("Experimental Preparation size is incorrect ", 2,
                graphParticipantEvidence.getExperimentalPreparations().size());

        Assert.assertEquals("Experimental Preparation Count is wrong ", 2,
                graphParticipantEvidence.getExperimentalPreparations().size());

        GraphCvTerm experimentalPreparation = null;
        String experimentalPreparationAc = "EBI-1537765";
        for (GraphCvTerm graphCvTerm : graphParticipantEvidence.getExperimentalPreparations()) {
            if (graphCvTerm.getAc().equals(experimentalPreparationAc)) {
                experimentalPreparation = graphCvTerm;
            }
        }

        Assert.assertNotNull("Experimental Praparation : " +
                experimentalPreparationAc + " not present", experimentalPreparation);
        Assert.assertEquals("Experimental Preparation is wrong ", "living cell", experimentalPreparation.getShortName());
        Assert.assertEquals("Confidences are expected to be empty ", 0, graphParticipantEvidence.getConfidences().size());
        Assert.assertEquals("Parameters are expected to be empty ", 0, graphParticipantEvidence.getParameters().size());
        Assert.assertNotNull("Aliases are null ", graphParticipantEvidence.getAliases());
        Assert.assertEquals("Aliases size is incorrect", 1, graphParticipantEvidence.getAliases().size());
        Assert.assertEquals("Alias is wrong", "Pan3", graphParticipantEvidence.getAliases().iterator().next().getName());
        Assert.assertNotNull("Features are null ", graphParticipantEvidence.getFeatures());
        Assert.assertEquals("Features size is incorrect", 1, graphParticipantEvidence.getFeatures().size());
        Assert.assertEquals("Feature is wrong", "strep tag",
                graphParticipantEvidence.getFeatures().iterator().next().getShortName());
        Assert.assertNotNull("Interactor is null", graphParticipantEvidence.getInteractor());
        Assert.assertNotNull("Interactor Ac is not present",
                ((GraphInteractor) graphParticipantEvidence.getInteractor()).getAc());
        Assert.assertEquals("Interactor is wrong", "pan3_yeast", graphParticipantEvidence.getInteractor().getShortName());
        Assert.assertNotNull("Interactor Type is null", graphParticipantEvidence.getInteractor().getInteractorType());
        Assert.assertEquals("Interactor Type is wrong", "protein",
                graphParticipantEvidence.getInteractor().getInteractorType().getShortName());
        Assert.assertEquals("Preferred Identifier is wrong", "P36102",
                ((GraphInteractor) graphParticipantEvidence.getInteractor()).getPreferredIdentifierStr());

        Assert.assertEquals("Interactor Alias Count is wrong ", 5,
                ((GraphInteractor) graphParticipantEvidence.getInteractor()).getAliases().size());
        GraphAlias graphAlias = null;
        String itorAliasAc = "EBI-55299";
        for (GraphAlias itorAlias : ((GraphInteractor) graphParticipantEvidence.getInteractor()).getAliases()) {
            if (itorAlias.getAc().equals(itorAliasAc)) {
                graphAlias = itorAlias;
            }
        }

        Assert.assertNotNull("Interactor Alias with ac:" + itorAliasAc + " not present", graphAlias);
        Assert.assertEquals("Interactor Alias is wrong", "PAN3", graphAlias.getName());
        Assert.assertEquals("Interactor Name is wrong", "pan3_yeast",
                graphParticipantEvidence.getInteractor().getShortName());
        Assert.assertNotNull("Interactor Species is null", graphParticipantEvidence.getInteractor().getOrganism());
        Assert.assertEquals("Interactor Species is wrong", "yeast",
                graphParticipantEvidence.getInteractor().getOrganism().getCommonName());

        String interactionAc2 = "EBI-10051289";
        Page<GraphParticipantEvidence> page2 = graphParticipantEvidenceRepository.findByInteractionAc(interactionAc2,
                PageRequest.of(pageNumber, pageSize));

        Assert.assertNotNull("Page is Null", page2);

        List<GraphParticipantEvidence> graphParticipantEvidenceList2 = page2.getContent();
        boolean annotationPresent = false;
        boolean xrefPresent = false;
        for (GraphParticipantEvidence graphParticipantEvidence1 : graphParticipantEvidenceList2) {
            if (graphParticipantEvidence1.getAnnotations() != null && !graphParticipantEvidence1.getAnnotations().isEmpty()) {
                Assert.assertEquals("Only one annotation was expected", 1, graphParticipantEvidence1.getAnnotations().size());
                GraphAnnotation graphAnnotation = graphParticipantEvidence1.getAnnotations().iterator().next();
                Assert.assertEquals("Annotation comment is wrong", "comment", graphAnnotation.getTopic().getShortName());
                Assert.assertEquals("Annotation desc is wrong", "Monomeric and dimeric forms of TfR1 detetcted in the precipitate.", graphAnnotation.getValue());
                annotationPresent = true;
            }

            if (graphParticipantEvidence1.getXrefs() != null && !graphParticipantEvidence1.getXrefs().isEmpty()) {
                xrefPresent = true;
            }
        }

        Assert.assertTrue("Annotation Should have been present", annotationPresent);
        Assert.assertFalse("Xref should not have been present", xrefPresent);


    }
}
