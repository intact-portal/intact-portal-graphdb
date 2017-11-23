package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.VariableParameterValueSet;
import psidev.psi.mi.jami.model.impl.DefaultVariableParameterValueSet;

@NodeEntity
public class GraphVariableParameterValueSet extends DefaultVariableParameterValueSet {

    @GraphId
    protected Long id;

    public GraphVariableParameterValueSet(VariableParameterValueSet variableParameterValueSet){

    }
}
