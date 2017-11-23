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
    protected Long id;

    private CvTerm type;
    private BigDecimal uncertainty;
    private CvTerm unit;
    private ParameterValue value;

    public GraphParameter() {
    }

    public GraphParameter(Parameter parameter) {
    }

    public GraphParameter(CvTerm type, ParameterValue value) {
        if (type == null) {
            throw new IllegalArgumentException("The parameter type is required and cannot be null");
        }
        this.type = type;
        if (value == null) {
            throw new IllegalArgumentException("The parameter value is required and cannot be null");
        }
        this.value = value;
    }

    public GraphParameter(CvTerm type, ParameterValue value, CvTerm unit) {
        this(type, value);
        this.unit = unit;
    }

    public GraphParameter(CvTerm type, ParameterValue value, CvTerm unit, BigDecimal uncertainty) {
        this(type, value, unit);
        this.uncertainty = uncertainty;
    }

    public GraphParameter(CvTerm type, ParameterValue value, BigDecimal uncertainty) {
        this(type, value);
        this.uncertainty = uncertainty;
    }

    public GraphParameter(CvTerm type, String value) throws IllegalParameterException {
        if (type == null) {
            throw new IllegalArgumentException("The parameter type is required and cannot be null");
        }
        this.type = type;

        Parameter param = ParameterUtils.createParameterFromString(type, value);
        this.value = param.getValue();
        this.uncertainty = param.getUncertainty();
    }

    public GraphParameter(CvTerm type, String value, CvTerm unit) throws IllegalParameterException {
        this(type, value);
        this.unit = unit;
    }

    public CvTerm getType() {
        return this.type;
    }

    public void setType(CvTerm type) {
        this.type = type;
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
        this.unit = unit;
    }

    public ParameterValue getValue() {
        return this.value;
    }

    public void setValue(ParameterValue value) {
        this.value = value;
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
