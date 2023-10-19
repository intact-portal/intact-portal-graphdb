package uk.ac.ebi.intact.graphdb.ws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.graphdb.ws.controller.model.ParticipantDetails;
import uk.ac.ebi.intact.graphdb.ws.controller.model.ParticipantDetailsResult;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;
import uk.ac.ebi.intact.graphdb.service.GraphParticipantService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Elisabet Barrera
 */
@RestController
@RequestMapping("/participants")
public class ParticipantController {
    private GraphParticipantService graphParticipantService;

    @Autowired
    public ParticipantController(GraphParticipantService graphParticipantService) {
        this.graphParticipantService = graphParticipantService;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/detailsOld/{ac}",
            method = RequestMethod.GET)
    public Page<GraphParticipantEvidence> getParticipantsOld(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return graphParticipantService.findByInteractionAc(ac, page, pageSize);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/details/{ac}",
            method = RequestMethod.GET)
    public Page<ParticipantDetails> getParticipantsNWithPagination(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<GraphParticipantEvidence> participantEvidences = graphParticipantService.findByInteractionAc(ac, page, pageSize);

        return new ParticipantDetailsResult(participantEvidences);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/datatables/{ac}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getParticipantsDatatablesHandler(@PathVariable String ac,
                                                                   HttpServletRequest request) throws IOException {

        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        Page<GraphParticipantEvidence> participantEvidences = graphParticipantService.findByInteractionAc(ac, page, pageSize);

        ParticipantDetailsResult participantDetailsResult = new ParticipantDetailsResult(participantEvidences);

        JSONObject result = new JSONObject();
        result.put("draw", request.getParameter("draw"));
        result.put("recordsTotal", participantDetailsResult.getTotalElements());
        result.put("recordsFiltered", participantDetailsResult.getTotalElements());

        JSONArray data = new JSONArray();

        for (ParticipantDetails participantDetails : participantDetailsResult.getContent()) {
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, participantDetails);
            data.add(writer);
        }

        result.put("data", data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);

    }
}
