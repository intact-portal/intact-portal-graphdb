package uk.ac.ebi.intact.graphdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.*;
import uk.ac.ebi.intact.graphdb.repository.GraphBinaryInteractionEvidenceRepository;
import uk.ac.ebi.intact.graphdb.repository.GraphInteractionEvidenceRepository;

import java.util.*;

/**
 * Created by anjali on 12/07/18.
 */
@Service
public class GraphInteractionService {

    final private GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository;
    final private GraphInteractionEvidenceRepository graphInteractionEvidenceRepository;

    @Autowired
    public GraphInteractionService(GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository,
                                   GraphInteractionEvidenceRepository graphInteractionEvidenceRepository) {
        this.graphBinaryInteractionEvidenceRepository = graphBinaryInteractionEvidenceRepository;
        this.graphInteractionEvidenceRepository = graphInteractionEvidenceRepository;
    }

    /*
    * TODO... After having binaryids in database change this method to find by binary id instead
    * */
    public GraphBinaryInteractionEvidence findWithBinaryId(int id, int depth) {
        Optional<GraphBinaryInteractionEvidence> optionalGraphInteractionEvidence =
                graphBinaryInteractionEvidenceRepository.findByBinaryId(id, depth);
        return optionalGraphInteractionEvidence.orElse(null);
    }

    public Page<GraphBinaryInteractionEvidence> findAll(Pageable page, int depth) {
        return this.graphBinaryInteractionEvidenceRepository.findAll(page, depth);
    }

    public Slice<GraphBinaryInteractionEvidence> getAllGraphBinaryInteractionEvidences(Pageable page) {
        return this.graphBinaryInteractionEvidenceRepository.getAllGraphBinaryInteractionEvidences(page);
    }

    public GraphClusteredInteraction findClusteredInteraction(String idA, String idB) {
        return this.graphBinaryInteractionEvidenceRepository.getClusteredInteraction(idA, idB);
    }
//   TODO: Evaluate if using the method below has an unexpected consequences, but there shoulb be only one ac for
//    one interactionEvidence.
//
//    public GraphInteractionEvidence findByInteractionAc(String ac) {
//        GraphInteractionEvidence graphInteractionEvidence = null;
//        Page<GraphInteractionEvidence> page = graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0, 1), 0);
//        if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
//            graphInteractionEvidence = page.getContent().get(0);
//        }
//        return graphInteractionEvidence;
//    }

    public GraphInteractionEvidence findByInteractionAc(String ac) {
        return graphInteractionEvidenceRepository.findByAc(ac);
    }

    public Page<GraphInteractionEvidence> findByInteractionAcs(Iterable<String> acs, Pageable page) {
        return graphInteractionEvidenceRepository.findByAcIn(acs, page, 0);
    }

    public Slice<GraphBinaryInteractionEvidence> findByBinaryInteractionIds(Iterable<Long> ids, Pageable page) {
        return graphBinaryInteractionEvidenceRepository.findByBinaryIdIn(ids, page, 0);
    }

    public GraphBinaryInteractionEvidence findBinaryInteractionByUniqueKey(String uniqueKey, int depth) {
        Optional<GraphBinaryInteractionEvidence> optionalGraphInteractionEvidence =
                graphBinaryInteractionEvidenceRepository.findByUniqueKey(uniqueKey, depth);
        return optionalGraphInteractionEvidence.orElse(null);
    }

    public GraphInteractionEvidence findByInteractionAcForMiJson(String ac) {
        GraphInteractionEvidence graphInteractionEvidence = graphInteractionEvidenceRepository.findByAc(ac);
        if (graphInteractionEvidence != null) {
            // below is needed so that everytime participants come in same order so that unit tests do not fail unnecessarily

            Comparator<GraphParticipantEvidence> participantEvidenceComparator = Comparator.comparing(GraphParticipantEvidence::getAc);
            Comparator<GraphFeature> featureEvidenceComparator = Comparator.comparing(GraphFeature::getAc);

            // below is needed so that everytime features come in same order so that unit tests do not fail unnecessarily
            for (GraphParticipantEvidence graphParticipantEvidence : graphInteractionEvidence.getParticipants()) {
                ((List<GraphFeature>) graphParticipantEvidence.getFeatures()).sort(featureEvidenceComparator);
            }

            ((List<GraphParticipantEvidence>) graphInteractionEvidence.getParticipants()).sort(participantEvidenceComparator);
            return graphInteractionEvidence;
        }
        return null;
    }

    public Iterable<Map<String, Object>> findNetworkEdges(Set<String> interactorAcs, boolean neighboursRequired) {
        return this.graphBinaryInteractionEvidenceRepository.findNetworkEdges(interactorAcs, neighboursRequired);
    }

}
