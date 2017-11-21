package uk.ac.ebi.intact.graphdb.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 10/09/2014
 * Time: 18:50
 */

@RepositoryRestResource(collectionResourceRel = "interactions", path = "interactions")
public interface InteractionRepository extends GraphRepository<BinaryInteractionEvidence> {


    BinaryInteractionEvidence findById(Long id);

    Page<BinaryInteractionEvidence> findByInteractorA_Accession(Pageable pageable, String accession);

    Page<BinaryInteractionEvidence> findByInteractorB_Accession(Pageable pageable, String accession);
}
