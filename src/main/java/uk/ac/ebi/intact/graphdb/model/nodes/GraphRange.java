package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Entity;
import psidev.psi.mi.jami.model.Position;
import psidev.psi.mi.jami.model.Range;
import psidev.psi.mi.jami.model.ResultingSequence;

/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphRange implements Range {

    @GraphId
    protected Long graphId;

    private GraphPosition start;
    private GraphPosition end;
    private boolean isLink;
    private GraphResultingSequence resultingSequence;
    private GraphEntity participant;

    public GraphRange() {
    }

    public GraphRange(Range range) {
        setPositions(range.getStart(), range.getEnd());
        setLink(range.isLink());
        setResultingSequence(range.getResultingSequence());
        setParticipant(range.getParticipant());
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

    public Position getStart() {
        return this.start;
    }

    public void setStart(Position start) {
        if (start != null) {
            if (start instanceof GraphPosition) {
                this.start = (GraphPosition) start;
            } else {
                this.start = new GraphPosition(start);
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
                this.end = new GraphPosition(end);
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
                this.resultingSequence = new GraphResultingSequence(resultingSequence);
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
        this.participant = participant;
    }

    @Override
    public String toString() {
        return getStart().toString() + " - " + getEnd().toString() + (isLink() ? "(linked)" : "");
    }
}
