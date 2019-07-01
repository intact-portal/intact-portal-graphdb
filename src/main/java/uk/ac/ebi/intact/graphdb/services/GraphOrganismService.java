package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psidev.psi.mi.jami.model.Organism;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphOrganism;
import uk.ac.ebi.intact.graphdb.repositories.GraphOrganismRepository;

import java.util.Optional;

@Service
public class GraphOrganismService {

    final private GraphOrganismRepository graphOrganismRepository;

    @Autowired
    public GraphOrganismService(GraphOrganismRepository graphOrganismRepository) {
        this.graphOrganismRepository = graphOrganismRepository;
    }

    public Organism findHostOrganismByExperimentAc(String experimentAc){
        Optional<GraphOrganism> organismOpt = graphOrganismRepository.findHostOrganismByExperimentAc(experimentAc);
        return organismOpt.orElse(null);
    }
}
