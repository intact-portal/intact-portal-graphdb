package uk.ac.ebi.intact.graphdb.ws.controller.writer;

import psidev.psi.mi.jami.datasource.InteractionWriter;
import uk.ac.ebi.intact.graphdb.ws.controller.model.InteractionExportFormat;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.io.IOException;
import java.io.OutputStream;

public class SerialisedSearchInteractionWriter implements SearchInteractionWriter {

    private final InteractionExportFormat format;
    private final InteractionWriter interactionWriter;
    private final OutputStream outputStream;

    public SerialisedSearchInteractionWriter(InteractionExportFormat format, InteractionWriter interactionWriter, OutputStream outputStream) {
        this.format = format;
        this.interactionWriter = interactionWriter;
        this.outputStream = outputStream;
    }

    @Override
    public void start() {
        interactionWriter.start();
    }

    @Override
    public void flush() {
        interactionWriter.flush();
    }

    @Override
    public void write(SearchInteraction interaction) throws IOException {
        // New line before each interaction to have each interaction on a different line.
        // This is particularly relevant for the MiTab formats.
        outputStream.write("\n".getBytes());
        switch (format) {
            case miJSON:
                outputStream.write(interaction.getJsonFormat().getBytes());
                break;
            case miXML25:
                outputStream.write(interaction.getXml25Format().getBytes());
                break;
            case miXML30:
                outputStream.write(interaction.getXml30Format().getBytes());
                break;
            case miTab25:
                outputStream.write(interaction.getTab25Format().getBytes());
                break;
            case miTab26:
                outputStream.write(interaction.getTab26Format().getBytes());
                break;
            case miTab27:
                outputStream.write(interaction.getTab27Format().getBytes());
                break;
        }
    }

    @Override
    public void end() {
        interactionWriter.end();
    }

    @Override
    public void close() {
        interactionWriter.close();
    }
}
