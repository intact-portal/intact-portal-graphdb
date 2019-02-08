package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import psidev.psi.mi.jami.model.Interaction;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.services.GraphInteractionService;
import uk.ac.ebi.intact.graphdb.services.ImportInteractionService;

import java.util.List;
import java.util.Optional;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController("/")
public class InteractionController {

    final ImportInteractionService importInteractionService;

    @Autowired
    GraphInteractionService graphInteractionService;

    @Autowired
    public InteractionController(ImportInteractionService importInteractionService) {
        this.importInteractionService = importInteractionService;
    }

    @RequestMapping("/populate")
    public List<Interaction> populate() {
        return importInteractionService.importInteractions();
    }

    @RequestMapping(value = "/getInteractionByAc",
            params = {
                    "ac",
                    "depth"
            },
            method = RequestMethod.GET)
    public GraphInteractionEvidence getInteractionByAc(@RequestParam(value = "ac",required = false) String ac,
                                                                 @RequestParam(value = "depth",required = false) int depth) {
        return graphInteractionService. getInteractionEvidenceByAc(ac,depth);
    }
}
