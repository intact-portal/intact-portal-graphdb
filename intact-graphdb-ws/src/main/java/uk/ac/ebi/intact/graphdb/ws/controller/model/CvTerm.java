package uk.ac.ebi.intact.graphdb.ws.controller.model;

/**
 * @author Elisabet Barrera
 */
public class CvTerm {
    private String shortName;
    private String identifier;

    public CvTerm(String shortName, String identifier) {
        this.shortName = shortName;
        this.identifier = identifier;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof CvTerm) {
            CvTerm cvTerm = (CvTerm) object;
            if (this.getShortName() != null && cvTerm.getShortName() != null) {
                if (!this.getShortName().equals(cvTerm.getShortName())) {
                    return false;
                }
            } else if (!(this.getShortName() == null && cvTerm.getShortName() == null)) {// when one of them is null
                return false;
            }

            if (this.getIdentifier() != null && cvTerm.getIdentifier() != null) {
                if (!this.getIdentifier().equals(cvTerm.getIdentifier())) {
                    return false;
                }
            } else if (!(this.getIdentifier() == null && cvTerm.getIdentifier() == null)) {// when one of them is null
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
        if (this.getIdentifier() != null) {
            hashCode = 31 + this.getIdentifier().hashCode();
        }
        if (this.getShortName() != null) {
            hashCode = 31 + this.getShortName().hashCode();
        }
        return hashCode;
    }
}
