package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.*;

@NodeEntity
public class GraphCausalRelationship implements CausalRelationship {

    @GraphId
    private Long graphId;

    private GraphCvTerm relationType;
    private GraphEntity target;

    public GraphCausalRelationship() {
    }

    public GraphCausalRelationship(CausalRelationship causalRelationship) {
        setRelationType(causalRelationship.getRelationType());
        setTarget(causalRelationship.getTarget());
    }

    public GraphCausalRelationship(CvTerm relationType, Participant target) {
        if (relationType == null) {
            throw new IllegalArgumentException("The relationType in a CausalRelationship cannot be null");
        }
        setRelationType(relationType);

        if (target == null) {
            throw new IllegalArgumentException("The participat target in a CausalRelationship cannot be null");
        }
        setTarget(target);
    }

    public CvTerm getRelationType() {
        return relationType;
    }

    public void setRelationType(CvTerm relationType) {
        if (relationType != null) {
            if (relationType instanceof GraphCvTerm) {
                this.relationType = (GraphCvTerm) relationType;
            } else {
                this.relationType = new GraphCvTerm(relationType);
            }
        } else {
            this.relationType = null;
        }
        //TODO login it
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        if (target != null) {
            if (target instanceof GraphEntity) {
                this.target = (GraphEntity) target;
            } else {
                this.target = new GraphEntity(target);
            }
        } else {
            this.target = null;
        }
    }

    @Override
    public String toString() {
        return getRelationType().toString() + ": " + getTarget().toString();
    }

}
