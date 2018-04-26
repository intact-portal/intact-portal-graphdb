package uk.ac.ebi.intact.graphdb.utils;

import org.apache.commons.lang3.ClassUtils;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.model.nodes.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by anjali on 06/12/17.
 */
public class CommonUtility {

    private static final Map<Class, Label[]> labelMap = new HashMap<>();

    public String extractAc(Collection<Xref> xrefs) {
        String ac = null;

        for (Xref xref : xrefs) {
            if (xref.getDatabase() != null && xref.getDatabase().getShortName() != null & xref.getDatabase().getShortName().equals(Constants.INTACT_DB)) {
                ac = xref.getId();
                break;
            }
        }

        return ac;
    }

    /**
     * Getting all SimpleNames as neo4j labels, for given class.
     *
     * @param clazz Clazz of object that will result form converting the instance (eg Pathway, Reaction)
     * @return Array of Neo4j SchemaClassCount
     */
    private static Label[] getAllClassNames(Class clazz) {
        List<?> superClasses = ClassUtils.getAllSuperclasses(clazz);
        List<Label> labels = new ArrayList<>();
        labels.add(Label.label(clazz.getSimpleName()));
        for (Object object : superClasses) {
            Class superClass = (Class) object;
            if (!superClass.equals(Object.class)) {
                labels.add(Label.label(superClass.getSimpleName()));
            }
        }
        return labels.toArray(new Label[labels.size()]);
    }

    /**
     * Getting all SimpleNames as neo4j labels, for given class.
     *
     * @param clazz Clazz of object that will result form converting the instance (eg Pathway, Reaction)
     * @return Array of Neo4j SchemaClassCount
     */
    public static Label[] getLabels(Class clazz) {

        if (!labelMap.containsKey(clazz)) {
            Label[] labels = getAllClassNames(clazz);
            labelMap.put(clazz, labels);
            return labels;
        } else {
            return labelMap.get(clazz);
        }
    }

    /**
     * Simple wrapper for creating a isUnique constraint
     *
     * @param clazz specific Class
     * @param name  fieldName
     */
    public static void createSchemaConstraint(Class clazz, String name) {
        try {
            CreationConfig.batchInserter.createDeferredConstraint(Label.label(clazz.getSimpleName())).assertPropertyIsUnique(name).create();
        } catch (Throwable e) {
            //ConstraintViolationException and PreexistingIndexEntryConflictException are both catch here
            //importLogger.warn("Could not create Constraint on " + clazz.getSimpleName() + " for " + name);
        }
    }


    /**
     * Simple wrapper for creating an index
     *
     * @param clazz specific Class
     * @param name  fieldName
     */
    public static void createDeferredSchemaIndex(Class clazz, String name) {
        try {
            CreationConfig.batchInserter.createDeferredSchemaIndex(Label.label(clazz.getSimpleName())).on(name).create();
        } catch (Throwable e) {
            //ConstraintViolationException and PreexistingIndexEntryConflictException are both catch here
            //importLogger.warn("Could not create Index on " + clazz.getSimpleName() + " for " + name);
        }
    }

    /*
    *
    *
    * */
    public static void createXrefRelationShips(Collection<GraphXref> relCollection, long fromId, String relationName) {
        for (GraphXref obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }

    public static void createAliasRelationShips(Collection<GraphAlias> relCollection, long fromId, String relationName) {
        for (GraphAlias obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }

    public static void createAnnotationRelationShips(Collection<GraphAnnotation> relCollection, long fromId, String relationName) {
        for (GraphAnnotation obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }

    public static void createBinaryInteractionEvidenceRelationShips(Collection<GraphBinaryInteractionEvidence> relCollection, long fromId, String relationName) {
        for (GraphBinaryInteractionEvidence obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }

    public static void createFeatureEvidenceRelationShips(Collection<GraphFeatureEvidence> relCollection, long fromId, String relationName) {
        for (GraphFeatureEvidence obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }

    public static void createCausalRelationshipRelationShips(Collection<GraphCausalRelationship> relCollection, long fromId, String relationName) {
        for (GraphCausalRelationship obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createConfidenceRelationShips(Collection<GraphConfidence> relCollection, long fromId, String relationName) {
        for (GraphConfidence obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createVariableParameterRelationShips(Collection<GraphVariableParameter> relCollection, long fromId, String relationName) {
        for (GraphVariableParameter obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createCvTermRelationShips(Collection<GraphCvTerm> relCollection, long fromId, String relationName) {
        for (GraphCvTerm obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createParameterRelationShips(Collection<GraphParameter> relCollection, long fromId, String relationName) {
        for (GraphParameter obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createVariableParameterValueSetRelationShips(Collection<GraphVariableParameterValueSet> relCollection, long fromId, String relationName) {
        for (GraphVariableParameterValueSet obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createVariableParameterValueRelationShips(Collection<GraphVariableParameterValue> relCollection, long fromId, String relationName) {
        for (GraphVariableParameterValue obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createCheckSumRelationShips(Collection<GraphChecksum> relCollection, long fromId, String relationName) {
        for (GraphChecksum obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createRangeRelationShips(Collection<GraphRange> relCollection, long fromId, String relationName) {
        for (GraphRange obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }
    public static void createExperimentRelationShips(Collection<GraphExperiment> relCollection, long fromId, String relationName) {
        for (GraphExperiment obj : relCollection) {
            createRelationShip(obj, fromId, relationName);
        }
    }

    public static void createRelationShip(Object relObj, long fromId, String relationName) {
        try {
            Class clazz = relObj.getClass();
            Method method = clazz.getMethod("getGraphId");
            long endId = (Long) method.invoke(clazz.cast(relObj));
            RelationshipType relationshipType = RelationshipType.withName(relationName);
            CreationConfig.batchInserter.createRelationship(fromId, endId, relationshipType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long createNode(Map<String, Object> nodeProperties, Label[] labels) {
        return CreationConfig.batchInserter.createNode(nodeProperties, labels);
    }
}

