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
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

import java.util.Collection;
import java.util.List;

/**
 * Created by anjali on 14/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class GraphFeatureRepositoryTest {

    @Autowired
    GraphFeatureRepository graphFeatureRepository;

    @Test
    public void checkFeaturePagination() {
        String interactionAc = "EBI-10049645";
        int pageNumber = 0;
        int pageSize = 1;

        Page<GraphFeatureEvidence> page1 = graphFeatureRepository.findByInteractionAc(interactionAc, PageRequest.of(pageNumber, pageSize));
        Assert.assertNotNull("Page is Null", page1);
        Assert.assertEquals("Page1 should contain only 1 feature", 1, page1.getContent().size());

        Page<GraphFeatureEvidence> page2 = graphFeatureRepository.findByInteractionAc(interactionAc, page1.nextPageable());
        Assert.assertNotNull("Page is Null", page2);
        Assert.assertEquals("Page2 should contain only 1 feature", 1, page2.getContent().size());

    }

    @Test
    public void getFeaturesByInteractionAc() {
        String interactionAc = "EBI-10000974";
        int pageNumber = 0;
        int pageSize = 10;

        Page<GraphFeatureEvidence> page = graphFeatureRepository.findByInteractionAc(interactionAc, PageRequest.of(pageNumber, pageSize));
        Assert.assertNotNull("Page is Null", page);

        List<GraphFeatureEvidence> graphFeatureEvidenceList = page.getContent();
        Assert.assertNotNull("Features is null ", graphFeatureEvidenceList);
        Assert.assertEquals("Features Count is wrong ", 4, graphFeatureEvidenceList.size());

        GraphFeatureEvidence graphFeatureEvidence = null;
        String featureAc = "EBI-10000987";
        for (GraphFeatureEvidence graphFeatureEvidence1 : graphFeatureEvidenceList) {
            if (graphFeatureEvidence1.getAc().equals(featureAc)) {
                graphFeatureEvidence = graphFeatureEvidence1;
            }
        }

        Assert.assertNotNull("Feature :" + featureAc + " not present", graphFeatureEvidence);
        Assert.assertEquals("Feature short label is incorrect", "n_terminus", graphFeatureEvidence.getShortName());
        Assert.assertNotNull("Feature Type is null", graphFeatureEvidence.getType());
        Assert.assertEquals("Feature Type is incorrect", "acres", graphFeatureEvidence.getType().getShortName());
        Assert.assertNotNull("Feature ranges is null", graphFeatureEvidence.getRanges());
        Assert.assertEquals("Feature range count is incorrect", 1, graphFeatureEvidence.getRanges().size());
        Assert.assertEquals("Feature range is incorrect", "n-n", graphFeatureEvidence.getRanges().iterator().next().getRangeString());
        Assert.assertNotNull("Participant is null", graphFeatureEvidence.getParticipant());
        Assert.assertEquals("Participant is not correct", "EBI-10000978", ((GraphParticipantEvidence) graphFeatureEvidence.getParticipant()).getAc());
        Assert.assertNotNull("Interactor is null", graphFeatureEvidence.getParticipant().getInteractor());
        Assert.assertNotNull("Interactor preferredIdentifier xref not present",
                graphFeatureEvidence.getParticipant().getInteractor().getPreferredIdentifier());
        Assert.assertEquals("Interactor preferredIdentifier xref is not correct", "EBI-9998887",
                graphFeatureEvidence.getParticipant().getInteractor().getPreferredIdentifier().getId());


        String interactionAc2 = "EBI-1003953";

        Page<GraphFeatureEvidence> page2 = graphFeatureRepository.findByInteractionAc(interactionAc2, PageRequest.of(pageNumber, pageSize));
        Assert.assertNotNull("Page is Null", page2);

        List<GraphFeatureEvidence> graphFeatureEvidenceList2 = page2.getContent();
        Assert.assertNotNull("Features is null ", graphFeatureEvidenceList2);
        Assert.assertEquals("Features Count is wrong ", 5, graphFeatureEvidenceList2.size());

        GraphFeatureEvidence graphFeatureEvidence2 = null;
        String featureAc2 = "EBI-1003985";
        for (GraphFeatureEvidence graphFeatureEvidence1 : graphFeatureEvidenceList2) {
            if (graphFeatureEvidence1.getAc().equals(featureAc2)) {
                graphFeatureEvidence2 = graphFeatureEvidence1;
            }
        }
        Assert.assertNotNull("Feature :" + featureAc + " not present", graphFeatureEvidence2);
        Assert.assertNotNull("Xrefs should not empty", graphFeatureEvidence2.getXrefs() == null || graphFeatureEvidence2.getXrefs().isEmpty());
        Assert.assertEquals("Xrefs count is wrong", 1, graphFeatureEvidence2.getXrefs().size());
        GraphXref graphXref = graphFeatureEvidence2.getXrefs().iterator().next();
        Assert.assertNotNull("Xref database is null", graphXref.getDatabase());
        Assert.assertEquals("Xref database is wrong", "interpro", graphXref.getDatabase().getShortName());
        Assert.assertEquals("Xref identifier is wrong", "IPR000712", graphXref.getId());

        Assert.assertNotNull("Identifiers should not empty", graphFeatureEvidence2.getIdentifiers() == null || graphFeatureEvidence2.getIdentifiers().isEmpty());
        Assert.assertEquals("Xrefs count is wrong", 1, graphFeatureEvidence2.getIdentifiers().size());
        GraphXref identifier = graphFeatureEvidence2.getIdentifiers().iterator().next();
        Assert.assertNotNull("Xref database is null", identifier.getDatabase());
        Assert.assertEquals("Xref database is wrong", "intact", identifier.getDatabase().getShortName());
        Assert.assertEquals("Xref identifier is wrong", "EBI-1003985", identifier.getId());

        Collection<GraphCvTerm> detectionmethods = graphFeatureEvidence2.getDetectionMethods();
        Assert.assertFalse("Detection method should not be empty", detectionmethods == null || detectionmethods.isEmpty());
        Assert.assertEquals("Only one detection method was expected", 1, detectionmethods.size());
        GraphCvTerm detectionMethod = graphFeatureEvidence2.getDetectionMethods().iterator().next();
        Assert.assertEquals("Detection Method is wrong", "mutation analysis", detectionMethod.getShortName());

        Assert.assertNull("Role is expected to be null", graphFeatureEvidence2.getRole());
        Assert.assertTrue("Parameters are expected to be null", graphFeatureEvidence2.getParameters() == null || graphFeatureEvidence2.getParameters().isEmpty());


    }
}
