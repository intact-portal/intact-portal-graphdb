package uk.ac.ebi.intact.graphdb.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.relationships.Interaction;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 10/09/2014
 * Time: 18:50
 */

@RepositoryRestResource(collectionResourceRel = "interactions", path = "interactions")
public interface InteractionRepository extends GraphRepository<Interaction> {


    Interaction findById(Long id);

    Page<Interaction> findByInteractorA_Accession(Pageable pageable, String accession);

    Page<Interaction> findByInteractorB_Accession(Pageable pageable, String accession);
}
