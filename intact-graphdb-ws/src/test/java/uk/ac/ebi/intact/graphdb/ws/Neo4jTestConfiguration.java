  
package uk.ac.ebi.intact.graphdb.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class Neo4jTestConfiguration {

    /* The main configuration from GraphDBApplication.class is appended to this test configuration thanks to
    * @TestConfiguration annotation */
    public static void main(String[] args) {
        SpringApplication.run(Neo4jTestConfiguration.class, args);
    }
}