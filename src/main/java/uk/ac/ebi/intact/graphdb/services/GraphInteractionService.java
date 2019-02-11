package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphClusteredInteraction;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.repositories.GraphBinaryInteractionEvidenceRepository;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractionEvidenceRepository;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractorRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by anjali on 12/07/18.
 */
@Service
public class GraphInteractionService {

    final private GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository;

    final private GraphInteractionEvidenceRepository graphInteractionEvidenceRepository;

    @Autowired
    public GraphInteractionService(GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository,
                                   GraphInteractionEvidenceRepository graphInteractionEvidenceRepository) {
        this.graphBinaryInteractionEvidenceRepository = graphBinaryInteractionEvidenceRepository;
        this.graphInteractionEvidenceRepository = graphInteractionEvidenceRepository;
    }

    public Page<GraphBinaryInteractionEvidence> findAll(Pageable page, int depth) {
        return this.graphBinaryInteractionEvidenceRepository.findAll(page, depth);
    }

    public GraphClusteredInteraction findClusteredInteraction(String uniqueKey) {
        return this.graphBinaryInteractionEvidenceRepository.getClusteredInteraction(uniqueKey);
    }

    public GraphInteractionEvidence findByInteractionAc(String ac, int depth) {
        GraphInteractionEvidence graphInteractionEvidence = null;
        Page<GraphInteractionEvidence> page= graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0,1), depth);
        if (page != null && page.getContent() != null && !page.getContent().isEmpty()){
            graphInteractionEvidence = page.getContent().get(0);
        }
        return graphInteractionEvidence;
    }
}
