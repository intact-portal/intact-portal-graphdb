package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.ResultingSequence;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.range.ResultingSequenceComparator;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphResultingSequence implements ResultingSequence {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String originalSequence;
    private String newSequence;
    private Collection<GraphXref> xrefs;

    public GraphResultingSequence() {

    }

    public GraphResultingSequence(ResultingSequence resultingSequence) {
        setOriginalSequence(resultingSequence.getOriginalSequence());
        setNewSequence(resultingSequence.getNewSequence());
        setXrefs(resultingSequence.getXrefs());
        setUniqueKey(this.toString());

        if (CreationConfig.createNatively) {
            createNodeNatively();
            createRelationShipNatively();
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getOriginalSequence() != null) nodeProperties.put("originalSequence", this.getOriginalSequence());
            if (this.getNewSequence() != null) nodeProperties.put("newSequence", this.getNewSequence());

            Label[] labels = CommonUtility.getLabels(GraphResultingSequence.class);

            setGraphId(CommonUtility.createNode(nodeProperties, labels));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createXrefRelationShips(xrefs, this.graphId, "xrefs");
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

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

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

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public boolean equals(Object o) {
        return this == o ? true : (!(o instanceof ResultingSequence) ? false : ResultingSequenceComparator.areEquals(this, (ResultingSequence) o));
    }

    public int hashCode() {
        return ResultingSequenceComparator.hashCode(this);
    }

    public String toString() {
        return (this.getOriginalSequence() != null ? "original sequence: " + this.getOriginalSequence() : "") + (this.getNewSequence() != null ? "new sequence: " + this.getNewSequence() : "");
    }
}
