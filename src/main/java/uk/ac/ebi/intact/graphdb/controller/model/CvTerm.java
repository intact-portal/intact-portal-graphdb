package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class CvTerm {
    private String shortName;
    private String miIdentifier;

    public CvTerm(String shortName, String miIdentifier) {
        this.shortName = shortName;
        this.miIdentifier = miIdentifier;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMiIdentifier() {
        return miIdentifier;
    }

    public void setMiIdentifier(String miIdentifier) {
        this.miIdentifier = miIdentifier;
    }
}
