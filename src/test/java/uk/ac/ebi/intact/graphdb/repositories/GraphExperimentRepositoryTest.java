package uk.ac.ebi.intact.graphdb.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * Created by anjali on 13/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class GraphExperimentRepositoryTest {

    @Autowired
    private GraphExperimentRepository graphExperimentRepository;

    @Test
    public void getExperimentByInteractionAc() {
        String interactionAc = "EBI-10052707";

        Optional<GraphExperiment> graphExperimentOptional = graphExperimentRepository.findByInteractionAc(interactionAc);

        Assert.assertTrue("GraphExperiment is not present ", graphExperimentOptional.isPresent());

        GraphExperiment graphExperiment = graphExperimentOptional.get();
        Assert.assertEquals("GraphExperiment is not correct", "EBI-9837129", graphExperiment.getAc());
        Assert.assertNotNull("Host Organism is null", graphExperiment.getHostOrganism());
        Assert.assertEquals("Host Organism is incorrect", "in vitro", graphExperiment.getHostOrganism().getCommonName());
        Assert.assertNotNull("Interaction Detection method is null", graphExperiment.getInteractionDetectionMethod());
        Assert.assertEquals("Interaction Detection Method is incorect", "pull down",
                           graphExperiment.getInteractionDetectionMethod().getShortName());
        Assert.assertNotNull("Experiment Xrefs is null", graphExperiment.getXrefs());
        Assert.assertEquals("Experiment Xrefs count is wrong", 2, graphExperiment.getXrefs().size());

        GraphXref experimentXref = null;
        String imexIdOfExp = "IM-23527";
        for (GraphXref xref : graphExperiment.getXrefs()) {
            if (xref.getId() != null && xref.getId().equals(imexIdOfExp)) {
                experimentXref = xref;
            }
        }
        Assert.assertNotNull("Experiment Imex Id :" + imexIdOfExp + " Xref not present", experimentXref);

        Assert.assertNotNull("Experiment Annotations is null", graphExperiment.getAnnotations());
        Assert.assertEquals("Experiment Annotations count is wrong", 8, graphExperiment.getAnnotations().size());

        GraphPublication graphPublication = (GraphPublication) graphExperiment.getPublication();

        Assert.assertNotNull("GraphPublication is not present", graphPublication);
        Assert.assertEquals("GraphPublication is not correct", "EBI-9836453", graphPublication.getAc());
        Assert.assertEquals("pubmedid is not correct", "24872509", graphPublication.getPubmedIdStr());
        Assert.assertEquals("Publication Title is wrong", "Structural basis for Pan3 binding to Pan2 and its function" +
                           " in mRNA recruitment and deadenylation.", graphPublication.getTitle());
        Assert.assertEquals("Journal is wrong", "EMBO J. (0261-4189)", graphPublication.getJournal());
        try {
            Assert.assertEquals("Publication Date is wrong", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2014"),
                                graphPublication.getPublicationDate());
        } catch (Exception parseException) {
            parseException.printStackTrace();
        }

        Assert.assertNotNull("Publication Xrefs is null", graphPublication.getXrefs());
        Assert.assertEquals("Publication Xrefs count is wrong", 1, graphPublication.getXrefs().size());

        GraphXref publicationXref = null;
        String imexIdOfPub = "IM-23527";
        for (GraphXref xref : graphPublication.getXrefs()) {
            if (xref.getId() != null && xref.getId().equals(imexIdOfPub)) {
                publicationXref = xref;
            }
        }

        Assert.assertNotNull("Publication Imex Id :" + imexIdOfPub + " Xref not present", publicationXref);

        Assert.assertNotNull("Publication Annotations is null", graphPublication.getAnnotations());
        Assert.assertEquals("Publication Annotations count is wrong", 4, graphPublication.getAnnotations().size());

    }
}
