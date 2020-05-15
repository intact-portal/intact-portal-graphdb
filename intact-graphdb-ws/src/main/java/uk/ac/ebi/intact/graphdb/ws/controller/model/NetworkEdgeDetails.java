package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Collection;

/**
 * Created by anjali on 24/04/20.
 */
public class NetworkEdgeDetails {

    private Long id;
    private Collection<Annotation> annotations;
    private Collection<Parameter> parameters;

    public NetworkEdgeDetails(Long id, Collection<Annotation> annotations, Collection<Parameter> parameters) {
        this.id = id;
        this.annotations = annotations;
        this.parameters = parameters;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
