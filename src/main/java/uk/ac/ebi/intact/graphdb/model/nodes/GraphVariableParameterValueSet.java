package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.VariableParameterValue;
import psidev.psi.mi.jami.model.VariableParameterValueSet;
import psidev.psi.mi.jami.model.impl.DefaultVariableParameterValueSet;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@NodeEntity
public class GraphVariableParameterValueSet extends DefaultVariableParameterValueSet {

    @Transient
    public transient boolean preventLazyLoading = false;
    @Transient
    public transient boolean isLoaded = false;
    @GraphId
    private Long graphId;
    @Index(unique = true, primary = true)
    private String uniqueKey;

    /* below two are duplicated from GraphDatabaseObject
    *  as we cannot extend that class from here*/
    private Collection<GraphVariableParameterValue> variableParameterValues;
    @Transient
    private boolean isAlreadyCreated;

    public GraphVariableParameterValueSet() {

    }

    //TODO Review it
    public GraphVariableParameterValueSet(VariableParameterValueSet variableParameterValueSet) {
        setUniqueKey(createUniqueKey(variableParameterValueSet));
        if (CreationConfig.createNatively) {
            createNodeNatively();
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }

        }
        if (variableParameterValueSet != null) {
            setVariableParameterValueSets(Arrays.asList(Arrays.copyOf(variableParameterValueSet.toArray(), variableParameterValueSet.toArray().length, VariableParameterValue[].class)));
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            Label[] labels = CommonUtility.getLabels(GraphVariableParameterValueSet.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createVariableParameterValueRelationShips(variableParameterValues, this.graphId);
    }


    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Collection<GraphVariableParameterValue> getVariableParameterValues() {
        if (this.variableParameterValues == null) {
            this.variableParameterValues = new ArrayList<GraphVariableParameterValue>();
        }
        return this.variableParameterValues;
    }

    public void setVariableParameterValueSets(Collection<VariableParameterValue> variableParameterValues) {
        if (variableParameterValues != null) {
            this.variableParameterValues = CollectionAdaptor.convertVariableParameterValueIntoGraphModel(variableParameterValues);
        } else {
            this.variableParameterValues = new ArrayList<GraphVariableParameterValue>();
        }
    }

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(VariableParameterValueSet variableParameterValueSet) {
        return UniqueKeyGenerator.createVariableParametersValueSetKey(variableParameterValueSet);
    }

    @JsonIgnore
    public <T extends GraphVariableParameterValueSet> T preventLazyLoading() {
        return preventLazyLoading(true);
    }

    @SuppressWarnings({"unchecked", "WeakerAccess", "UnusedReturnValue"})
    @JsonIgnore
    public <T extends GraphVariableParameterValueSet> T preventLazyLoading(boolean preventLazyLoading) {
        if (this.preventLazyLoading == preventLazyLoading) return (T) this;

        this.preventLazyLoading = preventLazyLoading;

        //Here we go through all the getters and prevent LazyLoading for all the objects
        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("get")) continue;
            try {
                Class<?> methodReturnClazz = method.getReturnType();

                if (GraphDatabaseObject.class.isAssignableFrom(methodReturnClazz)) {
                    GraphDatabaseObject object = (GraphDatabaseObject) method.invoke(this);
                    if (object != null && object.preventLazyLoading != preventLazyLoading) {
                        object.preventLazyLoading(preventLazyLoading);
                    }
                }

                if (Collection.class.isAssignableFrom(methodReturnClazz)) {
                    ParameterizedType stringListType = (ParameterizedType) method.getGenericReturnType();
                    Class<?> type = (Class<?>) stringListType.getActualTypeArguments()[0];
                    String clazz = type.getSimpleName();
                    if (GraphDatabaseObject.class.isAssignableFrom(type)) {
                        Collection collection = (Collection) method.invoke(this);
                        if (collection != null) {
                            for (Object obj : collection) {
                                GraphDatabaseObject object = (GraphDatabaseObject) obj;
                                if (object != null && object.preventLazyLoading != preventLazyLoading) {
                                    object.preventLazyLoading(preventLazyLoading);
                                }
                            }
                        }
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return (T) this;
    }
}
