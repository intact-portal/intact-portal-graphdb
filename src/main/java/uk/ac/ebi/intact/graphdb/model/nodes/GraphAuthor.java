package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 30/04/18.
 */
public class GraphAuthor extends GraphDatabaseObject {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String authorName;

    @Transient
    private boolean isAlreadyCreated;

    public GraphAuthor() {

    }

    public GraphAuthor(String authorName) {
        this.setAuthorName(authorName);
        setUniqueKey(createUniqueKey());

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }
    }

    public void createNodeNatively() {

        BatchInserter batchInserter = CreationConfig.batchInserter;

        Map<String, Object> nodeProperties = new HashMap<String, Object>();
        nodeProperties.put("uniqueKey", this.getUniqueKey());
        if (this.getAuthorName() != null) nodeProperties.put("authorName", this.getAuthorName());

        Label[] labels = CommonUtility.getLabels(GraphAuthor.class);

        NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
        setGraphId(nodeDataFeed.getGraphId());
        setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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


    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey() {
        return UniqueKeyGenerator.createAuthorKey(getAuthorName());
    }

}