package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class ExperimentDetails {

    private String experimentAc;
    private String interactionDetMethod;
    private String hostOrganism;
    private Collection<Xref> experimentXrefs;
    private Collection<Annotation> experimentAnnotations;

    public ExperimentDetails(String experimentAc, String interactionDetMethod, String hostOrganism,
                             Collection<Xref> experimentXrefs,
                             Collection<Annotation> experimentAnnotations) {
        this.experimentAc = experimentAc;
        this.interactionDetMethod = interactionDetMethod;
        this.hostOrganism = hostOrganism;
        this.experimentXrefs = experimentXrefs;
        this.experimentAnnotations = experimentAnnotations;
    }

    public String getExperimentAc() {
        return experimentAc;
    }

    public void setExperimentAc(String experimentAc) {
        this.experimentAc = experimentAc;
    }

    public String getInteractionDetMethod() {
        return interactionDetMethod;
    }

    public void setInteractionDetMethod(String interactionDetMethod) {
        this.interactionDetMethod = interactionDetMethod;
    }

    public String getHostOrganism() {
        return hostOrganism;
    }

    public void setHostOrganism(String hostOrganism) {
        this.hostOrganism = hostOrganism;
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
