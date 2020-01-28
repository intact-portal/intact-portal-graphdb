package uk.ac.ebi.intact.graphdb.ws.controller.model;

import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeature;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class FeatureDetails {

    private String featureAc;
    private String name; //Shortname
    private CvTerm type;
    private CvTerm role;
    private Collection<String> ranges; //ranges.rangeString
    private Collection<GraphFeature> linkedFeatures; //linkedFeatures.shortName, linkedFeatures.ac
    private String participantName; // participant.participant.shortName
    private Xref participant; // participant.participant.preferredIdentifierStr
    private String participantAc; //participant.ac
    private Collection<CvTerm> detectionMethods;
    private Collection<Parameter> parameters;
    private Collection<Xref> identifiers;
    private Collection<Xref> xrefs;
    private Collection<Annotation> annotations;

    public FeatureDetails(String featureAc, String name, CvTerm type, CvTerm role, Collection<String> ranges,
                          Collection<GraphFeature> linkedFeatures, String participantName, Xref participant,
                          String participantAc, Collection<CvTerm> detectionMethods, Collection<Parameter> parameters,
                          Collection<Xref> identifiers, Collection<Xref> xrefs, Collection<Annotation> annotations) {
        this.featureAc = featureAc;
        this.name = name;
        this.type = type;
        this.role = role;
        this.ranges = ranges;
        this.linkedFeatures = linkedFeatures;
        this.participantName = participantName;
        this.participant = participant;
        this.participantAc = participantAc;
        this.detectionMethods = detectionMethods;
        this.parameters = parameters;
        this.identifiers = identifiers;
        this.xrefs = xrefs;
        this.annotations = annotations;
    }

    public String getFeatureAc() {
        return featureAc;
    }

    public void setFeatureAc(String featureAc) {
        this.featureAc = featureAc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CvTerm getType() {
        return type;
    }

    public void setType(CvTerm type) {
        this.type = type;
    }

    public CvTerm getRole() {
        return role;
    }

    public void setRole(CvTerm role) {
        this.role = role;
    }

    public Collection<String> getRanges() {
        return ranges;
    }

    public void setRanges(Collection<String> ranges) {
        this.ranges = ranges;
    }

    public Collection<GraphFeature> getLinkedFeatures() {
        return linkedFeatures;
    }

    public void setLinkedFeatures(Collection<GraphFeature> linkedFeatures) {
        this.linkedFeatures = linkedFeatures;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public Xref getParticipant() {
        return participant;
    }

    public void setParticipant(Xref participant) {
        this.participant = participant;
    }

    public String getParticipantAc() {
        return participantAc;
    }

    public void setParticipantAc(String participantAc) {
        this.participantAc = participantAc;
    }

    public Collection<CvTerm> getDetectionMethods() {
        return detectionMethods;
    }

    public void setDetectionMethods(Collection<CvTerm> detectionMethods) {
        this.detectionMethods = detectionMethods;
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Collection<Xref> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        this.identifiers = identifiers;
    }

    public Collection<Xref> getXrefs() {
        return xrefs;
    }

    public void setXrefs(Collection<Xref> xrefs) {
        this.xrefs = xrefs;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        this.annotations = annotations;
    }
}
