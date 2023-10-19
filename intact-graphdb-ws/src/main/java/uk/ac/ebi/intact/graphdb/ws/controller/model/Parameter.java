package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Parameter {

    private CvTerm type;
    private CvTerm unit;
    private String value; //value, base, exponent, uncertainty String containing the four of them

    public Parameter(CvTerm type, CvTerm unit, String value) {
        this.type = type;
        this.unit = unit;
        this.value = value;
    }

    public CvTerm getType() {
        return type;
    }

    public void setType(CvTerm type) {
        this.type = type;
    }

    public CvTerm getUnit() {
        return unit;
    }

    public void setUnit(CvTerm unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
