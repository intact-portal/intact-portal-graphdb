package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import psidev.psi.mi.jami.model.Interaction;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;
import uk.ac.ebi.intact.graphdb.services.*;

import java.util.List;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController
@RequestMapping("/graph/interaction")
public class InteractionController {

    private GraphInteractionService graphInteractionService;

    @Autowired
    private GraphParticipantService graphParticipantService;

    @Autowired
    private GraphExperimentService graphExperimentService;

    @Autowired
    private GraphFeatureService graphFeatureService;

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

    @RequestMapping(value = "/getParticpantByInteractionAc",
            params = {
                    "ac","pageNo","pageSize"

            },
            method = RequestMethod.GET)
    public Page<GraphParticipantEvidence> getParticpantByInteractionAc
            (@RequestParam(value = "ac",required = false) String ac,
             @RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
             @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        return graphParticipantService.findByInteractionAc(ac, pageNo, pageSize);
    }

    @RequestMapping(value = "/getExpPubByInteractionAc",
            params = {
                    "ac"

            },
            method = RequestMethod.GET)
    public GraphExperiment getExpPubByInteractionAc
            (@RequestParam(value = "ac",required = false) String ac) {
        return graphExperimentService.findByInteractionAc(ac);
    }

    @RequestMapping(value = "/getFeaturesByInteractionAc",
            params = {
                    "ac","pageNo","pageSize"

            },
            method = RequestMethod.GET)
    public Page<GraphFeatureEvidence> getFeaturesByInteractionAc
            (@RequestParam(value = "ac",required = false) String ac,
             @RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
             @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        return graphFeatureService.findByInteractionAc(ac, pageNo, pageSize);
    }
}
