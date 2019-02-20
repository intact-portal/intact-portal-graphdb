package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class InteractionDetailsXRefs {

    private String database;
    private String identifier;
    private String miIdentifier;

    public InteractionDetailsXRefs(String database, String identifier, String miIdentifier) {
        this.database = database;
        this.identifier = identifier;
        this.miIdentifier = miIdentifier;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMiIdentifier() {
        return miIdentifier;
    }

    public void setMiIdentifier(String miIdentifier) {
        this.miIdentifier = miIdentifier;
    }
}
