package uk.ac.ebi.intact.graphdb.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anjali on 08/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class GraphInteractionServiceTest {

    @Autowired
    private GraphInteractionService graphInteractionService;

    @Test
    public void getInteractionEvidenceByAc() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10000974";
        graphInteractionEvidence = graphInteractionService.findByInteractionAc(ac);

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
    public void findByInteractionAcs() {
        int pageNumber = 0;
        int pageSize = 10;
        int totalElements = 7;

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

        page = graphInteractionService.findByInteractionAcs(interactionAcsToTest, PageRequest.of(pageNumber, pageSize));
        Assert.assertEquals(pageNumber, page.getTotalPages()-1);
        Assert.assertEquals(totalElements, page.getTotalElements());
    }
}
