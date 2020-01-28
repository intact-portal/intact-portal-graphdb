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
}
