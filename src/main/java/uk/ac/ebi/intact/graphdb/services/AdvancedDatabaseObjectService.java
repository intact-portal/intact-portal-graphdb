package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphDatabaseObject;
import uk.ac.ebi.intact.graphdb.repositories.AdvancedDatabaseObjectRepository;

import java.util.Collection;

/**
 * Created by anjali on 25/07/19.
 */
@Service
public class AdvancedDatabaseObjectService {

    @Autowired
    private AdvancedDatabaseObjectRepository advancedDatabaseObjectRepository;

    public Collection<GraphDatabaseObject> findCollectionByRelationship(Long dbId, String clazz, Class<?> collectionClazz, String direction, String... relationships) {
        return advancedDatabaseObjectRepository.findCollectionByRelationship(dbId, clazz, collectionClazz, direction, relationships);
    }

    public <T extends GraphDatabaseObject> T findByRelationship(Long dbId, String clazz, String direction, String... relationships) {
        return advancedDatabaseObjectRepository.findByRelationship(dbId, clazz, direction, relationships);
    }
}