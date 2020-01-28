package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphGene;

/**
 * Created by anjali on 23/11/18.
 */
@Repository
public interface GraphGeneRepository extends Neo4jRepository<GraphGene, Long> {
}
