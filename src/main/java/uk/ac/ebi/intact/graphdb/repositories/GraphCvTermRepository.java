package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

import java.util.Optional;

/**
 * Created by ntoro on 02/06/19.
 */
@RepositoryRestResource(collectionResourceRel = "cvTerm", path = "cvTerms")
public interface GraphCvTermRepository extends Neo4jRepository<GraphCvTerm, String> {

    @Query(value = CypherQueries.GET_INTERACTION_DETMETHOD_BY_EXPERIMENT_AC)
    Optional<GraphCvTerm> findInteractionDetMethodByExperimentAc(String experimentAc);

}
