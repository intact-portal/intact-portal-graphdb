package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.Confidence;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Parameter;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.model.impl.DefaultStoichiometry;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.xml.model.BiologicalRole;
import psidev.psi.mi.xml.model.Feature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphParticipant  implements psidev.psi.mi.jami.model.ParticipantEvidence {


    @GraphId
    private Long id;


    private CvTerm experimentalRole;
    private Collection<CvTerm> identificationMethods;
    private Collection<CvTerm> experimentalPreparations;
    private Organism expressedIn;
    private Collection<Confidence> confidences;
    private Collection<Parameter> parameters;
    private EntityInteractorChangeListener changeListener;
    private GraphInteractor interactor;
    private Stoichiometry stoichiometry;
    private Collection<CausalRelationship> causalRelationships;
    private Collection<Feature> features;
    private CvTerm biologicalRole;


    protected void initialiseExperimentalPreparations() {
        this.experimentalPreparations = new ArrayList<CvTerm>();
    }

    protected void initialiseConfidences() {
        this.confidences = new ArrayList<Confidence>();
    }

    protected void initialiseParameters() {
        this.parameters = new ArrayList<Parameter>();
    }

    protected void initialiseIdentificationMethods(){
        this.identificationMethods = new ArrayList<CvTerm>();
    }

    protected void initialiseIdentificationMethodsWith(Collection<CvTerm> methods){
        if (methods == null){
            this.identificationMethods = Collections.EMPTY_LIST;
        }
        else {
            this.identificationMethods = methods;
        }
    }

    protected void initialiseFeatures(){
        this.features = new ArrayList<Feature>();
    }

    protected void initialiseCausalRelationships(){
        this.causalRelationships = new ArrayList<CausalRelationship>();
    }

    protected void initialiseCausalRelationshipsWith(Collection<CausalRelationship> relationships) {
        if (relationships == null){
            this.causalRelationships = Collections.EMPTY_LIST;
        }
        else {
            this.causalRelationships = relationships;
        }
    }

    protected void initialiseFeaturesWith(Collection<Feature> features) {
        if (features == null){
            this.features = Collections.EMPTY_LIST;
        }
        else {
            this.features = features;
        }
    }


    public Interactor getInteractor() {
        return this.interactor;
    }

    public void setInteractor(Interactor interactor) {
        if (interactor == null){
            throw new IllegalArgumentException("The interactor cannot be null.");
        }
        Interactor oldInteractor = this.interactor;
        this.interactor = (GraphInteractor) interactor;
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

    public Collection<Feature> getFeatures() {
        if (features == null){
            initialiseFeatures();
        }
        return this.features;
    }




    protected void initialiseExperimentalPreparationsWith(Collection<CvTerm> expPreparations) {
        if (expPreparations == null){
            this.experimentalPreparations = Collections.EMPTY_LIST;
        }
        else {
            this.experimentalPreparations = expPreparations;
        }
    }

    protected void initialiseConfidencesWith(Collection<Confidence> confidences) {
        if (confidences == null){
            this.confidences = Collections.EMPTY_LIST;
        }
        else {
            this.confidences = confidences;
        }
    }

    protected void initialiseParametersWith(Collection<Parameter> parameters) {
        if (parameters == null){
            this.parameters = Collections.EMPTY_LIST;
        }
        else {
            this.parameters = parameters;
        }
    }

    public CvTerm getExperimentalRole() {
        return this.experimentalRole;
    }

    public void setExperimentalRole(CvTerm expRole) {
        if (expRole == null){
            this.experimentalRole = CvTermUtils.createUnspecifiedRole();
        }
        else {
            this.experimentalRole = expRole;
        }
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

    public Organism getExpressedInOrganism() {
        return this.expressedIn;
    }

    public void setExpressedInOrganism(Organism organism) {
        this.expressedIn = organism;
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

    public EntityInteractorChangeListener getChangeListener() {
        return this.changeListener;
    }

    public void setChangeListener(EntityInteractorChangeListener listener) {
        this.changeListener = listener;
    }

    @Override
    public boolean addFeature(FeatureEvidence feature) {
        return false;
    }

    @Override
    public boolean removeFeature(FeatureEvidence feature) {
        return false;
    }

    @Override
    public boolean addAllFeatures(Collection<? extends FeatureEvidence> features) {
        return false;
    }

    @Override
    public boolean removeAllFeatures(Collection<? extends FeatureEvidence> features) {
        return false;
    }

    @Override
    public String toString() {
        return super.toString()
                + (getExperimentalRole() != null ? ", " + getExperimentalRole().toString() : "")
                + (getExpressedInOrganism() != null ? ", " + getExpressedInOrganism().toString() : "");
    }


    @Override
    public void setInteractionAndAddParticipant(InteractionEvidence interaction) {

    }

    @Override
    public InteractionEvidence getInteraction() {
        return null;
    }

    @Override
    public void setInteraction(InteractionEvidence interaction) {

    }



    @Override
    public <X extends Xref> Collection<X> getXrefs() {
        return null;
    }

    @Override
    public <A extends Annotation> Collection<A> getAnnotations() {
        return null;
    }

    @Override
    public <A extends psidev.psi.mi.jami.model.Alias> Collection<A> getAliases() {
        return null;
    }


    @Override
    public CvTerm getBiologicalRole() {
        return biologicalRole;
    }

    @Override
    public void setBiologicalRole(CvTerm biologicalRole) {
        this.biologicalRole = biologicalRole;
    }
}
