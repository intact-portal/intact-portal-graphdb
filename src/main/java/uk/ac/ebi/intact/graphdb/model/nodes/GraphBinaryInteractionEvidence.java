package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Interactor;
import psidev.psi.mi.jami.model.ParticipantEvidence;

@NodeEntity
public class GraphBinaryInteractionEvidence extends GraphInteractionEvidence implements BinaryInteractionEvidence {

    @GraphId
    private Long graphId;

    @Relationship(type = "bie-participant", direction = Relationship.OUTGOING)
    private GraphParticipantEvidence participantA;

    @Relationship(type = "bie-participant", direction = Relationship.OUTGOING)
    private GraphParticipantEvidence participantB;

    private GraphInteractor interactorA;
    private GraphInteractor interactorB;

    private GraphCvTerm complexExpansion;


    public GraphBinaryInteractionEvidence(){
        super();
    }

    public GraphBinaryInteractionEvidence(BinaryInteractionEvidence binaryInteractionEvidence) {
        super(binaryInteractionEvidence);
        setParticipantA(binaryInteractionEvidence.getParticipantA());
        setParticipantB(binaryInteractionEvidence.getParticipantB());
        setInteractorA(binaryInteractionEvidence.getParticipantA().getInteractor());
        setInteractorB(binaryInteractionEvidence.getParticipantB().getInteractor());
        setComplexExpansion(binaryInteractionEvidence.getComplexExpansion());

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

    public GraphInteractor getInteractorA() {
        return interactorA;
    }

    public void setInteractorA(Interactor interactorA) {
        if (interactorA != null) {
            if (interactorA instanceof GraphInteractor) {
                this.interactorA = (GraphInteractor) interactorA;
            } else {
                this.interactorA = new GraphInteractor(interactorA);
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
                this.interactorB = new GraphInteractor(interactorB);
            }
        } else {
            this.interactorB = null;
        }

    }


    @Override
    public String toString() {
        return "Interaction: " + (getShortName() != null ? getShortName() + ", " : "") + (getInteractionType() != null ? getInteractionType().toString() : "");
    }
}
