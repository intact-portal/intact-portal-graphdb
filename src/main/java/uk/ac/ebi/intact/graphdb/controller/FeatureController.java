package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.graphdb.controller.model.FeatureDetails;
import uk.ac.ebi.intact.graphdb.controller.model.FeatureDetailsResult;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.services.GraphFeatureService;

/**
 * @author Elisabet Barrera
 */
@RestController
@RequestMapping("/graph/features")
public class FeatureController {

    private GraphFeatureService graphFeatureService;

    @Autowired
    public FeatureController(GraphFeatureService graphFeatureService) {
        this.graphFeatureService = graphFeatureService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Feature Controller";
    }

    @RequestMapping(value = "/detailsOld/{ac}",
            method = RequestMethod.GET)
    public Page<GraphFeatureEvidence> getFeatures(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return graphFeatureService.findByInteractionAc(ac, page, pageSize);
    }

    @RequestMapping(value = "/details/{ac}",
            method = RequestMethod.GET)
    public Page<FeatureDetails> getFeaturesWithPagination(
            @PathVariable String ac,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<GraphFeatureEvidence> featureEvidences = graphFeatureService.findByInteractionAc(ac, page, pageSize);

        return new FeatureDetailsResult(featureEvidences);
    }
}
