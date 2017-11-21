package uk.ac.ebi.intact.graphdb.repositories;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import psidev.psi.mi.jami.model.Interactor;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InteractorRepositoryTest {

    public static final String P12345 = "P12345";
    public static final String P12346 = "P12346";


//    @Autowired
//    private GraphDatabaseService graphDatabaseService;
//
//    @Autowired
//    Neo4jOperations neo4jOperations;

    //comment for used the server
    public static final String P12347 = "P12347";
    
    @Autowired
    protected ProteinRepository proteinRepository;
    @Autowired
    protected InteractorRepository interactorRepository;

    @Before
    public void setUp() throws Exception {

        proteinRepository.deleteAll();
        interactorRepository.deleteAll();

//
//        GraphProtein p12345 = new GraphProtein("P12345");
//        GraphProtein p12346 = new GraphProtein("P12346");
//        GraphProtein p12347 = new GraphProtein("P12347");

        Interactor p12345 = new GraphInteractor(P12345);
        Interactor p12346 = new GraphInteractor(P12346);
        Interactor p12347 = new GraphInteractor(P12347);

        // automatically persisted
//        proteinRepository.save(p12345);
//        proteinRepository.save(p12346);
//        proteinRepository.save(p12347);

        // we can create all the relationships and persist or create the relationships in one direction and
        // retrieve the previous persisted entity to avoid overwrite it

//        p12345.interactsWith(p12346);
//        p12345.interactsWith(p12347);
//
//        p12346.interactsWith(p12347);
//        p12346.interactsWith(p12345);
//
//        p12347.interactsWith(p12346);
//        p12347.interactsWith(p12345);
//
//        interactorRepository.save(p12345);
//        interactorRepository.save(p12346);
//        interactorRepository.save(p12347);

        //OR

        interactorRepository.save(p12345);
        interactorRepository.save(p12346);
        interactorRepository.save(p12347);

        p12345 = interactorRepository.findByShortName(p12345.getShortName());
//        p12345.interactsWith(p12346, 0.0);
//        p12345.interactsWith(p12347, 0.0);
        interactorRepository.save(p12345);

        p12346 = interactorRepository.findByShortName(p12346.getShortName());
//        p12346 = interactorRepository.findByShortName(p12346.getShortName());
//        p12346.interactsWith(p12347, 0.0);

        // We already know that p12346 works with p12345

        // We already know p12347 works with p12346 and p12345

    }

    @After
    public void tearDown() throws Exception {

        proteinRepository.deleteAll();
        interactorRepository.deleteAll();
    }

    @Test
    public void testInteractorRepository() throws Exception {

        Interactor interactorA = interactorRepository.findByShortName(P12345);
        Interactor interactorB = interactorRepository.findByShortName(P12346);
        Interactor interactorC = interactorRepository.findByShortName(P12347);
        Assert.assertEquals(interactorA.getShortName(), P12345);
        Assert.assertEquals(interactorB.getShortName(), P12346);
        Assert.assertEquals(interactorC.getShortName(), P12347);

//        Result<Interactor> interactorResultA = interactorRepository.findAllBySchemaPropertyValue("accession", P12345);
//        Result<Interactor> interactorResultB = interactorRepository.findAllBySchemaPropertyValue("accession", P12346);
//        Result<Interactor> interactorResultC = interactorRepository.findAllBySchemaPropertyValue("accession", P12347);
//        Assert.assertEquals(interactorResultA.single().getShortName(), P12345);
//        Assert.assertEquals(interactorResultB.single().getShortName(), P12346);
//        Assert.assertEquals(interactorResultC.single().getShortName(), P12347);

        Assert.assertEquals(interactorRepository.count(), 3);
        for (Interactor interactor : interactorRepository.findAll()) {
            System.out.println(interactor);
        }

        Page<Interactor> page = interactorRepository.findAll(new PageRequest(0, 10));
        Assert.assertEquals(page.getTotalElements(), 3);
        for (Interactor interactor : page.getContent()) {
            System.out.println(interactor);
        }

    }

    @Test
    public void testInteractorInteractions() throws Exception {

        System.out.println("Lookup each interactor by accession...");
        for (String name : new String[]{P12345, P12346, P12347}) {
            GraphInteractor interactor = (GraphInteractor) interactorRepository.findByShortName(name);
            Assert.assertEquals(interactor.getShortName(), name);
            Assert.assertNotNull(interactor.getInteractions());
            Assert.assertEquals(interactor.getInteractions().size(), 2);
            System.out.println(interactor.getInteractions());
        }
    }
}