/*
package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultXref;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

*/
/**
 * Created by anjali on 23/11/17.
 *//*

public class GraphInteractionEvidence implements InteractionEvidence {

    private String imexId;
    private GraphExperiment experiment;
    private String availability;
    private Collection<GraphParameter> parameters;
    private boolean isInferred = false;
    private Collection<GraphConfidence> confidences;
    private boolean isNegative;
    private Collection<GraphVariableParameterValueSet> variableParameterValueSets;
    private String shortName;
    private String rigid;
    private Collection<GraphChecksum> checksums;
    private Collection<GraphXref> identifiers;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private Date updatedDate;
    private Date createdDate;
    private GraphCvTerm interactionType;
    private Collection<GraphParticipantEvidence> participants;


    public GraphInteractionEvidence(){

    }

    public GraphInteractionEvidence(InteractionEvidence interactionEvidence){
        setImexId(interactionEvidence.getImexId());
        setExperiment(interactionEvidence.getExperiment());
        setAvailability(interactionEvidence.getAvailability());
        setParameters(interactionEvidence.getParameters());
        setInferred(interactionEvidence.isInferred());
        setConfidences(interactionEvidence.getConfidences());
        setNegative(interactionEvidence.isNegative());
        setVariableParameterValueSets(interactionEvidence.getVariableParameterValues());
        setShortName(interactionEvidence.getShortName());
        setRigid(interactionEvidence.getRigid());
        setChecksums(interactionEvidence.getChecksums());
        setIdentifiers(interactionEvidence.getIdentifiers());
        setXrefs(interactionEvidence.getXrefs());
        setAnnotations(interactionEvidence.getAnnotations());
    }

    public Collection<GraphAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<GraphAnnotation> annotations) {
        this.annotations = annotations;
    }

    public Collection<GraphXref> getXrefs() {
        if(this.xrefs==null){
            this.xrefs=new ArrayList<GraphXref>();
        }
        return this.xrefs;
    }

    public void setXrefs(Collection<Xref> xrefs) {
        if(xrefs!=null) {
            this.xrefs = CollectionAdaptor.convertXrefIntoGraphModel(xrefs);
        }
        else{
            this.xrefs = new ArrayList<GraphXref>();
        }

    }

    public Collection<GraphXref> getIdentifiers() {
        if(this.identifiers==null){
            this.identifiers=new ArrayList<GraphXref>();
        }
        return this.identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        if(identifiers!=null) {
            this.identifiers = CollectionAdaptor.convertXrefIntoGraphModel(identifiers);
        }
        else{
            this.identifiers = new ArrayList<GraphXref>();
        }
    }

    public Collection<GraphChecksum> getChecksums() {
        if(this.checksums==null){
            this.checksums=new ArrayList<GraphChecksum>();
        }
        return this.checksums;
        }

    public void setChecksums(Collection<Checksum> checksums) {
        if(checksums!=null) {
            this.checksums = CollectionAdaptor.convertChecksumIntoGraphModel(checksums);
        }
        else{
            this.checksums = new ArrayList<GraphChecksum>();
        }
    }

    @Override
    public String getRigid() {
        return rigid;
    }

    @Override
    public void setRigid(String rigid) {
        this.rigid = rigid;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setExperiment(GraphExperiment experiment) {
        this.experiment = experiment;
    }

    public Collection<GraphVariableParameterValueSet> getVariableParameterValueSets() {
        if(this.variableParameterValueSets==null){
            this.variableParameterValueSets=new ArrayList<GraphVariableParameterValueSet>();
        }
        return this.variableParameterValueSets;
    }

    public void setVariableParameterValueSets(Collection<VariableParameterValueSet> variableParameterValueSets) {
        if(variableParameterValueSets!=null) {
            this.variableParameterValueSets = CollectionAdaptor.convertVariableParameterValueIntoGraphModel(variableParameterValueSets);
        }
        else{
            this.variableParameterValueSets = new ArrayList<GraphVariableParameterValueSet>();
        }
    }

    public String getImexId() {
        return this.imexId;
    }

    public void setImexId(String imexId) {
        this.imexId = imexId;
    }

    // dummy method for interface, may be To do in future.
    public void assignImexId(String identifier) {
       */
