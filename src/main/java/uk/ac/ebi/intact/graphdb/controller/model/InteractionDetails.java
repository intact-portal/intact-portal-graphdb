package uk.ac.ebi.intact.graphdb.controller.model;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class InteractionDetails {

    private String interactionAc;
    private String interactionType;
    private Collection<InteractionDetailsXRefs> xrefs;
    private Collection<TypeValueObject> annotations;
    private Collection<TypeValueObject> parameters;
    private Collection<TypeValueObject> confidences;

    public InteractionDetails(String interactionAc, String interactionType, Collection<InteractionDetailsXRefs> xrefs,
                              Collection<TypeValueObject> annotations, Collection<TypeValueObject> parameters,
                              Collection<TypeValueObject> confidences) {
        this.interactionAc = interactionAc;
        this.interactionType = interactionType;
        this.xrefs = xrefs;
        this.annotations = annotations;
        this.parameters = parameters;
        this.confidences = confidences;
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

    public Collection<InteractionDetailsXRefs> getXrefs() {
        return xrefs;
    }

    public void setXrefs(Collection<InteractionDetailsXRefs> xrefs) {
        this.xrefs = xrefs;
    }

    public Collection<TypeValueObject> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<TypeValueObject> annotations) {
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
}
