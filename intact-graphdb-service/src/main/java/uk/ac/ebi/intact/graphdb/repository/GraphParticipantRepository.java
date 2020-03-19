package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;

/**
 * Created by anjali on 23/07/18.
 */
@Repository
public interface GraphParticipantRepository extends Neo4jRepository<GraphParticipantEvidence, String> {

    Page<GraphParticipantEvidence> findByInteractionAc(String ac, Pageable page);
}
