package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by anjali on 30/04/19.
 */
@NodeEntity
public class GraphModelledInteraction implements ModelledInteraction {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private Date updatedDate;
    private Date createdDate;
    private String rigid;
    private String shortName;

    @Relationship(type = RelationshipTypes.SOURCE)
    private GraphSource source;

    @Relationship(type = RelationshipTypes.EVIDENCE_TYPE)
    private GraphCvTerm evidenceType;

    @Relationship(type = RelationshipTypes.INTERACTION_TYPE)
    private GraphCvTerm interactionType;

    @Relationship(type = RelationshipTypes.CHECKSUMS)
    private Collection<GraphChecksum> checksums;

    @Relationship(type = RelationshipTypes.INTERACTIONS)
    private Collection<GraphInteractionEvidence> interactionEvidences;

    @Relationship(type = RelationshipTypes.CONFIDENCE)
    private Collection<GraphModelledConfidence> modelledConfidences;

    @Relationship(type = RelationshipTypes.PARAMETERS)
    private Collection<GraphModelledParameter> modelledParameters;

    @Relationship(type = RelationshipTypes.COOPERATIVE_EFFECT)
    private Collection<GraphCooperativeEffect> cooperativeEffects;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.IC_PARTICIPANT, direction = Relationship.OUTGOING)
    @JsonManagedReference
    private Collection<GraphModelledParticipant> participants;

    @Transient
    private boolean isAlreadyCreated;

    public GraphModelledInteraction() {

    }

    public GraphModelledInteraction(ModelledInteraction modelledInteraction) {
        String callingClasses = Arrays.toString(Thread.currentThread().getStackTrace());

        setShortName(modelledInteraction.getShortName());
        setRigid(modelledInteraction.getRigid());
        setUpdatedDate(modelledInteraction.getUpdatedDate());
        setCreatedDate(modelledInteraction.getCreatedDate());
        setInteractionType(modelledInteraction.getInteractionType());
        setEvidenceType(modelledInteraction.getEvidenceType());
        setSource(modelledInteraction.getSource());
        setAc(CommonUtility.extractAc(modelledInteraction));
        setUniqueKey(createUniqueKey(modelledInteraction));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }


        setChecksums(modelledInteraction.getChecksums());
        setIdentifiers(modelledInteraction.getIdentifiers());
        setXrefs(modelledInteraction.getXrefs());
        setAnnotations(modelledInteraction.getAnnotations());
        setInteractionEvidences(modelledInteraction.getInteractionEvidences());
        setModelledConfidences(modelledInteraction.getModelledConfidences());
        setModelledParameters(modelledInteraction.getModelledParameters());
        setCooperativeEffects(modelledInteraction.getCooperativeEffects());

        if (!callingClasses.contains("GraphModelledParticipant")) {
            setParticipants(modelledInteraction.getParticipants());
        }

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated()) {
                createRelationShipNatively(this.getGraphId());
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getShortName() != null) nodeProperties.put("shortName", this.getShortName());
            if (this.getRigid() != null) nodeProperties.put("rigid", this.getRigid());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.UK);
            //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            if (this.getUpdatedDate() != null)
                nodeProperties.put("updatedDate", dateFormat.format(this.getUpdatedDate()));
            if (this.getCreatedDate() != null)
                nodeProperties.put("createdDate", dateFormat.format(this.getCreatedDate()));

            Label[] labels = CommonUtility.getLabels(GraphModelledInteraction.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively(Long graphId) {
        CommonUtility.createRelationShip(interactionType, graphId, RelationshipTypes.INTERACTION_TYPE);
        CommonUtility.createRelationShip(evidenceType, graphId, RelationshipTypes.EVIDENCE_TYPE);
        CommonUtility.createRelationShip(source, graphId, RelationshipTypes.SOURCE);
        CommonUtility.createInteractionEvidenceRelationShips(interactionEvidences, graphId);
        CommonUtility.createModelledConfidenceRelationShips(modelledConfidences, graphId);
        CommonUtility.createModelledParameterRelationShips(modelledParameters, graphId);
        CommonUtility.createCooperativeEffectRelationShips(cooperativeEffects, graphId);
        CommonUtility.createChecksumRelationShips(checksums, graphId);
        CommonUtility.createIdentifierRelationShips(identifiers, graphId);
        CommonUtility.createXrefRelationShips(xrefs, graphId);
        CommonUtility.createModelledParticipantsRelationShips(participants, graphId);
        CommonUtility.createAnnotationRelationShips(annotations, graphId);
    }


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

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();

    }

    public String createUniqueKey(ModelledInteraction modelledInteraction) {
        return UniqueKeyGenerator.createModelledInteractionKey(modelledInteraction);
    }
}
