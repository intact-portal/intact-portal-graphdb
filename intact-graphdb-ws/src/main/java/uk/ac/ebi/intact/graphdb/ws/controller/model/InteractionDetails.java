package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Collection;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */
public class InteractionDetails {

    private String interactionAc;
    private CvTerm type;
    private String shortLabel;
    private Set<Xref> xrefs;
    private Set<Annotation> annotations;
    private Collection<Parameter> parameters;
    private Collection<Confidence> confidences;
    private PublicationDetails publication;
    private CvTerm detectionMethod;
    private String hostOrganism;

    public InteractionDetails(String interactionAc, CvTerm interactionType, String shortLabel,
                              Set<Xref> xrefs, Set<Annotation> annotations,
                              Collection<Parameter> parameters, Collection<Confidence> confidences,
                              PublicationDetails publication, CvTerm detectionMethod, String hostOrganism) {
        this.interactionAc = interactionAc;
        this.type = interactionType;
        this.shortLabel = shortLabel;
        this.xrefs = xrefs;
        this.annotations = annotations;
        this.parameters = parameters;
        this.confidences = confidences;
        this.publication = publication;
        this.detectionMethod = detectionMethod;
        this.hostOrganism = hostOrganism;
    }

    public String getInteractionAc() {
        return interactionAc;
    }

    public void setInteractionAc(String interactionAc) {
        this.interactionAc = interactionAc;
    }

    public CvTerm getType() {
        return type;
    }

    public void setType(CvTerm type) {
        this.type = type;
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

    public PublicationDetails getPublication() {
        return publication;
    }

    public void setPublication(PublicationDetails publication) {
        this.publication = publication;
    }

    public CvTerm getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(CvTerm detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public String getHostOrganism() {
        return hostOrganism;
    }

    public void setHostOrganism(String hostOrganism) {
        this.hostOrganism = hostOrganism;
    }
}
