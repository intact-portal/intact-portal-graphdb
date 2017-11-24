package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.ResultingSequence;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.range.ResultingSequenceComparator;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@NodeEntity
public class GraphResultingSequence implements ResultingSequence {

    @GraphId
    protected Long graphId;

    private String originalSequence;
    private String newSequence;
    private Collection<GraphXref> xrefs;

    public GraphResultingSequence(){

    }

    public GraphResultingSequence(ResultingSequence resultingSequence) {
        setOriginalSequence(resultingSequence.getOriginalSequence());
        setNewSequence(resultingSequence.getNewSequence());
        setXrefs(resultingSequence.getXrefs());
    }


/*   public GraphResultingSequence() {
        this.originalSequence = null;
        this.newSequence = null;
    }

    public GraphResultingSequence(String oldSequence, String newSequence) {
        this.originalSequence = oldSequence;
        this.newSequence = newSequence;
    }*/

 /*   protected void initialiseXrefs() {
        this.xrefs = new ArrayList();
    }

    protected void initialiseXrefsWith(Collection<Xref> xrefs) {
        if(xrefs == null) {
            this.xrefs = Collections.EMPTY_LIST;
        } else {
            this.xrefs = xrefs;
        }

    }*/

    public String getNewSequence() {
        return this.newSequence;
    }

    public void setNewSequence(String sequence) {
        this.newSequence = sequence;
    }

    public String getOriginalSequence() {
        return this.originalSequence;
    }

    public void setOriginalSequence(String sequence) {
        this.originalSequence = sequence;
    }

    public Collection<GraphXref> getXrefs() {
        if (xrefs == null) {
            this.xrefs = new ArrayList<GraphXref>();
        }
        return this.xrefs;
    }

    public void setXrefs(Collection<Xref> xrefs) {
        if (xrefs != null) {
            this.xrefs = CollectionAdaptor.convertXrefIntoGraphModel(xrefs);
        } else {
            this.xrefs = new ArrayList<GraphXref>();
        }
    }

    public boolean equals(Object o) {
        return this == o?true:(!(o instanceof ResultingSequence)?false: ResultingSequenceComparator.areEquals(this, (ResultingSequence)o));
    }

    public int hashCode() {
        return ResultingSequenceComparator.hashCode(this);
    }

    public String toString() {
        return (this.getOriginalSequence() != null?"original sequence: " + this.getOriginalSequence():"") + (this.getNewSequence() != null?"new sequence: " + this.getNewSequence():"");
    }
}
