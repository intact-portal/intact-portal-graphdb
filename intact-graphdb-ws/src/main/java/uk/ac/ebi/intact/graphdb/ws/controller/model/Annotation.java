package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Annotation {
    private CvTerm topic;
    private String description;

    public Annotation(CvTerm topic, String description) {
        this.topic = topic;
        this.description = description;
    }

    public CvTerm getTopic() {
        return topic;
    }

    public void setTopic(CvTerm topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Annotation) {
            Annotation annotation = (Annotation) object;
            if (this.getTopic() != null && annotation.getTopic() != null) {
                if (!this.getTopic().equals(annotation.getTopic())) {
                    return false;
                }
            } else if (!(this.getTopic() == null && annotation.getTopic() == null)) {// when one of them is null
                return false;
            }

            if (this.getDescription() != null && annotation.getDescription() != null) {
                if (!this.getDescription().equals(annotation.getDescription())) {
                    return false;
                }
            } else if (!(this.getDescription() == null && annotation.getDescription() == null)) {// when one of them is null
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 31;
        if (this.getTopic() != null) {
            hashCode = 31 + this.getTopic().hashCode();
        }
        if (this.getDescription() != null) {
            hashCode = 31 + this.getDescription().hashCode();
        }
        return hashCode;
    }
}
