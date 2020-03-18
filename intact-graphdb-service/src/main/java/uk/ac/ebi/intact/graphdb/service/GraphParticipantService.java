package uk.ac.ebi.intact.graphdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;
import uk.ac.ebi.intact.graphdb.repository.GraphParticipantRepository;

import java.util.Optional;

/**
 * Created by anjali on 23/07/18.
 */
@Service
public class GraphParticipantService {

    final private GraphParticipantRepository graphParticpantRepository;

    @Autowired
    public GraphParticipantService(GraphParticipantRepository graphParticpantRepository) {
        this.graphParticpantRepository = graphParticpantRepository;
    }

    public Optional<GraphParticipantEvidence> find(String id) {
        return graphParticpantRepository.findById(id);
    }

    public Optional<GraphParticipantEvidence> findWithDepth(String id, int depth) {
        return graphParticpantRepository.findById(id, depth);
    }

    public Page<GraphParticipantEvidence> findAll(Pageable page, int depth) {
        return graphParticpantRepository.findAll(page, depth);
    }

    public Page<GraphParticipantEvidence> findByInteractionAc(String ac, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return graphParticpantRepository.findByInteractionAc(ac, pageRequest);
    }
}
