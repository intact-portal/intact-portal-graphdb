package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.ParameterValue;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphParameterValue extends ParameterValue {

    @GraphId
    private Long graphId;

    public GraphParameterValue(BigDecimal factor, short base, short exponent) {
        super(factor, base, exponent);
        if (CreationConfig.createNatively) {
            createNodeNatively();
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();

            Label[] labels = CommonUtility.getLabels(GraphParameterValue.class);

            setGraphId(CommonUtility.createNode(nodeProperties, labels));

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
}
