package uk.ac.ebi.intact.graphdb.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.internal.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;

import java.util.ArrayList;
import java.util.List;
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
        // null parameters check
        // with parameters check
        // interactor count check
        // interaction count check
        List<String> identifiers = new ArrayList<>();
        identifiers.add("Q9BZD4");
        identifiers.add("O14777");
        Iterable<Map<String, Object>> nodesIterable1 = graphBinaryInteractionEvidenceRepository.findBinaryInteractionsForCyAppNodes(identifiers);
        Assert.assertEquals(152, Iterables.count(nodesIterable1));

        /*Iterable<Map<String, Object>> nodesIterable2 = graphBinaryInteractionEvidenceRepository.findBinaryInteractionsForCyAppNodes(null);
        Assert.assertEquals(112316, Iterables.count(nodesIterable2));*/

        Iterable<Map<String, Object>> edgesIterable = graphBinaryInteractionEvidenceRepository.findBinaryInteractionsForCyAppEdges();
        Assert.assertEquals(432, Iterables.count(edgesIterable));
    }
}