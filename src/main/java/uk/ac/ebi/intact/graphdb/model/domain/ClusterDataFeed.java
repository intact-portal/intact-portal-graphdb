package uk.ac.ebi.intact.graphdb.model.domain;

import org.springframework.data.neo4j.annotation.QueryResult;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.Interactor;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;

import java.util.Set;

/**
 * Created by anjali on 01/03/18.
 */

@QueryResult
public class ClusterDataFeed {

    private Set<GraphBinaryInteractionEvidence> binaryInteractionEvidences;
    private Interactor interactorA;
    private Interactor interactorB;

    public Interactor getInteractorA() {
        return interactorA;
    }

    public void setInteractorA(Interactor interactorA) {
        this.interactorA = interactorA;
    }

    public Interactor getInteractorB() {
        return interactorB;
    }

    public void setInteractorB(Interactor interactorB) {
        this.interactorB = interactorB;
    }

    public Set<GraphBinaryInteractionEvidence> getBinaryInteractionEvidences() {
        return binaryInteractionEvidences;
    }

    public void setBinaryInteractionEvidences(Set<GraphBinaryInteractionEvidence> binaryInteractionEvidences) {
        this.binaryInteractionEvidences = binaryInteractionEvidences;
    }
}
