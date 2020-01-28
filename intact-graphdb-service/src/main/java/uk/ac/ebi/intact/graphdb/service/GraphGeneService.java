package uk.ac.ebi.intact.graphdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphGene;
import uk.ac.ebi.intact.graphdb.repository.GraphGeneRepository;

import java.util.Optional;

/**
 * Created by anjali on 23/11/18.
 */
@Service
public class GraphGeneService {

    final private GraphGeneRepository graphGeneRepository;

    @Autowired
    public GraphGeneService(GraphGeneRepository graphGeneRepository) {
        this.graphGeneRepository = graphGeneRepository;
    }

    public Optional<GraphGene> findWithDepth(Long id, int depth) {
        return graphGeneRepository.findById(id, depth);
    }
}
