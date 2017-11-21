package uk.ac.ebi.intact.graphdb.repositories;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.Protein;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InteractionRepositoryTest {

    public static final String P12345 = "P12345";
    public static final String P12346 = "P12346";
    public static final String P12347 = "P12347";

    @Autowired
    private Session session;

    @Autowired
    private ProteinRepository proteinRepository;
    @Autowired
    private InteractorRepository interactorRepository;
    @Autowired
    private InteractionRepository interactionRepository;

    @Before
    public void setUp() throws Exception {

        proteinRepository.deleteAll();
        interactorRepository.deleteAll();

//
        Protein p12345 = new Protein(P12345);
        Protein p12346 = new Protein(P12346);
        Protein p12347 = new Protein(P12347);

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

        p12345 = proteinRepository.findByAccession(p12345.getAccession());
//        p12345 = interactorRepository.findByAccession(p12345.getAccession());
        p12345.interactsWith(p12346, 0.0);
        p12345.interactsWith(p12347, 0.0);
        proteinRepository.save(p12345);

        p12346 = proteinRepository.findByAccession(p12346.getAccession());
//        p12346 = interactorRepository.findByAccession(p12346.getAccession());
        p12346.interactsWith(p12347, 0.0);

        // We already know that p12346 works with p12345
        proteinRepository.save(p12346);

        // We already know p12347 works with p12346 and p12345

    }

    @After
    public void tearDown() throws Exception {
        session.purgeDatabase();
    }

    @Test
    public void testInteractions() throws Exception {

        long count = interactionRepository.count();
        Assert.assertEquals(3, count);
        Page<Interaction> result = interactionRepository.findAll(new PageRequest(0, 10));
        Assert.assertEquals(3, result.getContent().size());
        for (Interaction interaction : interactionRepository.findAll()) {
            System.out.println(interaction);
        }

        Page<Interaction> interactionsPage = interactionRepository.findAll(new PageRequest(0, 10));
        Assert.assertEquals(3, interactionsPage.getContent().size());
        Assert.assertEquals(3, interactionsPage.getTotalElements());
        for (Interaction interaction : interactionsPage) {
            System.out.println(interaction);
        }

        result = interactionRepository.findByInteractorB_Accession(new PageRequest(0, 10), P12345);
        Assert.assertEquals(0, result.getNumberOfElements());

        for (Interaction interaction : result) {
            System.out.println(interaction.getInteractorA().getAccession() +
                    " interacts with " + interaction.getInteractorB().getAccession() + ".");
        }

        result = interactionRepository.findByInteractorA_Accession(new PageRequest(0, 10), P12345);
        Assert.assertEquals(0, result.getNumberOfElements());

        for (Interaction interaction : result) {
            System.out.println(interaction.getInteractorA().getAccession() +
                    " interacts with " + interaction.getInteractorB().getAccession() + ".");
        }

    }

}