package uk.ac.ebi.intact.graphdb.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;

/**
 * Created by anjali on 08/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class GraphInteractionServiceTest {

    @Autowired
    private GraphInteractionService graphInteractionService;

    @Test
    public void getInteractionEvidenceByAc() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10000974";
        int depth = 2;
        graphInteractionEvidence = graphInteractionService.findByInteractionAc(ac, depth);

        Assert.assertEquals("Interaction not present", ac, graphInteractionEvidence.getAc());
    }

    @Test
    public void findByInteractionAcForMiJson() {
        String interactionAc = "EBI-10052707";
        GraphInteractionEvidence graphInteractionEvidence = graphInteractionService.findByInteractionAcForMiJson(interactionAc);

        Assert.assertNotNull("GraphInteractionEvidence not present", graphInteractionEvidence);
        Assert.assertEquals("GraphInteractionEvidence is incorrect", interactionAc, graphInteractionEvidence.getAc());
    }

    @Test
    public void findByInteractionAcForMiJson(){
        String interactionAc="EBI-10052707";
        GraphInteractionEvidence graphInteractionEvidence=graphInteractionService.findByInteractionAcForMiJson(interactionAc);

        Assert.assertNotNull("GraphInteractionEvidence not present",graphInteractionEvidence);
        Assert.assertEquals("GraphInteractionEvidence is incorrect",interactionAc,graphInteractionEvidence.getAc());
    }
}
