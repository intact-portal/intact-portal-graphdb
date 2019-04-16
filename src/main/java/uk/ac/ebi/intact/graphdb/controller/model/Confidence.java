package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Confidence {
    private CvTerm type;
    private String value;

    public Confidence(CvTerm type, String value) {
        this.type = type;
        this.value = value;
    }

    public CvTerm getType() {
        return type;
    }

    public void setType(CvTerm type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
