package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Stoichiometry;
import psidev.psi.mi.jami.utils.comparator.participant.StoichiometryComparator;

@NodeEntity
public class GraphStoichiometry implements Stoichiometry {

    @GraphId
    protected Long id;

    private int minValue;
    private int maxValue;

    public GraphStoichiometry(int value){

        this.minValue = value;
        this.maxValue = value;
    }

    public GraphStoichiometry(int minValue, int maxValue){
        if (minValue > maxValue){
           throw new IllegalArgumentException("The minValue " + minValue + " cannot be bigger than the maxValue " + maxValue);
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o){
            return true;
        }

        if (!(o instanceof Stoichiometry)){
            return false;
        }

        return StoichiometryComparator.areEquals(this, (Stoichiometry) o);
    }

    @Override
    public int hashCode() {
        return StoichiometryComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return "minValue: " + getMinValue() + ", maxValue: " + getMaxValue();
    }
}
