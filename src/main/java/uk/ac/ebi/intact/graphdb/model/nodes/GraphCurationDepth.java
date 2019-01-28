package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.CurationDepth;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 01/05/18.
 */
@NodeEntity
public class GraphCurationDepth {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private String curationDepth;
    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private boolean forceHashCodeGeneration;

    public GraphCurationDepth() {

    }

    public GraphCurationDepth(CurationDepth curationDepth) {
        setForceHashCodeGeneration(true);
        setCurationDepth(curationDepth.name());
        setAc(CommonUtility.extractAc(curationDepth));
        setUniqueKey(createUniqueKey(curationDepth));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            nodeProperties.put("curationDepth", this.getCurationDepth());
            Label[] labels = CommonUtility.getLabels(GraphCurationDepth.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getCurationDepth() {
        return curationDepth;
    }

    public void setCurationDepth(String curationDepth) {
        this.curationDepth = curationDepth;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public int hashCode() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        if (this.getCurationDepth() != null) {
            hashcode = 31 * hashcode + this.getCurationDepth().toLowerCase().hashCode();
        }

        return hashcode;
    }

    public String createUniqueKey(CurationDepth curationDepth) {
        return hashCode()+"";
    }

    public boolean isForceHashCodeGeneration() {
        return forceHashCodeGeneration;
    }

    public void setForceHashCodeGeneration(boolean forceHashCodeGeneration) {
        this.forceHashCodeGeneration = forceHashCodeGeneration;
    }
}
