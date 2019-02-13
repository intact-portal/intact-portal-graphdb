package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.graphdb.controller.model.InteractionDetails;
import uk.ac.ebi.intact.graphdb.controller.model.TypeValueObject;
import uk.ac.ebi.intact.graphdb.controller.model.InteractionDetailsXRefs;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.services.GraphInteractionService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController
@RequestMapping("/graph/interaction")
public class InteractionController {

    private GraphInteractionService graphInteractionService;

    @Autowired
    public InteractionController(GraphInteractionService graphInteractionService) {
        this.graphInteractionService = graphInteractionService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot GraphDB Example";
    }

    @RequestMapping(value = "/details",
            params = {"ac"},
            method = RequestMethod.GET)
    public InteractionDetails getInteractionDetails(
            @RequestParam(value = "ac") String ac,
            @RequestParam(value = "depth", defaultValue = "2", required = false) int depth) {

        GraphInteractionEvidence gie = graphInteractionService.findByInteractionAc(ac, depth);

        return createInteractionDetails(gie);
    }

    @RequestMapping(value = "/detailsOld",
            params = {"ac"},
            method = RequestMethod.GET)
    public GraphInteractionEvidence getDetailsOld(
            @RequestParam(value = "ac") String ac,
            @RequestParam(value = "depth", defaultValue = "2", required = false) int depth) {
        return graphInteractionService.findByInteractionAc(ac, depth);
    }

    /**
     * CONVERTS from GraphInteractionEvidence to InteractionDetails model
     **/
    private InteractionDetails createInteractionDetails(GraphInteractionEvidence graphInteractionEvidence) {

        String ac = graphInteractionEvidence.getAc();

        String interactionType = graphInteractionEvidence.getInteractionType().getShortName();

        List<InteractionDetailsXRefs> xrefs = new ArrayList<>();
        graphInteractionEvidence.getXrefs().forEach(xref ->
                xrefs.add(new InteractionDetailsXRefs(xref.getDatabase().getShortName(), xref.getId())));

        List<TypeValueObject> annotations = new ArrayList<>();
        graphInteractionEvidence.getAnnotations().forEach(annotation ->
                annotations.add(new TypeValueObject(annotation.getTopic().getShortName(), annotation.getValue())));

        List<TypeValueObject> parameters = new ArrayList<>();
        graphInteractionEvidence.getParameters().forEach(param ->
                parameters.add(new TypeValueObject(param.getType().getShortName(), param.getValue().toString())));

        List<TypeValueObject> confidences = new ArrayList<>();
        graphInteractionEvidence.getConfidences().forEach(confidence ->
                confidences.add(new TypeValueObject(confidence.getType().getShortName(), confidence.getValue())));

        return new InteractionDetails(ac, interactionType, xrefs, annotations, parameters, confidences);
    }

}
