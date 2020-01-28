package uk.ac.ebi.intact.graphdb.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;

;

/**
 * Created by anjali on 13/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class GraphExperimentServiceTest {

    @Autowired
    GraphExperimentService graphExperimentService;

    @Test
    public void getExperimentByInteractionAc() {
        String interactionAc = "EBI-10052707";
        GraphExperiment graphExperiment = graphExperimentService.findByInteractionAc(interactionAc);

        Assert.assertNotNull("GraphExperiment not present", graphExperiment);
        Assert.assertEquals("GraphExperiment is incorrect", "EBI-9837129", graphExperiment.getAc());
    }
}
