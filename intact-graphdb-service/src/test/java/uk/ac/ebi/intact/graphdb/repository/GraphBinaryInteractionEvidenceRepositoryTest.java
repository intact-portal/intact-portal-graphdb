package uk.ac.ebi.intact.graphdb.repository;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;

import java.util.Optional;

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
        Slice<GraphBinaryInteractionEvidence> slice;

        do {
            // findAll pagination does not work
            slice = graphBinaryInteractionEvidenceRepository.getAllGraphBinaryInteractionEvidences(PageRequest.of(pageNumber, pageSize));
            Assert.assertNotNull("Page is Null", slice);
            totalElements = totalElements + slice.getNumberOfElements();
            pageNumber++;
        } while (slice.hasNext());

        Assert.assertEquals(16, pageNumber);
        Assert.assertEquals(1534, totalElements);
        Assert.assertTrue(pageNumber > 1);
    }

    @Test
    @Ignore
    /*
     * TODO... This test won't be needed when we have binary ids in database
     * */
    public void findById() {
        int id = 20188;
        Optional<GraphBinaryInteractionEvidence> optionalGraphBinaryInteractionEvidence = graphBinaryInteractionEvidenceRepository.findWithBinaryId(id, 0);
        Assert.assertNotNull(optionalGraphBinaryInteractionEvidence.orElse(null));
    }

}