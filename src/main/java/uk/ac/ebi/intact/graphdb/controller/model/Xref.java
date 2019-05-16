package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Xref {

    private CvTerm database;
    private String identifier;
    private CvTerm qualifier;

    public Xref(CvTerm database, String identifier) {
        this.database = database;
        this.identifier = identifier;
    }

    public Xref(CvTerm database, String identifier, CvTerm qualifier) {
        this.database = database;
        this.identifier = identifier;
        this.qualifier = qualifier;
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

    public CvTerm getQualifier() {
        return qualifier;
    }

    public void setQualifier(CvTerm qualifier) {
        this.qualifier = qualifier;
    }
}
