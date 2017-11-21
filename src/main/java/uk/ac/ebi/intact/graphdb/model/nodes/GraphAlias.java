package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.alias.UnambiguousAliasComparator;

public class GraphAlias implements Alias {

    private CvTerm type;
    private String name;

    public GraphAlias() {
    }

    public GraphAlias(CvTerm type, String name) {
        this(name);
        this.type = type;
    }

    public GraphAlias(String name) {
        if (name == null){
            throw new IllegalArgumentException("The alias name is required and cannot be null");
        }
        this.name = name;
    }

    public CvTerm getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (!(o instanceof Alias)){
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
        return getName() + (getType() != null ? "("+getType().toString()+")" : "");
    }

}
