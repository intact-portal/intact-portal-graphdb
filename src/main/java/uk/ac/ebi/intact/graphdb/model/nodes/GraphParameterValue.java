package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.ParameterValue;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphParameterValue extends ParameterValue {

    @Transient
    public transient boolean preventLazyLoading = false;
    @Transient
    public transient boolean isLoaded = false;
    @GraphId
    private Long graphId;
    @Index(unique = true, primary = true)
    private String uniqueKey;
    private short base;
    private BigDecimal factor;
    private short exponent = 0;
    @Transient
    private boolean isAlreadyCreated;

    public GraphParameterValue() {
        super(new BigDecimal(0));
    }

    public GraphParameterValue(BigDecimal factorC, short baseC, short exponentC) {
        super(factorC, baseC, exponentC);
        // below is needed because ParameterValue is not a GraphClass
        setFactor(factorC);
        setBase(baseC);
        setExponent(exponentC);

        setUniqueKey(createUniqueKey());
        if (CreationConfig.createNatively) {
            createNodeNatively();
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getFactor() != null) nodeProperties.put("factor", this.getFactor().toString());
            nodeProperties.put("base", this.getBase());
            nodeProperties.put("exponent", this.getExponent());
            Label[] labels = CommonUtility.getLabels(GraphParameterValue.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey() {
        return UniqueKeyGenerator.createParameterValueKey(this);
    }

    public String toString() {
        return (getBase() != 0 && getFactor().doubleValue() != 0 ? getFactor().toString() + (getExponent() != 0 ? "x" + getBase() + "^(" + getExponent() + ")" : "") : "0");
    }


    @Override
    public short getBase() {
        return base;
    }

    public void setBase(short base) {
        this.base = base;
    }

    @Override
    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    @Override
    public short getExponent() {
        return exponent;
    }

    public void setExponent(short exponent) {
        this.exponent = exponent;
    }

    @JsonIgnore
    public <T extends GraphParameterValue> T preventLazyLoading() {
        return preventLazyLoading(true);
    }

    @SuppressWarnings({"unchecked", "WeakerAccess", "UnusedReturnValue"})
    @JsonIgnore
    public <T extends GraphParameterValue> T preventLazyLoading(boolean preventLazyLoading) {
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
