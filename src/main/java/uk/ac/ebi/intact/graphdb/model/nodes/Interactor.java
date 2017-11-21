package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.ogm.annotation.*;
import uk.ac.ebi.intact.graphdb.model.relationships.Interaction;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 05/05/2014
 * Time: 19:19
 */
@NodeEntity
public class Interactor {

    @GraphId
    protected Long id;

    @Index(unique = true,primary = true)
    protected String accession;

    @Property
    protected Integer taxId;

//    @RelatedTo(type = "INTERACTS_IN", direction = Direction.BOTH)
//    @Fetch
//    private Set<Interactor> interactors;

    @Relationship(direction = Relationship.UNDIRECTED, type = RelationshipTypes.INTERACTS_IN)
    private Set<Interaction> interactions;

    public Interactor() {
    }

    public Interactor(String accession) {
        if (accession == null) {
            throw new IllegalArgumentException("The participant accession can not be null");
        }
        if (accession.isEmpty()) {
            throw new IllegalArgumentException("The participant accession can not be empty");
        }
        this.accession = accession;
    }

    public Interactor(String accession, Integer taxId) {
        this(accession);
        setTaxId(taxId);
    }

    public Interaction interactsWith(Interactor interactor, Double score) {
        if (interactions == null) {
            interactions = new HashSet<Interaction>();
        }

        Interaction interaction = new Interaction(this, interactor, score);
        interactions.add(interaction);

        return interaction;
    }


//    public void interactsWith(Interactor interactor) {
//           if (interactors == null) {
//               interactors = new HashSet<Interactor>();
//           }
//
//           interactors.add(interactor);
//       }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    public Set<Interaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(Set<Interaction> interactions) {
        this.interactions = interactions;
    }

    //    public Set<Interactor> getInteractors() {
//        return interactors;
//    }
//
//    public void setInteractors(Set<Interactor> interactors) {
//        this.interactors = interactors;
//    }

//    @Override
//    public String toString() {
//        String results = accession + "'s interactions include\n";
//        if (interactions != null) {
//            for (Interaction interaction : interactions) {
//                results += "\t- " + interaction.toString() + "\n";
//            }
//        }
//
//        return results;
//    }

    @Override
    public int hashCode() {
        return Objects.hash(accession);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Interactor other = (Interactor) obj;
        return Objects.equals(this.accession, other.accession);
    }

    //    @Override
//    public String toString() {
//        String results = accession + "'s interactors include\n";
//        if (interactors != null) {
//            for (Interactor interactor : interactors) {
//                results += "\t- " + interactor.getAccession() + "\n";
//            }
//        }
//
//        return results;
//    }
}
