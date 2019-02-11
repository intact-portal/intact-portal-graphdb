package uk.ac.ebi.intact.graphdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EntityScan({"uk.ac.ebi.intact.graphdb.model"})
@EnableNeo4jRepositories("uk.ac.ebi.intact.graphdb.repositories")
public class GraphDBApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphDBApplication.class, args);
    }
}
