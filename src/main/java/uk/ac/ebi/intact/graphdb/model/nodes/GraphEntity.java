package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphEntity implements ExperimentalEntity {

    @GraphId
    private Long graphId;

    private GraphInteractor interactor;
    private Collection<GraphFeatureEvidence> features;
    private GraphStoichiometry stoichiometry;
    private Collection<GraphCausalRelationship> causalRelationships;
    private EntityInteractorChangeListener changeListener;

    public GraphEntity() {
    }

    public GraphEntity(Entity entity) {
        setInteractor(entity.getInteractor());
        setStoichiometry(entity.getStoichiometry());
        setChangeListener(entity.getChangeListener());

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setFeatures(entity.getFeatures());
        setCausalRelationships(entity.getCausalRelationships());

        if (CreationConfig.createNatively) {
            createRelationShipNatively();
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();

            Label[] labels = CommonUtility.getLabels(GraphEntity.class);

            setGraphId(CommonUtility.createNode(nodeProperties, labels));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(interactor, this.graphId, "interactor");
        CommonUtility.createRelationShip(stoichiometry, this.graphId, "stoichiometry");
        CommonUtility.createRelationShip(changeListener, this.graphId, "changeListener");
        CommonUtility.createFeatureEvidenceRelationShips(features, this.graphId, "features");
        CommonUtility.createCausalRelationshipRelationShips(causalRelationships, this.graphId, "causalRelationships");
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
            this.interactor = new GraphInteractor(interactor);
        }
        if (this.changeListener != null) {
            this.changeListener.onInteractorUpdate(this, oldInteractor);
        }
    }

    @Override
    public Collection<GraphFeatureEvidence> getFeatures() {
        if (features == null) {
            features = new ArrayList<GraphFeatureEvidence>();
        }
        return this.features;
    }

    public void setFeatures(Collection<FeatureEvidence> features) {
        if (features != null) {
            addAllFeatures(features);
        } else {
            this.features = new ArrayList<GraphFeatureEvidence>();
        }
    }


    @Override
    public boolean addFeature(FeatureEvidence feature) {
        if (feature == null) {
            return false;
        }

        if (getFeatures().add(new GraphFeatureEvidence(feature))) {
            feature.setParticipant(this);
            return true;
        }
        return false;
    }

    //Todo review
    @Override
    public boolean removeFeature(FeatureEvidence feature) {
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
    public boolean addAllFeatures(Collection<? extends FeatureEvidence> features) {
        if (features == null) {
            return false;
        }

        boolean added = false;
        for (FeatureEvidence feature : features) {
            if (addFeature(feature)) {
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean removeAllFeatures(Collection<? extends FeatureEvidence> features) {
        if (features == null) {
            return false;
        }

        boolean added = false;
        for (FeatureEvidence feature : features) {
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
}
