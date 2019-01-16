package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Stoichiometry;
import psidev.psi.mi.jami.utils.comparator.participant.StoichiometryComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphStoichiometry implements Stoichiometry {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private int minValue;
    private int maxValue;

    @Transient
    private boolean isAlreadyCreated;

    public GraphStoichiometry() {
    }

    public GraphStoichiometry(Stoichiometry stoichiometry) {
        this(stoichiometry.getMinValue(), stoichiometry.getMaxValue());
        setUniqueKey(createUniqueKey(stoichiometry));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            nodeProperties.put("minValue", this.getMinValue());
            nodeProperties.put("maxValue", this.getMaxValue());

            Label[] labels = CommonUtility.getLabels(GraphStoichiometry.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GraphStoichiometry(int value) {
        this(value, value);
    }

    public GraphStoichiometry(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("The minValue " + minValue + " cannot be bigger than the maxValue " + maxValue);
        }
        setMinValue(minValue);
        setMaxValue(maxValue);
        setUniqueKey(this.toString());
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Stoichiometry)) {
            return false;
        }

        return StoichiometryComparator.areEquals(this, (Stoichiometry) o);
    }

    @Override
    public int hashCode() {

        if(this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        return StoichiometryComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return "minValue: " + getMinValue() + ", maxValue: " + getMaxValue();
    }

    public String createUniqueKey(Stoichiometry stoichiometry) {
        return stoichiometry != null ? StoichiometryComparator.hashCode(stoichiometry) + "" : "";
    }
}
