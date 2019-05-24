package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.*;
import uk.ac.ebi.intact.graphdb.repositories.GraphBinaryInteractionEvidenceRepository;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractionEvidenceRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public Page<GraphBinaryInteractionEvidence> findAll(Pageable page, int depth) {
        return this.graphBinaryInteractionEvidenceRepository.findAll(page, depth);
    }

    public GraphClusteredInteraction findClusteredInteraction(String uniqueKey) {
        return this.graphBinaryInteractionEvidenceRepository.getClusteredInteraction(uniqueKey);
    }

    public GraphInteractionEvidence findByInteractionAc(String ac, int depth) {
        GraphInteractionEvidence graphInteractionEvidence = null;
        Page<GraphInteractionEvidence> page = graphInteractionEvidenceRepository.findTopByAc(ac, PageRequest.of(0, 1), depth);
        if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
            graphInteractionEvidence = page.getContent().get(0);
        }
        return graphInteractionEvidence;
    }

    public GraphInteractionEvidence findByInteractionAcForMiJson(String ac) {
        Optional<GraphInteractionEvidence> optionalExp = graphInteractionEvidenceRepository.findByInteractionAcForMiJson(ac);
        if (optionalExp.isPresent()) {
            // below is needed so that everytime participants come in same order so that unit tests do not fail unnecessarily
            GraphInteractionEvidence graphInteractionEvidence = optionalExp.get();
            Comparator<GraphParticipantEvidence> participantEvidenceComparator = new Comparator<GraphParticipantEvidence>() {
                @Override
                public int compare(GraphParticipantEvidence e1, GraphParticipantEvidence e2) {
                    return e1.getAc().compareTo(e2.getAc());
                }
            };

            // below is needed so that everytime features come in same order so that unit tests do not fail unnecessarily
            for (GraphParticipantEvidence graphParticipantEvidence : graphInteractionEvidence.getParticipants()) {

                Comparator<GraphFeatureEvidence> featureEvidenceComparator = new Comparator<GraphFeatureEvidence>() {
                    @Override
                    public int compare(GraphFeatureEvidence e1, GraphFeatureEvidence e2) {
                        return e1.getAc().compareTo(e2.getAc());
                    }
                };

                Collections.sort((List<GraphFeatureEvidence>) graphParticipantEvidence.getFeatures(), featureEvidenceComparator);
            }

            Collections.sort((List<GraphParticipantEvidence>) graphInteractionEvidence.getParticipants(), participantEvidenceComparator);
            return graphInteractionEvidence;
        }
        return null;
    }
}
