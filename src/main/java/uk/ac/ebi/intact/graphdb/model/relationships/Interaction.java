package uk.ac.ebi.intact.graphdb.model.relationships;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import uk.ac.ebi.intact.graphdb.model.nodes.CvParam;
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

    //    @RelatedTo(type = "PUBLISH_IN", direction = Direction.INCOMING)
    private Set<CvParam> publications;

    private Set<CvParam> detectionMethods;

    private Set<CvParam> interactionTypes;

    public Interaction(Interactor interactorA, Interactor interactorB) {
        this.interactorA = interactorA;
        this.interactorB = interactorB;
    }

    public Interaction(Interactor interactorA, Interactor interactorB, Double score) {
   		this(interactorA, interactorB, score, null, null, null);
   	}

   	/* TODO: Decouple with the Binary Interaction */
   	public Interaction(Interactor interactorA, Interactor interactorB, Double score,
                       Set<CvParam> publications,
                       Set<CvParam> detectionMethods,
                       Set<CvParam> interactionTypes) {
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

    public Set<CvParam> getPublications() {
        return publications;
    }

    public void setPublications(Set<CvParam> publications) {
        this.publications = publications;
    }

    public Set<CvParam> getDetectionMethods() {
        return detectionMethods;
    }

    public void setDetectionMethods(Set<CvParam> detectionMethods) {
        this.detectionMethods = detectionMethods;
    }

    public Set<CvParam> getInteractionTypes() {
        return interactionTypes;
    }

    public void setInteractionTypes(Set<CvParam> interactionTypes) {
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
