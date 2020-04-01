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
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(readOnly = true)
public class GraphBinaryInteractionEvidenceRepositoryTest {

    @Autowired
    private GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository;

    @Test
    public void testGraphBinaryInteractionEvidenceRepositoryPagination() {
        int pageNumber = 0;
        int totalElements = 0;
        int pageSize = 100;
        int depth = 0;
        Page<GraphBinaryInteractionEvidence> page;

        do {
            page = graphBinaryInteractionEvidenceRepository.findAll(PageRequest.of(pageNumber, pageSize), depth);
            Assert.assertNotNull("Page is Null", page);
            totalElements = totalElements + page.getNumberOfElements();
            pageNumber++;
        } while (page.hasNext());

        Assert.assertEquals(pageNumber, page.getTotalPages());
        Assert.assertEquals(totalElements, page.getTotalElements());
        Assert.assertEquals(1534, totalElements);
        Assert.assertTrue(pageNumber > 1);
    }

    @Test
    public void testCytoscapeAppQuery() {
        Iterable<Map<String, Object>> iterable = graphBinaryInteractionEvidenceRepository.findBinaryInteractionsForCyApp();
        System.out.println(iterable);
    }
}