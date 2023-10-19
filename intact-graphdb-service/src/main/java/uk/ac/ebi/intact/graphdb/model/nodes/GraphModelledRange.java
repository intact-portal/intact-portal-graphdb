package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.RangeUtils;
import psidev.psi.mi.jami.utils.comparator.range.UnambiguousRangeComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 03/05/19.
 */
@NodeEntity
public class GraphModelledRange extends GraphDatabaseObject implements Range {

    @Index(unique = true, primary = true)
    private String uniqueKey;


    private String ac;

    @Relationship(type = RelationshipTypes.START)
    private GraphPosition start;

    @Relationship(type = RelationshipTypes.END)
    private GraphPosition end;

    private boolean isLink;

    private String rangeString;

    @Relationship(type = RelationshipTypes.RESULTING_SEQUENCE)
    private GraphResultingSequence resultingSequence;

    @Relationship(type = RelationshipTypes.PARTICIPANT)
    private GraphModelledEntity participant;

    @Transient
    private boolean isAlreadyCreated;

    public GraphModelledRange() {
    }

    public GraphModelledRange(Range range, String featureUniqueKey) {
        setRangeString(RangeUtils.convertRangeToString(range));
        setLink(range.isLink());
        setParticipant(range.getParticipant());
        setAc(CommonUtility.extractAc(range));
        setUniqueKey(createUniqueKey(range, featureUniqueKey));
        setPositions(range.getStart(), range.getEnd());
        setResultingSequence(range.getResultingSequence());

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            if (this.getUniqueKey() != null) nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getRangeString() != null) nodeProperties.put("rangeString", this.getRangeString());
            nodeProperties.put("isLink", this.isLink());

            Label[] labels = CommonUtility.getLabels(GraphModelledRange.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(start, this.getGraphId(), RelationshipTypes.START);
        CommonUtility.createRelationShip(end, this.getGraphId(), RelationshipTypes.END);
        CommonUtility.createRelationShip(resultingSequence, this.getGraphId(), RelationshipTypes.RESULTING_SEQUENCE);
        CommonUtility.createRelationShip(participant, this.getGraphId(), RelationshipTypes.PARTICIPANT);
    }

    public void setPositions(Position start, Position end) {
        if (start == null) {
            throw new IllegalArgumentException("The start position is required and cannot be null");
        }
        if (end == null) {
            throw new IllegalArgumentException("The end position is required and cannot be null");
        }

        if (start.getEnd() != 0 && end.getStart() != 0 && start.getEnd() > end.getStart()) {
            throw new IllegalArgumentException("The start position cannot be ending before the end position");
        }
        setStart(start);
        setEnd(end);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Position getStart() {
        return this.start;
    }

    public void setStart(Position start) {
        if (start != null) {
            if (start instanceof GraphPosition) {
                this.start = (GraphPosition) start;
            } else {
                this.start = new GraphPosition(start, this.getUniqueKey());
            }
        } else {
            this.start = null;
        }
        //TODO login it
    }

    public Position getEnd() {
        return this.end;
    }

    public void setEnd(Position end) {
        if (end != null) {
            if (end instanceof GraphPosition) {
                this.end = (GraphPosition) end;
            } else {
                this.end = new GraphPosition(end, this.getUniqueKey());
            }
        } else {
            this.end = null;
        }
        //TODO login it
    }

    public boolean isLink() {
        return this.isLink;
    }

    public void setLink(boolean link) {
        this.isLink = link;
    }

    public ResultingSequence getResultingSequence() {
        return this.resultingSequence;
    }

    public void setResultingSequence(ResultingSequence resultingSequence) {
        if (resultingSequence != null) {
            if (resultingSequence instanceof GraphResultingSequence) {
                this.resultingSequence = (GraphResultingSequence) resultingSequence;
            } else {
                this.resultingSequence = new GraphResultingSequence(resultingSequence, this.getUniqueKey());
            }
        } else {
            this.resultingSequence = null;
        }
        //TODO login it
    }

    public Entity getParticipant() {
        return this.participant;
    }

    public void setParticipant(Entity participant) {
        if (participant != null) {
            if (participant instanceof GraphModelledEntity) {
                this.participant = (GraphModelledEntity) participant;
            } else if (participant instanceof ModelledParticipant) {
                this.participant = new GraphModelledParticipant((ModelledParticipant) participant);
            }
        } else {
            this.participant = null;
        }

    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Range)) {
            return false;
        }

        return UnambiguousRangeComparator.areEquals(this, (Range) o);
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
        return (this.start != null ? this.start.toString() : "") + (this.end != null ? this.end.toString() : "") + (isLink() ? "(linked)" : "");

    }

    public String createUniqueKey(Range range, String featureUniqueKey) {
        return UniqueKeyGenerator.createRangeKey(range, featureUniqueKey);
    }

    public String getRangeString() {
        return rangeString;
    }

    public void setRangeString(String rangeString) {
        this.rangeString = rangeString;
    }
}
