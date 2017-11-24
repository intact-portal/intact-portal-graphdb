package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.alias.UnambiguousAliasComparator;

@NodeEntity
public class GraphAlias implements Alias {

    @GraphId
    protected Long graphId;

    private GraphCvTerm type;
    private String name;

    public GraphAlias() {
    }

    public GraphAlias(Alias alias) {
        setType(alias.getType());
        setName(alias.getName());
    }

    public GraphAlias(CvTerm type, String name) {
        this(name);
        setType(type);
    }

    public GraphAlias(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The alias name is required and cannot be null");
        }
        setName(name);
    }

    public CvTerm getType() {
        return type;
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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Alias)) {
            return false;
        }

        return UnambiguousAliasComparator.areEquals(this, (Alias) o);
    }

    @Override
    public int hashCode() {
        return UnambiguousAliasComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return getName() + (getType() != null ? "(" + getType().toString() + ")" : "");
    }

}
