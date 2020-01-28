package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;

/**
 * Created by anjali on 09/08/18.
 */
@Repository
public interface GraphPublicationRepository extends Neo4jRepository<GraphPublication, String> {
}
