package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class CvTerm {
    private String shortName;
    private String identifier;

    public CvTerm(String shortName, String identifier) {
        this.shortName = shortName;
        this.identifier = identifier;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