/* // add new imex if not null
        if (identifier != null){
            ExperimentalInteractionXrefList interactionXrefs = (ExperimentalInteractionXrefList) getXrefs();
            CvTerm imexDatabase = CvTermUtils.createImexDatabase();
            CvTerm imexPrimaryQualifier = CvTermUtils.createImexPrimaryQualifier();
            // first remove old doi if not null
            if (this.imexId != null){
                interactionXrefs.removeOnly(this.imexId);
            }
            this.imexId = new DefaultXref(imexDatabase, identifier, imexPrimaryQualifier);
            interactionXrefs.addOnly(this.imexId);
        }
        else {
            throw new IllegalArgumentException("The imex id has to be non null.");
        }*//*

    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    public void setExperiment(Experiment experiment) {
        if (experiment != null) {
            if (experiment instanceof GraphExperiment) {
                this.experiment = (GraphExperiment) experiment;
            } else {
                this.experiment = new GraphExperiment(experiment);
            }
        } else {
            this.experiment = null;
        }
    }

    //Dummy Now for interface, may to do later.
    public void setExperimentAndAddInteractionEvidence(Experiment experiment) {
 */
/*        if (this.experiment != null){
            this.experiment.removeInteractionEvidence(this);
        }

        if (experiment != null){
            experiment.addInteractionEvidence(this);
        }*//*

    }

    public Collection<GraphVariableParameterValueSet> getVariableParameterValues() {

        return this.variableParameterValueSets;
    }

    public Collection<GraphConfidence> getConfidences() {
        if(this.confidences==null){
            this.confidences=new ArrayList<GraphConfidence>();
        }
        return this.confidences;
    }

    public void setConfidences(Collection<Confidence> confidences) {
        if(confidences!=null) {
            this.confidences = CollectionAdaptor.convertConfidenceIntoGraphModel(confidences);
        }
        else{
            this.confidences = new ArrayList<GraphConfidence>();
        }
    }



    public String getAvailability() {
        return this.availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public boolean isNegative() {
        return this.isNegative;
    }

    public void setNegative(boolean negative) {
        this.isNegative = negative;
    }

    public Collection<GraphParameter> getParameters() {
        if(this.parameters==null){
            this.parameters=new ArrayList<GraphParameter>();
        }
        return this.parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        if(parameters!=null) {
            this.parameters = CollectionAdaptor.convertParameterIntoGraphModel(parameters);
        }
        else{
            this.parameters = new ArrayList<GraphParameter>();
        }
    }

    public boolean isInferred() {
        return this.isInferred;
    }

    public void setInferred(boolean inferred) {
        this.isInferred = inferred;
    }

*/
/*    protected void processAddedXrefEvent(Xref added) {

        // the added identifier is imex and the current imex is not set
        if (imexId == null && XrefUtils.isXrefFromDatabase(added, Xref.IMEX_MI, Xref.IMEX)){
            // the added xref is imex-primary
            if (XrefUtils.doesXrefHaveQualifier(added, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY)){
                imexId = added;
            }
        }
    }

    protected void processRemovedXrefEvent(Xref removed) {
        // the removed identifier is pubmed
        if (imexId != null && imexId.equals(removed)){
            Collection<Xref> existingImex = XrefUtils.collectAllXrefsHavingDatabaseAndQualifier(getXrefs(), Xref.IMEX_MI, Xref.IMEX, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY);
            if (!existingImex.isEmpty()){
                imexId = existingImex.iterator().next();
            }
        }
    }*//*


    protected void clearPropertiesLinkedToXrefs() {
        imexId = null;
    }

    @Override
    public String toString() {
        return "Interaction evidence: "+(getImexId() != null ? getImexId() :
                (getShortName() != null ? getShortName()+", " : "") + (getInteractionType() != null ? getInteractionType().toString() : ""));
    }



    @Override
    public boolean addParticipant(ParticipantEvidence part) {
        return false;
    }

    @Override
    public boolean removeParticipant(ParticipantEvidence part) {
        return false;
    }

    @Override
    public boolean addAllParticipants(Collection<? extends ParticipantEvidence> participants) {
        return false;
    }

    @Override
    public boolean removeAllParticipants(Collection<? extends ParticipantEvidence> participants) {
        return false;
    }
}
*/
