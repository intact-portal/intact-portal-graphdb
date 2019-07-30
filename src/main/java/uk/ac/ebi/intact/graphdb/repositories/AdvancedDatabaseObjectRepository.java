package uk.ac.ebi.intact.graphdb.repositories;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphDatabaseObject;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;

import java.util.*;

/**
 * Created by anjali on 24/07/19.
 */
@Repository
public class AdvancedDatabaseObjectRepository {
    private Session session;

    @Autowired
    public AdvancedDatabaseObjectRepository(Session session) {
        this.session = session;
    }

    public Collection<GraphDatabaseObject> findCollectionByRelationship(Long dbId, String clazz, Class<?> collectionClass, String direction, String... relationships) {
        Result result = queryRelationshipTypesByDbId(dbId, clazz, direction, relationships);

        Collection<GraphDatabaseObject> databaseObjects;
        if (collectionClass.getName().equals(Set.class.getName())) {
            databaseObjects = new HashSet<>();
            //No need to check stoichiometry
            for (Map<String, Object> stringObjectMap : result) {
                databaseObjects.add((GraphDatabaseObject) stringObjectMap.get("m"));
            }
        } else {
            databaseObjects = new ArrayList<>();
            //Here stoichiometry has to be taken into account
            for (Map<String, Object> stringObjectMap : result) {
                databaseObjects.add((GraphDatabaseObject) stringObjectMap.get("m"));

            }
        }
        return databaseObjects.isEmpty() ? null : databaseObjects;
    }

    private Result queryRelationshipTypesByDbId(Long dbId, String clazz, String direction, String... relationships) {
        String query;
        String relationShipClass;
        if (clazz == null) {
            relationShipClass = "GraphDatabaseObject";
        } else {
            relationShipClass = clazz;
        }
        switch (direction) {
            case "OUTGOING":
                query = "MATCH (x:GraphDatabaseObject)-[r" + CommonUtility.getRelationshipAsString(relationships) + "]->(m:" + relationShipClass + ") WHERE ID(x)={dbId} RETURN m";
                break;
            case "INCOMING":
                query = "MATCH (x:GraphDatabaseObject)<-[r" + CommonUtility.getRelationshipAsString(relationships) + "]-(m:" + relationShipClass + ") WHERE ID(x)={dbId} RETURN m";
                break;
            default: //UNDIRECTED
                query = "MATCH (x:GraphDatabaseObject)-[r" + CommonUtility.getRelationshipAsString(relationships) + "]-(m:" + relationShipClass + ") WHERE ID(x)={dbId} RETURN m";
                break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dbId", dbId);

        return session.query(query, map);
    }

    public <T extends GraphDatabaseObject> T findByRelationship(Long dbId, String clazz, String direction, String... relationships) {
        Result result = queryRelationshipTypesByDbId(dbId, clazz, direction, relationships);

        if (result != null && result.iterator().hasNext())
            return (T) result.iterator().next().get("m");
        return null;
    }

}
