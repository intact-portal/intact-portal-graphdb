package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;

@NodeEntity
public class GraphXref implements Xref {

    @GraphId
    protected Long id;

    private CvTerm database;
    private String identifier;
    private String version;
    private CvTerm qualifier;

    public GraphXref() {
    }

    public GraphXref(CvTerm database, String identifier, CvTerm qualifier) {
        this(database, identifier);
        this.qualifier = qualifier;
    }

    public GraphXref(CvTerm database, String identifier, String version, CvTerm qualifier) {
        this(database, identifier, version);
        this.qualifier = qualifier;
    }

    public GraphXref(CvTerm database, String identifier, String version) {
        this(database, identifier);
        this.version = version;
    }

    public GraphXref(CvTerm database, String identifier) {
        if (database == null) {
            throw new IllegalArgumentException("The database is required and cannot be null");
        }
        this.database = database;

        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("The id is required and cannot be null or empty");
        }
        this.identifier = identifier;
    }

    public CvTerm getDatabase() {
        return database;
    }

    public void setDatabase(CvTerm database) {
        this.database = database;
    }

    public String getId() {
        return identifier;
    }

    public void setId(String identifier) {
        this.identifier = identifier;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public CvTerm getQualifier() {
        return this.qualifier;
    }

    public void setQualifier(CvTerm qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        // Xrefs are different and it has to be ExternalIdentifier
        if (!(o instanceof Xref)) {
            return false;
        }

        return UnambiguousXrefComparator.areEquals(this, (Xref) o);
    }

    @Override
    public int hashCode() {
        return UnambiguousXrefComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return getDatabase().toString() + ":" + getId() + (getQualifier() != null ? " (" + getQualifier().toString() + ")" : "");
    }
}
