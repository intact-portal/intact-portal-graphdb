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
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class GraphInteractorRepositoryTest {

     @Autowired
     private GraphInteractorRepository graphInteractorRepository;

     @Test
     public void checkInteractorPagination() {
          int pageNumber = 0;
          int pageSize = 1;
          int depth = 0;

          Page<GraphInteractor> page = graphInteractorRepository.findAll(PageRequest.of(pageNumber, pageSize), depth);
          Assert.assertNotNull("Page is Null", page);
          Assert.assertEquals(655, page.getTotalElements());

     }
}