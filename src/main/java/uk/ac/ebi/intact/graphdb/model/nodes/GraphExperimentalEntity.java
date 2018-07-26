package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.ExperimentalEntity;
import psidev.psi.mi.jami.utils.comparator.experiment.UnambiguousExperimentComparator;
import psidev.psi.mi.jami.utils.comparator.participant.UnambiguousExperimentalEntityComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.HashCode;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphExperimentalEntity extends GraphEntity {

    @GraphId
    private Long graphId;

    @Index(unique = true,primary = true)
    private String uniqueKey;

    @Transient
    private boolean isAlreadyCreated;

    public GraphExperimentalEntity() {
        super();
    }

    public GraphExperimentalEntity(ExperimentalEntity experimentalEntity) {
        //TODO...
        super(experimentalEntity,true);
        setUniqueKey(createUniqueKey());

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
            nodeProperties.putAll(super.getNodeProperties());
            Label[] labels = CommonUtility.getLabels(GraphExperimentalEntity.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
    }

    @Override
    public Long getGraphId() {
        return graphId;
    }

    @Override
    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
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
        int hashcode = 31;
        hashcode = 31 * hashcode + "ExperimentalEntity".hashCode();
        hashcode = 31 * hashcode + super.hashCode();
        if (this.getFeatures() != null) {
            hashcode = 31 * hashcode + HashCode.featuresGraphHashCode(super.getFeatures());
        }
        return hashcode;
    }


    public String createUniqueKey(){
        return hashCode() + "";
    }
}
