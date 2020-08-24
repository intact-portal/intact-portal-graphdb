package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */
public class ExperimentDetails {

    private String experimentAc;
    private String interactionDetectionMethod;
    private String participantDetectionMethod;
    private String interactionHostOrganism;
    private Set<Xref> experimentXrefs;
    private Set<Annotation> experimentAnnotations;

    public ExperimentDetails(String experimentAc, String interactionDetMethod, String participantDetMethod, String interactionHostOrganism,
                             Set<Xref> experimentXrefs,
                             Set<Annotation> experimentAnnotations) {
        this.experimentAc = experimentAc;
        this.interactionDetectionMethod = interactionDetMethod;
        this.participantDetectionMethod = participantDetMethod;
        this.interactionHostOrganism = interactionHostOrganism;
        this.experimentXrefs = experimentXrefs;
        this.experimentAnnotations = experimentAnnotations;
    }

    public String getExperimentAc() {
        return experimentAc;
    }

    public void setExperimentAc(String experimentAc) {
        this.experimentAc = experimentAc;
    }

    public String getInteractionDetectionMethod() {
        return interactionDetectionMethod;
    }

    public void setInteractionDetectionMethod(String interactionDetectionMethod) {
        this.interactionDetectionMethod = interactionDetectionMethod;
    }

    public String getInteractionHostOrganism() {
        return interactionHostOrganism;
    }

    public void setInteractionHostOrganism(String interactionHostOrganism) {
        this.interactionHostOrganism = interactionHostOrganism;
    }

    public Set<Xref> getExperimentXrefs() {
        return experimentXrefs;
    }

    public void setExperimentXrefs(Set<Xref> experimentXrefs) {
        this.experimentXrefs = experimentXrefs;
    }

    public Set<Annotation> getExperimentAnnotations() {
        return experimentAnnotations;
    }

    public void setExperimentAnnotations(Set<Annotation> experimentAnnotations) {
        this.experimentAnnotations = experimentAnnotations;
    }

    public String getParticipantDetectionMethod() {
        return participantDetectionMethod;
    }

    public void setParticipantDetectionMethod(String participantDetectionMethod) {
        this.participantDetectionMethod = participantDetectionMethod;
    }
}