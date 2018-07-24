package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractorRepository;
import uk.ac.ebi.intact.graphdb.repositories.GraphParticpantRepository;

import java.util.Optional;

/**
 * Created by anjali on 23/07/18.
 */
@Service
public class GraphParticipantService {

    final private GraphParticpantRepository graphParticpantRepository;

    @Autowired
    public GraphParticipantService(GraphParticpantRepository graphParticpantRepository) {
        this.graphParticpantRepository = graphParticpantRepository;
    }

    public Optional<GraphParticipantEvidence> find(String id) {
        return graphParticpantRepository.findById(id);
    }

    public Optional<GraphParticipantEvidence> findWithDepth(String id, int depth) {
        return graphParticpantRepository.findById(id,depth);
    }

    public Page<GraphParticipantEvidence> findAll(Pageable page, int depth) {
        return graphParticpantRepository.findAll(page, depth);
    }
}
