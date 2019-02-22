package uk.ac.ebi.intact.graphdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.graphdb.controller.model.*;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.services.GraphExperimentService;
import uk.ac.ebi.intact.graphdb.services.GraphInteractionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController
@RequestMapping("/graph/interaction")
public class InteractionController {

    private GraphInteractionService graphInteractionService;
    private GraphExperimentService graphExperimentService;

    @Autowired
    public InteractionController(GraphInteractionService graphInteractionService,
                                 GraphExperimentService graphExperimentService) {
        this.graphInteractionService = graphInteractionService;
        this.graphExperimentService = graphExperimentService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot GraphDB Example";
    }

    @RequestMapping(value = "/details/{ac}", method = RequestMethod.GET)
    public InteractionDetails getInteractionDetails(
            @PathVariable String ac,
            @RequestParam(value = "depth", defaultValue = "2", required = false) int depth) {

        GraphInteractionEvidence graphInteractionEvidence = graphInteractionService.findByInteractionAc(ac, depth);
        GraphExperiment graphExperiment = graphExperimentService.findByInteractionAc(ac);

        return createInteractionDetails(graphInteractionEvidence, graphExperiment);
    }

    @RequestMapping(value = "/detailsOld",
            params = {"ac"},
            method = RequestMethod.GET)
    public GraphInteractionEvidence getDetailsOld(
            @RequestParam(value = "ac") String ac,
            @RequestParam(value = "depth", defaultValue = "2", required = false) int depth) {
        return graphInteractionService.findByInteractionAc(ac, depth);
    }

    @RequestMapping(value = "/experiment/{ac}", method = RequestMethod.GET)
    public GraphExperiment getExperimentAndPublicationDetails(
            @PathVariable String ac) {
        return graphExperimentService.findByInteractionAc(ac);
    }

    /**
     * CONVERTS from GraphInteractionEvidence and GraphExperiment to InteractionDetails model
     **/
    private InteractionDetails createInteractionDetails(GraphInteractionEvidence graphInteractionEvidence,
                                                        GraphExperiment graphExperiment) {
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

        List<TermType> parameters = new ArrayList<>();
        graphInteractionEvidence.getParameters().forEach(param ->
                parameters.add(new TermType(param.getType().getShortName(), param.getValue().toString())));

        List<TermType> confidences = new ArrayList<>();
        graphInteractionEvidence.getConfidences().forEach(confidence ->
                confidences.add(new TermType(confidence.getType().getShortName(), confidence.getValue())));

        ExperimentDetails experimentDetails = createExperimentDetails(graphExperiment);
        PublicationDetails publicationDetails = createPublicationDetails(graphExperiment);

        return new InteractionDetails(ac, interactionType, shortLabel, xrefs, annotations, parameters, confidences, experimentDetails, publicationDetails);
    }

    private ExperimentDetails createExperimentDetails(GraphExperiment graphExperiment) {
        String experimentAc = graphExperiment.getAc();
        String hostOrganism = graphExperiment.getHostOrganism().getScientificName();
        String interactionDetMethod = graphExperiment.getInteractionDetectionMethod().getShortName();

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

        return new ExperimentDetails(experimentAc, interactionDetMethod, hostOrganism, experimentXrefs, experimentAnnotations);
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

}
