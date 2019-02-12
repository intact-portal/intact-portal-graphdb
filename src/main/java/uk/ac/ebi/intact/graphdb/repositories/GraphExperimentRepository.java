package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

import java.util.Optional;

/**
 * Created by anjali on 24/07/18.
 */
@RepositoryRestResource(collectionResourceRel = "experiments", path = "experiments")
public interface GraphExperimentRepository extends Neo4jRepository<GraphExperiment, String> {

    GraphExperiment findByAc(@Param("ac") String ac );

    @Query(value= CypherQueries.GET_EXP_PUB_BY_INTERACTION_AC)
    Optional<GraphExperiment> findByInteractionAc(String ac);

}
