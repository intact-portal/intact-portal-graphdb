package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Checksum;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.checksum.UnambiguousChecksumComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphChecksum extends GraphDatabaseObject implements Checksum {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.METHOD)
    private GraphCvTerm method;

    private String value;

    @Transient
    private boolean isAlreadyCreated;

    public GraphChecksum() {
    }

    public GraphChecksum(Checksum checksum) {
        this(checksum.getMethod(), checksum.getValue());
        setUniqueKey(createUniqueKey(checksum));

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public GraphChecksum(CvTerm method, String value) {
        if (method == null) {
            throw new IllegalArgumentException("The method is required and cannot be null");
        }
        setMethod(method);
        if (value == null) {
            throw new IllegalArgumentException("The checksum value is required and cannot be null");
        }
        setValue(value);
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getValue() != null) nodeProperties.put("value", this.getValue());

            Label[] labels = CommonUtility.getLabels(GraphChecksum.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(method, this.getGraphId(), RelationshipTypes.METHOD);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public CvTerm getMethod() {
        return this.method;
    }

    public void setMethod(CvTerm method) {
        if (method != null) {
            if (method instanceof GraphCvTerm) {
                this.method = (GraphCvTerm) method;
            } else {
                this.method = new GraphCvTerm(method, false);
            }
        } else {
            this.method = null;
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

        if (!(o instanceof Checksum)) {
            return false;
        }

        return UnambiguousChecksumComparator.areEquals(this, (Checksum) o);
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
        return this.method.toString() + ": " + getValue();
    }

    public String createUniqueKey(Checksum checksum) {
        return UniqueKeyGenerator.createChecksumKey(checksum);
    }
}
