package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Xref {

    private CvTerm database;
    private String identifier;
    private CvTerm qualifier;
    private String ac;

    public Xref(CvTerm database, String identifier) {
        this.database = database;
        this.identifier = identifier;
    }

    public Xref(CvTerm database, String identifier, CvTerm qualifier, String ac) {
        this.database = database;
        this.identifier = identifier;
        this.qualifier = qualifier;
        this.ac = ac;
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

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }
}
