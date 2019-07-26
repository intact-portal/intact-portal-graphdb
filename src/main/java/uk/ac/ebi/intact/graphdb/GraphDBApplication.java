package uk.ac.ebi.intact.graphdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import uk.ac.ebi.intact.graphdb.aop.LazyFetchAspect;


@SpringBootApplication
@EntityScan({"uk.ac.ebi.intact.graphdb.model"})
@EnableNeo4jRepositories("uk.ac.ebi.intact.graphdb.repositories")
@EnableAspectJAutoProxy
public class GraphDBApplication {

/*    @Value("${aop.enabled}")
    private boolean aopEnabled;*/

    public static void main(String[] args) {
        SpringApplication.run(GraphDBApplication.class, args);
    }

/*    @Bean
    public LazyFetchAspect lazyFetchAspect() {
        LazyFetchAspect lazyFetchAspect=org.aspectj.lang.Aspects.aspectOf(LazyFetchAspect.class);
        lazyFetchAspect.setEnableAOP(aopEnabled);
        return lazyFetchAspect;
    }*/
}
