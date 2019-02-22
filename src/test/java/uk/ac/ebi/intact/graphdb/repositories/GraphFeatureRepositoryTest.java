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
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;

import java.util.List;

/**
 * Created by anjali on 14/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class GraphFeatureRepositoryTest {

    @Autowired
    GraphFeatureRepository graphFeatureRepository;

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
        Assert.assertNotNull("Participant is null", graphFeatureEvidence.getParticipantEvidence());
        Assert.assertEquals("Participant is not correct", "EBI-10000978", graphFeatureEvidence.getParticipantEvidence().getAc());
        Assert.assertNotNull("Interactor is null", graphFeatureEvidence.getParticipantEvidence().getInteractor());
        Assert.assertNotNull("Interactor preferredIdentifier xref not present",
                             graphFeatureEvidence.getParticipantEvidence().getInteractor().getPreferredIdentifier());
        Assert.assertEquals("Interactor preferredIdentifier xref is not correct", "EBI-9998887",
                             graphFeatureEvidence.getParticipantEvidence().getInteractor().getPreferredIdentifier().getId());

    }
}
