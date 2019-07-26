package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Confidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.confidence.UnambiguousConfidenceComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphConfidence extends GraphDatabaseObject implements Confidence {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.TYPE)
    private GraphCvTerm type;

    private String value;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private Map<String, Object> nodeProperties = new HashMap<String, Object>();

    public GraphConfidence() {
    }

    public GraphConfidence(Confidence confidence, boolean childAlreadyCreated) {
        setType(confidence.getType());
        setValue(confidence.getValue());
        setUniqueKey(createUniqueKey(confidence));

        if (CreationConfig.createNatively) {
            initializeNodeProperties();
            if (!childAlreadyCreated) {
                createNodeNatively();
                if (!isAlreadyCreated()) {
                    createRelationShipNatively(this.getGraphId());
                }
            }
        }
    }

    public void initializeNodeProperties() {
        if (this.getValue() != null) getNodeProperties().put("value", this.getValue());
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            nodeProperties.put("uniqueKey", this.getUniqueKey());

            Label[] labels = CommonUtility.getLabels(GraphConfidence.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively(Long graphId) {
        CommonUtility.createRelationShip(type, graphId, RelationshipTypes.TYPE);
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
                this.type = new GraphCvTerm(type, false);
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
        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(Confidence confidence) {
        return UniqueKeyGenerator.createConfidenceKey(confidence);
    }

    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }
}
