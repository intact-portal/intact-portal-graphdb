package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPolymer;

/**
 * Created by anjali on 23/11/18.
 */
@RepositoryRestResource(collectionResourceRel = "polymer", path = "polymers")
public interface GraphPolymerRepository extends Neo4jRepository<GraphPolymer, Long> {
}
