package uk.ac.ebi.intact.graphdb.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import uk.ac.ebi.intact.graphdb.aop.LazyFetchAspect;

@EnableSolrRepositories(basePackages = "uk.ac.ebi.intact.search", schemaCreationSupport = true)
@EnableNeo4jRepositories("uk.ac.ebi.intact.graphdb.repository")
@SpringBootApplication(scanBasePackages = {"uk.ac.ebi.intact.graphdb.service",
        "uk.ac.ebi.intact.search.interactions.service",
        "uk.ac.ebi.intact.graphdb.repository",
        "uk.ac.ebi.intact.graphdb.ws"})
@EntityScan({"uk.ac.ebi.intact.graphdb.model"})
public class GraphDBServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphDBServiceApplication.class, args);
    }

    /* This enables aspectJ (together with the aop.enable property in application.properties) */
    @Bean
    public LazyFetchAspect lazyFetchAspect() {
        return org.aspectj.lang.Aspects.aspectOf(LazyFetchAspect.class);
    }

    /*
     * Hack for https://issues.apache.org/jira/browse/SOLR-12858 for embedded POST request issue
     * */
    @Bean
    public boolean isEmbeddedSolr() {
        return false;
    }

}
