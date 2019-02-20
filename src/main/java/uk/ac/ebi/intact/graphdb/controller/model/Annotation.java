package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Annotation {
    private String topic;
    private String description;
    private String miIdentifier;

    public Annotation(String topic, String description, String miIdentifier) {
        this.topic = topic;
        this.description = description;
        this.miIdentifier = miIdentifier;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMiIdentifier() {
        return miIdentifier;
    }

    public void setMiIdentifier(String miIdentifier) {
        this.miIdentifier = miIdentifier;
    }
}
