package uk.ac.ebi.intact.graphdb.ws.controller.writer;

import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.io.IOException;
import java.io.OutputStream;

public class XgmmlSearchInteractionWriter extends AbstractXgmmlWriter implements SearchInteractionWriter {

    public XgmmlSearchInteractionWriter(OutputStream outputStream, String query, int nInteractions) {
        super(outputStream, query, nInteractions);
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
    }

    @Override
    public void write(SearchInteraction interaction) throws IOException {
        getByteArrayOutputStream().write("\n".getBytes());
        getByteArrayOutputStream().write(interaction.getTab27Format().getBytes());
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
        writeXGMML();
    }
}
