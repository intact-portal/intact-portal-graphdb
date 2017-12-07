package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Stoichiometry;
import psidev.psi.mi.jami.utils.comparator.participant.StoichiometryComparator;

@NodeEntity
public class GraphStoichiometry implements Stoichiometry {

    @GraphId
    private Long graphId;

    @Index(unique = true,primary = true)
    private String uniqueKey;

    private int minValue;
    private int maxValue;

    public GraphStoichiometry() {
    }

    public GraphStoichiometry(Stoichiometry stoichiometry) {
        this(stoichiometry.getMinValue(), stoichiometry.getMaxValue());
    }

    public GraphStoichiometry(int value) {
        this(value, value);
    }

    public GraphStoichiometry(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("The minValue " + minValue + " cannot be bigger than the maxValue " + maxValue);
        }
        setMinValue(minValue);
        setMaxValue(maxValue);
        setUniqueKey(this.toString());
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Stoichiometry)) {
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
