package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Xref {

    private CvTerm database;
    private String identifier;

    public Xref(CvTerm database, String identifier ) {
        this.database = database;
        this.identifier = identifier;
    }

    public CvTerm getDatabase() {
        return database;
    }

    public void setDatabase(CvTerm database) {
        this.database = database;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
