package uk.ac.ebi.intact.graphdb.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;

import java.util.List;

/**
 * Created by anjali on 14/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class GraphFeatureServiceTest {

    @Autowired
    GraphFeatureService graphFeatureService;

    @Test
    public void getFeaturesByInteractionAc() {
        String interactionAc = "EBI-10000974";
        int pageNumber = 0;
        int pageSize = 10;

        Page<GraphFeatureEvidence> page = graphFeatureService.findByInteractionAc(interactionAc, pageNumber, pageSize);
        Assert.assertNotNull("Page is Null", page);

        List<GraphFeatureEvidence> graphFeatureEvidenceList = page.getContent();
        Assert.assertNotNull("Features is null ", graphFeatureEvidenceList);
        Assert.assertEquals("Features Count is wrong ", 4, graphFeatureEvidenceList.size());

    }
}
