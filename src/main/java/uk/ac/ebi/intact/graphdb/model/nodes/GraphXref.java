package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;

public class GraphXref implements Xref {

    private CvTerm database;
    private String id;
    private String version;
    private CvTerm qualifier;

    public GraphXref() {
    }

    public GraphXref(CvTerm database, String id, CvTerm qualifier) {
        this(database, id);
        this.qualifier = qualifier;
    }

    public GraphXref(CvTerm database, String id, String version, CvTerm qualifier) {
        this(database, id, version);
        this.qualifier = qualifier;
    }

    public GraphXref(CvTerm database, String id, String version) {
        this(database, id);
        this.version = version;
    }

    public GraphXref(CvTerm database, String id) {
        if (database == null) {
            throw new IllegalArgumentException("The database is required and cannot be null");
        }
        this.database = database;

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("The id is required and cannot be null or empty");
        }
        this.id = id;
    }

    public CvTerm getDatabase() {
        return database;
    }

    public void setDatabase(CvTerm database) {
        this.database = database;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
