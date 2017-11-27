package uk.ac.ebi.intact.graphdb.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.tab.extension.MitabBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.error.GraphDbException;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.repositories.BinaryInteractionEvidenceRepository;
import uk.ac.ebi.intact.graphdb.repositories.InteractorRepository;
import uk.ac.ebi.intact.graphdb.utils.InteractionProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 09/02/2012
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ImportInteractionService {

    private static final Logger log = LoggerFactory.getLogger(ImportInteractionService.class);

    private final InteractionProvider interactionProvider;

    @Autowired
    private BinaryInteractionEvidenceRepository binaryInteractionEvidenceRepository;

    @Autowired
    private InteractorRepository interactorRepository;

    @Autowired
    public ImportInteractionService(@Qualifier("miTabInteractionProvider") InteractionProvider interactionProvider) {
        this.interactionProvider = interactionProvider;
    }

    public List<Interaction> importInteractions() {

        List<Interaction> interactions = new ArrayList<Interaction>();

//        log.info("Deleting previous interactions");
//        cleanDb();
//        log.info("Previous interactions deleted");

        log.info("Loading interactions");
        try {
            Iterator<BinaryInteractionEvidence> interactionIterator = interactionProvider.getInteractions();

            while (interactionIterator.hasNext()) {
                psidev.psi.mi.jami.model.Interaction interaction = (psidev.psi.mi.jami.model.Interaction) interactionIterator.next();

                // most of the interactions will have experimental data attached to them so they will be of type InteractionEvidence
                if (interaction instanceof MitabBinaryInteractionEvidence) {
                    MitabBinaryInteractionEvidence interactionEvidence = (MitabBinaryInteractionEvidence) interaction;
                    // process the interaction evidence
                    binaryInteractionEvidenceRepository.save(new GraphBinaryInteractionEvidence(interactionEvidence));
                    interactions.add(interactionEvidence);
                }
            }

        } catch (GraphDbException e) {
            log.error(e.getMessage());
        }

        log.info("Loaded " + interactions.size() + " interactions");

        return interactions;

    }
}
