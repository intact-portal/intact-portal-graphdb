package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphPublication;

/**
 * Created by anjali on 09/08/18.
 */
@RepositoryRestResource(collectionResourceRel = "publications", path = "publications")
public interface GraphPublicationRepository extends Neo4jRepository<GraphPublication, String> {
}
