package uk.ac.ebi.intact.graphdb.aspect;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.AnnotationUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParameter;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParameterValue;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractionEvidenceRepository;

import java.util.Collection;

/**
 * Created by anjali on 26/07/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LazyFetchAspectTest {

    @Autowired
    private GraphInteractionEvidenceRepository graphInteractionEvidenceRepository;

    @Test
    public void getInteractionEvidenceByAc() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10000974";
        int depth = 0;
        Page<GraphInteractionEvidence> page = graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0, 1),
                depth);
        if (page != null && !page.getContent().isEmpty()) {
            graphInteractionEvidence = page.getContent().get(0);
        }

        Assert.assertNotNull("Interaction is null", graphInteractionEvidence);
        Assert.assertEquals("Interaction is incorrect", ac, graphInteractionEvidence.getAc());

        //test for depth coverage

        Assert.assertNotNull("Experiment not present", graphInteractionEvidence.getExperiment());
        Assert.assertNotNull("Publication not present", graphInteractionEvidence.getExperiment().getPublication());
        Assert.assertEquals("Publication shortlabel not present", "25314077",
                ((GraphPublication) graphInteractionEvidence.getExperiment().getPublication()).getPubmedIdStr());
        Assert.assertNotNull("Interaction xrefs is null", graphInteractionEvidence.getXrefs());
        Assert.assertEquals("Interaction xref count is wrong", 1, graphInteractionEvidence.getXrefs().size());
    }

    @Test
    public void getInteractionEvidenceByAcForDetailsPage() {
        GraphInteractionEvidence graphInteractionEvidence = null;
        String ac = "EBI-10000974";
        int depth = 0;
        Page<GraphInteractionEvidence> page = graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0, 1),
                depth);
        if (page != null && !page.getContent().isEmpty()) {
            graphInteractionEvidence = page.getContent().get(0);
        }

        // interaction
        Assert.assertEquals("GraphInteractionEvidence is not correct", ac, graphInteractionEvidence.getAc());
        Assert.assertNotNull("Interaction type is null", graphInteractionEvidence.getInteractionType());
        Assert.assertEquals("Interaction type is wrong", "physical association", graphInteractionEvidence.getInteractionType().
                getShortName());

        //xrefs
        Assert.assertNotNull("Interaction xrefs is null", graphInteractionEvidence.getXrefs());
        Assert.assertEquals("Interaction xref count is wrong", 1, graphInteractionEvidence.getXrefs().size());

        String imexIdOfInteraction = "IM-23546-1";
        Collection<Xref> ieImexIdXrefs = XrefUtils.collectAllXrefsHavingDatabaseAndId(graphInteractionEvidence.getXrefs(),
                Xref.IMEX_MI, "imex", imexIdOfInteraction);
        Assert.assertNotNull("Interaction Imex Id :" + imexIdOfInteraction + " Identifier not present", ieImexIdXrefs);
        Assert.assertEquals("Interaction Imex Id count is wrong", 1, ieImexIdXrefs.size());

        //annotations
        Assert.assertNotNull("Interaction Annotations is null", graphInteractionEvidence.getAnnotations());
        Assert.assertEquals("Interaction annotations count is wrong", 1, graphInteractionEvidence.getAnnotations().size());

        String figure_legend = "S5D";
        Annotation figures = AnnotationUtils.collectFirstAnnotationWithTopicAndValue(graphInteractionEvidence.getAnnotations(),
                Annotation.FIGURE_LEGEND_MI, Annotation.FIGURE_LEGEND, "S5D");
        Assert.assertNotNull("Figure Legend :" + figure_legend + " not present", figures);

        //parameters
        Assert.assertNotNull("Interaction parameters is null", graphInteractionEvidence.getParameters());
        Assert.assertEquals("Interaction parameters count is wrong", 1, graphInteractionEvidence.getParameters().size());

        GraphParameter graphParameter = graphInteractionEvidence.getParameters().iterator().next();
        Assert.assertNotNull("GraphParameter Unit is null", graphParameter.getUnit());
        Assert.assertEquals("GraphParameter Unit name is wrong", "molar", graphParameter.getUnit().getShortName());
        Assert.assertNotNull("Graph Parameter Type is null", graphParameter.getType());
        Assert.assertEquals("Graph Parameter Type name is wrong", "ic50", graphParameter.getType().getShortName());

        GraphParameterValue ieParameterValue = (GraphParameterValue) graphParameter.getValue();
        Assert.assertNotNull("Interaction Evidence Parameter Value is null", ieParameterValue);
        Assert.assertEquals("Interaction Evidence Parameter Value - Net Value - is wrong", "0.1499999999999999944488848768742172978818416595458984375x10^(-9)", ieParameterValue.toString());

        //confidences
        Assert.assertTrue("Confidences should be empty", graphInteractionEvidence.getConfidences().isEmpty());


    }
}
