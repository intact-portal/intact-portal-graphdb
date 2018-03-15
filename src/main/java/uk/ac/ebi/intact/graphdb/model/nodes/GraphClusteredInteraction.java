package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Interactor;

import java.util.Set;

/**
 * Created by anjali on 06/03/18.
 */
@NodeEntity
public class GraphClusteredInteraction {

    @GraphId
    private Long graphId;


    private Set<GraphBinaryInteractionEvidence> interactions;
    private Interactor interactorPA;
    private Interactor interactorPB;
    private double miscore;

    public GraphClusteredInteraction(){

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

    public Interactor getInteractorPA() {
        return interactorPA;
    }

    public void setInteractorPA(Interactor interactorPA) {
        this.interactorPA = interactorPA;
    }

    public Interactor getInteractorPB() {
        return interactorPB;
    }

    public void setInteractorPB(Interactor interactorPB) {
        this.interactorPB = interactorPB;
    }
}
