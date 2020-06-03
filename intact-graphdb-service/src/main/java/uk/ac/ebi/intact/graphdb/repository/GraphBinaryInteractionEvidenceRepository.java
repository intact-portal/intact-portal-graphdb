package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.domain.ClusterDataFeed;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphClusteredInteraction;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by anjali on 24/11/17.
 */
@Repository
public interface GraphBinaryInteractionEvidenceRepository extends Neo4jRepository<GraphBinaryInteractionEvidence, Long> {

    GraphBinaryInteractionEvidence findByShortName(@Param("shortName") String shortName);

    GraphBinaryInteractionEvidence findByIdentifiers(GraphXref identifier);

    // @Query(value=CypherQueries.COMM_NEIGH_OF_INTOR,countQuery=CypherQueries.INTERACTOR_PAIR_COUNT)
    @Query(value = CypherQueries.COMM_NEIGH_OF_INTOR)
    Slice<ClusterDataFeed> getInteractorPairWithEvidences(Pageable page);

    @Query(value = CypherQueries.COMM_NEIGH_OF_INTOR_SELF_INTERACTION_CASE)
    Slice<ClusterDataFeed> getSelfInteractingEvidences(Pageable page);

    @Query(value = CypherQueries.COMM_NEIGH_OF_INTOR_SELF_PUTATIVE_CASE)
    Slice<ClusterDataFeed> getSelfPutativeEvidences(Pageable page);

    List<GraphBinaryInteractionEvidence> findByUniqueKeyIn(Set<String> uniqueKey, @Depth int depth);

    @Query(CypherQueries.INTERACTOR_PAIR_COUNT)
    long getInteractorPairCount();

    @Query(CypherQueries.GET_CLUSTERED_INTERACTION)
    GraphClusteredInteraction getClusteredInteraction(String idA, String idB);

    @Query(value = CypherQueries.GET_NETWORK_EDGES)
    Iterable<Map<String, Object>> findNetworkEdges(@Param("identifiers") Set<String> identifiers, @Param("species") Set<Integer> species, @Param("neighboursRequired") boolean neighboursRequired);

    @Query(value = CypherQueries.INTERACTION_BY_BINARY_ID)
    Optional<GraphBinaryInteractionEvidence> findWithBinaryId(@Param("binary_id") int id, @Depth int depth);

    Optional<GraphBinaryInteractionEvidence> findByAc(String ac, @Depth int depth);

    Optional<GraphBinaryInteractionEvidence> findByUniqueKey(String uniqueKey, @Depth int depth);

    @Query(value = CypherQueries.ALL_BINARY_INTERACTIONS)
    Slice<GraphBinaryInteractionEvidence> getAllGraphBinaryInteractionEvidences(Pageable page);

}

