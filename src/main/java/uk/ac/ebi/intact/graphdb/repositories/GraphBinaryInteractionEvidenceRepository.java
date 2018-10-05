package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.domain.ClusterDataFeed;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphClusteredInteraction;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

import java.util.List;
import java.util.Set;

/**
 * Created by anjali on 24/11/17.
 */
@RepositoryRestResource(collectionResourceRel = "binaryInteractions", path = "binaryInteractions")
public interface GraphBinaryInteractionEvidenceRepository extends Neo4jRepository<GraphBinaryInteractionEvidence, Long> {

    GraphBinaryInteractionEvidence findByShortName(@Param("shortName") String shortName);

    GraphBinaryInteractionEvidence findByIdentifiers(GraphXref identifier);

   // @Query(value=CypherQueries.COMM_NEIGH_OF_INTOR,countQuery=CypherQueries.INTERACTOR_PAIR_COUNT)
   @Query(value=CypherQueries.COMM_NEIGH_OF_INTOR)
   Slice<ClusterDataFeed> getInteractorPairWithEvidences(Pageable page);

    List<GraphBinaryInteractionEvidence> findByUniqueKeyIn(Set<String> uniqueKey,@Depth int depth);

    @Query(CypherQueries.INTERACTOR_PAIR_COUNT)
    long getInteractorPairCount();

    @Query(CypherQueries.GET_CLUSTERED_INTERACTION)
    GraphClusteredInteraction getClusteredInteraction(String uniqueKey);

}