package uk.ac.ebi.intact.graphdb.ws.controller.writer;

import psidev.psi.mi.jami.datasource.InteractionWriter;
import uk.ac.ebi.intact.graphdb.ws.controller.model.InteractionExportFormat;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.io.IOException;
import java.io.OutputStream;

public class SerialisedJsonSearchInteractionWriter extends SerialisedSearchInteractionWriter {

    private boolean anyInteractionAlreadyWritten;

    public SerialisedJsonSearchInteractionWriter(InteractionWriter interactionWriter, OutputStream outputStream) {
        super(InteractionExportFormat.miJSON, interactionWriter, outputStream);
        this.anyInteractionAlreadyWritten = false;
    }

    @Override
    public void write(SearchInteraction interaction) throws IOException {
        // If any interaction has already been written, then we add a comma to separate JSON entries.
        if (anyInteractionAlreadyWritten) {
            getOutputStream().write(",".getBytes());
        } else {
            anyInteractionAlreadyWritten = true;
        }
        getOutputStream().write(interaction.getJsonFormat().getBytes());
    }
}
