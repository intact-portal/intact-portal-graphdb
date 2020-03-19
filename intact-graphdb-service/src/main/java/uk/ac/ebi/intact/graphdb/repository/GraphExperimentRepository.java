package uk.ac.ebi.intact.graphdb.repository;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphExperiment;

/**
 * Created by anjali on 24/07/18.
 */
@Repository
public interface GraphExperimentRepository extends Neo4jRepository<GraphExperiment, String> {

    GraphExperiment findByAc(@Param("ac") String ac, @Depth int depth);

}
