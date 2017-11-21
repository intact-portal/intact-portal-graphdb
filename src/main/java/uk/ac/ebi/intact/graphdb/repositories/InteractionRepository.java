package uk.ac.ebi.intact.graphdb.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.Xref;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 10/09/2014
 * Time: 18:50
 */

@RepositoryRestResource(collectionResourceRel = "interactions", path = "interactions")
public interface InteractionRepository extends GraphRepository<InteractionEvidence> {

    BinaryInteractionEvidence findByShortName(@Param("shortName") String shortName);

    BinaryInteractionEvidence findByIdentifiers(Xref identifier);

    Page<BinaryInteractionEvidence> findByInteractorA_ShortName(Pageable pageable, @Param("shortName") String shortName);

    Page<BinaryInteractionEvidence> findByInteractorB_ShortName(Pageable pageable, @Param("shortName") String shortName);
}
