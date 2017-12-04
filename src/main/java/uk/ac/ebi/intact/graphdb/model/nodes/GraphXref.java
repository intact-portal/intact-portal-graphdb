package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;
import uk.ac.ebi.intact.graphdb.utils.Constants;
import uk.ac.ebi.intact.graphdb.utils.EntityCache;

@NodeEntity
public class GraphXref implements Xref {

    @GraphId
    private Long graphId;

    private GraphCvTerm database;
    private String identifier;
    private String version;
    private GraphCvTerm qualifier;

    public GraphXref() {
    }

    public GraphXref(Xref xref) {
        setDatabase(xref.getDatabase());
        setId(xref.getId());
        setVersion(xref.getVersion());
        setQualifier(xref.getQualifier());
    }

    public GraphXref(CvTerm database, String identifier, CvTerm qualifier) {
        this(database, identifier);
        setQualifier(qualifier);
    }

    public GraphXref(CvTerm database, String identifier, String version, CvTerm qualifier) {
        this(database, identifier, version);
        setQualifier(qualifier);
    }

    public GraphXref(CvTerm database, String identifier, String version) {
        this(database, identifier);
        setVersion(version);
    }

    public GraphXref(CvTerm database, String identifier) {
        if (database == null) {
            throw new IllegalArgumentException("The database is required and cannot be null");
        }
        setDatabase(database);

        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("The id is required and cannot be null or empty");
        }
        setId(identifier);
    }

    public CvTerm getDatabase() {
        return database;
    }

    public void setDatabase(CvTerm database) {
        if (database != null) {

            if (database instanceof GraphCvTerm) {
                this.database = (GraphCvTerm) database;
            } else if (database != null && EntityCache.PSIMI_CVTERM != null && CvTerm.PSI_MI.equals(database.getShortName())) {
                this.database = EntityCache.PSIMI_CVTERM;
            } else {
                this.database = new GraphCvTerm(database);
            }
        } else {
            this.database = null;
        }
        //TODO login it
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
        if (qualifier != null) {
            if (qualifier instanceof GraphCvTerm) {
                this.qualifier = (GraphCvTerm) qualifier;
            } else if (qualifier != null && EntityCache.IDENTITY != null && Constants.IDENTITY.equals(qualifier.getShortName())) {
                setQualifier(EntityCache.IDENTITY);
            } else {
                this.qualifier = new GraphCvTerm(qualifier);
            }
        } else {
            this.qualifier = null;
        }
        //TODO login it
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
