package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphProtein;

/**
 * Created by anjali on 23/11/18.
 */
@Repository
public interface GraphProteinRepository extends Neo4jRepository<GraphProtein, Long> {
}
