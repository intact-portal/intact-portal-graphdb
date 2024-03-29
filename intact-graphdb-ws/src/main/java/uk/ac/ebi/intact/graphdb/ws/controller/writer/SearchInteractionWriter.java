package uk.ac.ebi.intact.graphdb.ws.controller.writer;

import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.io.IOException;

public interface SearchInteractionWriter {

    void start() throws IOException;

    void flush();

    void write(SearchInteraction interaction) throws IOException;

    void end() throws IOException;

    void close();
}
