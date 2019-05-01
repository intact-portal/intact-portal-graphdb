package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by anjali on 30/04/19.
 */
public class GraphModelledInteraction implements ModelledInteraction {

    private Date updatedDate;
    private Date createdDate;
    private GraphSource source;
    private GraphCvTerm evidenceType;
    private String rigid;
    private String shortName;

    @Relationship(type = RelationshipTypes.INTERACTION_TYPE)
    private GraphCvTerm interactionType;

    @Relationship(type = RelationshipTypes.CHECKSUMS)
    private Collection<GraphChecksum> checksums;

    private Collection<GraphInteractionEvidence> interactionEvidences;
    private Collection<GraphModelledConfidence> modelledConfidences;
    private Collection<GraphModelledParameter> modelledParameters;
    private Collection<GraphCooperativeEffect> cooperativeEffects;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.IE_PARTICIPANT, direction = Relationship.OUTGOING)
    @JsonManagedReference
    private Collection<GraphModelledParticipant> participants;


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

    public Collection<GraphChecksum> getChecksums() {
        if (this.checksums == null) {
            this.checksums = new ArrayList<GraphChecksum>();
        }
        return this.checksums;
    }

    public void setChecksums(Collection<Checksum> checksums) {
        if (checksums != null) {
            this.checksums = CollectionAdaptor.convertChecksumIntoGraphModel(checksums);
        } else {
            this.checksums = new ArrayList<GraphChecksum>();
        }
    }

    public CvTerm getInteractionType() {
        return this.interactionType;
    }

    public void setInteractionType(CvTerm interactionType) {
        if (interactionType != null) {
            if (interactionType instanceof GraphCvTerm) {
                this.interactionType = (GraphCvTerm) interactionType;
            } else {
                this.interactionType = new GraphCvTerm(interactionType, false);
            }
        } else {
            this.interactionType = null;
        }
    }

    public String getRigid() {
        return this.rigid != null ? this.rigid : null;
    }

    public void setRigid(String rigid) {
        this.rigid = rigid;
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

    public Collection<GraphXref> getIdentifiers() {
        if (this.identifiers == null) {
            this.identifiers = new ArrayList<GraphXref>();
        }
        return this.identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        if (identifiers != null) {
            this.identifiers = CollectionAdaptor.convertXrefIntoGraphModel(identifiers);
        } else {
            this.identifiers = new ArrayList<GraphXref>();
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
        if (modelledParameters == null) {
            this.modelledParameters = new ArrayList<GraphModelledParameter>();
        }
        return this.modelledParameters;
    }

    public void setModelledParameters(Collection<ModelledParameter> modelledParameters) {
        if (modelledParameters != null) {
            this.modelledParameters = CollectionAdaptor.convertModelledParameterIntoGraphModel(modelledParameters);
        } else {
            this.modelledParameters = new ArrayList<GraphModelledParameter>();
        }
    }

    public Collection<GraphCooperativeEffect> getCooperativeEffects() {
        if (cooperativeEffects == null) {
            this.cooperativeEffects = new ArrayList<GraphCooperativeEffect>();
        }
        return this.cooperativeEffects;
    }

    public void setCooperativeEffects(Collection<CooperativeEffect> cooperativeEffects) {
        if (cooperativeEffects != null) {
            this.cooperativeEffects = CollectionAdaptor.convertCooperativeEffectIntoGraphModel(cooperativeEffects);
        } else {
            this.cooperativeEffects = new ArrayList<GraphCooperativeEffect>();
        }
    }

    public Collection<GraphModelledParticipant> getParticipants() {
        if (this.participants == null) {
            this.participants = new ArrayList<GraphModelledParticipant>();
        }
        return participants;
    }

    public void setParticipants(Collection<ModelledParticipant> participants) {
        if (participants != null) {
            this.participants = CollectionAdaptor.convertModelledParticipantIntoGraphModel(participants);
        } else {
            this.participants = new ArrayList<GraphModelledParticipant>();
        }
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String name) {
        this.shortName = name;
    }

    @Override
    public GraphCvTerm getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(CvTerm evidenceType) {
        if (evidenceType != null) {
            if (evidenceType instanceof GraphCvTerm) {
                this.evidenceType = (GraphCvTerm) evidenceType;
            } else {
                this.evidenceType = new GraphCvTerm(evidenceType, false);
            }
        } else {
            this.evidenceType = null;
        }
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Date updated) {
        this.updatedDate = updated;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created) {
        this.createdDate = created;
    }


    // dummy method for the interface
    public boolean removeParticipant(ModelledParticipant part) {
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
    public boolean addParticipant(ModelledParticipant part) {
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
    public boolean addAllParticipants(Collection<? extends ModelledParticipant> participants) {
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
    public boolean removeAllParticipants(Collection<? extends ModelledParticipant> participants) {
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
}
