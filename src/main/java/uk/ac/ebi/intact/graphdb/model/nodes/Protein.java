package uk.ac.ebi.intact.graphdb.model.nodes;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 26/03/2014
 * Time: 20:26
 */
public class Protein extends Interactor {

    private String sequence;

    public Protein() {
    }

    public Protein(String accession) {
        super(accession);
    }

    public Protein(String accession, String sequence) {
        super(accession);
        this.sequence = sequence;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "Protein{" +
                "accession='" + accession + '\'' +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Protein)) return false;
//
//        Protein protein = (Protein) o;
//
//        return  Objects.equals(accession, protein.accession) &&
//                Objects.equals(sequence, protein.sequence);
//
//    }
//
//    @Override
//    public int hashCode() {
//      return Objects.hash(accession, sequence);
//    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(sequence);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final Protein other = (Protein) obj;
        return Objects.equals(this.sequence, other.sequence);
    }


//    @Override
//    public int hashCode() {
//        return Objects.hash(sequence);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//        final Protein other = (Protein) obj;
//        return Objects.equals(this.sequence, other.sequence);
//    }
}