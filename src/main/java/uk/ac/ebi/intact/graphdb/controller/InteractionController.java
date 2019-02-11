package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import psidev.psi.mi.jami.model.Interaction;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.services.GraphInteractionService;
import uk.ac.ebi.intact.graphdb.services.ImportInteractionService;

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
    public GraphInteractionEvidence getInteractionByAc(
            @RequestParam(value = "ac") String ac,
            @RequestParam(value = "depth", defaultValue = "2", required = false) int depth) {
        return graphInteractionService.findByInteractionAc(ac, depth);
    }
}
