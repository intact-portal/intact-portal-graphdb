package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.ExperimentalEntity;

@NodeEntity
public class GraphExperimentalEntity extends GraphEntity {

    @GraphId
    private Long graphId;

    public GraphExperimentalEntity() {
        super();
    }

    public GraphExperimentalEntity(ExperimentalEntity experimentalEntity) {
        super(experimentalEntity);
    }

}
