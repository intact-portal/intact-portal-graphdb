package uk.ac.ebi.intact.graphdb.ws.controller.writer;

import psidev.psi.mi.jami.datasource.InteractionWriter;
import psidev.psi.mi.jami.xml.utils.PsiXmlUtils;
import uk.ac.ebi.intact.graphdb.ws.controller.model.InteractionExportFormat;

import java.io.IOException;
import java.io.OutputStream;

public class SerialisedXmlSearchInteractionWriter extends SerialisedSearchInteractionWriter {

    public SerialisedXmlSearchInteractionWriter(InteractionExportFormat format, InteractionWriter interactionWriter, OutputStream outputStream) {
        super(format, interactionWriter, outputStream);
    }

    @Override
    public void start() throws IOException {
        super.start();
        // The XML writer does not close the entrySet tag until either an interaction is written, or the end of the
        // document is written, so it can decide between writing '<entrySet>...</entrySet>' or '</entrySet>'.
        // Since we are not writing interactions using the XML writer, but writing directly to the output stream,
        // we need to manually close the entrySet tag.
        getOutputStream().write(">".getBytes());
    }

    @Override
    public void end() throws IOException {
        // Since we are not writing interactions using the XML writer, but writing directly to the output stream,
        // if we call the XML writer end method, it thinks no interactions have been written, and it tries to close
        // the entrySet tag as a single tag with no children, '<entrySet/>'.
        // So, we cannot call the XML writer end method, and we need to add the entrySet closing tag manually to the
        // output stream.
        getOutputStream().write(("\n</" + PsiXmlUtils.ENTRYSET_TAG + ">").getBytes());
    }
}
