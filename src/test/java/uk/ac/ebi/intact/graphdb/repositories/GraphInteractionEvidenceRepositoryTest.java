package uk.ac.ebi.intact.graphdb.repositories;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;

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
    public void getInteractionEvidenceByAc(){
        GraphInteractionEvidence graphInteractionEvidence=null;
        String ac="EBI-10000974";
        int depth=2;
        Page<GraphInteractionEvidence> page= graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0,1), depth);
        if(page!=null&&page.getContent()!=null&&!page.getContent().isEmpty()){
            graphInteractionEvidence=page.getContent().get(0);
        }

        Assert.assertEquals(ac,graphInteractionEvidence.getAc());
    }
}
