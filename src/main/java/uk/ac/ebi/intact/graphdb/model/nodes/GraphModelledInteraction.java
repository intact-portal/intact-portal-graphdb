package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.ModelledInteraction;

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
        return interactionEvidences;
    }

    public void setInteractionEvidences(Collection<InteractionEvidence> interactionEvidences) {
        this.interactionEvidences = interactionEvidences;
    }

    @Override
    public GraphSource getSource() {
        return source;
    }

    public void setSource(GraphSource source) {
        this.source = source;
    }

    public Collection<GraphModelledConfidence> getModelledConfidences() {
        return modelledConfidences;
    }

    public void setModelledConfidences(Collection<GraphModelledConfidence> modelledConfidences) {
        this.modelledConfidences = modelledConfidences;
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
