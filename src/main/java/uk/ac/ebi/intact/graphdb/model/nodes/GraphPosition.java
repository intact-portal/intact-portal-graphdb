package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Position;
import psidev.psi.mi.jami.utils.comparator.range.UnambiguousPositionComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphPosition implements Position {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.STATUS)
    private GraphCvTerm status;

    private long start;
    private long end;
    private boolean isPositionUndetermined;

    @Transient
    private boolean isAlreadyCreated;

    public GraphPosition() {
    }

    public GraphPosition(Position position) {
        setStatus(position.getStatus());
        setStart(position.getStart());
        setEnd(position.getEnd());
        setPositionUndetermined(position.isPositionUndetermined());
        setUniqueKey(createUniqueKey(position));

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
            nodeProperties.put("start", this.getStart());
            nodeProperties.put("end", this.getEnd());
            nodeProperties.put("isPositionUndetermined", this.isPositionUndetermined());

            Label[] labels = CommonUtility.getLabels(GraphPosition.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(status, this.graphId, RelationshipTypes.STATUS);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public CvTerm getStatus() {
        return this.status;
    }

    public void setStatus(CvTerm status) {
        if (status != null) {
            if (status instanceof GraphCvTerm) {
                this.status = (GraphCvTerm) status;
            } else {
                this.status = new GraphCvTerm(status, false);
            }
        } else {
            this.status = null;
        }
        //TODO login it
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean isPositionUndetermined() {
        return this.isPositionUndetermined;
    }

    public void setPositionUndetermined(boolean positionUndetermined) {
        isPositionUndetermined = positionUndetermined;
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

        if (!(o instanceof Position)) {
            return false;
        }

        return UnambiguousPositionComparator.areEquals(this, (Position) o);
    }

    @Override
    public String toString() {
        return this.status.toString() + ": " + getStart() + ".." + getEnd();
    }

    @Override
    public int hashCode() {
        int hashcode;
        try {
            hashcode = UnambiguousPositionComparator.hashCode(this);
        } catch (Exception e) {
            //Hash Code Could not be created, creating default ; this was needed for the cases where all values are not initialized by neo4j
            hashcode = super.hashCode();
        }
        return hashcode;
    }

    public String createUniqueKey(Position position) {
        return position != null ? UnambiguousPositionComparator.hashCode(position) + "" : "";
    }

}
