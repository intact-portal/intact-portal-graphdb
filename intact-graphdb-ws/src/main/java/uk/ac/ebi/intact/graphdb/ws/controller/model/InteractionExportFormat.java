package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * Created by anjali on 26/02/19.
 */
public enum InteractionExportFormat {

    JSON("json"),
    XML25("xml25"),
    XML30("xml30");

    String format;

    InteractionExportFormat(String f) {
        format = f;
    }


    String showFormat() {
        return format;
    }

    public static InteractionExportFormat formatOf(String format) {
        if (JSON.format.equals(format)) return JSON;
        else if (XML25.format.equals(format)) return XML25;
        else if (XML30.format.equals(format)) return XML30;
        else return JSON;
    }
}
