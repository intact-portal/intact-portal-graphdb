package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.ResultingSequence;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.range.ResultingSequenceComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphResultingSequence extends GraphDatabaseObject implements ResultingSequence {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String originalSequence;
    private String newSequence;
    private Collection<GraphXref> xrefs;

    @Transient
    private boolean isAlreadyCreated;

    public GraphResultingSequence() {

    }

    public GraphResultingSequence(ResultingSequence resultingSequence, String rangeUniqueKey) {
        setOriginalSequence(resultingSequence.getOriginalSequence());
        setNewSequence(resultingSequence.getNewSequence());
        setXrefs(resultingSequence.getXrefs());
        setUniqueKey(createUniqueKey(resultingSequence, rangeUniqueKey));

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
            if (this.getOriginalSequence() != null) nodeProperties.put("originalSequence", this.getOriginalSequence());
            if (this.getNewSequence() != null) nodeProperties.put("newSequence", this.getNewSequence());

            Label[] labels = CommonUtility.getLabels(GraphResultingSequence.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createXrefRelationShips(xrefs, this.getGraphId());
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

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public boolean equals(Object o) {
        return this == o ? true : (!(o instanceof ResultingSequence) ? false : ResultingSequenceComparator.areEquals(this, (ResultingSequence) o));
    }

    public int hashCode() {
        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String toString() {
        return (this.getOriginalSequence() != null ? "original sequence: " + this.getOriginalSequence() : "") + (this.getNewSequence() != null ? "new sequence: " + this.getNewSequence() : "");
    }

    public String createUniqueKey(ResultingSequence resultingSequence, String rangeUniqueKey) {
        return UniqueKeyGenerator.createResultingSequenceKey(resultingSequence, rangeUniqueKey);
    }
}
