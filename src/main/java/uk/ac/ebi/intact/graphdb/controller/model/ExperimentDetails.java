package uk.ac.ebi.intact.graphdb.controller.model;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class ExperimentDetails {

    private String experimentAc;
    private String interactionDetMethod;
    private String hostOrganism;
    private Collection<InteractionDetailsXRefs> experimentXrefs;
    private Collection<Annotation> experimentAnnotations;

    public ExperimentDetails(String experimentAc, String interactionDetMethod, String hostOrganism,
                             Collection<InteractionDetailsXRefs> experimentXrefs,
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

    public Collection<InteractionDetailsXRefs> getExperimentXrefs() {
        return experimentXrefs;
    }

    public void setExperimentXrefs(Collection<InteractionDetailsXRefs> experimentXrefs) {
        this.experimentXrefs = experimentXrefs;
    }

    public Collection<Annotation> getExperimentAnnotations() {
        return experimentAnnotations;
    }

    public void setExperimentAnnotations(Collection<Annotation> experimentAnnotations) {
        this.experimentAnnotations = experimentAnnotations;
    }
}
