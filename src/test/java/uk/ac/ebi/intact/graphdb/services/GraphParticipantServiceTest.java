package uk.ac.ebi.intact.graphdb.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;

import java.util.List;

/**
 * Created by anjali on 12/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class GraphParticipantServiceTest {

    @Autowired
    private GraphParticipantService graphParticipantService;

    @Test
    public void getParticipantEvidencesByInteractionAc() {
        Page<GraphParticipantEvidence> graphParticipantEvidencePage = null;
        String interactionAc = "EBI-10052707";
        int pageNumber = 0;
        int pageSize = 10;
        graphParticipantEvidencePage = graphParticipantService.findByInteractionAc(interactionAc, pageNumber, pageSize);
        Assert.assertNotNull("Page is Null", graphParticipantEvidencePage);

        List<GraphParticipantEvidence> graphParticipantEvidenceList = graphParticipantEvidencePage.getContent();

        Assert.assertNotNull("graphParticipantEvidenceList is null ", graphParticipantEvidenceList);
        Assert.assertEquals("Participant Count is wrong ", 2, graphParticipantEvidenceList.size());

        GraphParticipantEvidence graphParticipantEvidence = null;
        String participantAc = "EBI-10052709";
        for (GraphParticipantEvidence graphParticipantEvidence1 : graphParticipantEvidenceList) {
            if (graphParticipantEvidence1.getAc().equals(participantAc)) {
                graphParticipantEvidence = graphParticipantEvidence1;
            }
        }
        Assert.assertNotNull(participantAc + " participant is not present", graphParticipantEvidence);
    }
}
