package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

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
        setFeatures(entity.getFeatures());
        setStoichiometry(entity.getStoichiometry());
        setCausalRelationships(entity.getCausalRelationships());
        setChangeListener(entity.getChangeListener());
    }

    @Override
    public Interactor getInteractor() {
        return this.interactor;
    }

    @Override
    public void setInteractor(Interactor interactor) {
        if (interactor == null){
            throw new IllegalArgumentException("The interactor cannot be null.");
        }
        Interactor oldInteractor = this.interactor;
        if (interactor instanceof GraphInteractor) {
            this.interactor = (GraphInteractor) interactor;
        } else {
            this.interactor = new GraphInteractor(interactor);
        }
        if (this.changeListener != null){
            this.changeListener.onInteractorUpdate(this, oldInteractor);
        }
    }
    @Override
    public Collection<GraphFeatureEvidence> getFeatures() {
        if (features == null){
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
        if (feature == null){
            return false;
        }

        if (getFeatures().add(new GraphFeatureEvidence(feature))){
            feature.setParticipant(this);
            return true;
        }
        return false;
    }

    //Todo review
    @Override
    public boolean removeFeature(FeatureEvidence feature) {
        if (feature == null){
            return false;
        }

        if (getFeatures().remove(feature)){
            feature.setParticipant(null);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAllFeatures(Collection<? extends FeatureEvidence> features) {
        if (features == null){
            return false;
        }

        boolean added = false;
        for (FeatureEvidence feature : features){
            if (addFeature(feature)){
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean removeAllFeatures(Collection<? extends FeatureEvidence> features) {
        if (features == null){
            return false;
        }

        boolean added = false;
        for (FeatureEvidence feature : features){
            if (removeFeature(feature)){
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
        if (this.causalRelationships == null){
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
}
