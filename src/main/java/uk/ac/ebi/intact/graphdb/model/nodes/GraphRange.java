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
    private Long id;

    private Position start;
    private Position end;
    private boolean isLink;
    private ResultingSequence resultingSequence;
    private Entity participant;

    public Position getStart() {
        return this.start;
    }

    public Position getEnd() {
        return this.end;
    }

    public void setPositions(Position start, Position end) {
        if (start == null){
            throw new IllegalArgumentException("The start position is required and cannot be null");
        }
        if (end == null){
            throw new IllegalArgumentException("The end position is required and cannot be null");
        }

        if (start.getEnd() != 0 && end.getStart() != 0 && start.getEnd() > end.getStart()){
            throw new IllegalArgumentException("The start position cannot be ending before the end position");
        }
        this.start = start;
        this.end = end;
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
        this.resultingSequence = resultingSequence;
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
