package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultChecksum;
import psidev.psi.mi.jami.utils.ChecksumUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@NodeEntity
public class GraphBinaryInteraction implements BinaryInteractionEvidence {

    @GraphId
    protected Long graphId;

    private String imexId;
    private GraphExperiment experiment;
    private String availability;
    private Collection<GraphParameter> parameters;
    private boolean isInferred = false;
    private Collection<GraphConfidence> confidences;
    private boolean isNegative;
    private Collection<GraphVariableParameterValueSet> variableParameterValueSets;
    private GraphParticipantEvidence participantA;
    private GraphParticipantEvidence participantB;
    private GraphCvTerm complexExpansion;
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

    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    private Collection<GraphInteractor> interactors;

    private GraphInteractor interactorA;
    private GraphInteractor interactorB;

    public GraphBinaryInteraction(BinaryInteractionEvidence binaryInteractionEvidence) {
        setImexId(binaryInteractionEvidence.getImexId());
        setExperiment(binaryInteractionEvidence.getExperiment());
        setAvailability(binaryInteractionEvidence.getAvailability());
        setParameters(binaryInteractionEvidence.getParameters());
        setInferred(binaryInteractionEvidence.isInferred());
        setConfidences(binaryInteractionEvidence.getConfidences());
        setNegative(binaryInteractionEvidence.isNegative());
        setVariableParameterValueSets(binaryInteractionEvidence.getVariableParameterValues());
        setParticipantA(binaryInteractionEvidence.getParticipantA());
        setParticipantB(binaryInteractionEvidence.getParticipantB());
        setComplexExpansion(binaryInteractionEvidence.getComplexExpansion());
        setShortName(binaryInteractionEvidence.getShortName());
        setRigid(binaryInteractionEvidence.getRigid());
        setChecksums(binaryInteractionEvidence.getChecksums());
        setIdentifiers(binaryInteractionEvidence.getIdentifiers());
        setXrefs(binaryInteractionEvidence.getXrefs());
        setAnnotations(binaryInteractionEvidence.getAnnotations());
        setUpdatedDate(binaryInteractionEvidence.getUpdatedDate());
        setCreatedDate(binaryInteractionEvidence.getCreatedDate());
        setInteractionType(binaryInteractionEvidence.getInteractionType());
        setParticipants(binaryInteractionEvidence.getParticipants());
    }

    public String getImexId() {
        return this.imexId != null ? this.imexId : null;
    }

