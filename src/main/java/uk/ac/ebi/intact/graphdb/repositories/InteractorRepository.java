package uk.ac.ebi.intact.graphdb.repositories;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 26/03/2014
 * Time: 20:32
 */
@RepositoryRestResource(collectionResourceRel = "interactor", path = "interactor")
public interface InteractorRepository extends GraphRepository<GraphInteractor> {

    GraphInteractor findByShortName(@Param("shortName") String shortName );

    @Query("MATCH (a:Interactor)<-[i:INTERACTS_IN]-(b:Interactor) RETURN i,a,b LIMIT {limit}")
    Collection<GraphInteractor> graph(@Param("limit") int limit);

}

