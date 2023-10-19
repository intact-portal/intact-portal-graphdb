package uk.ac.ebi.intact.graphdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.repository.GraphFeatureRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by anjali on 27/07/18.
 */
@Service
public class GraphFeatureService {

    final private GraphFeatureRepository graphFeatureRepository;

    @Autowired
    public GraphFeatureService(GraphFeatureRepository graphFeatureRepository) {
        this.graphFeatureRepository = graphFeatureRepository;
    }

    public Optional<GraphFeatureEvidence> find(String id) {
        return graphFeatureRepository.findById(id);
    }

    public Optional<GraphFeatureEvidence> findWithDepth(String id, int depth) {
        return graphFeatureRepository.findById(id,depth);
    }

    public Page<GraphFeatureEvidence> findAll(Pageable page, int depth) {
        return graphFeatureRepository.findAll(page, depth);
    }

    public List<GraphFeatureEvidence> findByUniqueKeyIn(Set<String> uniqueKeys, @Depth int depth){
        return graphFeatureRepository.findByUniqueKeyIn(uniqueKeys,depth);
    }

    public Page<GraphFeatureEvidence> findByInteractionAc(String ac, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return graphFeatureRepository.findByInteractionAc(ac, pageRequest);
    }
}
