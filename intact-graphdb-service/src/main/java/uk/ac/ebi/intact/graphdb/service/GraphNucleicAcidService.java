package uk.ac.ebi.intact.graphdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphNucleicAcid;
import uk.ac.ebi.intact.graphdb.repository.GraphNucleicAcidRepository;

import java.util.Optional;

/**
 * Created by anjali on 23/11/18.
 */
@Service
public class GraphNucleicAcidService {

    final private GraphNucleicAcidRepository graphNucleicAcidRepository;

    @Autowired
    public GraphNucleicAcidService(GraphNucleicAcidRepository graphNucleicAcidRepository) {
        this.graphNucleicAcidRepository = graphNucleicAcidRepository;
    }

    public Optional<GraphNucleicAcid> findWithDepth(Long id, int depth) {
        return graphNucleicAcidRepository.findById(id, depth);
    }
}
