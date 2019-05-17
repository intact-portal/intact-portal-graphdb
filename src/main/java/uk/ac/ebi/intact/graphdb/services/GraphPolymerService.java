package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPolymer;
import uk.ac.ebi.intact.graphdb.repositories.GraphPolymerRepository;

import java.util.Optional;

/**
 * Created by anjali on 23/11/18.
 */
@Service
public class GraphPolymerService {

    final private GraphPolymerRepository graphPolymerRepository;

    @Autowired
    public GraphPolymerService(GraphPolymerRepository graphPolymerRepository) {
        this.graphPolymerRepository = graphPolymerRepository;
    }

    public Optional<GraphPolymer> findWithDepth(Long id, int depth) {
        return graphPolymerRepository.findById(id, depth);
    }
}
