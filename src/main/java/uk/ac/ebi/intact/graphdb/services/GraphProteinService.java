package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphProtein;
import uk.ac.ebi.intact.graphdb.repositories.GraphProteinRepository;

import java.util.Optional;

/**
 * Created by anjali on 23/11/18.
 */
@Service
public class GraphProteinService {

    final private GraphProteinRepository graphProteinRepository;

    @Autowired
    public GraphProteinService(GraphProteinRepository graphProteinRepository) {
        this.graphProteinRepository = graphProteinRepository;
    }

    public Optional<GraphProtein> findWithDepth(Long id, int depth) {
        return graphProteinRepository.findById(id,depth);
    }
}
