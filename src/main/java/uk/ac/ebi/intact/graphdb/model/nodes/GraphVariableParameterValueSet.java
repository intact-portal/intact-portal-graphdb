package uk.ac.ebi.intact.graphdb.model.nodes;

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
import uk.ac.ebi.intact.graphdb.utils.HashCode;

import java.util.*;

@NodeEntity
public class GraphVariableParameterValueSet extends DefaultVariableParameterValueSet {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

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

        if(this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        if (this.getVariableParameterValues() != null) {
            hashcode = 31 * hashcode + HashCode.variableParametersValuesGraphHashCode(this.getVariableParameterValues());
        }
        return hashcode;
    }

    public String createUniqueKey(VariableParameterValueSet variableParameterValueSet) {
        // since there was not hashcode implemented in jami, we had to come up with this
        int hashcode = 31;
        if (this.getVariableParameterValues() != null) {
            hashcode = 31 * hashcode + HashCode.variableParametersValuesHashCode(variableParameterValueSet);
        }

        return hashcode + "";
    }

}
