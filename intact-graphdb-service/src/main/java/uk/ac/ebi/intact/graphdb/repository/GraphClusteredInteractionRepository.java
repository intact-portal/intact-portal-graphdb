package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphClusteredInteraction;

/**
 * Created by anjali on 06/03/18.
 */
@Repository
public interface GraphClusteredInteractionRepository extends Neo4jRepository<GraphClusteredInteraction, Long> {

}
