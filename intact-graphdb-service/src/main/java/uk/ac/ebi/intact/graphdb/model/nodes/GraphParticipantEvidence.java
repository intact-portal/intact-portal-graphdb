package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.*;


/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphParticipantEvidence extends GraphExperimentalEntity implements ParticipantEvidence {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;

    @Relationship(type = RelationshipTypes.EXPERIMENTAL_ROLE)
    private GraphCvTerm experimentalRole;

    @Relationship(type = RelationshipTypes.BIOLOGICAL_ROLE)
    private GraphCvTerm biologicalRole;

    @Relationship(type = RelationshipTypes.EXPRESSED_IN)
    private GraphOrganism expressedInOrganism;

    @Relationship(type = RelationshipTypes.IE_PARTICIPANT, direction = Relationship.INCOMING)
    @JsonBackReference
    private GraphInteractionEvidence interaction;

    @Relationship(type = RelationshipTypes.BIE_PARTICIPANT_A, direction = Relationship.INCOMING)
    @JsonBackReference
    private GraphBinaryInteractionEvidence binaryInteractionEvidenceA;

    @Relationship(type = RelationshipTypes.BIE_PARTICIPANT_B, direction = Relationship.INCOMING)
    @JsonBackReference
    private GraphBinaryInteractionEvidence binaryInteractionEvidenceB;

    @Relationship(type = RelationshipTypes.CONFIDENCE)
    private Collection<GraphConfidence> confidences;

    @Relationship(type = RelationshipTypes.PARAMETERS)
    private Collection<GraphParameter> parameters;

    @Relationship(type = RelationshipTypes.IDENTIFICATION_METHOD)
    private Collection<GraphCvTerm> identificationMethods;

    @Relationship(type = RelationshipTypes.EXPERIMENTAL_PREPARATION)
    private Collection<GraphCvTerm> experimentalPreparations;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    @Transient
    private boolean isAlreadyCreated;

    public GraphParticipantEvidence() {
    }

    public GraphParticipantEvidence(ParticipantEvidence participantEvidence) {
        super(participantEvidence, true);
        String callingClasses = Arrays.toString(Thread.currentThread().getStackTrace());
        setExperimentalRole(participantEvidence.getExperimentalRole());
        setBiologicalRole(participantEvidence.getBiologicalRole());
        setExpressedInOrganism(participantEvidence.getExpressedInOrganism());

        if (!callingClasses.contains("GraphInteractionEvidence") && !callingClasses.contains("GraphBinaryInteractionEvidence")) {
            setInteraction(participantEvidence.getInteraction());
        }
        /*if (!callingClasses.contains("GraphBinaryInteractionEvidence")) {
            setBinaryInteractionEvidence(participantEvidence.getInteraction());
        }*/
        setAc(CommonUtility.extractAc(participantEvidence));
        setUniqueKey(createUniqueKey(participantEvidence));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setConfidences(participantEvidence.getConfidences());
        setParameters(participantEvidence.getParameters());
        setIdentificationMethods(participantEvidence.getIdentificationMethods());
        setExperimentalPreparations(participantEvidence.getExperimentalPreparations());
        setXrefs(participantEvidence.getXrefs());
        setAnnotations(participantEvidence.getAnnotations());
        setAliases(participantEvidence.getAliases());

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());

            Label[] labels = CommonUtility.getLabels(GraphParticipantEvidence.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(experimentalRole, this.getGraphId(), RelationshipTypes.EXPERIMENTAL_ROLE);
        CommonUtility.createRelationShip(biologicalRole, this.getGraphId(), RelationshipTypes.BIOLOGICAL_ROLE);
        CommonUtility.createRelationShip(expressedInOrganism, this.getGraphId(), RelationshipTypes.EXPRESSED_IN);
        CommonUtility.createIncomingRelationShip(interaction, this.getGraphId(), RelationshipTypes.IE_PARTICIPANT);
        //CommonUtility.createRelationShip(binaryInteractionEvidence, this.graphId, RelationshipTypes.BIE_PARTICIPANT);
        CommonUtility.createConfidenceRelationShips(confidences, this.getGraphId());
        CommonUtility.createParameterRelationShips(parameters, this.getGraphId());
        CommonUtility.createIdentificationMethodRelationShips(identificationMethods, this.getGraphId());
        CommonUtility.createExperimentalPreparationRelationShips(experimentalPreparations, this.getGraphId());
        CommonUtility.createXrefRelationShips(xrefs, this.getGraphId());
        CommonUtility.createAnnotationRelationShips(annotations, this.getGraphId());
        CommonUtility.createAliasRelationShips(aliases, this.getGraphId());
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
                this.experimentalRole = new GraphCvTerm(experimentalRole, false);
            }
        } else {
            this.experimentalRole = new GraphCvTerm(CvTermUtils.createUnspecifiedRole(), false);
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
                this.biologicalRole = new GraphCvTerm(biologicalRole, false);
            }
        } else {
            this.biologicalRole = new GraphCvTerm(CvTermUtils.createUnspecifiedRole(), false);
        }
        //TODO login it
    }

    @Override
    public Organism getExpressedInOrganism() {
        return this.expressedInOrganism;
    }

    @Override
    public void setExpressedInOrganism(Organism organism) {
        if (organism != null) {
            if (organism instanceof GraphOrganism) {
                this.expressedInOrganism = (GraphOrganism) organism;
            } else {
                this.expressedInOrganism = new GraphOrganism(organism);
            }
        } else {
            this.expressedInOrganism = null;
        }
        //TODO login it
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
                this.interaction = new GraphInteractionEvidence(interaction, false);
            }
        } else {
            this.interaction = null;
        }
        //TODO login it
    }

    public GraphBinaryInteractionEvidence getBinaryInteractionEvidenceA() {
        return binaryInteractionEvidenceA;
    }

    public void setBinaryInteractionEvidenceA(GraphBinaryInteractionEvidence binaryInteractionEvidenceA) {
        if (binaryInteractionEvidenceA != null) {
            if (binaryInteractionEvidenceA instanceof GraphBinaryInteractionEvidence) {
                this.binaryInteractionEvidenceA = (GraphBinaryInteractionEvidence) binaryInteractionEvidenceA;
            } else {
                this.binaryInteractionEvidenceA = new GraphBinaryInteractionEvidence((BinaryInteractionEvidence) binaryInteractionEvidenceA);
            }
        } else {
            this.binaryInteractionEvidenceA = null;
        }
    }

    public GraphBinaryInteractionEvidence getBinaryInteractionEvidenceB() {
        return binaryInteractionEvidenceB;
    }

    public void setBinaryInteractionEvidenceB(GraphBinaryInteractionEvidence binaryInteractionEvidenceB) {
        if (binaryInteractionEvidenceB != null) {
            if (binaryInteractionEvidenceB instanceof GraphBinaryInteractionEvidence) {
                this.binaryInteractionEvidenceB = (GraphBinaryInteractionEvidence) binaryInteractionEvidenceB;
            } else {
                this.binaryInteractionEvidenceB = new GraphBinaryInteractionEvidence((BinaryInteractionEvidence) binaryInteractionEvidenceB);
            }
        } else {
            this.binaryInteractionEvidenceB = null;
        }
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

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    @Override
    //TODO Needs to be implemented
    public CvTerm getBiologicalEffect() {
        throw new UnsupportedOperationException();
    }

    @Override
    //TODO Needs to be implemented
    public void setBiologicalEffect(CvTerm biologicalEffect) {
        throw new UnsupportedOperationException();
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String toString() {
        return "Participant: " + this.getInteractor().toString() + (this.getStoichiometry() != null ? ", stoichiometry: " + this.getStoichiometry().toString() : "") + (this.getExperimentalRole() != null ? ", " + this.getExperimentalRole().toString() : "") + (this.getExpressedInOrganism() != null ? ", " + this.getExpressedInOrganism().toString() : "");
    }

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(ParticipantEvidence participantEvidence) {
        return UniqueKeyGenerator.createParticipantEvidenceKey(participantEvidence);
    }

}
