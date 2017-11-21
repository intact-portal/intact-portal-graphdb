package uk.ac.ebi.intact.graphdb.repositories;


import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import psidev.psi.mi.jami.model.Protein;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 26/03/2014
 * Time: 20:32
 */
public interface ProteinRepository extends GraphRepository<Protein> {

    Protein findByShortName(@Param("shortName") String shortName );

}

