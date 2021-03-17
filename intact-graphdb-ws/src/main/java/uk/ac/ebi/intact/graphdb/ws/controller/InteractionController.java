package uk.ac.ebi.intact.graphdb.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.service.GraphInteractionService;
import uk.ac.ebi.intact.graphdb.ws.controller.model.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by ntoro on 02/08/2017.
 */
@RestController
@RequestMapping("/interaction")
public class InteractionController {

    private final GraphInteractionService graphInteractionService;

    @Autowired
    public InteractionController(GraphInteractionService graphInteractionService) {
        this.graphInteractionService = graphInteractionService;
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

    /**
     * CONVERTS from GraphInteractionEvidence and GraphExperiment to InteractionDetails model
     **/
    private InteractionDetails createInteractionDetails(GraphInteractionEvidence graphInteractionEvidence) {
        String ac = graphInteractionEvidence.getAc();

        CvTerm interactionType = new CvTerm(graphInteractionEvidence.getInteractionType().getShortName(), graphInteractionEvidence.getInteractionType().getMIIdentifier());

        String shortLabel = graphInteractionEvidence.getShortName();

        Set<Xref> xrefs = new HashSet<>();
        graphInteractionEvidence.getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            xrefs.add(new Xref(term, xref.getId()));
        });

        Set<Annotation> annotations = new HashSet<>();
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
        InteractionDetails interactionDetails = new InteractionDetails(ac, interactionType, shortLabel, xrefs, annotations, parameters, confidences, publicationDetails, null, null);

        shuffleDataBetweenModels(experimentDetails, publicationDetails, interactionDetails);
        return interactionDetails;
    }

    private ExperimentDetails createExperimentDetails(GraphExperiment graphExperiment) {
        String experimentAc = graphExperiment.getAc();
        Organism hostOrganism = new Organism(graphExperiment.getHostOrganism().getScientificName(), graphExperiment.getHostOrganism().getTaxId());
        CvTerm interactionDetMethod = new CvTerm(graphExperiment.getInteractionDetectionMethod().getShortName(), graphExperiment.getInteractionDetectionMethod().getMIIdentifier());

        String participantDetMethod = null;
        if (graphExperiment.getParticipantDetectionMethod() != null) {
            participantDetMethod = graphExperiment.getParticipantDetectionMethod().getShortName();
        }

        Set<Xref> experimentXrefs = new HashSet<>();
        graphExperiment.getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            experimentXrefs.add(new Xref(term, xref.getId()));
        });

        Set<Annotation> experimentAnnotations = new HashSet<>();
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
        //TODO...Check if we can fix this from graph db side, difficult to do as WFH
        String publicationDate = null;
        if (graphExperiment.getPublication().getPublicationDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            publicationDate = dateFormat.format(graphExperiment.getPublication().getPublicationDate());
        }

        Set<Xref> publicationXrefs = new HashSet<>();
        graphExperiment.getPublication().getXrefs().forEach(xref -> {
            CvTerm term = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
            publicationXrefs.add(new Xref(term, xref.getId()));
        });

        Set<Annotation> publicationAnnotation = new HashSet<>();
        graphExperiment.getPublication().getAnnotations().forEach(annotation -> {
            CvTerm term = new CvTerm(annotation.getTopic().getShortName(), annotation.getTopic().getMIIdentifier());
            publicationAnnotation.add(new Annotation(term, annotation.getValue()));
        });

        return new PublicationDetails(pubmedId, title, journal, authors, publicationDate, publicationXrefs, publicationAnnotation);
    }

    /*
     * For better user experience it was required to shuffle experiment and publication data between
     * publication and interaction data
     * */
    public void shuffleDataBetweenModels(ExperimentDetails experimentDetails, PublicationDetails publicationDetails, InteractionDetails interactionDetails) {

        //add experiment data to interaction data
        interactionDetails.getXrefs().addAll(experimentDetails.getExperimentXrefs());
        interactionDetails.getAnnotations().addAll(experimentDetails.getExperimentAnnotations());

        //add publication data to interaction data (some entries need to be at interaction level)
        interactionDetails.getAnnotations().addAll(publicationDetails.getPublicationAnnotations());

        //retain specific pub annotations
        // Noe: Maybe we should have used AnnotationUtils.collectAllAnnotationsHavingTopic("MI:XXX") here to compact the code
        // and reuse our existing function to do it
        List<Annotation> annotationsToRetain = new ArrayList<>(); // as already in publication fields
        annotationsToRetain.addAll(publicationDetails.getPublicationAnnotations().stream().filter(annotation ->
                (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0634")) || //contact-email
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0878")) || //author-submitted
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0612")) || //comments
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0618")) || //cautions
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0614"))   //urls
        ).collect(Collectors.toList()));
        publicationDetails.getPublicationAnnotations().retainAll(annotationsToRetain);


        //remove publication data from interaction data (will remove duplication on page)
        interactionDetails.getXrefs().removeAll(publicationDetails.getPublicationXrefs());
        interactionDetails.getAnnotations().removeAll(publicationDetails.getPublicationAnnotations());

        //remove some unwanted xref and annotations from details page
        List<Annotation> interactionAnnotationsToDelete = new ArrayList<>();
        interactionAnnotationsToDelete.addAll(interactionDetails.getAnnotations().stream().filter(annotation ->
                (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0957")) ||         //full-coverage
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0959")) || // imex curation
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0886")) || //publication year
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0885")) || //journal
                        (annotation.getTopic().getIdentifier() != null && annotation.getTopic().getIdentifier().equals("MI:0636"))    //authors
        ).collect(Collectors.toList()));
        interactionDetails.getAnnotations().removeAll(interactionAnnotationsToDelete);

        List<Xref> xrefsToRemove = new ArrayList<>(); // as already in publication fields
        xrefsToRemove.addAll(interactionDetails.getXrefs().stream().filter(xref ->
                xref.getDatabase().getIdentifier() != null && xref.getDatabase().getIdentifier().equals("MI:0446")// pubmed id
        ).collect(Collectors.toList()));
        interactionDetails.getXrefs().removeAll(xrefsToRemove);

        interactionDetails.setHostOrganism(experimentDetails.getInteractionHostOrganism());
        interactionDetails.setDetectionMethod(experimentDetails.getInteractionDetectionMethod());
    }

}
