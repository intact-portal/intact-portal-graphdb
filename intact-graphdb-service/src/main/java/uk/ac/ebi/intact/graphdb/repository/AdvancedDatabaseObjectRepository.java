package uk.ac.ebi.intact.graphdb.repository;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphDatabaseObject;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParameterValue;
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

    public Collection<Object> findCollectionByRelationship(Long dbId, String clazz, Class<?> collectionClass, String direction, String... relationships) {
        Result result = queryRelationshipTypesByDbId(dbId, clazz, direction, relationships);

        Collection<Object> databaseObjects;
        if (collectionClass.getName().equals(Set.class.getName())) {
            databaseObjects = new HashSet<>();
            //No need to check stoichiometry
            for (Map<String, Object> stringObjectMap : result) {
                databaseObjects.add(stringObjectMap.get("m"));
            }
        } else {
            databaseObjects = new ArrayList<>();
            //Here stoichiometry has to be taken into account
            for (Map<String, Object> stringObjectMap : result) {
                databaseObjects.add(stringObjectMap.get("m"));

            }
        }
        return databaseObjects.isEmpty() ? null : databaseObjects;
    }

    private Result queryRelationshipTypesByDbId(Long dbId, String clazz, String direction, String... relationships) {
        String query;

        switch (direction) {
            case "OUTGOING":
                query = "MATCH (x:GraphDatabaseObject)-[r" + CommonUtility.getRelationshipAsString(relationships) + "]->(m:" + clazz + ") WHERE ID(x)={dbId} RETURN m";
                break;
            case "INCOMING":
                query = "MATCH (x:GraphDatabaseObject)<-[r" + CommonUtility.getRelationshipAsString(relationships) + "]-(m:" + clazz + ") WHERE ID(x)={dbId} RETURN m";
                break;
            default: //UNDIRECTED
                query = "MATCH (x:GraphDatabaseObject)-[r" + CommonUtility.getRelationshipAsString(relationships) + "]-(m:" + clazz + ") WHERE ID(x)={dbId} RETURN m";
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

    public GraphParameterValue findValueForGraphParameter(Long dbId, String clazz, String direction, String... relationships) {
        Result result = queryRelationshipTypesByDbId(dbId, clazz, direction, relationships);

        if (result != null && result.iterator().hasNext())
            return (GraphParameterValue) result.iterator().next().get("m");
        return null;
    }

}
