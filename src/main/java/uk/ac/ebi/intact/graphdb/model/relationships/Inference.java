package uk.ac.ebi.intact.graphdb.model.relationships;


import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import uk.ac.ebi.intact.graphdb.model.nodes.Peptide;
import uk.ac.ebi.intact.graphdb.model.nodes.Protein;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 05/05/2014
 * Time: 18:43
 */
@RelationshipEntity(type = RelationshipTypes.INFERS)
public class Inference {

    @GraphId
    Long id;

    @StartNode
    Peptide peptide;

    @EndNode
    Protein protein;


    public Inference(Peptide peptide, Protein protein) {
        this.peptide = peptide;
        this.protein = protein;
    }

    public Inference() {
    }

    public Peptide getPeptide() {
        return peptide;
    }

    public void setPeptide(Peptide peptide) {
        this.peptide = peptide;
    }

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +
                ", interactorA=" + peptide +
                ", interactorB=" + protein +
                '}';
    }
}
