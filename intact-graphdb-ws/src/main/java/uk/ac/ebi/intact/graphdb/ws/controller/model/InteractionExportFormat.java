package uk.ac.ebi.intact.graphdb.ws.controller.model;

import org.springframework.http.MediaType;

/**
 * Created by anjali on 26/02/19.
 */
public enum InteractionExportFormat {

    MIJSON("json", MediaType.APPLICATION_JSON, "json"),
    MIXML25("xml25", MediaType.APPLICATION_XML, "xml"),
    MIXML30("xml30", MediaType.APPLICATION_XML, "xml"),
    MITAB25("tab25", MediaType.TEXT_PLAIN, "txt"),
    MITAB26("tab26", MediaType.TEXT_PLAIN, "txt"),
    MITAB27("tab27", MediaType.TEXT_PLAIN, "txt"),
    UNKNOWN("", MediaType.TEXT_PLAIN, "txt");

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

        return UNKNOWN;
    }
}
