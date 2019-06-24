package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psidev.psi.mi.jami.model.CvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.repositories.GraphCvTermRepository;

import java.util.Optional;

@Service
public class GraphCvTermService {

    final private GraphCvTermRepository graphCvTermRepository;

    @Autowired
    public GraphCvTermService(GraphCvTermRepository graphCvTermRepository) {
        this.graphCvTermRepository = graphCvTermRepository;
    }

    public CvTerm findInteractionDetMethodByExperimentAc(String experimentAc){
        Optional<GraphCvTerm>  graphCvTermOpt = graphCvTermRepository.findInteractionDetMethodByExperimentAc(experimentAc);
        return graphCvTermOpt.orElse(null);
    }
}
