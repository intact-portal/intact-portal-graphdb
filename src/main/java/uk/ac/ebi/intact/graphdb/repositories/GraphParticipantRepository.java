package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

/**
 * Created by anjali on 23/07/18.
 */
@RepositoryRestResource(collectionResourceRel = "participants", path = "participants")
public interface GraphParticipantRepository extends Neo4jRepository<GraphParticipantEvidence, String> {

    @Query(value= CypherQueries.GET_PARTICIPANTS_BY_INTERACTION_AC,
            countQuery=CypherQueries.GET_PARTICIPANTS_BY_INTERACTION_AC_COUNT)
    Page<GraphParticipantEvidence> findByInteractionAc(String ac,Pageable page);
}
