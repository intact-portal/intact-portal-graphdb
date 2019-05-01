package uk.ac.ebi.intact.graphdb.utils;

import org.apache.commons.lang3.ClassUtils;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.nodes.*;

import java.lang.reflect.Method;
import java.util.*;

import static uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes.*;

/**
 * Created by anjali on 06/12/17.
 */
public class CommonUtility {

    private static final Map<Class, Label[]> labelMap = new HashMap<>();

    public static String extractAc(Object object) {
        String ac = null;
        try {
            if (object != null) {
                Class clazz = object.getClass();
                Class classItr = clazz;
                boolean acClassFound = true;
                while (acClassFound && classItr.getName().contains("uk.ac.ebi.intact")) {
                    try {
                        Method method = classItr.getMethod("getAc");
                        ac = (String) method.invoke(classItr.cast(object));
                        acClassFound = false;

                    } catch (NoSuchMethodException e) {
                        classItr = classItr.getSuperclass();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static void createXrefRelationShips(Collection<GraphXref> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphXref obj : relCollection) {
                createRelationShip(obj, fromId, XREFS);
            }
        }
    }

    public static void createIdentifierRelationShips(Collection<GraphXref> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphXref obj : relCollection) {
                createRelationShip(obj, fromId, IDENTIFIERS);
            }
        }
    }

    public static void createAliasRelationShips(Collection<GraphAlias> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphAlias obj : relCollection) {
                createRelationShip(obj, fromId, ALIASES);
            }
        }
    }

    public static void createSynonymRelationShips(Collection<GraphAlias> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphAlias obj : relCollection) {
                createRelationShip(obj, fromId, SYNONYMS);
            }
        }
    }

    public static void createAnnotationRelationShips(Collection<GraphAnnotation> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphAnnotation obj : relCollection) {
                createRelationShip(obj, fromId, ANNOTATIONS);
            }
        }
    }

    public static void createBinaryInteractionEvidenceRelationShips(Collection<GraphBinaryInteractionEvidence> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphBinaryInteractionEvidence obj : relCollection) {
                createRelationShip(obj, fromId, INTERACTIONS);

            }
        }
    }

    public static void createInteractionEvidenceRelationShips(Collection<GraphInteractionEvidence> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphInteractionEvidence obj : relCollection) {
                createRelationShip(obj, fromId, INTERACTIONS);

            }
        }
    }

    public static void createParticipantsRelationShips(Collection<GraphParticipantEvidence> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphParticipantEvidence obj : relCollection) {
                createRelationShip(obj, fromId, IE_PARTICIPANT);

            }
        }
    }

    public static void createModelledParticipantsRelationShips(Collection<GraphModelledParticipant> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphModelledParticipant obj : relCollection) {
                createRelationShip(obj, fromId, IE_PARTICIPANT);

            }
        }
    }

    public static void createInteractorRelationShips(Collection<GraphInteractor> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphInteractor obj : relCollection) {
                createRelationShip(obj, fromId, INTERACTORS);

            }
        }
    }

    public static void createFeatureEvidenceRelationShips(Collection<GraphFeatureEvidence> relCollection, long fromId, String relationName) {
        if (relCollection != null) {
            for (GraphFeatureEvidence obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

    public static void createModelledFeatureRelationShips(Collection<GraphModelledFeature> relCollection, long fromId, String relationName) {
        if (relCollection != null) {
            for (GraphModelledFeature obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

/*    public static void createFeatureRelationShips(Collection<GraphFeature> relCollection, long fromId, String relationName) {
        if (relCollection != null) {
            for (GraphFeature obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }*/

    public static void createCausalRelationshipRelationShips(Collection<GraphCausalRelationship> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphCausalRelationship obj : relCollection) {
                createRelationShip(obj, fromId, CAUSAL_RELATIONSHIP);
            }
        }
    }

    public static void createConfidenceRelationShips(Collection<GraphConfidence> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphConfidence obj : relCollection) {
                createRelationShip(obj, fromId, CONFIDENCE);
            }
        }
    }

    public static void createModelledConfidenceRelationShips(Collection<GraphModelledConfidence> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphModelledConfidence obj : relCollection) {
                createRelationShip(obj, fromId, CONFIDENCE);
            }
        }
    }

    public static void createCooperativeEffectRelationShips(Collection<GraphCooperativeEffect> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphCooperativeEffect obj : relCollection) {
                createRelationShip(obj, fromId, COOPERATIVE_EFFECT);
            }
        }
    }

    public static void createVariableParameterRelationShips(Collection<GraphVariableParameter> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphVariableParameter obj : relCollection) {
                createRelationShip(obj, fromId, VARIABLE_PARAMETER);
            }
        }
    }

    public static void createCvTermRelationShips(Collection<GraphCvTerm> relCollection, long fromId, String relationName) {
        if (relCollection != null) {
            for (GraphCvTerm obj : relCollection) {
                createRelationShip(obj, fromId, relationName);
            }
        }
    }

    public static void createExperimentalPreparationRelationShips(Collection<GraphCvTerm> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphCvTerm obj : relCollection) {
                createRelationShip(obj, fromId, EXPERIMENTAL_PREPARATION);
            }
        }
    }

    public static void createIdentificationMethodRelationShips(Collection<GraphCvTerm> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphCvTerm obj : relCollection) {
                createRelationShip(obj, fromId, IDENTIFICATION_METHOD);
            }
        }
    }

    public static void createDetectionMethodRelationShips(Collection<GraphCvTerm> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphCvTerm obj : relCollection) {
                createRelationShip(obj, fromId, DETECTION_METHOD);
            }
        }
    }

    public static void createParameterRelationShips(Collection<GraphParameter> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphParameter obj : relCollection) {
                createRelationShip(obj, fromId, PARAMETERS);
            }
        }
    }

    public static void createModelledParameterRelationShips(Collection<GraphModelledParameter> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphModelledParameter obj : relCollection) {
                createRelationShip(obj, fromId, PARAMETERS);
            }
        }
    }

    public static void createVariableParameterValueSetRelationShips(Collection<GraphVariableParameterValueSet> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphVariableParameterValueSet obj : relCollection) {
                createRelationShip(obj, fromId, VARIABLE_PARAMETERS_VALUE_SETS);
            }
        }
    }

    public static void createVariableParameterValueRelationShips(Collection<GraphVariableParameterValue> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphVariableParameterValue obj : relCollection) {
                createRelationShip(obj, fromId, VARIABLE_VALUES);
            }
        }
    }

    public static void createChecksumRelationShips(Collection<GraphChecksum> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphChecksum obj : relCollection) {
                createRelationShip(obj, fromId, CHECKSUMS);
            }
        }
    }

    public static void createRangeRelationShips(Collection<GraphRange> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphRange obj : relCollection) {
                createRelationShip(obj, fromId, RANGES);
            }
        }
    }

    public static void createExperimentRelationShips(Collection<GraphExperiment> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphExperiment obj : relCollection) {
                createRelationShip(obj, fromId, PUB_EXP);
            }
        }
    }

    public static void createAuthorRelationShips(Collection<GraphAuthor> relCollection, long fromId) {
        if (relCollection != null) {
            for (GraphAuthor obj : relCollection) {
                createRelationShip(obj, fromId, GRAPH_AUTHORS);
            }
        }
    }

    public static void createRelationShip(Object relObj, long fromId, String relationName) {
        try {
            if (relObj != null) {
                Class clazz = relObj.getClass();
                Method method = clazz.getMethod("getGraphId");
                long endId = (Long) method.invoke(clazz.cast(relObj));
                String relationIdentity = "" + fromId + "-" + endId + "-" + relationName;
                boolean relationshipAlreadyCreated = Constants.createdRelationShipList.contains(relationIdentity);
                if (!relationshipAlreadyCreated && fromId != endId) {
                    RelationshipType relationshipType = RelationshipType.withName(relationName);
                    CreationConfig.batchInserter.createRelationship(fromId, endId, relationshipType, null);
                    Constants.createdRelationShipList.add(relationIdentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NodeDataFeed createNode(Map<String, Object> nodeProperties, Label[] labels) {
        long id = -1;
        boolean isAlreadyCreated = false;
        String uniqueKey = (String) nodeProperties.get("uniqueKey");
        isAlreadyCreated = Constants.createdNodeIdMap.get(uniqueKey) != null ? true : false;

        if (isAlreadyCreated) {
            id = Constants.createdNodeIdMap.get(uniqueKey);
        } else {
            id = CreationConfig.batchInserter.createNode(nodeProperties, labels);
            if (uniqueKey != null && !uniqueKey.trim().equals("")) {
                if (!Constants.UNIQUE_KEY_NA.equals(uniqueKey)) {
                    Constants.createdNodeIdMap.put(uniqueKey, id);
                }
            }
        }
        return new NodeDataFeed(id, isAlreadyCreated);
    }

    public static GraphInteractor initializeInteractor(Interactor interactor) {

        GraphInteractor graphInteractor = null;

        if (interactor instanceof Protein) {
            graphInteractor = new GraphProtein((Protein) interactor);
        } else if (interactor instanceof NucleicAcid) {
            graphInteractor = new GraphNucleicAcid((NucleicAcid) interactor);
        } else if (interactor instanceof Polymer) {
            graphInteractor = new GraphPolymer((Polymer) interactor, false);
        } else if (interactor instanceof Gene) {
            graphInteractor = new GraphGene((Gene) interactor);
        } else if (interactor instanceof Molecule) {
            graphInteractor = new GraphMolecule((Molecule) interactor, false);
        } else {
            graphInteractor = new GraphInteractor(interactor, false);
        }

        return graphInteractor;
    }


}

