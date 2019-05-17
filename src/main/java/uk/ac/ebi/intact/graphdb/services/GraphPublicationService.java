package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;
import uk.ac.ebi.intact.graphdb.repositories.GraphPublicationRepository;

import java.util.Optional;

/**
 * Created by anjali on 09/08/18.
 */
@Service
public class GraphPublicationService {
    final private GraphPublicationRepository graphPublicationRepository;

    @Autowired
    public GraphPublicationService(GraphPublicationRepository graphPublicationRepository) {
        this.graphPublicationRepository = graphPublicationRepository;
    }

    public Optional<GraphPublication> find(String id) {
        return graphPublicationRepository.findById(id);
    }

    public Optional<GraphPublication> findWithDepth(String id, int depth) {
        return graphPublicationRepository.findById(id, depth);
    }

    public Page<GraphPublication> findAll(Pageable page, int depth) {
        return graphPublicationRepository.findAll(page, depth);
    }

}
