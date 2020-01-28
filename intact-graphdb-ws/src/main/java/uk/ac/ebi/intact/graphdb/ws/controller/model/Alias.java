package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Alias {

    private String name;
    private CvTerm type;

    public Alias(String name, CvTerm type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CvTerm getType() {
        return type;
    }

    public void setType(CvTerm type) {
        this.type = type;
    }
}
