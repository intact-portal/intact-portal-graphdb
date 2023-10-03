package uk.ac.ebi.intact.graphdb.ws.controller.writer;

import org.hupo.psi.calimocho.tab.util.MitabDocumentDefinitionFactory;
import org.hupo.psi.calimocho.xgmml.XgmmlStreamingGrapBuilder;
import psidev.psi.mi.jami.exception.MIIOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractXgmmlWriter {

    private final OutputStream outputStream;
    private final String query;
    private final int nInteractions;
    private final ByteArrayOutputStream byteArrayOutputStream;

    public AbstractXgmmlWriter(OutputStream outputStream, String query, int nInteractions) {

        this.outputStream = outputStream;
        this.query = query;
        this.nInteractions = nInteractions;
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    protected ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    protected void writeXGMML() throws MIIOException {
        XgmmlStreamingGrapBuilder graphBuilder = null;
        try {
            graphBuilder = new XgmmlStreamingGrapBuilder(
                    "IntAct Export " + System.currentTimeMillis(),
                    "Results for query: " + query,
                    "IntAct");

            try (InputStream is = new ByteArrayInputStream(byteArrayOutputStream.toString().trim().getBytes())) {

                graphBuilder.open(outputStream, nInteractions);
                graphBuilder.writeNodesAndEdgesFromMitab(is, MitabDocumentDefinitionFactory.mitab27());
                outputStream.flush();

            } catch (JAXBException | XMLStreamException | IOException e) {
                throw new MIIOException("Failed to write nodes to XgmmlStreamingGrapBuilder", e);
            }
        } catch (JAXBException e) {
            throw new MIIOException("Failed to write nodes to XgmmlStreamingGrapBuilder", e);
        } finally {
            if (graphBuilder != null) {
                try {
                    graphBuilder.close();
                } catch (XMLStreamException | IOException e) {
                    throw new MIIOException("Failed to close XgmmlStreamingGrapBuilder", e);
                }
            }
        }
    }
}
