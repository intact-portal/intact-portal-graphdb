package uk.ac.ebi.intact.graphdb.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psidev.psi.mi.jami.bridges.exception.BridgeFailedException;
import psidev.psi.mi.jami.bridges.ols.CachedOlsOntologyTermFetcher;
import psidev.psi.mi.jami.datasource.InteractionWriter;
import psidev.psi.mi.jami.factory.InteractionWriterFactory;
import psidev.psi.mi.jami.json.InteractionViewerJson;
import psidev.psi.mi.jami.json.MIJsonOptionFactory;
import psidev.psi.mi.jami.json.MIJsonType;
import psidev.psi.mi.jami.model.InteractionCategory;
import psidev.psi.mi.jami.model.InteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.service.GraphExperimentService;
import uk.ac.ebi.intact.graphdb.service.GraphInteractionService;
import uk.ac.ebi.intact.graphdb.ws.controller.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController
@RequestMapping("/interaction")
public class InteractionController {

    private GraphInteractionService graphInteractionService;
    private GraphExperimentService graphExperimentService;

    @Autowired
    public InteractionController(GraphInteractionService graphInteractionService,
                                 GraphExperimentService graphExperimentService) {
        this.graphInteractionService = graphInteractionService;
        this.graphExperimentService = graphExperimentService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/details/{ac}", produces = {APPLICATION_JSON_VALUE})
    public InteractionDetails getInteractionDetails(
            @PathVariable String ac) {

        GraphInteractionEvidence graphInteractionEvidence = graphInteractionService.findByInteractionAc(ac);

        return createInteractionDetails(graphInteractionEvidence);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/detailsOld",
            params = {"ac"},
            produces = {APPLICATION_JSON_VALUE})
    public GraphInteractionEvidence getDetailsOld(
            @RequestParam(value = "ac") String ac,
            @RequestParam(value = "depth", defaultValue = "2", required = false) int depth) {
        return graphInteractionService.findByInteractionAc(ac);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/export/{ac}")
    public ResponseEntity<String> exportInteraction(@PathVariable String ac,
                                                    @RequestParam(value = "format", defaultValue = "json", required = false) String format,
                                                    HttpServletResponse response) throws Exception {
        Boolean exportAsFile = false;

        InteractionEvidence interactionEvidence = graphInteractionService.findByInteractionAcForMiJson(ac);

        ResponseEntity<String> responseEntity = null;
        if (interactionEvidence != null) {
            InteractionWriterFactory writerFactory = InteractionWriterFactory.getInstance();
            switch (InteractionExportFormat.formatOf(format)) {
                case JSON:
                default:
                    responseEntity = createJsonResponse(interactionEvidence, writerFactory);
                    break;
            }
            return responseEntity;
        }
        throw new Exception("Export failed " + ac + ". No Interaction result");
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/cytoscape", produces = {APPLICATION_JSON_VALUE})
    public Iterable<Map<String, Object>> cytoscape(
            @RequestParam(value = "identifiers", required = false) List<String> identifiers) {
        return graphInteractionService.findCyAppNodes(identifiers);
    }

    /**
     * CONVERTS from GraphInteractionEvidence and GraphExperiment to InteractionDetails model
     **/
    private InteractionDetails createInteractionDetails(GraphInteractionEvidence graphInteractionEvidence) {
        String ac = graphInteractionEvidence.getAc();

        String interactionType = graphInteractionEvidence.getInteractionType().getShortName();

        String shortLabel = graphInteractionEvidence.getShortName();

        List<Xref> xrefs = new ArrayList<>();
        graphInteractionEvidence.getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            xrefs.add(new Xref(term, xref.getId()));
        });

        List<Annotation> annotations = new ArrayList<>();
        graphInteractionEvidence.getAnnotations().forEach(annotation -> {
            CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());
            annotations.add(new Annotation(term, annotation.getValue()));
        });

        List<Parameter> parameters = new ArrayList<>();
        graphInteractionEvidence.getParameters().forEach(parameter -> {
            CvTerm paramType = new CvTerm(parameter.getType().getShortName(), parameter.getType().getMIIdentifier());
            CvTerm paramUnit = new CvTerm(parameter.getUnit().getShortName(), parameter.getUnit().getMIIdentifier());
            parameters.add(new Parameter(paramType, paramUnit, parameter.getValue().toString()));

        });

        List<Confidence> confidences = new ArrayList<>();
        graphInteractionEvidence.getConfidences().forEach(confidence -> {
            CvTerm cvTerm = new CvTerm(confidence.getType().getShortName(), confidence.getType().getMIIdentifier());
            confidences.add(new Confidence(cvTerm, confidence.getValue()));
        });

        GraphExperiment graphExperiment = (GraphExperiment) graphInteractionEvidence.getExperiment();

        ExperimentDetails experimentDetails = createExperimentDetails(graphExperiment);
        PublicationDetails publicationDetails = createPublicationDetails(graphExperiment);

        return new InteractionDetails(ac, interactionType, shortLabel, xrefs, annotations, parameters, confidences, experimentDetails, publicationDetails);
    }

