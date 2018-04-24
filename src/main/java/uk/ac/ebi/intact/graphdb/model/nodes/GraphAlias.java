package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.alias.UnambiguousAliasComparator;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

@NodeEntity
public class GraphAlias implements Alias {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private GraphCvTerm type;
    private String name;

    public GraphAlias() {
    }

    public GraphAlias(Alias alias) {
        if (GraphEntityCache.cvTermCacheMap.get(alias.getType().getShortName()) != null) {
            type=(GraphEntityCache.cvTermCacheMap.get(alias.getType().getShortName()));
        } else {
            setType(alias.getType());
        }
        setName(alias.getName());
        setUniqueKey(this.toString());
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

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
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
        return getName() + (this.type != null ? "(" + this.type.toString() + ")" : "");
    }

}
