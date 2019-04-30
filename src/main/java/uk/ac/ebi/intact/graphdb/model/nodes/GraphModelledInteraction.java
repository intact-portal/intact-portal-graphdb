package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.ModelledConfidence;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.model.Source;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anjali on 30/04/19.
 */
public class GraphModelledInteraction implements ModelledInteraction {

    private Collection<GraphInteractionEvidence> interactionEvidences;
    private GraphSource source;
    private Collection<GraphModelledConfidence> modelledConfidences;
    private Collection<GraphModelledParameter> modelledParameters;
    private Collection<GraphCooperativeEffect> cooperativeEffects;
    private GraphCvTerm evidenceType;


    public Collection<GraphInteractionEvidence> getInteractionEvidences() {
        if (interactionEvidences == null) {
            this.interactionEvidences = new ArrayList<GraphInteractionEvidence>();
        }
        return this.interactionEvidences;
    }

    public void setInteractionEvidences(Collection<InteractionEvidence> interactionEvidences) {
        if (interactionEvidences != null) {
            this.interactionEvidences = CollectionAdaptor.convertInteractionEvidenceIntoGraphModel(interactionEvidences);
        } else {
            this.interactionEvidences = new ArrayList<GraphInteractionEvidence>();
        }
    }

    public Source getSource() {
        return this.source;
    }

    public void setSource(Source source) {
        if (source != null) {
            if (source instanceof GraphSource) {
                this.source = (GraphSource) source;
            } else {
                this.source = new GraphSource(source);
            }
        } else {
            this.source = null;
        }
    }

    public Collection<GraphModelledConfidence> getModelledConfidences() {
        if (modelledConfidences == null) {
            this.modelledConfidences = new ArrayList<GraphModelledConfidence>();
        }
        return this.modelledConfidences;
    }

    public void setModelledConfidences(Collection<ModelledConfidence> modelledConfidences) {
        if (modelledConfidences != null) {
            this.modelledConfidences = CollectionAdaptor.convertModelledConfidenceIntoGraphModel(modelledConfidences);
        } else {
            this.modelledConfidences = new ArrayList<GraphModelledConfidence>();
        }
    }

    public Collection<GraphModelledParameter> getModelledParameters() {
        return modelledParameters;
    }

    public void setModelledParameters(Collection<GraphModelledParameter> modelledParameters) {
        this.modelledParameters = modelledParameters;
    }

    public Collection<GraphCooperativeEffect> getCooperativeEffects() {
        return cooperativeEffects;
    }

    public void setCooperativeEffects(Collection<GraphCooperativeEffect> cooperativeEffects) {
        this.cooperativeEffects = cooperativeEffects;
    }

    @Override
    public GraphCvTerm getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(GraphCvTerm evidenceType) {
        this.evidenceType = evidenceType;
    }
}
