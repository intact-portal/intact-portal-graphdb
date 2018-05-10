package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.Constants;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.util.*;


/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphParticipantEvidence implements ParticipantEvidence {

    @GraphId
    private Long graphId;

    @Index(unique = true,primary = true)
    private String uniqueKey;

    private GraphCvTerm experimentalRole;
    private GraphCvTerm biologicalRole;
    private GraphOrganism expressedIn;
    private GraphStoichiometry stoichiometry;
    private GraphInteractor interactor;

    @Relationship(type = RelationshipTypes.IE_PARTICIPANT, direction = Relationship.INCOMING)
    private GraphInteractionEvidence interaction;

    @Relationship(type = RelationshipTypes.BIE_PARTICIPANT, direction = Relationship.INCOMING)
    private GraphBinaryInteractionEvidence binaryInteractionEvidence;
    private EntityInteractorChangeListener changeListener;

    private Collection<GraphFeatureEvidence> features;
    private Collection<GraphConfidence> confidences;
    private Collection<GraphParameter> parameters;
    private Collection<GraphCvTerm> identificationMethods;
    private Collection<GraphCvTerm> experimentalPreparations;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private Collection<GraphAlias> aliases;
    private Collection<GraphCausalRelationship> causalRelationships;

    @Transient
    private boolean isAlreadyCreated;



    public GraphParticipantEvidence() {
    }

    public GraphParticipantEvidence(ParticipantEvidence participantEvidence) {
        String callingClasses = Arrays.toString(Thread.currentThread().getStackTrace());

        setExperimentalRole(participantEvidence.getExperimentalRole());
        setBiologicalRole(participantEvidence.getBiologicalRole());
        setExpressedInOrganism(participantEvidence.getExpressedInOrganism());
        setStoichiometry(participantEvidence.getStoichiometry());
        setInteractor(participantEvidence.getInteractor());

        if (!callingClasses.contains("GraphInteractionEvidence") && !callingClasses.contains("GraphBinaryInteractionEvidence")) {
            setInteraction(participantEvidence.getInteraction());
        }
        if (!callingClasses.contains("GraphBinaryInteractionEvidence")) {
            setBinaryInteractionEvidence(participantEvidence.getInteraction());
        }
        setChangeListener(participantEvidence.getChangeListener());
        setUniqueKey(this.toString());

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setFeatures(participantEvidence.getFeatures());
        setConfidences(participantEvidence.getConfidences());
        setParameters(participantEvidence.getParameters());
        setIdentificationMethods(participantEvidence.getIdentificationMethods());
        setExperimentalPreparations(participantEvidence.getExperimentalPreparations());
        setXrefs(participantEvidence.getXrefs());
        setAnnotations(participantEvidence.getAnnotations());
        setAliases(participantEvidence.getAliases());
        setCausalRelationships(participantEvidence.getCausalRelationships());

        if (CreationConfig.createNatively) {
            if(!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());

            Label[] labels = CommonUtility.getLabels(GraphParticipantEvidence.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(experimentalRole, this.graphId, "experimentalRole");
        CommonUtility.createRelationShip(biologicalRole, this.graphId, "biologicalRole");
        CommonUtility.createRelationShip(expressedIn, this.graphId, "expressedIn");
        CommonUtility.createRelationShip(stoichiometry, this.graphId, "stoichiometry");
        CommonUtility.createRelationShip(interactor, this.graphId, "interactor");
        CommonUtility.createRelationShip(interaction, this.graphId, RelationshipTypes.IE_PARTICIPANT);
        CommonUtility.createRelationShip(binaryInteractionEvidence, this.graphId, RelationshipTypes.BIE_PARTICIPANT);
        CommonUtility.createRelationShip(changeListener, this.graphId, "changeListener");
        CommonUtility.createFeatureEvidenceRelationShips(features, this.graphId, "features");
        CommonUtility.createConfidenceRelationShips(confidences, this.graphId, "confidences");
        CommonUtility.createParameterRelationShips(parameters, this.graphId, "parameters");
        CommonUtility.createCvTermRelationShips(identificationMethods, this.graphId, "identificationMethods");
        CommonUtility.createCvTermRelationShips(experimentalPreparations, this.graphId, "experimentalPreparations");
        CommonUtility.createXrefRelationShips(xrefs, this.graphId, "xrefs");
        CommonUtility.createAnnotationRelationShips(annotations, this.graphId, "annotations");
        CommonUtility.createAliasRelationShips(aliases, this.graphId, "aliases");
        CommonUtility.createCausalRelationshipRelationShips(causalRelationships, this.graphId, "causalRelationships");

    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @Override
    public CvTerm getExperimentalRole() {
        return this.experimentalRole;
    }

    @Override
    public void setExperimentalRole(CvTerm experimentalRole) {
        if (experimentalRole != null) {
            if (experimentalRole instanceof GraphCvTerm) {
                this.experimentalRole = (GraphCvTerm) experimentalRole;
            } else {
                this.experimentalRole = new GraphCvTerm(experimentalRole,false);
            }
        } else {
            this.experimentalRole = new GraphCvTerm(CvTermUtils.createUnspecifiedRole(),false);
        }
    }

    @Override
    public CvTerm getBiologicalRole() {
        return this.biologicalRole;
    }

    @Override
    public void setBiologicalRole(CvTerm biologicalRole) {
        if (biologicalRole != null) {
            if (biologicalRole instanceof GraphCvTerm) {
                this.biologicalRole = (GraphCvTerm) biologicalRole;
            } else {
                this.biologicalRole = new GraphCvTerm(biologicalRole,false);
            }
        } else {
            this.biologicalRole = new GraphCvTerm(CvTermUtils.createUnspecifiedRole(),false);
        }
        //TODO login it
    }

    @Override
    public Organism getExpressedInOrganism() {
        return this.expressedIn;
    }

    @Override
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

    @Override
    public Stoichiometry getStoichiometry() {
        return this.stoichiometry;
    }

    @Override
    public void setStoichiometry(Stoichiometry stoichiometry) {
        if (stoichiometry != null) {
            if (stoichiometry instanceof GraphStoichiometry) {
                this.stoichiometry = (GraphStoichiometry) stoichiometry;
            } else {
                this.stoichiometry = new GraphStoichiometry(stoichiometry);
            }
        } else {
            this.stoichiometry = null;
        }
        //TODO login it
    }

    @Override
    public void setStoichiometry(Integer stoichiometry) {
        if (stoichiometry != null) {
            this.stoichiometry = new GraphStoichiometry(stoichiometry);

        } else {
            this.stoichiometry = null;
        }
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
            this.interactor = new GraphInteractor(interactor,false);
        }
        if (this.changeListener != null) {
            this.changeListener.onInteractorUpdate(this, oldInteractor);
        }
    }

    @Override
    public void setInteractionAndAddParticipant(InteractionEvidence interaction) {

        if (this.interaction != null) {
            this.interaction.removeParticipant(this);
        }

        if (interaction != null) {
            interaction.addParticipant(this);
        }
    }

    @Override
    public InteractionEvidence getInteraction() {
        return this.interaction;
    }

    @Override
    public void setInteraction(InteractionEvidence interaction) {
        if (interaction != null) {
            if (interaction instanceof GraphBinaryInteractionEvidence) {
                this.interaction = (GraphBinaryInteractionEvidence) interaction;
            } else if (interaction instanceof GraphInteractionEvidence) {
                this.interaction = (GraphInteractionEvidence) interaction;
            } else if (interaction instanceof BinaryInteractionEvidence) {
                this.interaction = new GraphBinaryInteractionEvidence((BinaryInteractionEvidence) interaction);
            } else {
                this.interaction = new GraphInteractionEvidence(interaction,false);
            }
        } else {
            this.interaction = null;
        }
        //TODO login it
    }

    public GraphBinaryInteractionEvidence getBinaryInteractionEvidence() {
        return binaryInteractionEvidence;
    }

    public void setBinaryInteractionEvidence(InteractionEvidence binaryInteractionEvidence) {
        if (binaryInteractionEvidence != null) {
            if (binaryInteractionEvidence instanceof GraphBinaryInteractionEvidence) {
                this.binaryInteractionEvidence = (GraphBinaryInteractionEvidence) binaryInteractionEvidence;
            } else {
                this.binaryInteractionEvidence = new GraphBinaryInteractionEvidence((BinaryInteractionEvidence) binaryInteractionEvidence);
            }
        } else {
            this.binaryInteractionEvidence = null;
        }
    }

    @Override
    public Collection<GraphFeatureEvidence> getFeatures() {
        if (features == null) {
            features = new ArrayList<GraphFeatureEvidence>();
        }
        return this.features;
    }

    public void setFeatures(Collection<FeatureEvidence> features) {
        if (features != null) {
            addAllFeatures(features);
        } else {
            this.features = new ArrayList<GraphFeatureEvidence>();
        }
    }

    @Override
    public boolean addFeature(FeatureEvidence feature) {
        if (feature == null) {
            return false;
        }

        if (getFeatures().add(new GraphFeatureEvidence(feature))) {
            feature.setParticipant(this);
            return true;
        }
        return false;
    }

    //Todo review
    @Override
    public boolean removeFeature(FeatureEvidence feature) {
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
    public boolean addAllFeatures(Collection<? extends FeatureEvidence> features) {
        if (features == null) {
            return false;
        }

        boolean added = false;
        for (FeatureEvidence feature : features) {
            if (addFeature(feature)) {
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean removeAllFeatures(Collection<? extends FeatureEvidence> features) {
        if (features == null) {
            return false;
        }

        boolean added = false;
        for (FeatureEvidence feature : features) {
            if (removeFeature(feature)) {
                added = true;
            }
        }
        return added;
    }

    @Override
    public Collection<GraphConfidence> getConfidences() {
        if (this.confidences == null) {
            this.confidences = new ArrayList<GraphConfidence>();
        }
        return this.confidences;
    }

    public void setConfidences(Collection<Confidence> confidences) {
        if (confidences != null) {
            this.confidences = CollectionAdaptor.convertConfidenceIntoGraphModel(confidences);
        } else {
            this.confidences = new ArrayList<GraphConfidence>();
        }
    }

    @Override
    public Collection<GraphParameter> getParameters() {
        if (this.parameters == null) {
            this.parameters = new ArrayList<GraphParameter>();
        }
        return this.parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        if (parameters != null) {
            this.parameters = CollectionAdaptor.convertParameterIntoGraphModel(parameters);
        } else {
            this.parameters = new ArrayList<GraphParameter>();
        }
    }

    @Override
    public Collection<GraphCvTerm> getIdentificationMethods() {
        if (identificationMethods == null) {
            this.identificationMethods = new ArrayList<GraphCvTerm>();
        }
        return this.identificationMethods;
    }

    public void setIdentificationMethods(Collection<CvTerm> identificationMethods) {
        if (identificationMethods != null) {
            this.identificationMethods = CollectionAdaptor.convertCvTermIntoGraphModel(identificationMethods);
        } else {
            this.identificationMethods = new ArrayList<GraphCvTerm>();
        }
    }

    @Override
    public Collection<GraphCvTerm> getExperimentalPreparations() {
        if (experimentalPreparations == null) {
            this.experimentalPreparations = new ArrayList<GraphCvTerm>();
        }
        return this.experimentalPreparations;
    }

    public void setExperimentalPreparations(Collection<CvTerm> experimentalPreparations) {
        if (experimentalPreparations != null) {
            this.experimentalPreparations = CollectionAdaptor.convertCvTermIntoGraphModel(experimentalPreparations);
        } else {
            this.experimentalPreparations = new ArrayList<GraphCvTerm>();
        }
    }

    @Override
    public Collection<GraphXref> getXrefs() {
        if (xrefs == null) {
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

    @Override
    public Collection<GraphAnnotation> getAnnotations() {
        if (annotations == null) {
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

    @Override
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
    public Collection<GraphCausalRelationship> getCausalRelationships() {
        if (this.causalRelationships == null) {
            this.causalRelationships = new ArrayList<GraphCausalRelationship>();
        }
        return this.causalRelationships;
    }

    public void setCausalRelationships(Collection<CausalRelationship> causalRelationships) {
        if (causalRelationships != null) {
            this.causalRelationships = CollectionAdaptor.convertCausalRelationshipIntoGraphModel(causalRelationships);
        } else {
            this.causalRelationships = new ArrayList<GraphCausalRelationship>();
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

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String toString() {
        return "Participant: " + this.getInteractor().toString() + (this.getStoichiometry() != null?", stoichiometry: " + this.getStoichiometry().toString():"") + (this.getExperimentalRole() != null?", " + this.getExperimentalRole().toString():"") + (this.getExpressedInOrganism() != null?", " + this.getExpressedInOrganism().toString():"");
    }
}
