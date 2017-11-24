package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import psidev.psi.mi.jami.model.Protein;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;

/**
 * Created by anjali on 24/11/17.
 */
@Repository
public interface BinaryInteractionEvidenceRepository extends GraphRepository<GraphBinaryInteractionEvidence> {



}
