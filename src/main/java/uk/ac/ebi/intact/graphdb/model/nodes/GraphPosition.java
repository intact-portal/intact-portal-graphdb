package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Position;
import psidev.psi.mi.jami.utils.comparator.range.UnambiguousPositionComparator;

/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphPosition implements Position {

    @GraphId
    protected Long graphId;

    private GraphCvTerm status;
    private long start;
    private long end;
    private boolean isPositionUndetermined;

    public GraphPosition() {
    }

    public GraphPosition(Position position) {
        setStatus(position.getStatus());
        setStart(position.getStart());
        setEnd(position.getEnd());
        setPositionUndetermined(position.isPositionUndetermined());
    }

    public CvTerm getStatus() {
        return this.status;
    }

    public void setStatus(CvTerm status) {
        if (status != null) {
            if (status instanceof GraphCvTerm) {
                this.status = (GraphCvTerm) status;
            } else {
                this.status = new GraphCvTerm(status);
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
        return getStatus().toString() + ": " + getStart() + ".." + getEnd();
    }

    @Override
    public int hashCode() {
        return UnambiguousPositionComparator.hashCode(this);
    }


}
