package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

import java.util.List;
import java.util.Set;

/**
 * Created by anjali on 27/07/18.
 */
@RepositoryRestResource(collectionResourceRel = "feature", path = "feature")
public interface GraphFeatureRepository extends Neo4jRepository<GraphFeatureEvidence, String> {

    List<GraphFeatureEvidence> findByUniqueKeyIn(Set<String> uniqueKey, @Depth int depth);

    @Query(value= CypherQueries.GET_FEATURES_BY_INTERACTION_AC,
            countQuery= CypherQueries.GET_FEATURES_BY_INTERACTION_AC_COUNT)
    Page<GraphFeatureEvidence> findByInteractionAc(String ac, Pageable page);
}
