package uk.ac.ebi.intact.graphdb.aspect;

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
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractionEvidenceRepository;

/**
 * Created by anjali on 26/07/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LazyFetchAspectTest {

    @Autowired
    private GraphInteractionEvidenceRepository graphInteractionEvidenceRepository;

    @Test
    public void getInteractionEvidenceByAc() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10000974";
        int depth = 0;
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
        Assert.assertNotNull("Interaction xrefs is null", graphInteractionEvidence.getXrefs());
        Assert.assertEquals("Interaction xref count is wrong", 1, graphInteractionEvidence.getXrefs().size());
    }
}
