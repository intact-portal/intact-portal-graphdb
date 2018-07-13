package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.repositories.GraphBinaryInteractionEvidenceRepository;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractorRepository;

/**
 * Created by anjali on 12/07/18.
 */
@Service
public class GraphInteractionService {

    final private GraphBinaryInteractionEvidenceRepository graphInteractionEvidenceRepository;

    @Autowired
    public GraphInteractionService(GraphBinaryInteractionEvidenceRepository graphInteractorRepository) {
        this.graphInteractionEvidenceRepository = graphInteractorRepository;
    }

    public Page<GraphBinaryInteractionEvidence> findAll(Pageable page, int depth) {
        return this.graphInteractionEvidenceRepository.findAll(page, depth);
    }
}
