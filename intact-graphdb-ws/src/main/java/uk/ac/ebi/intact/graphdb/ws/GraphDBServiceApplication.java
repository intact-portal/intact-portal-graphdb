package uk.ac.ebi.intact.graphdb.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import uk.ac.ebi.intact.graphdb.aop.LazyFetchAspect;


@SpringBootApplication(scanBasePackages = {"uk.ac.ebi.intact.graphdb.service",
        "uk.ac.ebi.intact.graphdb.repository",
        "uk.ac.ebi.intact.graphdb.ws.controller"})
@EntityScan({"uk.ac.ebi.intact.graphdb.model"})
@EnableNeo4jRepositories("uk.ac.ebi.intact.graphdb.repository")
public class GraphDBServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphDBServiceApplication.class, args);
    }

    /* This enables aspectJ (together with the aop.enable property in application.properties) */
    @Bean
    public LazyFetchAspect lazyFetchAspect() {
        return org.aspectj.lang.Aspects.aspectOf(LazyFetchAspect.class);
    }
}
