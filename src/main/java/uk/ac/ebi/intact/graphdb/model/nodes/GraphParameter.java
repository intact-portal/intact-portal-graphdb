package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.exception.IllegalParameterException;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Parameter;
import psidev.psi.mi.jami.model.ParameterValue;
import psidev.psi.mi.jami.utils.ParameterUtils;
import psidev.psi.mi.jami.utils.comparator.parameter.UnambiguousParameterComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphParameter implements Parameter {

    @GraphId
    private Long graphId;

    private String ac;
    private GraphCvTerm type;
    private BigDecimal uncertainty;
    private GraphCvTerm unit;
    private GraphParameterValue value;

    @Transient
    private boolean isAlreadyCreated;

    public GraphParameter() {
    }

    public GraphParameter(Parameter parameter) {
        setType(parameter.getType());
        setUncertainty(parameter.getUncertainty());
        setUnit(parameter.getUnit());
        setValue(parameter.getValue());
        setAc(CommonUtility.extractAc(parameter));

        if (CreationConfig.createNatively) {
            createNodeNatively();
            if(!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getUncertainty() != null) nodeProperties.put("uncertainty", this.getUncertainty());

            Label[] labels = CommonUtility.getLabels(GraphParameter.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(type, this.graphId, "type");
        CommonUtility.createRelationShip(unit, this.graphId, "unit");
        CommonUtility.createRelationShip(value, this.graphId, "value");
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
                this.type = new GraphCvTerm(type,false);
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
                this.unit = new GraphCvTerm(unit,false);
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

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
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
        return this.type.toString() + ": " + getValue()
                + (this.uncertainty != null ? " ~" + this.uncertainty.toString() : ""
                + (this.unit != null ? "(" + this.unit.toString() + ")" : ""));
    }

    @Override
    public int hashCode() {
        return UnambiguousParameterComparator.hashCode(this);
    }

}
