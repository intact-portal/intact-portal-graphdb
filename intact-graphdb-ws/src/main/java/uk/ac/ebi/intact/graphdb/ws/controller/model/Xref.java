package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * @author Elisabet Barrera
 */
public class Xref {

    private CvTerm database;
    private String identifier;
    private CvTerm qualifier;
    private String ac;

    public Xref(CvTerm database, String identifier) {
        this.database = database;
        this.identifier = identifier;
    }

    public Xref(CvTerm database, String identifier, CvTerm qualifier, String ac) {
        this.database = database;
        this.identifier = identifier;
        this.qualifier = qualifier;
        this.ac = ac;
    }

    public Xref(CvTerm database, String identifier, CvTerm qualifier) {
        this.database = database;
        this.identifier = identifier;
        this.qualifier = qualifier;
    }

    public CvTerm getDatabase() {
        return database;
    }

    public void setDatabase(CvTerm database) {
        this.database = database;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public CvTerm getQualifier() {
        return qualifier;
    }

    public void setQualifier(CvTerm qualifier) {
        this.qualifier = qualifier;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Xref) {
            Xref xref = (Xref) object;
            if (this.getDatabase() != null && xref.getDatabase() != null) {
                if (!this.getDatabase().equals(xref.getDatabase())) {
                    return false;
                }
            } else if (!(this.getDatabase() == null && xref.getDatabase() == null)) {// when one of them is null
                return false;
            }

            if (this.getIdentifier() != null && xref.getIdentifier() != null) {
                if (!this.getIdentifier().equals(xref.getIdentifier())) {
                    return false;
                }
            } else if (!(this.getIdentifier() == null && xref.getIdentifier() == null)) {// when one of them is null
                return false;
            }

            if (this.getQualifier() != null && xref.getQualifier() != null) {
                if (!this.getQualifier().equals(xref.getQualifier())) {
                    return false;
                }
            } else if (!(this.getQualifier() == null && xref.getQualifier() == null)) {// when one of them is null
                return false;
            }

            if (this.getAc() != null && xref.getAc() != null) {
                if (!this.getAc().equals(xref.getAc())) {
                    return false;
                }
            } else if (!(this.getAc() == null && xref.getAc() == null)) {// when one of them is null
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 31;
        if (this.getDatabase() != null) {
            hashCode = 31 + this.getDatabase().hashCode();
        }
        if (this.getIdentifier() != null) {
            hashCode = 31 + this.getIdentifier().hashCode();
        }
        if (this.getQualifier() != null) {
            hashCode = 31 + this.getQualifier().hashCode();
        }
        if (this.getAc() != null) {
            hashCode = 31 + this.getAc().hashCode();
        }
        return hashCode;
    }
}
