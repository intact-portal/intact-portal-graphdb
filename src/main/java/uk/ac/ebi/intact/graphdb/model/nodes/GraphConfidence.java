package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Confidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.confidence.UnambiguousConfidenceComparator;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphConfidence implements Confidence {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private GraphCvTerm type;
    private String value;

    public GraphConfidence() {
    }

    public GraphConfidence(Confidence confidence) {
        setType(confidence.getType());
        setValue(confidence.getValue());
        setUniqueKey(this.toString());

        if (CreationConfig.createNatively) {
            createNodeNatively();
            createRelationShipNatively();
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getValue() != null) nodeProperties.put("value", this.getValue());

            Label[] labels = CommonUtility.getLabels(GraphConfidence.class);

            setGraphId(CommonUtility.createNode(nodeProperties, labels));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(type, this.getGraphId(), "type");
    }

/*    public GraphConfidence(CvTerm type, String value) {
        if (type == null) {
            throw new IllegalArgumentException("The confidence type is required and cannot be null");
        }
        this.type = type;
        if (value == null) {
            throw new IllegalArgumentException("The confidence value is required and cannot be null");
        }
        this.value = value;
    }*/

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public CvTerm getType() {
        return this.type;
    }

    public void setType(CvTerm type) {
        if (type != null) {
            if (type instanceof GraphCvTerm) {
                this.type = (GraphCvTerm) type;
            } else {
                this.type = new GraphCvTerm(type);
            }
        } else {
            this.type = null;
        }
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Confidence)) {
            return false;
        }

        return UnambiguousConfidenceComparator.areEquals(this, (Confidence) o);
    }

    @Override
    public String toString() {
        return this.type.toString() + ": " + getValue();
    }

    @Override
    public int hashCode() {
        return UnambiguousConfidenceComparator.hashCode(this);
    }


}
