package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Polymer;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.CvTermUtils;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 05/05/2014
 * Time: 18:40
 */
@NodeEntity
public class GraphPolymer extends GraphMolecule implements Polymer {

    @GraphId
    private Long graphId;

    private String sequence;

    public GraphPolymer() {
        super();
    }

    public GraphPolymer(Polymer polymer) {
        super(polymer);
        setSequence(polymer.getSequence());
    }

    public GraphPolymer(String name, CvTerm type) {
        super(name, type != null ? type : CvTermUtils.createPolymerInteractorType());
    }

    public GraphPolymer(String name, String fullName, CvTerm type) {
        super(name, fullName, type != null ? type : CvTermUtils.createPolymerInteractorType());
    }

    public GraphPolymer(String name, CvTerm type, Organism organism) {
        super(name, type != null ? type : CvTermUtils.createPolymerInteractorType(), organism);
    }

    public GraphPolymer(String name, String fullName, CvTerm type, Organism organism) {
        super(name, fullName, type != null ? type : CvTermUtils.createPolymerInteractorType(), organism);
    }

    public GraphPolymer(String name, CvTerm type, Xref uniqueId) {
        super(name, type != null ? type : CvTermUtils.createPolymerInteractorType(), uniqueId);
    }

    public GraphPolymer(String name, String fullName, CvTerm type, Xref uniqueId) {
        super(name, fullName, type != null ? type : CvTermUtils.createPolymerInteractorType(), uniqueId);
    }

    public GraphPolymer(String name, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, type != null ? type : CvTermUtils.createPolymerInteractorType(), organism, uniqueId);
    }

    public GraphPolymer(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, fullName, type != null ? type : CvTermUtils.createPolymerInteractorType(), organism, uniqueId);
    }

    public GraphPolymer(String name) {
        super(name, CvTermUtils.createPolymerInteractorType());
    }

    public GraphPolymer(String name, String fullName) {
        super(name, fullName, CvTermUtils.createPolymerInteractorType());
    }

    public GraphPolymer(String name, Organism organism) {
        super(name, CvTermUtils.createPolymerInteractorType(), organism);
    }

    public GraphPolymer(String name, String fullName, Organism organism) {
        super(name, fullName, CvTermUtils.createPolymerInteractorType(), organism);
    }

    public GraphPolymer(String name, Xref uniqueId) {
        super(name, CvTermUtils.createPolymerInteractorType(), uniqueId);
    }

    public GraphPolymer(String name, String fullName, Xref uniqueId) {
        super(name, fullName, CvTermUtils.createPolymerInteractorType(), uniqueId);
    }

    public GraphPolymer(String name, Organism organism, Xref uniqueId) {
        super(name, CvTermUtils.createPolymerInteractorType(), organism, uniqueId);
    }

    public GraphPolymer(String name, String fullName, Organism organism, Xref uniqueId) {
        super(name, fullName, CvTermUtils.createPolymerInteractorType(), organism, uniqueId);
    }

    public String getSequence() {
        return this.sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /*
      Sets the interactor type of this polymer.
      If the given interactorType is null, it sets the interactorType to 'biopolymer'(MI:0383)
     */
    @Override
    public void setInteractorType(CvTerm interactorType) {
        if (interactorType == null){
            super.setInteractorType(CvTermUtils.createPolymerInteractorType());
        }
        else {
            super.setInteractorType(interactorType);
        }
    }
}
