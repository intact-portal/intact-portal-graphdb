package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.annotation.UnambiguousAnnotationComparator;
import uk.ac.ebi.intact.graphdb.utils.Constants;
import uk.ac.ebi.intact.graphdb.utils.EntityCache;

@NodeEntity
public class GraphAnnotation implements Annotation {

    @GraphId
    private Long graphId;

    @Index(unique = true,primary = true)
    private String uniqueKey;

    private GraphCvTerm topic;
    private String value;

    public GraphAnnotation() {
    }

    public GraphAnnotation(Annotation annotation) {
        setTopic(annotation.getTopic());
        setValue(annotation.getValue());
        setUniqueKey(this.toString());
    }


    public GraphAnnotation(CvTerm topic) {
        if (topic == null) {
            throw new IllegalArgumentException("The annotation topic is required and cannot be null");
        }
        setTopic(topic);
    }

    public GraphAnnotation(CvTerm topic, String value) {
        this(topic);
        setValue(value);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public CvTerm getTopic() {
        return this.topic;
    }

    public void setTopic(CvTerm topic) {
        if (topic != null) {
            if (topic instanceof GraphCvTerm) {
                this.topic = (GraphCvTerm) topic;
            }else if (topic != null && EntityCache.USED_IN_CLASS != null && Constants.USED_IN_CLASS_TOPIC.equals(topic.getShortName())) {
                setTopic(EntityCache.USED_IN_CLASS);
            } else {
                this.topic = new GraphCvTerm(topic);
            }
        } else {
            this.topic = null;
        }
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
        return this.topic.toString() + (getValue() != null ? ": " + getValue() : "");
    }
}
