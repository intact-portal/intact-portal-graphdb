package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 30/04/19.
 */
public class GraphModelledEntity implements ModelledEntity {

    @GraphId
    private Long graphId;


    private String uniqueKey;

    @Relationship(type = RelationshipTypes.INTERACTOR)
    @JsonManagedReference
    private GraphInteractor interactor;

    @Relationship(type = RelationshipTypes.PARTICIPANT_FEATURE, direction = Relationship.OUTGOING)
    @JsonBackReference
    private Collection<GraphModelledFeature> features;

    @Relationship(type = RelationshipTypes.STOICHIOMETRY)
    private GraphStoichiometry stoichiometry;

    @Relationship(type = RelationshipTypes.CAUSAL_RELATIONSHIP)
    private Collection<GraphCausalRelationship> causalRelationships;

    @Relationship(type = RelationshipTypes.CHANGE_LISTENER)
    private EntityInteractorChangeListener changeListener;

    @Transient
    private boolean isAlreadyCreated;
    @Transient
    private Map<String, Object> nodeProperties = new HashMap<String, Object>();

    public GraphModelledEntity() {
    }

    public GraphModelledEntity(Entity entity, boolean childAlreadyCreated) {
        setInteractor(entity.getInteractor());
        setStoichiometry(entity.getStoichiometry());
        setChangeListener(entity.getChangeListener());
        setUniqueKey(createUniqueKey(entity));

        if (CreationConfig.createNatively) {
            if (!childAlreadyCreated) {
                createNodeNatively();
            }
        }

        setFeatures(entity.getFeatures());
        setCausalRelationships(entity.getCausalRelationships());

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated() && !childAlreadyCreated) {
                createRelationShipNatively(this.getGraphId());
            }
        }
    }

    public void initialzeNodeProperties() {
        // if needed
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Label[] labels = CommonUtility.getLabels(GraphEntity.class);
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            NodeDataFeed nodeDataFeed = CommonUtility.createNode(getNodeProperties(), labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively(Long graphId) {
        CommonUtility.createRelationShip(interactor, graphId, RelationshipTypes.INTERACTOR);
        CommonUtility.createRelationShip(stoichiometry, graphId, RelationshipTypes.STOICHIOMETRY);
        CommonUtility.createRelationShip(changeListener, graphId, RelationshipTypes.CHANGE_LISTENER);
        CommonUtility.createFeatureEvidenceRelationShips(features, graphId, RelationshipTypes.PARTICIPANT_FEATURE);
        CommonUtility.createCausalRelationshipRelationShips(causalRelationships, graphId);
    }

    @Override
    public Interactor getInteractor() {
        return this.interactor;
    }

    @Override
    public void setInteractor(Interactor interactor) {
        if (interactor == null) {
            throw new IllegalArgumentException("The interactor cannot be null.");
        }
        Interactor oldInteractor = this.interactor;
        if (interactor instanceof GraphInteractor) {
            this.interactor = (GraphInteractor) interactor;
        } else {
            this.interactor = CommonUtility.initializeInteractor(interactor);
        }
        if (this.changeListener != null) {
            this.changeListener.onInteractorUpdate(this, oldInteractor);
        }
    }

    @Override
    public Collection<GraphModelledFeature> getFeatures() {
        if (features == null) {
            features = new ArrayList<GraphModelledFeature>();
        }
        return this.features;
    }

    public void setFeatures(Collection<ModelledFeature> features) {
        if (features != null) {
            addAllFeatures(features);
        } else {
            this.features = new ArrayList<GraphModelledFeature>();
        }
    }


    @Override
    public boolean addFeature(ModelledFeature feature) {
        if (feature == null) {
            return false;
        }

        GraphModelledFeature graphModelledFeature = null;
        if (GraphEntityCache.featureCacheMap.get(feature.getShortName()) != null) {
            graphModelledFeature = GraphEntityCache.modelledFeatureCacheMap.get(feature.getShortName());

        } else {
            graphModelledFeature = new GraphModelledFeature(feature);
        }
        if (getFeatures().add(graphModelledFeature)) {
            feature.setParticipant(this);
            return true;
        }

        return false;
    }

    //Todo review
    @Override
    public boolean removeFeature(ModelledFeature feature) {
        if (feature == null) {
            return false;
        }

        if (getFeatures().remove(feature)) {
            feature.setParticipant(null);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAllFeatures(Collection<? extends ModelledFeature> features) {
        if (features == null) {
            return false;
        }

        boolean added = false;
        for (ModelledFeature feature : features) {
            if (addFeature(feature)) {
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean removeAllFeatures(Collection<? extends ModelledFeature> features) {
        if (features == null) {
            return false;
        }

        boolean added = false;
        for (ModelledFeature feature : features) {
            if (removeFeature(feature)) {
                added = true;
            }
        }
        return added;
    }

    @Override
    public Stoichiometry getStoichiometry() {
        return this.stoichiometry;
    }

    @Override
    public void setStoichiometry(Integer stoichiometry) {
        if (stoichiometry != null) {
            this.stoichiometry = new GraphStoichiometry(stoichiometry);

        } else {
            this.stoichiometry = null;
        }
    }

    @Override
    public void setStoichiometry(Stoichiometry stoichiometry) {
        if (stoichiometry != null) {
            if (stoichiometry instanceof GraphStoichiometry) {
                this.stoichiometry = (GraphStoichiometry) stoichiometry;
            } else {
                this.stoichiometry = new GraphStoichiometry(stoichiometry);
            }
        } else {
            this.stoichiometry = null;
        }
        //TODO login it
    }

    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    @Override
    public Collection<GraphCausalRelationship> getCausalRelationships() {
        if (this.causalRelationships == null) {
            this.causalRelationships = new ArrayList<GraphCausalRelationship>();
        }
        return this.causalRelationships;
    }

    public void setCausalRelationships(Collection<CausalRelationship> causalRelationships) {
        if (causalRelationships != null) {
            this.causalRelationships = CollectionAdaptor.convertCausalRelationshipIntoGraphModel(causalRelationships);
        } else {
            this.causalRelationships = new ArrayList<GraphCausalRelationship>();
        }
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    @Override
    public EntityInteractorChangeListener getChangeListener() {
        return changeListener;
    }

    @Override
    public void setChangeListener(EntityInteractorChangeListener changeListener) {
        this.changeListener = changeListener;
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


    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }


    public String createUniqueKey(Entity entity) {
        return UniqueKeyGenerator.createEntityKey(entity);
    }

}