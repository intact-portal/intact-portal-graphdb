package uk.ac.ebi.intact.graphdb.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;

import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 10/09/2014
 * Time: 18:50
 */

@RepositoryRestResource(collectionResourceRel = "interactions", path = "interactions")
public interface GraphInteractionEvidenceRepository extends Neo4jRepository<GraphInteractionEvidence, Long> {

    GraphInteractionEvidence findByShortName(String shortName);

    GraphInteractionEvidence findByIdentifiers(Xref identifier);

    Page<GraphInteractionEvidence> findTopByAc(String ac, Pageable pageable, @Depth int depth);
//
//    Page<BinaryInteractionEvidence> findByInteractorA_ShortName(Pageable pageable, @Param("shortName") String shortName);
//
//    Page<BinaryInteractionEvidence> findByInteractorB_ShortName(Pageable pageable, @Param("shortName") String shortName);
}
