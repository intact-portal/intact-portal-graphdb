package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Interactor;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.domain.ClusterDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by anjali on 06/03/18.
 */
@NodeEntity
public class GraphClusteredInteraction {

    @GraphId
    private Long graphId;


    private Set<GraphBinaryInteractionEvidence> interactions;
    private Interactor interactorPA;
    private Interactor interactorPB;
    private double miscore;

    @Transient
    private boolean isAlreadyCreated;

    public GraphClusteredInteraction(){

    }

    public GraphClusteredInteraction(ClusterDataFeed clusterDataFeed) {
        setInteractions(clusterDataFeed.getInteractions());
        setInteractorPA(clusterDataFeed.getInteractorA());
        setInteractorPB(clusterDataFeed.getInteractorB());
        setMiscore(clusterDataFeed.getMiscore());

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if(!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("miscore", this.getMiscore());

            Label[] labels = CommonUtility.getLabels(GraphClusteredInteraction.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(interactorPA, this.getGraphId(), "interactorPA");
        CommonUtility.createRelationShip(interactorPB, this.getGraphId(), "interactorPB");
        CommonUtility.createBinaryInteractionEvidenceRelationShips(interactions, this.getGraphId(), "interactions");
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

    public Interactor getInteractorPA() {
        return interactorPA;
    }

    public void setInteractorPA(Interactor interactorPA) {
        this.interactorPA = interactorPA;
    }

    public Interactor getInteractorPB() {
        return interactorPB;
    }

    public void setInteractorPB(Interactor interactorPB) {
        this.interactorPB = interactorPB;
    }


    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }
}
