package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Source;

@NodeEntity
public class GraphSource implements Source {

    @GraphId
    protected Long graphId;

    public GraphSource() {
    }
}
