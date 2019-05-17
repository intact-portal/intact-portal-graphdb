package uk.ac.ebi.intact.graphdb.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.repositories.GraphBinaryInteractionEvidenceRepository;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractorRepository;
import uk.ac.ebi.intact.graphdb.utils.InteractionProvider;

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
    private GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository;

    @Autowired
    private GraphInteractorRepository graphInteractorRepository;

    @Autowired
    public ImportInteractionService(@Qualifier("miTabInteractionProvider") InteractionProvider interactionProvider) {
        this.interactionProvider = interactionProvider;
    }
}