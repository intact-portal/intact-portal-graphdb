package uk.ac.ebi.intact.graphdb.repositories;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import psidev.psi.mi.jami.model.Protein;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphProtein;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ProteinRepositoryTest {

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
    protected GraphProteinRepository proteinRepository;
    @Autowired
    protected GraphInteractorRepository graphInteractorRepository;

    @Before
    public void setUp() throws Exception {

        proteinRepository.deleteAll();

//
        GraphProtein p12345 = new GraphProtein("P12345");
        GraphProtein p12346 = new GraphProtein("P12346");
        GraphProtein p12347 = new GraphProtein("P12347");

//        Interactor p12345 = new Interactor(P12345);
//        Interactor p12346 = new Interactor(P12346);
//        Interactor p12347 = new Interactor(P12347);

        // automatically persisted
        proteinRepository.save(p12345);
        proteinRepository.save(p12346);
        proteinRepository.save(p12347);

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

//        interactorRepository.save(p12345);
//        interactorRepository.save(p12346);
//        interactorRepository.save(p12347);

//        p12345 = (GraphProtein) proteinRepository.findByShortName(p12345.getShortName());
//        p12345 = interactorRepository.findByAccession(p12345.getAccession());
//        p12345.interactsWith(p12346, 0.0);
//        p12345.interactsWith(p12347, 0.0);
        proteinRepository.save(p12345);

//        p12346 = (GraphProtein) proteinRepository.findByShortName(p12346.getShortName());
//        p12346 = interactorRepository.findByAccession(p12346.getAccession());
//        p12346.interactsWith(p12347, 0.0);

        // We already know that p12346 works with p12345
        proteinRepository.save(p12346);

        // We already know p12347 works with p12346 and p12345

    }

//    @After
//    public void tearDown() throws Exception {
//
//        proteinRepository.deleteAll();
//        interactorRepository.deleteAll();
//    }

    @Test
    public void testProteinRepository() throws Exception {

//        Protein proteinA = proteinRepository.findByShortName(P12345);
//        Protein proteinB = proteinRepository.findByShortName(P12346);
//        Protein proteinC = proteinRepository.findByShortName(P12347);
//        Assert.assertEquals(proteinA.getShortName(), P12345);
//        Assert.assertEquals(proteinB.getShortName(), P12346);
//        Assert.assertEquals(proteinC.getShortName(), P12347);

//        Result<GraphProtein> proteinResultA = proteinRepository.findAllBySchemaPropertyValue("accession", P12345);
//        Result<GraphProtein> proteinResultB = proteinRepository.findAllBySchemaPropertyValue("accession", P12346);
//        Result<GraphProtein> proteinResultC = proteinRepository.findAllBySchemaPropertyValue("accession", P12347);
//        Assert.assertEquals(proteinResultA.single().getAccession(), P12345);
//        Assert.assertEquals(proteinResultB.single().getAccession(), P12346);
//        Assert.assertEquals(proteinResultC.single().getAccession(), P12347);


        Assert.assertEquals(proteinRepository.count(), 3);
        for (Protein protein : proteinRepository.findAll()) {
            System.out.println(protein);
        }

//        Page<Protein> page = proteinRepository.findAll(new PageRequest(0, 10));
//        Assert.assertEquals(page.getTotalElements(), 3);
//        for (Protein protein : page.getContent()) {
//            System.out.println(protein);
//        }

    }

    @Test
    public void testProteinInteractions() throws Exception {

//        System.out.println("Lookup each protein by accession...");
//        for (String name : new String[]{P12345, P12346, P12347}) {
//            GraphProtein protein = (GraphProtein) proteinRepository.findByShortName(name);
//            Assert.assertEquals(protein.getShortName(), name);
//            Assert.assertNotNull(protein.getInteractions());
//            Assert.assertEquals(protein.getInteractions().size(), 2);
//            System.out.println(protein.getInteractions());
//        }
    }
}