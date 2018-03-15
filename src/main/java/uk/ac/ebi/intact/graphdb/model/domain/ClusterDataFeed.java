package uk.ac.ebi.intact.graphdb.model.domain;

import org.neo4j.ogm.annotation.NodeEntity;
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

    private Set<GraphBinaryInteractionEvidence> interactions;
    private Interactor interactorA;
    private Interactor interactorB;
    private double miscore;

    public ClusterDataFeed(){

    }

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


    public Set<GraphBinaryInteractionEvidence> getInteractions() {
        return interactions;
    }

    public void setInteractions(Set<GraphBinaryInteractionEvidence> interactions) {
        this.interactions = interactions;
    }


    public double getMiscore() {
        return miscore;
    }

    public void setMiscore(double miscore) {
        this.miscore = miscore;
    }
}
