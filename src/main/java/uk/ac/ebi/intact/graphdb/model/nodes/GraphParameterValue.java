package uk.ac.ebi.intact.graphdb.model.nodes;

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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphParameterValue extends ParameterValue {

    @GraphId
    private Long graphId;

    @Index(unique = true,primary = true)
    private String uniqueKey;

    private short base;
    private BigDecimal factor;
    private short exponent=0;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private boolean forceHashCodeGeneration;

    public GraphParameterValue(){
       super(new BigDecimal(0));
    }

    public GraphParameterValue(BigDecimal factorC, short baseC, short exponentC) {
        super(factorC, baseC, exponentC);
        setForceHashCodeGeneration(true);
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

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
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

    public int hashCode(){

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31*hashcode + (getBase() != 0 && getFactor().doubleValue() != 0 ? getFactor().toString()+(getExponent() != 0 ? "x"+getBase()+"^("+getExponent()+")" : "") : "0").hashCode();

        return hashcode;
    }

    public String createUniqueKey() {
        return hashCode() + "";
    }

    public boolean isForceHashCodeGeneration() {
        return forceHashCodeGeneration;
    }

    public void setForceHashCodeGeneration(boolean forceHashCodeGeneration) {
        this.forceHashCodeGeneration = forceHashCodeGeneration;
    }

    public String toString(){
        return (base != 0 && factor.doubleValue() != 0 ? factor.toString()+(exponent != 0 ? "x"+base+"^("+exponent+")" : "") : "0");
    }
}
