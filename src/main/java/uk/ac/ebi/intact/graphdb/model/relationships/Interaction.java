package uk.ac.ebi.intact.graphdb.model.relationships;

import org.neo4j.ogm.annotation.*;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.Interactor;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 05/05/2014
 * Time: 11:02
 */
@RelationshipEntity(type = RelationshipTypes.INTERACTS_IN)
public class Interaction {

    @GraphId
    private Long id;

    @StartNode
    private Interactor interactorA;

    @EndNode
    private Interactor interactorB;

    private Double score;

    @Relationship(direction = Relationship.UNDIRECTED, type = RelationshipTypes.INTERACTS_IN)
    private Set<Interactor> interators;

    //    @RelatedTo(type = "PUBLISH_IN", direction = Direction.INCOMING)
    private Set<GraphCvTerm> publications;

    private Set<GraphCvTerm> detectionMethods;

    private Set<GraphCvTerm> interactionTypes;

    public Interaction(Interactor interactorA, Interactor interactorB) {
        this.interactorA = interactorA;
        this.interactorB = interactorB;
    }

    public Interaction(Interactor interactorA, Interactor interactorB, Double score) {
   		this(interactorA, interactorB, score, null, null, null);
   	}

   	/* TODO: Decouple with the Binary Interaction */
   	public Interaction(Interactor interactorA, Interactor interactorB, Double score,
                       Set<GraphCvTerm> publications,
                       Set<GraphCvTerm> detectionMethods,
                       Set<GraphCvTerm> interactionTypes) {
        this.interactorA = interactorA;
        this.interactorB = interactorB;
   		this.score = score;
   		setPublications(publications);
   		setDetectionMethods(detectionMethods);
   		setInteractionTypes(interactionTypes);
   	}

    public Interaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Set<GraphCvTerm> getPublications() {
        return publications;
    }

    public void setPublications(Set<GraphCvTerm> publications) {
        this.publications = publications;
    }

    public Set<GraphCvTerm> getDetectionMethods() {
        return detectionMethods;
    }

    public void setDetectionMethods(Set<GraphCvTerm> detectionMethods) {
        this.detectionMethods = detectionMethods;
    }

    public Set<GraphCvTerm> getInteractionTypes() {
        return interactionTypes;
    }

    public void setInteractionTypes(Set<GraphCvTerm> interactionTypes) {
        this.interactionTypes = interactionTypes;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +
                ", interactorA=" + interactorA.getAccession() +
                ", interactorB=" + interactorB.getAccession() +
                ", score=" + score +
                '}';
    }
}
