package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.CurationDepth;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 01/05/18.
 */
@NodeEntity
public class GraphCurationDepth extends GraphDatabaseObject {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private String curationDepth;
    @Transient
    private boolean isAlreadyCreated;

    public GraphCurationDepth() {

    }

    public GraphCurationDepth(CurationDepth curationDepth) {
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

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(CurationDepth curationDepth) {
        return UniqueKeyGenerator.createCurationDepthKey(curationDepth);
    }
}
