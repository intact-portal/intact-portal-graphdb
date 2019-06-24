package uk.ac.ebi.intact.graphdb.controller;

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
import uk.ac.ebi.intact.graphdb.controller.model.FeatureDetails;
import uk.ac.ebi.intact.graphdb.controller.model.FeatureDetailsResult;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.services.GraphFeatureService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Elisabet Barrera
 */
@RestController
@RequestMapping("/features")
public class FeatureController {

    private GraphFeatureService graphFeatureService;

    @Autowired
    public FeatureController(GraphFeatureService graphFeatureService) {
        this.graphFeatureService = graphFeatureService;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/detailsOld/{ac}",
            method = RequestMethod.GET)
    public Page<GraphFeatureEvidence> getFeatures(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return graphFeatureService.findByInteractionAc(ac, page, pageSize);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/details/{ac}",
            method = RequestMethod.GET)
    public Page<FeatureDetails> getFeaturesWithPagination(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<GraphFeatureEvidence> featureEvidences = graphFeatureService.findByInteractionAc(ac, page, pageSize);

        return new FeatureDetailsResult(featureEvidences);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/datatables/{ac}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFeaturesDatatablesHandler(@PathVariable String ac,
                                                               HttpServletRequest request) throws IOException {

        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        Page<GraphFeatureEvidence> featureEvidences = graphFeatureService.findByInteractionAc(ac, page, pageSize);

        FeatureDetailsResult featureDetailsResult = new FeatureDetailsResult(featureEvidences);

        JSONObject result = new JSONObject();
        result.put("draw", request.getParameter("draw"));
        result.put("recordsTotal", featureDetailsResult.getTotalElements());
        result.put("recordsFiltered", featureDetailsResult.getTotalElements());

        JSONArray data = new JSONArray();

        for (FeatureDetails featureDetails : featureDetailsResult.getContent()) {
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, featureDetails);
            data.add(writer);
        }

        result.put("data", data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);

    }
}
