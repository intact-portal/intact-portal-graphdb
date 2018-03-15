package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.domain.ClusterDataFeed;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphClusteredInteraction;

/**
 * Created by anjali on 06/03/18.
 */
@RepositoryRestResource(collectionResourceRel = "clusteredInteractions", path = "clusteredInteractions")
public interface GraphClusteredInteractionRepository extends GraphRepository<GraphClusteredInteraction> {

}
