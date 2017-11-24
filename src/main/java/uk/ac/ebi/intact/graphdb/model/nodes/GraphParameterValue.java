package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.ParameterValue;

import java.math.BigDecimal;

@NodeEntity
public class GraphParameterValue extends ParameterValue {

    @GraphId
    protected Long graphId;

    public GraphParameterValue(BigDecimal factor, short base, short exponent) {
        super(factor, base, exponent);
    }
}
