package uk.ac.ebi.intact.graphdb.utils.interactions.providers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import psidev.psi.mi.jami.commons.PsiJami;
import psidev.psi.mi.jami.factory.options.MIFileDataSourceOptions;
import psidev.psi.mi.jami.tab.extension.datasource.MitabBinaryEvidenceStreamSource;
import uk.ac.ebi.intact.graphdb.error.GraphDbException;
import uk.ac.ebi.intact.graphdb.utils.InteractionProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 11/09/2014
 * Time: 08:59
 */
@Component
public class MiTabInteractionProvider implements InteractionProvider {

    private static final Logger log = LoggerFactory.getLogger(MiTabInteractionProvider.class);
    private MitabBinaryEvidenceStreamSource interactionSource;

    @Value("${mitab.file.name}")
    private String fileName;

    public MiTabInteractionProvider() {
    }

    @Override
    public Iterator getInteractions() throws GraphDbException {

//        List<Interaction> interactions = new ArrayList<Interaction>();

        PsiJami.initialiseAllFactories();

        // the datasource factory for reading MITAB/PSI-MI XML files and other datasources
//        MitabDataSourceFactory dataSourceFactory = MitabDataSourceFactory.getInstance();
        Iterator interactionIterator=null;
        // get default options for a file. It will identify if the file is MITAB or PSI-MI XML file and then it will load the appropriate options.
        // By default, the datasource will be streaming (only returns an iterator of interactions), and returns a source of Interaction objects.
        // The default options can be overridden using the optionfactory or by manually adding options listed in MitabDataSourceOptions or PsiXmlDataSourceOptions

        try {
            // Get the stream of interactions knowing the default options for this file
            interactionSource = new MitabBinaryEvidenceStreamSource();
//            interactionSource = new MitabBinaryEvidenceStreamSource(new File(fileName));
            Map<String, Object> options = new HashMap<String, Object>();
            options.put(MIFileDataSourceOptions.INPUT_OPTION_KEY, new File(fileName));
            interactionSource.initialiseContext(options);
            interactionIterator = interactionSource.getInteractionsIterator();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // always close the opened interaction stream
            if (interactionSource != null) {
                interactionSource.close();
            }
        }

        return interactionIterator;
    }

}
