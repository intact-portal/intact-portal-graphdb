package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.ResultingSequence;

@NodeEntity
public class GraphResultingSequence implements ResultingSequence {

    @GraphId
    protected Long graphId;

    public GraphResultingSequence(ResultingSequence resultingSequence) {

    }
}
