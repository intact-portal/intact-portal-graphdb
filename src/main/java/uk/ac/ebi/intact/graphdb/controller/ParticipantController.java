package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.graphdb.controller.model.ParticipantDetails;
import uk.ac.ebi.intact.graphdb.controller.model.ParticipantDetailsResult;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;
import uk.ac.ebi.intact.graphdb.services.GraphParticipantService;

/**
 * @author Elisabet Barrera
 */
@RestController
@RequestMapping("/graph/participants")
public class ParticipantController {
    private GraphParticipantService graphParticipantService;

    @Autowired
    public ParticipantController(GraphParticipantService graphParticipantService) {
        this.graphParticipantService = graphParticipantService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Participant Controller";
    }

    @RequestMapping(value = "/detailsOld/{ac}",
            params = {
                    "page",
                    "pageSize"
            },
            method = RequestMethod.GET)
    public Page<GraphParticipantEvidence> getParticipants(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return graphParticipantService.findByInteractionAc(ac, page, pageSize);
    }

    @RequestMapping(value = "/details/{ac}",
            params = {
                    "page",
                    "pageSize"
            },
            method = RequestMethod.GET)
    public Page<ParticipantDetails> getParticipantsNew(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<GraphParticipantEvidence> participantEvidences = graphParticipantService.findByInteractionAc(ac, page, pageSize);

        return new ParticipantDetailsResult(participantEvidences);
    }


}
