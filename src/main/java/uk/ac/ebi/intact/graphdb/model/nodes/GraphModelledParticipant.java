package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anjali on 29/04/19.
 */
@NodeEntity
public class GraphModelledParticipant extends GraphModelledEntity implements ModelledParticipant {


    private GraphModelledInteraction interaction;

    @Relationship(type = RelationshipTypes.BIOLOGICAL_ROLE)
    private GraphCvTerm biologicalRole;

    @Relationship(type = RelationshipTypes.BIOLOGICAL_EFFECT)
    private GraphCvTerm biologicalEffect;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    private EntityInteractorChangeListener changeListener;

    @Relationship(type = RelationshipTypes.INTERACTOR)
    private GraphInteractor interactor;

    @Relationship(type = RelationshipTypes.PARTICIPANT_FEATURE, direction = Relationship.OUTGOING)
    private Collection<GraphModelledFeature> features;


    public CvTerm getBiologicalRole() {
        return this.biologicalRole;
    }

    public void setBiologicalRole(CvTerm biologicalRole) {
        if (biologicalRole != null) {
            if (biologicalRole instanceof GraphCvTerm) {
                this.biologicalRole = (GraphCvTerm) biologicalRole;
            } else {
                this.biologicalRole = new GraphCvTerm(biologicalRole, false);
            }
        } else {
            this.biologicalRole = null;
        }
    }


    public CvTerm getBiologicalEffect() {
        return this.biologicalEffect;
    }


    public void setBiologicalEffect(CvTerm biologicalEffect) {

        if (biologicalEffect != null) {
            if (biologicalEffect instanceof GraphCvTerm) {
                this.biologicalEffect = (GraphCvTerm) biologicalEffect;
            } else {
                this.biologicalEffect = new GraphCvTerm(biologicalEffect, false);
            }
        } else {
            this.biologicalEffect = null;
        }
    }

    public Collection<GraphXref> getXrefs() {
        if (this.xrefs == null) {
            this.xrefs = new ArrayList<GraphXref>();
        }
        return this.xrefs;
    }

    public void setXrefs(Collection<Xref> xrefs) {
        if (xrefs != null) {
            this.xrefs = CollectionAdaptor.convertXrefIntoGraphModel(xrefs);
        } else {
            this.xrefs = new ArrayList<GraphXref>();
        }
    }

    public Collection<GraphAnnotation> getAnnotations() {
        if (this.annotations == null) {
            this.annotations = new ArrayList<GraphAnnotation>();
        }
        return this.annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        if (annotations != null) {
            this.annotations = CollectionAdaptor.convertAnnotationIntoGraphModel(annotations);
        } else {
            this.annotations = new ArrayList<GraphAnnotation>();
        }
    }

    public Collection<GraphAlias> getAliases() {
        if (aliases == null) {
            this.aliases = new ArrayList<GraphAlias>();
        }
        return this.aliases;
    }

    public void setAliases(Collection<Alias> aliases) {
        if (aliases != null) {
            this.aliases = CollectionAdaptor.convertAliasIntoGraphModel(aliases);
        } else {
            this.aliases = new ArrayList<GraphAlias>();
        }
    }


    @Override
    public ModelledInteraction getInteraction() {
        return interaction;
    }

    public void setInteraction(ModelledInteraction interaction) {
        if (interaction != null) {
            if (interaction instanceof GraphModelledInteraction) {
                this.interaction = (GraphModelledInteraction) interaction;
            } else {
                this.interaction = new GraphModelledInteraction(interaction, false);
            }
        } else {
            this.interaction = null;
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
        GraphModelledFeature graphModelledFeature = new GraphModelledFeature(feature);
        if (getFeatures().add(graphModelledFeature)) {
            graphModelledFeature.setParticipant(this);
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
    public void setInteractionAndAddParticipant(ModelledInteraction interaction) {

        if (this.interaction != null) {
            this.interaction.removeParticipant(this);
        }

        if (interaction != null) {
            interaction.addParticipant(this);
        }
    }
}
