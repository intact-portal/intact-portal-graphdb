package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class ExperimentDetails {

    private String experimentAc;
    private String interactionDetectionMethod;
    private String interactionHostOrganism;
    private Collection<Xref> experimentXrefs;
    private Collection<Annotation> experimentAnnotations;

    public ExperimentDetails(String experimentAc, String interactionDetMethod, String interactionHostOrganism,
                             Collection<Xref> experimentXrefs,
                             Collection<Annotation> experimentAnnotations) {
        this.experimentAc = experimentAc;
        this.interactionDetectionMethod = interactionDetMethod;
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

    public Collection<Xref> getExperimentXrefs() {
        return experimentXrefs;
    }

    public void setExperimentXrefs(Collection<Xref> experimentXrefs) {
        this.experimentXrefs = experimentXrefs;
    }

    public Collection<Annotation> getExperimentAnnotations() {
        return experimentAnnotations;
    }

    public void setExperimentAnnotations(Collection<Annotation> experimentAnnotations) {
        this.experimentAnnotations = experimentAnnotations;
    }
}