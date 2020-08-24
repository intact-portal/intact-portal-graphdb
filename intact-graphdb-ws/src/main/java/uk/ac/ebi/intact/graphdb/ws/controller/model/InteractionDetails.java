package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Collection;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */
public class InteractionDetails {

    private String interactionAc;
    private String interactionType;
    private String shortLabel;
    private Set<Xref> xrefs;
    private Set<Annotation> annotations;
    private Collection<Parameter> parameters;
    private Collection<Confidence> confidences;
    private ExperimentDetails experiment;
    private PublicationDetails publication;

    public InteractionDetails(String interactionAc, String interactionType, String shortLabel,
                              Set<Xref> xrefs, Set<Annotation> annotations,
                              Collection<Parameter> parameters, Collection<Confidence> confidences,
                              ExperimentDetails experiment, PublicationDetails publication) {
        this.interactionAc = interactionAc;
        this.interactionType = interactionType;
        this.shortLabel = shortLabel;
        this.xrefs = xrefs;
        this.annotations = annotations;
        this.parameters = parameters;
        this.confidences = confidences;
        this.experiment = experiment;
        this.publication = publication;
    }

    public String getInteractionAc() {
        return interactionAc;
    }

    public void setInteractionAc(String interactionAc) {
        this.interactionAc = interactionAc;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public Set<Xref> getXrefs() {
        return xrefs;
    }

    public void setXrefs(Set<Xref> xrefs) {
        this.xrefs = xrefs;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Collection<Confidence> getConfidences() {
        return confidences;
    }

    public void setConfidences(Collection<Confidence> confidences) {
        this.confidences = confidences;
    }

    public ExperimentDetails getExperiment() {
        return experiment;
    }

    public void setExperiment(ExperimentDetails experiment) {
        this.experiment = experiment;
    }

    public PublicationDetails getPublication() {
        return publication;
    }

    public void setPublication(PublicationDetails publication) {
        this.publication = publication;
    }
}
