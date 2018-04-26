package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.VariableParameterValueSet;
import psidev.psi.mi.jami.model.impl.DefaultVariableParameterValueSet;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphVariableParameterValueSet extends DefaultVariableParameterValueSet {

    @GraphId
    private Long graphId;

    //TODO Review it
    public GraphVariableParameterValueSet(VariableParameterValueSet variableParameterValueSet){
        if (CreationConfig.createNatively) {
            createNodeNatively();

        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();

            Label[] labels = CommonUtility.getLabels(GraphVariableParameterValueSet.class);

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
