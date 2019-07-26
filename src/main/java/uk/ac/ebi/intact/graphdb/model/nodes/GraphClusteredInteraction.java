package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Interactor;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.domain.ClusterDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by anjali on 06/03/18.
 */
@NodeEntity
public class GraphClusteredInteraction extends GraphDatabaseObject {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.INTERACTIONS)
    private Set<GraphBinaryInteractionEvidence> interactions;

    @Relationship(type = RelationshipTypes.INTERACTOR_PA)
    private GraphInteractor interactorPA;

    @Relationship(type = RelationshipTypes.INTERACTOR_PB)
    private GraphInteractor interactorPB;

    private double miscore;

    @Transient
    private boolean isAlreadyCreated;

    public GraphClusteredInteraction() {

    }

    public GraphClusteredInteraction(ClusterDataFeed clusterDataFeed) {
        setInteractions(clusterDataFeed.getInteractions());
        setInteractorPA(clusterDataFeed.getInteractorA());
        setInteractorPB(clusterDataFeed.getInteractorB());
        setMiscore(clusterDataFeed.getMiscore());
        setUniqueKey(createUniqueKey());
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
            nodeProperties.put("miscore", this.getMiscore());

            Label[] labels = CommonUtility.getLabels(GraphClusteredInteraction.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(interactorPA, this.getGraphId(), RelationshipTypes.INTERACTOR_PA);
        CommonUtility.createRelationShip(interactorPB, this.getGraphId(), RelationshipTypes.INTERACTOR_PB);
        CommonUtility.createBinaryInteractionEvidenceRelationShips(interactions, this.getGraphId());
    }


    public Set<GraphBinaryInteractionEvidence> getInteractions() {
        return interactions;
    }

    public void setInteractions(Set<GraphBinaryInteractionEvidence> interactions) {
        this.interactions = interactions;
    }


    public double getMiscore() {
        return miscore;
    }

    public void setMiscore(double miscore) {
        this.miscore = miscore;
    }

    public GraphInteractor getInteractorPA() {
        return interactorPA;
    }

    public void setInteractorPA(Interactor interactorA) {
        if (interactorA != null) {
            if (interactorA instanceof GraphInteractor) {
                this.interactorPA = (GraphInteractor) interactorA;
            } else {
                this.interactorPA = CommonUtility.initializeInteractor(interactorA);
            }
        } else {
            this.interactorPA = null;
        }
    }

    public GraphInteractor getinteractorPB() {
        return interactorPB;
    }

    public void setInteractorPB(Interactor interactorB) {
        if (interactorB != null) {
            if (interactorB instanceof GraphInteractor) {
                this.interactorPB = (GraphInteractor) interactorB;
            } else {
                this.interactorPB = CommonUtility.initializeInteractor(interactorB);
            }
        } else {
            this.interactorPB = null;
        }

    }


    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public int hashCode() {
        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey() {
        return UniqueKeyGenerator.createClusteredInteractionKey(this);
    }
}
