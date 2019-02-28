package uk.ac.ebi.intact.graphdb.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Organism {
    private String scientificName;
    private int taxId;

    public Organism(String scientificName, int taxId) {
        this.scientificName = scientificName;
        this.taxId = taxId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }
}
