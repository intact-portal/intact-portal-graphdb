package uk.ac.ebi.intact.graphdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 */
@SpringBootApplication
@EntityScan({"uk.ac.ebi.intact.graphdb.model"})
@EnableNeo4jRepositories("uk.ac.ebi.intact.graphdb.repositories")
public class SampleIntActGraphApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleIntActGraphApplication.class, args);
    }
}
