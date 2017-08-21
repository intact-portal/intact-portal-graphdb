package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 05/05/2014
 * Time: 18:40
 */
@NodeEntity
public class Peptide {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String sequence;

    @Relationship(type = "INFERS", direction = Relationship.OUTGOING)
    private Set<Protein> ambiguityGroup;

    public Peptide() {
    }

    public Peptide(String sequence) {
        if (sequence == null) {
            throw new IllegalArgumentException("The peptide identifier can not be null");
        }
        if (sequence.isEmpty()) {
            throw new IllegalArgumentException("The peptide identifier can not be empty");
        }
        this.sequence = sequence;
    }

    public void infers(Protein protein) {
        if (ambiguityGroup == null) {
            ambiguityGroup = new HashSet<Protein>();
        }
        ambiguityGroup.add(protein);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Set<Protein> getAmbiguityGroup() {
        return ambiguityGroup;
    }

    public void setAmbiguityGroup(Set<Protein> ambiguityGroup) {
        this.ambiguityGroup = ambiguityGroup;
    }

    @Override
    public String toString() {
        String results = sequence + "'s ambiguity group includes\n";
        if (ambiguityGroup != null) {
            for (Protein protein : ambiguityGroup) {
                results += "\t- " + protein.getAccession() + "\n";
            }
        }
        return results;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Peptide other = (Peptide) obj;
        return Objects.equals(this.sequence, other.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence);
    }
}
