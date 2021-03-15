package uk.ac.ebi.intact.graphdb.model.nodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.ParticipantEvidence;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NodeEntity
public class GraphBinaryInteractionEvidence extends GraphInteractionEvidence implements BinaryInteractionEvidence {

    private static final Log log = LogFactory.getLog(GraphBinaryInteractionEvidence.class);

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.BIE_PARTICIPANT_A, direction = Relationship.OUTGOING)
    private GraphParticipantEvidence participantA;

    @Relationship(type = RelationshipTypes.BIE_PARTICIPANT_B, direction = Relationship.OUTGOING)
    private GraphParticipantEvidence participantB;

    @Relationship(type = RelationshipTypes.INTERACTOR_A)
    private GraphInteractor interactorA;

    @Relationship(type = RelationshipTypes.INTERACTOR_B)
    private GraphInteractor interactorB;

    //TODO
    @Relationship(type = RelationshipTypes.INTERACTORS, direction = Relationship.UNDIRECTED)
    private List<GraphInteractor> interactors;

    @Relationship(type = RelationshipTypes.COMPLEX_EXPANSION)
    private GraphCvTerm complexExpansion;

    @Relationship(type = RelationshipTypes.INTERACTION_EVIDENCE, direction = Relationship.UNDIRECTED)
    private GraphInteractionEvidence interactionEvidence;

    @Relationship(type = RelationshipTypes.INTERACTIONS, direction = Relationship.UNDIRECTED)
    private GraphClusteredInteraction clusteredInteraction;

    @Transient
    private boolean isAlreadyCreated;

    public GraphBinaryInteractionEvidence() {
        super();
    }

    public GraphBinaryInteractionEvidence(BinaryInteractionEvidence binaryInteractionEvidence) {
        super(binaryInteractionEvidence, true);
        //graphInteractionEvidence=super;
        setParticipantA(binaryInteractionEvidence.getParticipantA());
        setParticipantB(binaryInteractionEvidence.getParticipantB());
        setInteractionEvidence((InteractionEvidence) binaryInteractionEvidence);

        if (binaryInteractionEvidence.getParticipantA() != null)
            setInteractorA(binaryInteractionEvidence.getParticipantA().getInteractor());
        if (binaryInteractionEvidence.getParticipantB() != null)
            setInteractorB(binaryInteractionEvidence.getParticipantB().getInteractor());
        setComplexExpansion(binaryInteractionEvidence.getComplexExpansion());
        setUniqueKey(createUniqueKey(binaryInteractionEvidence));
        initializeInteractors();
        if (CreationConfig.createNatively) {
            createNodeNatively();
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            nodeProperties.putAll(super.getNodeProperties());
            List<Label> labelList = new ArrayList<>();
            labelList.add(Label.label(GraphBinaryInteractionEvidence.class.getSimpleName()));
            labelList.add(Label.label(GraphDatabaseObject.class.getSimpleName()));
            Label[] labels = labelList.toArray(new Label[labelList.size()]);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
            if (isAlreadyCreated) {
                log.info("Binary Interaction Evidence already created with Graph Id : " + getGraphId() + " Interaction ac : " + getAc());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(participantA, this.getGraphId(), RelationshipTypes.BIE_PARTICIPANT_A);
        CommonUtility.createRelationShip(participantB, this.getGraphId(), RelationshipTypes.BIE_PARTICIPANT_B);
        CommonUtility.createRelationShip(interactorA, this.getGraphId(), RelationshipTypes.INTERACTOR_A);
        CommonUtility.createRelationShip(interactorB, this.getGraphId(), RelationshipTypes.INTERACTOR_B);
        CommonUtility.createRelationShip(complexExpansion, this.getGraphId(), RelationshipTypes.COMPLEX_EXPANSION);
        CommonUtility.createRelationShip(interactionEvidence, this.getGraphId(), RelationshipTypes.INTERACTION_EVIDENCE);
        CommonUtility.createInteractorRelationShips(interactors, this.getGraphId());
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
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
                this.complexExpansion = new GraphCvTerm(expansion, false);
            }
        } else {
            this.complexExpansion = null;
        }
    }

    public Interactor getInteractorA() {
        return interactorA;
    }

    public void setInteractorA(Interactor interactorA) {
        if (interactorA != null) {
            if (interactorA instanceof GraphInteractor) {
                this.interactorA = (GraphInteractor) interactorA;
            } else {
                this.interactorA = CommonUtility.initializeInteractor(interactorA);
            }
        } else {
            this.interactorA = null;
        }
    }

    public Interactor getInteractorB() {
        return interactorB;
    }

    public void setInteractorB(Interactor interactorB) {
        if (interactorB != null) {
            if (interactorB instanceof GraphInteractor) {
                this.interactorB = (GraphInteractor) interactorB;
            } else {
                this.interactorB = CommonUtility.initializeInteractor(interactorB);
            }
        } else {
            this.interactorB = null;
        }

    }

    public List<? extends Interactor> getInteractors() {
        return interactors;
    }

    public void setInteractors(List<GraphInteractor> interactors) {
        this.interactors = interactors;
    }

    public void initializeInteractors() {
        if (interactors == null) {
            interactors = new ArrayList<GraphInteractor>();
            if (this.getInteractorA() != null) {
                interactors.add((GraphInteractor) this.getInteractorA());
            }
            if (this.getInteractorB() != null) {
                interactors.add((GraphInteractor) this.getInteractorB());
            }
        }
    }

    public InteractionEvidence getInteractionEvidence() {
        return interactionEvidence;
    }

    public void setInteractionEvidence(InteractionEvidence interactionEvidence) {
        if (interactionEvidence != null) {
            if (interactionEvidence instanceof GraphInteractionEvidence) {
                this.interactionEvidence = (GraphInteractionEvidence) interactionEvidence;
            } else {
                this.interactionEvidence = new GraphInteractionEvidence(interactionEvidence, false);
            }
        } else {
            this.complexExpansion = null;
        }
    }

    public GraphClusteredInteraction getClusteredInteraction() {
        return clusteredInteraction;
    }

    public void setClusteredInteraction(GraphClusteredInteraction clusteredInteraction) {
        this.clusteredInteraction = clusteredInteraction;
    }

    @Override
    //TODO Needs to be implemented
    public CvTerm getCausalRegulatoryMechanism() {
        throw new UnsupportedOperationException();
    }

    @Override
    //TODO Needs to be implemented
    public void setCausalRegulatoryMechanism(CvTerm causalRegulatoryMechanism) {
        throw new UnsupportedOperationException();
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    @Override
    public String toString() {
        return (getAc() != null ? getAc() : "") + " Binary interaction: participant A=[" + (this.getParticipantA() != null ? this.getParticipantA().toString() : "") + "], participant B=[" + (this.getParticipantB() != null ? this.getParticipantB().toString() : "") + "], Complex expansion=[" + (this.getComplexExpansion() != null ? this.getComplexExpansion().toString() : "") + "]";

    }

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(BinaryInteractionEvidence binaryInteractionEvidence) {
        return UniqueKeyGenerator.createBinaryInteractionEvidenceKey(binaryInteractionEvidence);
    }

}
