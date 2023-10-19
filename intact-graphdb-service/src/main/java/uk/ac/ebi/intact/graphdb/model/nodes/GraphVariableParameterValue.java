package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.VariableParameter;
import psidev.psi.mi.jami.model.VariableParameterValue;
import psidev.psi.mi.jami.utils.comparator.experiment.VariableParameterValueComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 24/11/17.
 */
public class GraphVariableParameterValue extends GraphDatabaseObject implements VariableParameterValue {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String value;
    private Integer order;

    @Relationship(type = RelationshipTypes.VARIABLE_PARAMETER)
    private GraphVariableParameter variableParameter;

    @Transient
    private boolean isAlreadyCreated;

    public GraphVariableParameterValue() {

    }


    public GraphVariableParameterValue(VariableParameterValue variableParameterValue) {
        setValue(variableParameterValue.getValue());
        setOrder(variableParameterValue.getOrder());
        setVariableParameter(variableParameterValue.getVariableParameter());
        setUniqueKey(createUniqueKey(variableParameterValue));

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getValue() != null) nodeProperties.put("value", this.getValue());
            if (this.getOrder() != null) nodeProperties.put("order", this.getOrder());

            Label[] labels = CommonUtility.getLabels(GraphVariableParameterValue.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(variableParameter, this.getGraphId(), RelationshipTypes.VARIABLE_PARAMETER);
    }

/*    public GraphVariableParameterValue(String value, VariableParameter variableParameter){
        if (value == null){
            throw new IllegalArgumentException("The value of a variableParameterValue cannot be null");
        }
        this.value = value;
        this.variableParameter = variableParameter;
    }

    public GraphVariableParameterValue(String value, VariableParameter variableParameter, Integer order){
        if (value == null){
            throw new IllegalArgumentException("The value of a variableParameterValue cannot be null");
        }
        this.value = value;
        this.variableParameter = variableParameter;
        this.order = order;
    }*/

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public VariableParameter getVariableParameter() {
        return variableParameter;
    }

    public void setVariableParameter(VariableParameter variableParameter) {
        if (variableParameter != null) {
            if (variableParameter instanceof GraphVariableParameter) {
                this.variableParameter = (GraphVariableParameter) variableParameter;
            } else {
                this.variableParameter = new GraphVariableParameter(variableParameter);
            }
        } else {
            this.variableParameter = null;
        }
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof VariableParameterValue)) {
            return false;
        }

        return VariableParameterValueComparator.areEquals(this, (VariableParameterValue) o);
    }

    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        return (getValue() != null ? getValue().toString() : "-");
    }

    public String createUniqueKey(VariableParameterValue variableParameterValue) {
        return UniqueKeyGenerator.createVariableParameterValueKey(variableParameterValue);
    }
}
