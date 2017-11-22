package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.annotation.UnambiguousAnnotationComparator;

@NodeEntity
public class GraphAnnotation implements Annotation {

    @GraphId
    protected Long id;

    private CvTerm topic;
    private String value;

    public GraphAnnotation() {
    }

    public GraphAnnotation(Annotation annotation) {

    }


    public GraphAnnotation(CvTerm topic) {
        if (topic == null) {
            throw new IllegalArgumentException("The annotation topic is required and cannot be null");
        }
        this.topic = topic;
    }

    public GraphAnnotation(CvTerm topic, String value) {
        this(topic);
        this.value = value;
    }

    public CvTerm getTopic() {
        return this.topic;
    }

    public void setTopic(CvTerm topic) {
        this.topic = topic;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return UnambiguousAnnotationComparator.hashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Annotation)) {
            return false;
        }

        return UnambiguousAnnotationComparator.areEquals(this, (Annotation) o);
    }

    @Override
    public String toString() {
        return getTopic().toString() + (getValue() != null ? ": " + getValue() : "");
    }
}
