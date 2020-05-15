package uk.ac.ebi.intact.graphdb.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.service.GraphInteractionService;
import uk.ac.ebi.intact.graphdb.service.GraphInteractorService;
import uk.ac.ebi.intact.graphdb.ws.controller.model.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by anjali on 24/04/20.
 */
@RestController
@RequestMapping("/network")
public class NetworkController {

    private GraphInteractionService graphInteractionService;
    private GraphInteractorService graphInteractorService;

    @Autowired
    public NetworkController(GraphInteractionService graphInteractionService,
                             GraphInteractorService graphInteractorService) {
        this.graphInteractionService = graphInteractionService;
        this.graphInteractorService = graphInteractorService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/data",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<NetworkJson> cytoscape(@RequestParam(value = "identifiers", required = false) Set<String> identifiers,
                                                 @RequestParam(value = "species", required = false) Set<Integer> species,
                                                /* @RequestParam(value = "neighboursRequired", required = false, defaultValue = "true") boolean neighboursRequired,*/
                                                 HttpServletRequest request) throws IOException {

        HttpStatus httpStatus = HttpStatus.OK;
        boolean neighboursRequired = true;// to be removed when accessed from request
        Instant processStarted = Instant.now();
        NetworkJson networkJson = new NetworkJson();
        try {

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(() -> {
                Iterable<Map<String, Object>> edgesIterable = graphInteractionService.findNetworkEdges(identifiers, species, neighboursRequired);
                networkJson.setEdges(edgesIterable);
            });
            executor.execute(() -> {
                Iterable<Map<String, Object>> nodesIterable = graphInteractorService.findNetworkNodes(identifiers, species, neighboursRequired);
                networkJson.setNodes(nodesIterable);
            });
            executor.shutdown();

            executor.awaitTermination(10, TimeUnit.MINUTES);
            executor.shutdownNow();

            Instant processEnded = Instant.now();
            Duration executionDuration = Duration.between(processStarted, processEnded);
            if (executionDuration.getSeconds() > 600) {
                httpStatus = HttpStatus.GATEWAY_TIMEOUT;
            }
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }

        if (networkJson.getNodes() == null || networkJson.getEdges() == null) {
            networkJson.setEdges(null);
            networkJson.setNodes(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<NetworkJson>(networkJson, headers, httpStatus);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/edge/details",
            produces = {APPLICATION_JSON_VALUE})
    public List<NetworkEdgeDetails> getEdgeDetails(@RequestParam(value = "ids", required = true) Set<Long> ids,
                                                   HttpServletRequest request) throws IOException {

        List<GraphBinaryInteractionEvidence> graphInteractionEvidences = graphInteractionService.findWithBinaryIds(ids, 0);

        return createNetworkEdgeDetails(graphInteractionEvidences);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/node/details",
            produces = {APPLICATION_JSON_VALUE})
    public List<NetworkNodeDetails> getNodeDetails(@RequestParam(value = "ids", required = true) Set<String> ids,
                                                   HttpServletRequest request) throws IOException {

        List<GraphInteractor> graphInteractors = graphInteractorService.findWithInteractorAcs(ids, 0);

        return createNetworkNodeDetails(graphInteractors);
    }

    /**
     * CONVERTS from GraphBinaryInteractionEvidence to CyAppInteractionDetails model
     **/
    private List<NetworkEdgeDetails> createNetworkEdgeDetails(List<GraphBinaryInteractionEvidence> graphBinaryInteractionEvidences) {

        List<NetworkEdgeDetails> networkEdgeDetails = new ArrayList<>();

        for (GraphBinaryInteractionEvidence graphBinaryInteractionEvidence : graphBinaryInteractionEvidences) {

            List<Annotation> annotations = new ArrayList<>();
            graphBinaryInteractionEvidence.getAnnotations().forEach(annotation -> {
                CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());
                annotations.add(new Annotation(term, annotation.getValue()));
            });

            List<Parameter> parameters = new ArrayList<>();
            graphBinaryInteractionEvidence.getParameters().forEach(parameter -> {
                CvTerm paramType = new CvTerm(parameter.getType().getShortName(), parameter.getType().getMIIdentifier());
                CvTerm paramUnit = new CvTerm(parameter.getUnit().getShortName(), parameter.getUnit().getMIIdentifier());
                parameters.add(new Parameter(paramType, paramUnit, parameter.getValue().toString()));

            });
            networkEdgeDetails.add(new NetworkEdgeDetails(graphBinaryInteractionEvidence.getGraphId(), annotations, parameters));
        }

        return networkEdgeDetails;
    }

    /**
     * CONVERTS from GraphInteractionEvidence and GraphExperiment to InteractionDetails model
     **/
    private List<NetworkNodeDetails> createNetworkNodeDetails(List<GraphInteractor> graphInteractors) {

        List<NetworkNodeDetails> networkNodeDetailsList = new ArrayList<>();

        for (GraphInteractor graphInteractor : graphInteractors) {

            List<Xref> xrefs = new ArrayList<>();
            graphInteractor.getXrefs().forEach(xref -> {
                CvTerm database = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
                CvTerm qualifier = null;
                if (xref.getQualifier() != null) {
                    qualifier = new CvTerm(xref.getQualifier().getShortName(), xref.getQualifier().getMIIdentifier());
                }
                xrefs.add(new Xref(database, xref.getId(), qualifier, xref.getAc()));
            });

            Collection<Alias> aliases = new ArrayList<>();
            graphInteractor.getAliases().forEach(alias -> {
                if (alias.getType() != null) {
                    aliases.add(new Alias(alias.getName(), new CvTerm(alias.getType().getShortName(), alias.getType().getMIIdentifier())));
                }
            });
            networkNodeDetailsList.add(new NetworkNodeDetails(graphInteractor.getAc(), xrefs, aliases));
        }

        return networkNodeDetailsList;
    }
}
