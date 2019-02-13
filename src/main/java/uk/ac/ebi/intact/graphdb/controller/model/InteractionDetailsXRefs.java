package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class InteractionDetailsXRefs {

    private String database;
    private String identifier;

    public InteractionDetailsXRefs(String database, String identifier) {
        this.database = database;
        this.identifier = identifier;
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
}
