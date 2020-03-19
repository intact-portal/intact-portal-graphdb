package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * Created by anjali on 19/03/20.
 */
public class LinkedFeature {

    private String shortName;
    private String ac;

    public LinkedFeature(String shortName, String ac) {
        this.setShortName(shortName);
        this.setAc(ac);
    }


    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }
}
