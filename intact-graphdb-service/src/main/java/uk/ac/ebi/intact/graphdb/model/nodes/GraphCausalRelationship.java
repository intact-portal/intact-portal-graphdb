package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.comparator.participant.UnambiguousCausalRelationshipComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphCausalRelationship extends GraphDatabaseObject implements CausalRelationship {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.RELATION_TYPE)
    private GraphCvTerm relationType;

    @Relationship(type = RelationshipTypes.TARGET)
    private GraphEntity<FeatureEvidence> target;

    @Transient
    private boolean isAlreadyCreated;

    public GraphCausalRelationship() {
    }

    public GraphCausalRelationship(CausalRelationship causalRelationship) {
        setRelationType(causalRelationship.getRelationType());
        setTarget(causalRelationship.getTarget());
        setUniqueKey(createUniqueKey(causalRelationship));

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public GraphCausalRelationship(CvTerm relationType, Participant target) {
        if (relationType == null) {
            throw new IllegalArgumentException("The relationType in a CausalRelationship cannot be null");
        }
        setRelationType(relationType);

        if (target == null) {
            throw new IllegalArgumentException("The participat target in a CausalRelationship cannot be null");
        }
        setTarget(target);
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());

            Label[] labels = CommonUtility.getLabels(GraphCausalRelationship.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(relationType, this.getGraphId(), RelationshipTypes.RELATION_TYPE);
        CommonUtility.createRelationShip(target, this.getGraphId(), RelationshipTypes.TARGET);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public CvTerm getRelationType() {
        return relationType;
    }

    public void setRelationType(CvTerm relationType) {
        if (relationType != null) {
            if (relationType instanceof GraphCvTerm) {
                this.relationType = (GraphCvTerm) relationType;
            } else {
                this.relationType = new GraphCvTerm(relationType, false);
            }
        } else {
            this.relationType = null;
        }
        //TODO login it
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        if (target != null) {
            if (target instanceof GraphEntity) {
                this.target = (GraphEntity) target;
            } else {
                this.target = new GraphEntity<FeatureEvidence>(target, false);
            }
        } else {
            this.target = null;
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

        if (!(o instanceof CausalRelationship)) {
            return false;
        }

        return UnambiguousCausalRelationshipComparator.areEquals(this, (CausalRelationship) o);
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
        return this.relationType.toString() + ": " + this.target.toString();
    }

    public String createUniqueKey(CausalRelationship causalRelationship) {
        return UniqueKeyGenerator.createCausalRelationshipKey(causalRelationship);
    }
}
