package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.CooperativeEffect;
import psidev.psi.mi.jami.model.CooperativityEvidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.ModelledInteraction;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anjali on 30/04/19.
 */
public class GraphCooperativeEffect implements CooperativeEffect {

    private Collection<GraphCooperativityEvidence> cooperativityEvidences;
    private Collection<GraphModelledInteraction> affectedInteractions;
    private Collection<GraphAnnotation> annotations;
    private GraphCvTerm outCome;
    private GraphCvTerm response;


    public Collection<GraphCooperativityEvidence> getCooperativityEvidences() {
        if (this.cooperativityEvidences == null) {
            this.cooperativityEvidences = new ArrayList<GraphCooperativityEvidence>();
        }
        return this.cooperativityEvidences;
    }

    public void setCooperativityEvidences(Collection<CooperativityEvidence> cooperativityEvidences) {
        if (cooperativityEvidences != null) {
            this.cooperativityEvidences = CollectionAdaptor.convertCooperativityEvidenceIntoGraphModel(cooperativityEvidences);
        } else {
            this.cooperativityEvidences = new ArrayList<GraphCooperativityEvidence>();
        }
    }

    public Collection<GraphModelledInteraction> getAffectedInteractions() {
        if (this.affectedInteractions == null) {
            this.affectedInteractions = new ArrayList<GraphModelledInteraction>();
        }
        return this.affectedInteractions;
    }

    public void setAffectedInteractions(Collection<ModelledInteraction> affectedInteractions) {
        if (affectedInteractions != null) {
            this.affectedInteractions = CollectionAdaptor.convertModelledInteractionIntoGraphModel(affectedInteractions);
        } else {
            this.affectedInteractions = new ArrayList<GraphModelledInteraction>();
        }
    }

    public Collection<GraphAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<GraphAnnotation> annotations) {
        this.annotations = annotations;
    }


    public CvTerm getOutCome() {
        return outCome;
    }

    public void setOutCome(CvTerm outcoutComeome) {
        if (outCome != null) {
            if (outCome instanceof GraphCvTerm) {
                this.outCome = (GraphCvTerm) outCome;
            } else {
                this.outCome = new GraphCvTerm(outCome, false);
            }
        } else {
            this.outCome = null;
        }
    }

    @Override
    public CvTerm getResponse() {
        return response;
    }

    public void setResponse(CvTerm response) {
        if (response != null) {
            if (response instanceof GraphCvTerm) {
                this.response = (GraphCvTerm) response;
            } else {
                this.response = new GraphCvTerm(response, false);
            }
        } else {
            this.response = null;
        }
    }
}