    private ExperimentDetails createExperimentDetails(GraphExperiment graphExperiment) {
        String experimentAc = graphExperiment.getAc();
        String hostOrganism = graphExperiment.getHostOrganism().getScientificName();
        String interactionDetMethod = graphExperiment.getInteractionDetectionMethod().getShortName();
        String participantDetMethod = null;
        if (graphExperiment.getParticipantDetectionMethod() != null) {
            participantDetMethod = graphExperiment.getParticipantDetectionMethod().getShortName();
        }

        List<Xref> experimentXrefs = new ArrayList<>();
        graphExperiment.getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            experimentXrefs.add(new Xref(term, xref.getId()));
        });

        List<Annotation> experimentAnnotations = new ArrayList<>();
        graphExperiment.getAnnotations().forEach(annotation -> {
            CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());

            experimentAnnotations.add(new Annotation(term, annotation.getValue()));
        });

        return new ExperimentDetails(experimentAc, interactionDetMethod, participantDetMethod, hostOrganism, experimentXrefs, experimentAnnotations);
    }

    private PublicationDetails createPublicationDetails(GraphExperiment graphExperiment) {

        GraphPublication graphPublication = (GraphPublication) graphExperiment.getPublication();
        String pubmedId = graphPublication.getPubmedIdStr(); //TODO: WHEN FIXED extract from the pubmedId method
        String title = graphExperiment.getPublication().getTitle();
        String journal = graphExperiment.getPublication().getJournal();
        List<String> authors = graphExperiment.getPublication().getAuthors();
        Date publicationDate = graphExperiment.getPublication().getPublicationDate();

        List<Xref> publicationXrefs = new ArrayList<>();
        graphExperiment.getPublication().getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            publicationXrefs.add(new Xref(term, xref.getId()));
        });

        List<Annotation> publicationAnnotation = new ArrayList<>();
        graphExperiment.getPublication().getAnnotations().forEach(annotation -> {
            CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());
            publicationAnnotation.add(new Annotation(term, annotation.getValue()));
        });

        return new PublicationDetails(pubmedId, title, journal, authors, publicationDate, publicationXrefs, publicationAnnotation);
    }


    private ResponseEntity<String> createJsonResponse(InteractionEvidence interactionEvidence,
                                                      InteractionWriterFactory writerFactory) {

        InteractionViewerJson.initialiseAllMIJsonWriters();
        MIJsonOptionFactory optionFactory = MIJsonOptionFactory.getInstance();
        StringWriter answer = new StringWriter();
        InteractionWriter writer = null;
        Map<String, Object> options = null;
        try {
            options = optionFactory.getJsonOptions(answer, InteractionCategory.evidence, null,
                    MIJsonType.n_ary_only, new CachedOlsOntologyTermFetcher(), null);
            writer = writerFactory.getInteractionWriterWith(options);
        } catch (BridgeFailedException e) {
            options = optionFactory.getJsonOptions(answer, InteractionCategory.evidence, null,
                    MIJsonType.n_ary_only, null, null);

        }
        writer = writerFactory.getInteractionWriterWith(options);

        try {
            writer.start();
            writer.write(interactionEvidence);
            writer.end();
        } finally {
            writer.close();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("X-Clacks-Overhead", "GNU Terry Pratchett"); //In memory of Sir Terry Pratchett
        return new ResponseEntity<String>(answer.toString(), httpHeaders, HttpStatus.OK);
    }

}
