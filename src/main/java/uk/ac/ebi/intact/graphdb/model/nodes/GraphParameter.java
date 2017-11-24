package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.exception.IllegalParameterException;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Parameter;
import psidev.psi.mi.jami.model.ParameterValue;
import psidev.psi.mi.jami.utils.ParameterUtils;
import psidev.psi.mi.jami.utils.comparator.parameter.UnambiguousParameterComparator;

import java.math.BigDecimal;

@NodeEntity
public class GraphParameter implements Parameter {

    @GraphId
    protected Long graphId;

    private GraphCvTerm type;
    private BigDecimal uncertainty;
    private GraphCvTerm unit;
    private GraphParameterValue value;

    public GraphParameter() {
    }

    public GraphParameter(Parameter parameter) {
        setType(parameter.getType());
        setUncertainty(parameter.getUncertainty());
        setUnit(parameter.getUnit());
        setValue(parameter.getValue());
    }

    public GraphParameter(CvTerm type, ParameterValue value) {
        if (type == null) {
            throw new IllegalArgumentException("The parameter type is required and cannot be null");
        }
        setType(type);
        if (value == null) {
            throw new IllegalArgumentException("The parameter value is required and cannot be null");
        }
        setValue(value);
    }

    public GraphParameter(CvTerm type, ParameterValue value, CvTerm unit) {
        this(type, value);
        setUnit(unit);
    }

    public GraphParameter(CvTerm type, ParameterValue value, CvTerm unit, BigDecimal uncertainty) {
        this(type, value, unit);
        setUncertainty(uncertainty);
    }

    public GraphParameter(CvTerm type, ParameterValue value, BigDecimal uncertainty) {
        this(type, value);
        setUncertainty(uncertainty);
    }

    public GraphParameter(CvTerm type, String value) throws IllegalParameterException {
        if (type == null) {
            throw new IllegalArgumentException("The parameter type is required and cannot be null");
        }
        setType(type);

        Parameter param = ParameterUtils.createParameterFromString(type, value);
        setValue(param.getValue());
        setUncertainty(param.getUncertainty());
    }

    public GraphParameter(CvTerm type, String value, CvTerm unit) throws IllegalParameterException {
        this(type, value);
        setUnit(unit);
    }

    public CvTerm getType() {
        return this.type;
    }

    public void setType(CvTerm type) {
        if (type != null) {
            if (type instanceof GraphCvTerm) {
                this.type = (GraphCvTerm) type;
            } else {
                this.type = new GraphCvTerm(type);
            }
        } else {
            this.type = null;
        }
        //TODO login it
    }

    public BigDecimal getUncertainty() {
        return this.uncertainty;
    }

    public void setUncertainty(BigDecimal uncertainty) {
        this.uncertainty = uncertainty;
    }

    public CvTerm getUnit() {
        return this.unit;
    }

    public void setUnit(CvTerm unit) {
        if (unit != null) {
            if (unit instanceof GraphCvTerm) {
                this.unit = (GraphCvTerm) unit;
            } else {
                this.unit = new GraphCvTerm(unit);
            }
        } else {
            this.unit = null;
        }
        //TODO login it
    }

    public ParameterValue getValue() {
        return this.value;
    }

    //TODO Review this setter with calm
    public void setValue(ParameterValue value) {
        if (value != null) {
            if (value instanceof GraphParameterValue) {
                this.value = (GraphParameterValue) value;
            } else {
                this.value = new GraphParameterValue(value.getFactor(), value.getBase(), value.getExponent());
            }
        } else {
            this.value = null;
        }
        //TODO login it
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Parameter)) {
            return false;
        }

        return UnambiguousParameterComparator.areEquals(this, (Parameter) o);
    }

    @Override
    public String toString() {
        return getType().toString() + ": " + getValue()
                + (getUncertainty() != null ? " ~" + getUncertainty().toString() : ""
                + (getUnit() != null ? "(" + getUnit().toString() + ")" : ""));
    }

    @Override
    public int hashCode() {
        return UnambiguousParameterComparator.hashCode(this);
    }

}
