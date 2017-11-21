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
    private Long id;

    private CvTerm status;
    private long start;
    private long end;
    private boolean isPositionUndetermined;

    public CvTerm getStatus() {
        return this.status;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    public boolean isPositionUndetermined() {
        return this.isPositionUndetermined;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (!(o instanceof Position)){
            return false;
        }

        return UnambiguousPositionComparator.areEquals(this, (Position) o);
    }

    @Override
    public String toString() {
        return getStatus().toString() + ": " + getStart()  +".."+ getEnd();
    }

    @Override
    public int hashCode() {
        return UnambiguousPositionComparator.hashCode(this);
    }


}
