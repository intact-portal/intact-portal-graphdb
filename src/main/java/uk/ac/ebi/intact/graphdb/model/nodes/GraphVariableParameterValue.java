package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import psidev.psi.mi.jami.model.VariableParameter;
import psidev.psi.mi.jami.model.VariableParameterValue;
import psidev.psi.mi.jami.utils.comparator.experiment.VariableParameterValueComparator;

/**
 * Created by anjali on 24/11/17.
 */
public class GraphVariableParameterValue implements VariableParameterValue {

    @GraphId
    private Long graphId;

    private String value;
    private Integer order;
    private GraphVariableParameter variableParameter;

    public GraphVariableParameterValue() {

    }


    public GraphVariableParameterValue(VariableParameterValue variableParameterValue) {
        setValue(variableParameterValue.getValue());
        setOrder(variableParameterValue.getOrder());
        setVariableParameter(variableParameterValue.getVariableParameter());
    }

/*    public GraphVariableParameterValue(String value, VariableParameter variableParameter){
        if (value == null){
            throw new IllegalArgumentException("The value of a variableParameterValue cannot be null");
        }
        this.value = value;
        this.variableParameter = variableParameter;
    }

    public GraphVariableParameterValue(String value, VariableParameter variableParameter, Integer order){
        if (value == null){
            throw new IllegalArgumentException("The value of a variableParameterValue cannot be null");
        }
        this.value = value;
        this.variableParameter = variableParameter;
        this.order = order;
    }*/

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public VariableParameter getVariableParameter() {
        return variableParameter;
    }

    public void setVariableParameter(VariableParameter variableParameter) {
        if (variableParameter != null) {
            if (variableParameter instanceof GraphVariableParameter) {
                this.variableParameter = (GraphVariableParameter) variableParameter;
            } else {
                this.variableParameter = new GraphVariableParameter(variableParameter);
            }
        } else {
            this.variableParameter = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof VariableParameterValue)) {
            return false;
        }

        return VariableParameterValueComparator.areEquals(this, (VariableParameterValue) o);
    }

    @Override
    public int hashCode() {
        return VariableParameterValueComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return (getValue() != null ? getValue().toString() : "-");
    }
}
