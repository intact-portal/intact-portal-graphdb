package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphOrganism;
import uk.ac.ebi.intact.graphdb.utils.CypherQueries;

import java.util.Optional;

/**
 * Created by ntoro on 02/06/19.
 */
@RepositoryRestResource(collectionResourceRel = "organism", path = "organisms")
public interface GraphOrganismRepository extends Neo4jRepository<GraphOrganism, String> {

    @Query(value = CypherQueries.GET_HOST_ORGANISM_BY_EXPERIMENT_AC)
    Optional<GraphOrganism> findHostOrganismByExperimentAc(String experimentAc);

}
