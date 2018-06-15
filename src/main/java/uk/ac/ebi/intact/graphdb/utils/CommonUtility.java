package uk.ac.ebi.intact.graphdb.utils;

import org.apache.commons.lang3.ClassUtils;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.nodes.*;
import uk.ac.ebi.intact.jami.model.IntactPrimaryObject;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by anjali on 06/12/17.
 */
public class CommonUtility {

    private static final Map<Class, Label[]> labelMap = new HashMap<>();

    public static String extractAc(Object object) {
        String ac = null;

        if(object instanceof IntactPrimaryObject){
            ac=((IntactPrimaryObject) object).getAc();
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
        if(relCollection!=null) {
            for (GraphXref obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

    public static void createAliasRelationShips(Collection<GraphAlias> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphAlias obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

    public static void createAnnotationRelationShips(Collection<GraphAnnotation> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphAnnotation obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

    public static void createBinaryInteractionEvidenceRelationShips(Collection<GraphBinaryInteractionEvidence> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphBinaryInteractionEvidence obj : relCollection) {
                createRelationShip(obj, fromId, relationName);

            }
        }
    }

    public static void createFeatureEvidenceRelationShips(Collection<GraphFeatureEvidence> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphFeatureEvidence obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

    public static void createCausalRelationshipRelationShips(Collection<GraphCausalRelationship> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphCausalRelationship obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createConfidenceRelationShips(Collection<GraphConfidence> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphConfidence obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createVariableParameterRelationShips(Collection<GraphVariableParameter> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphVariableParameter obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createCvTermRelationShips(Collection<GraphCvTerm> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphCvTerm obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createParameterRelationShips(Collection<GraphParameter> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphParameter obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createVariableParameterValueSetRelationShips(Collection<GraphVariableParameterValueSet> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphVariableParameterValueSet obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createVariableParameterValueRelationShips(Collection<GraphVariableParameterValue> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphVariableParameterValue obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createCheckSumRelationShips(Collection<GraphChecksum> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphChecksum obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createRangeRelationShips(Collection<GraphRange> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphRange obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createExperimentRelationShips(Collection<GraphExperiment> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphExperiment obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

    public static void createAuthorRelationShips(Collection<GraphAuthor> relCollection, long fromId, String relationName) {
        if(relCollection!=null) {
            for (GraphAuthor obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }
    public static void createRelationShip(Object relObj, long fromId, String relationName) {
        try {
            if(relObj!=null) {
                Class clazz = relObj.getClass();
                Method method = clazz.getMethod("getGraphId");
                long endId = (Long) method.invoke(clazz.cast(relObj));
                if(fromId!=endId) {
                    RelationshipType relationshipType = RelationshipType.withName(relationName);
                    CreationConfig.batchInserter.createRelationship(fromId, endId, relationshipType, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NodeDataFeed createNode(Map<String, Object> nodeProperties, Label[] labels) {
        long id=-1;
        boolean isAlreadyCreated=false;
        String uniqueKey = (String)nodeProperties.get("uniqueKey");
        isAlreadyCreated=Constants.createdNodeIdMap.get(uniqueKey)!=null?true:false;

        if(isAlreadyCreated){
            id= Constants.createdNodeIdMap.get(uniqueKey);
        }else{
            id = CreationConfig.batchInserter.createNode(nodeProperties, labels);
            Constants.createdNodeIdMap.put(uniqueKey,id);
        }
       return new NodeDataFeed(id,isAlreadyCreated);
    }


}

