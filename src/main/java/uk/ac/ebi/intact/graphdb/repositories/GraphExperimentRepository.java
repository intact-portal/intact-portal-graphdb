package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;

/**
 * Created by anjali on 24/07/18.
 */
@RepositoryRestResource(collectionResourceRel = "experiments", path = "experiments")
public interface GraphExperimentRepository extends Neo4jRepository<GraphExperiment, String> {
}
