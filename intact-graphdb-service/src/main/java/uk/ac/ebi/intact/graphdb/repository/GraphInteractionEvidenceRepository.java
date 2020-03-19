package uk.ac.ebi.intact.graphdb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 10/09/2014
 * Time: 18:50
 */

@Repository
public interface GraphInteractionEvidenceRepository extends Neo4jRepository<GraphInteractionEvidence, Long> {

    GraphInteractionEvidence findByShortName(String shortName);

    GraphInteractionEvidence findByIdentifiers(Xref identifier);

    Page<GraphInteractionEvidence> findTopByAc(String ac, Pageable pageable, @Depth int depth);

}
