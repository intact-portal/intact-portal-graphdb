package uk.ac.ebi.intact.graphdb.controller.model;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class InteractionDetails {

    private String interactionAc;
    private String interactionType;
    private String shortLabel;
    private Collection<InteractionDetailsXRefs> xrefs;
    private Collection<Annotation> annotations;
    private Collection<TypeValueObject> parameters;
    private Collection<TypeValueObject> confidences;
    private ExperimentDetails experiment;
    private PublicationDetails publication;

    public InteractionDetails(String interactionAc, String interactionType, String shortLabel,
                              Collection<InteractionDetailsXRefs> xrefs, Collection<Annotation> annotations,
                              Collection<TypeValueObject> parameters, Collection<TypeValueObject> confidences,
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

    public Collection<InteractionDetailsXRefs> getXrefs() {
        return xrefs;
    }

    public void setXrefs(Collection<InteractionDetailsXRefs> xrefs) {
        this.xrefs = xrefs;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Collection<TypeValueObject> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<TypeValueObject> parameters) {
        this.parameters = parameters;
    }

    public Collection<TypeValueObject> getConfidences() {
        return confidences;
    }

    public void setConfidences(Collection<TypeValueObject> confidences) {
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
