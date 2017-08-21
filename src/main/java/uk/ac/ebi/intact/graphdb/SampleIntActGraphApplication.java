package uk.ac.ebi.intact.graphdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 */
@SpringBootApplication
@EntityScan({"uk.ac.ebi.intact.graphdb"})
public class SampleIntActGraphApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleIntActGraphApplication.class, args);
    }
}
