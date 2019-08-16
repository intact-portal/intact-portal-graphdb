package uk.ac.ebi.intact.graphdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import uk.ac.ebi.intact.graphdb.aop.LazyFetchAspect;


@SpringBootApplication
@EntityScan({"uk.ac.ebi.intact.graphdb.model"})
@EnableNeo4jRepositories("uk.ac.ebi.intact.graphdb.repositories")
/*@EnableAspectJAutoProxy
@EnableLoadTimeWeaving(aspectjWeaving= EnableLoadTimeWeaving.AspectJWeaving.ENABLED)*/
public class GraphDBApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphDBApplication.class, args);
    }

    @Bean
    public LazyFetchAspect lazyFetchAspect() {
        LazyFetchAspect lazyFetchAspect = org.aspectj.lang.Aspects.aspectOf(LazyFetchAspect.class);
        return lazyFetchAspect;
    }
}
