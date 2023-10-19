package uk.ac.ebi.intact.graphdb.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    @Ignore
    public void getExperimentByInteractionAc() {
        //TODO...For future
    }
}
