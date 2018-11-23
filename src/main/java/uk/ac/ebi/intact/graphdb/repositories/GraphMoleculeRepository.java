package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphMolecule;

/**
 * Created by anjali on 23/11/18.
 */
@RepositoryRestResource(collectionResourceRel = "molecule", path = "molecules")
public interface GraphMoleculeRepository extends Neo4jRepository<GraphMolecule, Long> {
}
