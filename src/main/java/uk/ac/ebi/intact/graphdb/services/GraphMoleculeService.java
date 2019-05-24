package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphMolecule;
import uk.ac.ebi.intact.graphdb.repositories.GraphMoleculeRepository;

import java.util.Optional;

/**
 * Created by anjali on 23/11/18.
 */
@Service
public class GraphMoleculeService {

    final private GraphMoleculeRepository graphMoleculeRepository;

    @Autowired
    public GraphMoleculeService(GraphMoleculeRepository graphMoleculeRepository) {
        this.graphMoleculeRepository = graphMoleculeRepository;
    }

    public Optional<GraphMolecule> findWithDepth(Long id, int depth) {
        return graphMoleculeRepository.findById(id, depth);
    }
}
