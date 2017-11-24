package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultStoichiometry;
import psidev.psi.mi.jami.utils.CvTermUtils;

import java.util.Collection;


/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphParticipantEvidence implements ParticipantEvidence {

    @GraphId
    protected Long graphId;

    private GraphCvTerm experimentalRole;
    private GraphCvTerm biologicalRole;
    private GraphOrganism expressedIn;
    private GraphStoichiometry stoichiometry;
    private GraphInteractor interactor;
    private Collection<GraphFeature> features;
    private Collection<GraphConfidence> confidences;
    private Collection<GraphParameter> parameters;
    private Collection<GraphCvTerm> identificationMethods;
    private Collection<GraphCvTerm> experimentalPreparations;
    private Collection<GraphCausalRelationship> causalRelationships;
    private EntityInteractorChangeListener changeListener;

    public GraphParticipantEvidence() {
    }

    public GraphParticipantEvidence(ParticipantEvidence participantEvidence) {
        setExperimentalRole(participantEvidence.getExperimentalRole());
        setBiologicalRole(participantEvidence.getBiologicalRole());
        setExpressedInOrganism(participantEvidence.getExpressedInOrganism());
        setStoichiometry(participantEvidence.getStoichiometry());
        setInteractor(participantEvidence.getInteractor());
        setFeatures(participantEvidence.getFeatures());
        setConfidences(participantEvidence.getConfidences());
        setParameters(participantEvidence.getParameters());
        setIdentificationMethods(participantEvidence.getIdentificationMethods());
        setExperimentalPreparations(participantEvidence.getExperimentalPreparations());
        setCausalRelationships(participantEvidence.getCausalRelationships());
        setChangeListener(participantEvidence.getChangeListener());
    }

    public CvTerm getExperimentalRole() {
        return this.experimentalRole;
    }

    public void setExperimentalRole(CvTerm experimentalRole) {
        if (experimentalRole != null) {
            if (experimentalRole instanceof GraphCvTerm) {
                this.experimentalRole = (GraphCvTerm) experimentalRole;
            } else {
                this.experimentalRole = new GraphCvTerm(experimentalRole);
            }
        } else {
            this.experimentalRole = new GraphCvTerm(CvTermUtils.createUnspecifiedRole());
        }
    }

    public CvTerm getBiologicalRole() {
        return this.biologicalRole;
    }

    public void setBiologicalRole(CvTerm biologicalRole) {
        if (biologicalRole != null) {
            if (biologicalRole instanceof GraphCvTerm) {
                this.biologicalRole = (GraphCvTerm) biologicalRole;
            } else {
                this.biologicalRole = new GraphCvTerm(biologicalRole);
            }
        } else {
            this.biologicalRole = new GraphCvTerm(CvTermUtils.createUnspecifiedRole());
        }
        //TODO login it
    }

    public Organism getExpressedInOrganism() {
        return this.expressedIn;
    }

    public void setExpressedInOrganism(Organism organism) {
        if (organism != null) {
            if (organism instanceof GraphOrganism) {
                this.expressedIn = (GraphOrganism) organism;
            } else {
                this.expressedIn = new GraphOrganism(organism);
            }
        } else {
            this.expressedIn = null;
        }
        //TODO login it
    }

    public Interactor getInteractor() {
        return this.interactor;
    }

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

    public Collection<CausalRelationship> getCausalRelationships() {
        if (this.causalRelationships == null){
            initialiseCausalRelationships();
        }
        return this.causalRelationships;
    }

    public Stoichiometry getStoichiometry() {
        return this.stoichiometry;
    }

    public void setStoichiometry(Integer stoichiometry) {
        if (stoichiometry == null){
            this.stoichiometry = null;
        }
        else {
            this.stoichiometry = new DefaultStoichiometry(stoichiometry, stoichiometry);
        }
    }

    public void setStoichiometry(Stoichiometry stoichiometry) {
        this.stoichiometry = stoichiometry;
    }

    public Collection<F> getFeatures() {
        if (features == null){
            initialiseFeatures();
        }
        return this.features;
    }

    public Collection<Xref> getXrefs() {
        if (xrefs == null){
            initialiseXrefs();
        }
        return this.xrefs;
    }

    public Collection<Annotation> getAnnotations() {
        if (annotations == null){
            initialiseAnnotations();
        }
        return this.annotations;
    }

    public Collection<Alias> getAliases() {
        if (aliases == null){
            initialiseAliases();
        }
        return this.aliases;
    }

    public boolean addFeature(F feature) {

        if (feature == null){
            return false;
        }

        if (getFeatures().add(feature)){
            feature.setParticipant(this);
            return true;
        }
        return false;
    }

    public boolean removeFeature(F feature) {

        if (feature == null){
            return false;
        }

        if (getFeatures().remove(feature)){
            feature.setParticipant(null);
            return true;
        }
        return false;
    }

    public boolean addAllFeatures(Collection<? extends F> features) {
        if (features == null){
            return false;
        }

        boolean added = false;
        for (F feature : features){
            if (addFeature(feature)){
                added = true;
            }
        }
        return added;
    }

    public boolean removeAllFeatures(Collection<? extends F> features) {
        if (features == null){
            return false;
        }

        boolean added = false;
        for (F feature : features){
            if (removeFeature(feature)){
                added = true;
            }
        }
        return added;
    }

    public void setInteractionAndAddParticipant(I interaction) {

        if (this.interaction != null){
            this.interaction.removeParticipant(this);
        }

        if (interaction != null){
            interaction.addParticipant(this);
        }
    }

    public I getInteraction() {
        return this.interaction;
    }

    public void setInteraction(I interaction) {
        this.interaction = interaction;
    }




    public Collection<CvTerm> getIdentificationMethods() {
        if (identificationMethods == null){
            initialiseIdentificationMethods();
        }
        return this.identificationMethods;
    }

    public Collection<CvTerm> getExperimentalPreparations() {
        if (experimentalPreparations == null){
            initialiseExperimentalPreparations();
        }
        return this.experimentalPreparations;
    }

    public Collection<Confidence> getConfidences() {
        if (confidences == null){
            initialiseConfidences();
        }
        return this.confidences;
    }

    public Collection<Parameter> getParameters() {
        if (parameters == null){
            initialiseParameters();
        }
        return this.parameters;
    }
}
