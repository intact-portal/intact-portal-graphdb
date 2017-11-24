package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Checksum;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.utils.comparator.checksum.UnambiguousChecksumComparator;

@NodeEntity
public class GraphChecksum implements Checksum {

    @GraphId
    protected Long id;

    private GraphCvTerm method;
    private String value;

    public GraphChecksum() {
    }

    public GraphChecksum(Checksum checksum) {
        setMethod(checksum.getMethod());
        setValue(checksum.getValue());
    }

   /* public GraphChecksum(CvTerm method, String value){
        if (method == null){
            throw new IllegalArgumentException("The method is required and cannot be null");
        }
        this.method = method;
        if (value == null){
            throw new IllegalArgumentException("The checksum value is required and cannot be null");
        }
        this.value = value;
    }*/

    public CvTerm getMethod() {
        return this.method;
    }

    public void setMethod(CvTerm method) {
        if (method != null) {
            if (method instanceof GraphCvTerm) {
                this.method = (GraphCvTerm) method;
            } else {
                this.method = new GraphCvTerm(method);
            }
        } else {
            this.method = null;
        }
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Checksum)) {
            return false;
        }

        return UnambiguousChecksumComparator.areEquals(this, (Checksum) o);
    }

    @Override
    public int hashCode() {
        return UnambiguousChecksumComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return getMethod().toString() + ": " + getValue();
    }
}
