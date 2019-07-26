package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.ModelledConfidence;
import psidev.psi.mi.jami.model.Publication;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 30/04/19.
 */
@NodeEntity
public class GraphModelledConfidence extends GraphConfidence implements ModelledConfidence {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.PUBLICATION, direction = Relationship.OUTGOING)
    @JsonManagedReference
    private GraphPublication publication;

    @Transient
    private boolean isAlreadyCreated;

    public GraphModelledConfidence() {
    }

    public GraphModelledConfidence(ModelledConfidence modelledConfidence) {
        super(modelledConfidence, true);
        setPublication(modelledConfidence.getPublication());
        setUniqueKey(createUniqueKey(modelledConfidence));

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
            nodeProperties.putAll(super.getNodeProperties());

            Label[] labels = CommonUtility.getLabels(GraphModelledConfidence.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(publication, this.getGraphId(), RelationshipTypes.PUBLICATION);
    }

    public Publication getPublication() {
        return this.publication;
    }

    public void setPublication(Publication publication) {
        if (publication != null) {
            if (publication instanceof GraphPublication) {
                this.publication = (GraphPublication) publication;
            } else {
                this.publication = new GraphPublication(publication);
            }
        } else {
            this.publication = null;
        }
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(ModelledConfidence modelledConfidence) {
        return UniqueKeyGenerator.createModelledConfidenceKey(modelledConfidence);
    }

    @Override
    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    @Override
    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }
}
