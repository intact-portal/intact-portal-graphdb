package uk.ac.ebi.intact.graphdb.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

/**
 * Created by anjali on 24/11/17.
 */
@RepositoryRestResource(collectionResourceRel = "binaryInteractions", path = "binaryInteractions")
public interface BinaryInteractionEvidenceRepository extends GraphRepository<GraphBinaryInteractionEvidence> {

    GraphBinaryInteractionEvidence findByShortName(@Param("shortName") String shortName);

    GraphBinaryInteractionEvidence findByIdentifiers(GraphXref identifier);



}