    // Dummy Method because interface wants it
    public void assignImexId(String identifier) {


        /*// add new imex if not null
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
        }*/
    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    public void setExperiment(Experiment experiment) {

        if (experiment != null) {
            if (experiment instanceof GraphExperiment) {
                this.experiment = (GraphExperiment) experiment;
            } else {
                this.experiment = new GraphCvTerm(experiment);
            }
        } else {
            this.experiment = null;
        }
    }
 // dummy method for interface
    public void setExperimentAndAddInteractionEvidence(Experiment experiment) {
        /*if (this.experiment != null) {
            this.experiment.removeInteractionEvidence(this);
        }

        if (experiment != null) {
            experiment.addInteractionEvidence(this);
        }*/
    }

    public Collection<? extends VariableParameterValueSet> getVariableParameterValues() {
        return this.variableParameterValueSets;
    }

    public Collection<? extends Confidence> getConfidences() {
         return this.confidences;
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

    public Collection<? extends Parameter> getParameters() {
        return this.parameters;
    }

    public boolean isInferred() {
        return this.isInferred;
    }

    public void setInferred(boolean inferred) {
        this.isInferred = inferred;
    }

    public ParticipantEvidence getParticipantA() {
        return participantA;
    }

    public void setParticipantA(ParticipantEvidence participantA) {
        if (participantA != null) {
            if (participantA instanceof GraphParticipantEvidence) {
                this.participantA = (GraphParticipantEvidence) participantA;
            } else {
                this.participantA = new GraphParticipantEvidence(participantA);
            }
        } else {
            this.participantA = null;
        }
    }

    public ParticipantEvidence getParticipantB() {
        return participantB;
    }

    public void setParticipantB(ParticipantEvidence participantB) {
        if (participantB != null) {
            if (participantB instanceof GraphParticipantEvidence) {
                this.participantB = (GraphParticipantEvidence) participantB;
            } else {
                this.participantB = new GraphParticipantEvidence(participantB);
            }
        } else {
            this.participantB = null;
        }

    }

    public CvTerm getComplexExpansion() {
        return this.complexExpansion;
    }

    public void setComplexExpansion(CvTerm expansion) {
        if (expansion != null) {
            if (expansion instanceof GraphCvTerm) {
                this.complexExpansion = (GraphCvTerm) expansion;
            } else {
                this.complexExpansion = new GraphCvTerm(expansion);
            }
        } else {
            this.complexExpansion = null;
        }
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String name) {
        this.shortName = name;
    }

    public String getRigid() {
        return this.rigid != null ? this.rigid : null;
    }

    public void setRigid(String rigid) {
         this.rigid = rigid;
    }

    public Collection<? extends Xref> getIdentifiers() {
        return this.identifiers;
    }

    public Collection<? extends Xref> getXrefs() {
         return this.xrefs;
    }

    public Collection<? extends Checksum> getChecksums() {
          return this.checksums;
    }

    public Collection<? extends Annotation> getAnnotations() {
          return this.annotations;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Date updated) {
        this.updatedDate = updated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created) {
        this.createdDate = created;
    }

    public CvTerm getInteractionType() {
        return this.interactionType;
    }

    public void setInteractionType(CvTerm interactionType) {
        if (interactionType != null) {
            if (interactionType instanceof GraphCvTerm) {
                this.interactionType = (GraphCvTerm) interactionType;
            } else {
                this.interactionType = new GraphCvTerm(interactionType);
            }
        } else {
            this.interactionType = null;
        }
    }

    public Collection<? extends ParticipantEvidence> getParticipants() {
         return participants;
    }

    public void setImexId(String imexId) {
         this.imexId = imexId;

    }

    public void setParameters(Collection<Parameter> parameters) {
        if(parameters!=null) {
            this.parameters = CollectionAdaptor.convertParameterIntoGraphModel(parameters);
        }else{
            this.parameters=new ArrayList<GraphParameter>();
        }
    }

    public void setConfidences(Collection<Confidence> confidences) {
        if(confidences!=null) {
            this.confidences = CollectionAdaptor.convertConfidenceIntoGraphModel(confidences)
        }
        else{
            this.confidences = new ArrayList<GraphConfidence>();
        }
    }

    public Collection<GraphVariableParameterValueSet> getVariableParameterValueSets() {
        return variableParameterValueSets;
    }

    public void setVariableParameterValueSets(Collection<VariableParameterValueSet> variableParameterValueSets) {
        if(variableParameterValueSets!=null) {
            this.variableParameterValueSets = CollectionAdaptor.convertvariableParameterValueIntoGraphModel(variableParameterValueSets)
        }
        else{
            this.variableParameterValueSets = new ArrayList<GraphVariableParameterValueSet>();
        }
    }

    public void setChecksums(Collection<Checksum> checksums) {
        if(checksums!=null) {
            this.checksums = CollectionAdaptor.convertChecksumIntoGraphModel(checksums)
        }
        else{
            this.checksums = new ArrayList<GraphChecksum>();
        }
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        if(identifiers!=null) {
            this.identifiers = CollectionAdaptor.convertXrefIntoGraphModel(identifiers);
        }
        else{
            this.identifiers = new ArrayList<GraphXref>();
        }
    }

    public void setXrefs(Collection<Xref> xrefs) {
        if(xrefs!=null) {
            this.xrefs = CollectionAdaptor.convertXrefIntoGraphModel(xrefs);
        }
        else{
            this.xrefs = new ArrayList<GraphXref>();
        }
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        if(annotations!=null) {
            this.annotations = CollectionAdaptor.convertAnnotationIntoGraphModel(annotations);
        }
        else{
            this.annotations = new ArrayList<GraphAnnotation>();
        }
    }

    public void setParticipants(Collection<ParticipantEvidence> participants) {
        if(participants!=null) {
            this.participants = CollectionAdaptor.convertAnnotationIntoGraphModel(participants);
        }
        else{
            this.participants = new ArrayList<GraphParticipantEvidence>();
        }
    }

    public void setInteractors(Collection<GraphInteractor> interactors) {
        this.interactors = interactors;
    }

    public void setInteractorA(GraphInteractor interactorA) {
        this.interactorA = interactorA;
    }

    public void setInteractorB(GraphInteractor interactorB) {
        this.interactorB = interactorB;
    }

    // dummy method for the interface
    public boolean addParticipant(ParticipantEvidence part) {
        /*if (part == null) {
            return false;
        }
        if (getParticipants().add(part)) {
            part.setInteraction(this);
            return true;
        }*/
        return false;
    }

    // dummy method for the interface
    public boolean removeParticipant(ParticipantEvidence part) {
        /*if (part == null) {
            return false;
        }
        if (getParticipants().remove(part)) {
            part.setInteraction(null);
            return true;
        }*/
        return false;
    }
    // dummy method for the interface
    public boolean addAllParticipants(Collection<? extends ParticipantEvidence> participants) {
        /*if (participants == null) {
            return false;
        }

        boolean added = false;
        for (ParticipantEvidence p : participants) {
            if (addParticipant(p)) {
                added = true;
            }
        }*/
        return false;
    }
    // dummy method for the interface
    public boolean removeAllParticipants(Collection<? extends ParticipantEvidence> participants) {
        /*if (participants == null) {
            return false;
        }

        boolean removed = false;
        for (ParticipantEvidence p : participants) {
            if (removeParticipant(p)) {
                removed = true;
            }
        }*/
        return false;
    }

    public Collection<? extends Interactor> getInteractors() {
        return interactors;
    }

    public GraphInteractor getInteractorA() {
        return interactorA;
    }

    public GraphInteractor getInteractorB() {
        return interactorB;
    }

    // dummy method for the interface
    protected void processAddedChecksumEvent(Checksum added) {
       /* if (rigid == null && ChecksumUtils.doesChecksumHaveMethod(added, Checksum.RIGID_MI, Checksum.RIGID)) {
            // the rigid is not set, we can set the rigid
            rigid = added;
        }*/
    }
    // dummy method for the interface
    protected void processRemovedChecksumEvent(Checksum removed) {
        /*if (rigid == removed) {
            rigid = ChecksumUtils.collectFirstChecksumWithMethod(getChecksums(), Checksum.RIGID_MI, Checksum.RIGID);
        }*/
    }

    // dummy method for the interface
    protected void clearPropertiesLinkedToChecksums() {
        /*rigid = null;*/
    }
    // dummy method for the interface
    protected void processAddedXrefEvent(Xref added) {

        // the added identifier is imex and the current imex is not set
        /*if (imexId == null && XrefUtils.isXrefFromDatabase(added, Xref.IMEX_MI, Xref.IMEX)) {
            // the added xref is imex-primary
            if (XrefUtils.doesXrefHaveQualifier(added, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY)) {
                imexId = added;
            }
        }*/
    }
    // dummy method for the interface
    protected void processRemovedXrefEvent(Xref removed) {
        // the removed identifier is pubmed
        /*if (imexId != null && imexId.equals(removed)) {
            Collection<Xref> existingImex = XrefUtils.collectAllXrefsHavingDatabaseAndQualifier(getXrefs(), Xref.IMEX_MI, Xref.IMEX, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY);
            if (!existingImex.isEmpty()) {
                imexId = existingImex.iterator().next();
            }
        }*/
    }
    // dummy method for the interface
    protected void clearPropertiesLinkedToXrefs() {
        /*imexId = null;*/
    }

 /*   private class InteractionChecksumList extends AbstractListHavingProperties<Checksum> {
        public InteractionChecksumList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(Checksum added) {
            processAddedChecksumEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(Checksum removed) {
            processRemovedChecksumEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToChecksums();
        }
    }

    *//**
     * Experimental interaction Xref list
     *//*
    private class ExperimentalInteractionXrefList extends AbstractListHavingProperties<Xref> {
        public ExperimentalInteractionXrefList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(Xref added) {

            processAddedXrefEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(Xref removed) {
            processRemovedXrefEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToXrefs();
        }
    }*/

    @Override
    public String toString() {
        return "Interaction: " + (getShortName() != null ? getShortName() + ", " : "") + (getInteractionType() != null ? getInteractionType().toString() : "");
    }
}
