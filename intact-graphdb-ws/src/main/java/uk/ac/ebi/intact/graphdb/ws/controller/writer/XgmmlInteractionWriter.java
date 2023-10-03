package uk.ac.ebi.intact.graphdb.ws.controller.writer;

import psidev.psi.mi.jami.commons.MIWriterOptionFactory;
import psidev.psi.mi.jami.datasource.InteractionWriter;
import psidev.psi.mi.jami.factory.InteractionWriterFactory;
import psidev.psi.mi.jami.model.ComplexType;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractionCategory;
import psidev.psi.mi.jami.tab.MitabVersion;
import uk.ac.ebi.intact.graphdb.ws.controller.expansion.GraphDbExpansionMethod;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class XgmmlInteractionWriter extends AbstractXgmmlWriter implements InteractionWriter {

    private final InteractionWriter mitab27Writer;

    public XgmmlInteractionWriter(
            OutputStream outputStream,
            String query,
            int nInteractions,
            InteractionWriterFactory writerFactory,
            MIWriterOptionFactory optionFactory) {

        super(outputStream, query, nInteractions);
        this.mitab27Writer = writerFactory.getInteractionWriterWith(
                optionFactory.getMitabOptions(
                        getByteArrayOutputStream(),
                        InteractionCategory.evidence,
                        ComplexType.n_ary,
                        new GraphDbExpansionMethod(),
                        false,
                        MitabVersion.v2_7,
                        false));


    }

    @Override
    public void initialiseContext(Map options) {
        mitab27Writer.initialiseContext(options);
    }

    @Override
    public void start() {
        mitab27Writer.start();
    }

    @Override
    public void end() {
        mitab27Writer.end();
    }

    @Override
    public void write(Interaction interaction) {
        mitab27Writer.write(interaction);
    }

    @Override
    public void write(Collection interactions) {
        mitab27Writer.write(interactions);
    }

    @Override
    public void write(Iterator interactions) {
        mitab27Writer.write(interactions);
    }

    @Override
    public void flush() {
        mitab27Writer.flush();
    }

    @Override
    public void close() {
        mitab27Writer.close();
        writeXGMML();
    }

    @Override
    public void reset() {
        mitab27Writer.reset();
    }
}
