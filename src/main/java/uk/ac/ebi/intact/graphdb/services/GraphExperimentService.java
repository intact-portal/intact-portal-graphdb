package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.repositories.GraphExperimentRepository;

import java.util.Optional;

/**
 * Created by anjali on 24/07/18.
 */
@Service
public class GraphExperimentService {

    final private GraphExperimentRepository graphExperimentRepository;

    @Autowired
    public GraphExperimentService(GraphExperimentRepository graphExperimentRepository) {
        this.graphExperimentRepository = graphExperimentRepository;
    }

    public Optional<GraphExperiment> find(String id) {
        return graphExperimentRepository.findById(id);
    }

    public Optional<GraphExperiment> findWithDepth(String id, int depth) {
        return graphExperimentRepository.findById(id,depth);
    }

    public Page<GraphExperiment> findAll(Pageable page, int depth) {
        return graphExperimentRepository.findAll(page, depth);
    }

    public GraphExperiment findByAc(String ac) {
        return graphExperimentRepository.findByAc(ac);
    }

    public GraphExperiment findByInteractionAc(String ac) {
        if(graphExperimentRepository.findByInteractionAc(ac).isPresent()) {
            return graphExperimentRepository.findByInteractionAc(ac).get();
        }

        return null;
    }

}
