package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Experiment;
import psidev.psi.mi.jami.model.VariableParameter;
import psidev.psi.mi.jami.model.VariableParameterValue;
import psidev.psi.mi.jami.utils.comparator.experiment.UnambiguousVariableParameterComparator;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anjali on 24/11/17.
 */
public class GraphVariableParameter implements VariableParameter {

    private String description;
    private GraphCvTerm unit;
    private Collection<GraphVariableParameterValue> variableValues;
    private GraphExperiment experiment;

    public GraphVariableParameter() {

    }

    public GraphVariableParameter(VariableParameter variableParameter) {
        setDescription(variableParameter.getDescription());
        setUnit(variableParameter.getUnit());
        setVariableValues(variableParameter.getVariableValues());
        setExperiment(variableParameter.getExperiment());

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
                this.unit = new GraphCvTerm(unit);
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

    public boolean equals(Object o) {
        return this == o ? true : (!(o instanceof VariableParameter) ? false : UnambiguousVariableParameterComparator.areEquals(this, (VariableParameter) o));
    }

    public int hashCode() {
        return UnambiguousVariableParameterComparator.hashCode(this);
    }

    public String toString() {
        return this.getDescription().toString() + (this.getUnit() != null ? "(unit: " + this.getUnit().toString() + ")" : "");
    }

}