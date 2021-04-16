package uk.ac.ebi.intact.graphdb.ws.controller.model;

import org.springframework.http.MediaType;

/**
 * Created by anjali on 26/02/19.
 */
public enum InteractionExportFormat {

    // Note: We don't use the capitalization for aesthetic purposes.
    // The enums are shown to the users in the swagger documentation.
    miJSON("json", MediaType.APPLICATION_JSON, "json"),
    miXML25("xml25", MediaType.APPLICATION_XML, "xml"),
    miXML30("xml30", MediaType.APPLICATION_XML, "xml"),
    miTab25("tab25", MediaType.TEXT_PLAIN, "txt"),
    miTab26("tab26", MediaType.TEXT_PLAIN, "txt"),
    miTab27("tab27", MediaType.TEXT_PLAIN, "txt");

    String format;
    MediaType contentType;
    String extension;

    InteractionExportFormat(String format, MediaType contentType, String extension)
    {
        this.format = format;
        this.contentType = contentType;
        this.extension = extension;
    }

    public String getFormat() {
        return format;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }

    public static InteractionExportFormat findByFormat(String format) {

        for (InteractionExportFormat value : values()) {
            if(value.format.equals(format)){
                return value;
            }
        }

        return miJSON;
    }
}
