package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

import java.util.Optional;

/**
 * Created by ntoro on 02/06/19.
 */
@Repository
public interface GraphCvTermRepository extends Neo4jRepository<GraphCvTerm, String> {

    @Query(value = CypherQueries.GET_INTERACTION_DETMETHOD_BY_EXPERIMENT_AC)
    Optional<GraphCvTerm> findInteractionDetMethodByExperimentAc(String experimentAc);

}
