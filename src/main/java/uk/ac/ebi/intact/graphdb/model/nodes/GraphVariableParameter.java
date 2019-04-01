package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.VariableParameter;
import psidev.psi.mi.jami.model.VariableParameterValue;
import psidev.psi.mi.jami.utils.comparator.experiment.UnambiguousVariableParameterComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 24/11/17.
 */
@NodeEntity
public class GraphVariableParameter implements VariableParameter {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private String description;

    @Relationship(type = RelationshipTypes.UNIT)
    private GraphCvTerm unit;

    @Relationship(type = RelationshipTypes.VARIABLE_VALUES)
    private Collection<GraphVariableParameterValue> variableValues;

    @Relationship(type = RelationshipTypes.EXPERIMENT)
    private GraphExperiment experiment;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private boolean forceHashCodeGeneration;

    public GraphVariableParameter() {

    }

    public GraphVariableParameter(VariableParameter variableParameter) {
        setForceHashCodeGeneration(true);
        setDescription(variableParameter.getDescription());
        setUnit(variableParameter.getUnit());
        setExperiment(variableParameter.getExperiment());
        setAc(CommonUtility.extractAc(variableParameter));
        setUniqueKey(createUniqueKey(variableParameter));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }
        boolean wasInitializedBefore = false;
        if (GraphEntityCache.parameterValueCacheMap.get(this.getUniqueKey()) == null) {
            GraphEntityCache.parameterValueCacheMap.put(this.getUniqueKey(), this);
        } else {
            wasInitializedBefore = true;
        }
        if (!wasInitializedBefore) {
            setVariableValues(variableParameter.getVariableValues());
        }

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getDescription() != null) nodeProperties.put("description", this.getDescription());

            Label[] labels = CommonUtility.getLabels(GraphVariableParameter.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(unit, this.graphId, RelationshipTypes.UNIT);
        CommonUtility.createRelationShip(experiment, this.graphId, RelationshipTypes.EXPERIMENT);
        CommonUtility.createVariableParameterValueRelationShips(variableValues, this.graphId);
    }

   /* public GraphVariableParameter(String description) {
        if(description == null) {
            throw new IllegalArgumentException("The description of the variableParameter is required and cannot be null.");
        } else {
            this.description = description;
        }
    }

    public GraphVariableParameter(String description, Experiment experiment) {
        this(description);
        this.experiment = experiment;
    }

    public GraphVariableParameter(String description, CvTerm unit) {
        this(description);
        this.unit = unit;
    }

    public GraphVariableParameter(String description, Experiment experiment, CvTerm unit) {
        this(description, experiment);
        this.unit = unit;
    }*/

  /*  protected void initialiseVatiableParameterValues() {
        this.variableValues = new ArrayList();
    }

    protected void initialiseVatiableParameterValuesWith(Collection<VariableParameterValue> paramValues) {
        if(paramValues == null) {
            this.variableValues = Collections.EMPTY_SET;
        } else {
            this.variableValues = paramValues;
        }

    }*/

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("The description cannot be null");
        } else {
            this.description = description;
        }
    }

    public CvTerm getUnit() {
        return this.unit;
    }

    public void setUnit(CvTerm unit) {
        if (unit != null) {
            if (unit instanceof GraphCvTerm) {
                this.unit = (GraphCvTerm) unit;
            } else {
                this.unit = new GraphCvTerm(unit, false);
            }
        } else {
            this.unit = null;
        }
    }

    public Collection<GraphVariableParameterValue> getVariableValues() {
        if (this.variableValues == null) {
            this.variableValues = new ArrayList<GraphVariableParameterValue>();
        }
        return this.variableValues;
    }

    public void setVariableValues(Collection<VariableParameterValue> variableValues) {
        if (variableValues != null) {
            this.variableValues = CollectionAdaptor.convertVariableParameterValueIntoGraphModel(variableValues);
        } else {
            this.variableValues = new ArrayList<GraphVariableParameterValue>();
        }
    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    public void setExperiment(Experiment experiment) {
        if (experiment != null) {
            if (experiment instanceof GraphCvTerm) {
                this.experiment = (GraphExperiment) experiment;
            } else {
                this.experiment = new GraphExperiment(experiment);
            }
        } else {
            this.experiment = null;
        }
    }

    public void setExperimentAndAddVariableParameter(Experiment experiment) {
        if (this.experiment != null) {
            this.experiment.removeVariableParameter(this);
        }

        if (experiment != null) {
            experiment.addVariableParameter(this);
        }

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

    public boolean equals(Object o) {
        return this == o ? true : (!(o instanceof VariableParameter) ? false : UnambiguousVariableParameterComparator.areEquals(this, (VariableParameter) o));
    }

    public int hashCode() {

        if (!isForceHashCodeGeneration() && this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return Integer.parseInt(this.getUniqueKey());
        }

        return UnambiguousVariableParameterComparator.hashCode(this);
    }

    public String toString() {
        return this.description + (this.getUnit() != null ? "(unit: " + this.unit.toString() + ")" : "");
    }

    public String createUniqueKey(VariableParameter variableParameter) {
        return variableParameter != null ? UnambiguousVariableParameterComparator.hashCode(variableParameter) + "" : "";
    }

    public boolean isForceHashCodeGeneration() {
        return forceHashCodeGeneration;
    }

    public void setForceHashCodeGeneration(boolean forceHashCodeGeneration) {
        this.forceHashCodeGeneration = forceHashCodeGeneration;
    }
}
