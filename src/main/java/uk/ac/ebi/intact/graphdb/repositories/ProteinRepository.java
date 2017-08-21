package uk.ac.ebi.intact.graphdb.repositories;


import org.springframework.data.neo4j.repository.GraphRepository;
import uk.ac.ebi.intact.graphdb.model.nodes.Protein;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 26/03/2014
 * Time: 20:32
 */
public interface ProteinRepository extends GraphRepository<Protein> {

    Protein findByAccession(String accession);


}

