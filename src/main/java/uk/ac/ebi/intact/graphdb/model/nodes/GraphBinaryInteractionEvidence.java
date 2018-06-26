package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.ParticipantEvidence;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphBinaryInteractionEvidence extends GraphInteractionEvidence implements BinaryInteractionEvidence {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.BIE_PARTICIPANT, direction = Relationship.OUTGOING)
    private GraphParticipantEvidence participantA;

    @Relationship(type = RelationshipTypes.BIE_PARTICIPANT, direction = Relationship.OUTGOING)
    private GraphParticipantEvidence participantB;

    private GraphInteractor interactorA;
    private GraphInteractor interactorB;

    private GraphCvTerm complexExpansion;

    private GraphInteractionEvidence graphInteractionEvidence;

    @Transient
    private boolean isAlreadyCreated;

    public GraphBinaryInteractionEvidence() {
        super();
    }

    public GraphBinaryInteractionEvidence(BinaryInteractionEvidence binaryInteractionEvidence) {
        super(binaryInteractionEvidence,true);
        //graphInteractionEvidence=super;
        setParticipantA(binaryInteractionEvidence.getParticipantA());
        setParticipantB(binaryInteractionEvidence.getParticipantB());
        if(binaryInteractionEvidence.getParticipantA()!=null)setInteractorA(binaryInteractionEvidence.getParticipantA().getInteractor());
        if(binaryInteractionEvidence.getParticipantB()!=null)setInteractorB(binaryInteractionEvidence.getParticipantB().getInteractor());
        setComplexExpansion(binaryInteractionEvidence.getComplexExpansion());
        setUniqueKey(createUniqueKey());

        if (CreationConfig.createNatively) {
            createNodeNatively();
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
            nodeProperties.putAll(super.getNodeProperties());
            Label[] labels = CommonUtility.getLabels(GraphBinaryInteractionEvidence.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(participantA, this.getGraphId(), RelationshipTypes.BIE_PARTICIPANT);
        CommonUtility.createRelationShip(participantB, this.getGraphId(), RelationshipTypes.BIE_PARTICIPANT);
        CommonUtility.createRelationShip(interactorA, this.getGraphId(), "interactorA");
        CommonUtility.createRelationShip(interactorB, this.getGraphId(), "interactorB");
        CommonUtility.createRelationShip(complexExpansion, this.getGraphId(), "complexExpansion");
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
                this.complexExpansion = new GraphCvTerm(expansion,false);
            }
        } else {
            this.complexExpansion = null;
        }
    }

    public GraphInteractor getInteractorA() {
        return interactorA;
    }

    public void setInteractorA(Interactor interactorA) {
        if (interactorA != null) {
            if (interactorA instanceof GraphInteractor) {
                this.interactorA = (GraphInteractor) interactorA;
            } else {
                this.interactorA = new GraphInteractor(interactorA,false);
            }
        } else {
            this.interactorA = null;
        }
    }

    public GraphInteractor getInteractorB() {
        return interactorB;
    }

    public void setInteractorB(Interactor interactorB) {
        if (interactorB != null) {
            if (interactorB instanceof GraphInteractor) {
                this.interactorB = (GraphInteractor) interactorB;
            } else {
                this.interactorB = new GraphInteractor(interactorB,false);
            }
        } else {
            this.interactorB = null;
        }

    }


    public GraphInteractionEvidence getGraphInteractionEvidence() {
        return graphInteractionEvidence;
    }

    public void setGraphInteractionEvidence(GraphInteractionEvidence graphInteractionEvidence) {
        this.graphInteractionEvidence = graphInteractionEvidence;
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

    @Override
    public String toString() {
        return (getAc() != null ? getAc() : "") + " Binary interaction: participant A=[" + (this.getParticipantA() != null ? this.getParticipantA().toString() : "") + "], participant B=[" + (this.getParticipantB() != null ? this.getParticipantB().toString() : "") + "], Complex expansion=[" + (this.getComplexExpansion() != null ? this.getComplexExpansion().toString() : "") + "]";

    }

    public String createUniqueKey(){
        String uniqueString=(getAc() != null ? getAc() : getIdentifiers().toString()) + " Binary interaction: participant A=[" + (this.getParticipantA() != null ? this.getParticipantA().toString() : "") + "], participant B=[" + (this.getParticipantB() != null ? this.getParticipantB().toString() : "") + "], Complex expansion=[" + (this.getComplexExpansion() != null ? this.getComplexExpansion().toString() : "") + "]";
        BigInteger bi = new BigInteger(uniqueString.toLowerCase().getBytes());
        return bi.toString();
    }


}
