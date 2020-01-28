  
package uk.ac.ebi.intact.graphdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import uk.ac.ebi.intact.graphdb.aop.LazyFetchAspect;

@TestConfiguration
@EntityScan({"uk.ac.ebi.intact.graphdb.model"})
@EnableNeo4jRepositories(basePackages = "uk.ac.ebi.intact.graphdb.repository")
@SpringBootApplication
public class Neo4jTestConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(Neo4jTestConfiguration.class, args);
    }

    /* This enables aspectJ (together with the aop.enable property in application.properties) */
    @Bean
    public LazyFetchAspect lazyFetchAspect() {
        return org.aspectj.lang.Aspects.aspectOf(LazyFetchAspect.class);
    }
}