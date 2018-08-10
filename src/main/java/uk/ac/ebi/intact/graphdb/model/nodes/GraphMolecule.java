package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Molecule;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Xref;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphMolecule extends GraphInteractor implements Molecule {

    @GraphId
    private Long graphId;

    @Transient
    private boolean isAlreadyCreated;

    public GraphMolecule() {
        super();
    }

    public GraphMolecule(Molecule molecule,boolean childAlreadyCreated) {
        super(molecule,childAlreadyCreated);
    }

    public GraphMolecule(String name, CvTerm type) {
        super(name, type);
    }

    public GraphMolecule(String name, String fullName, CvTerm type) {
        super(name, fullName, type);
    }

    public GraphMolecule(String name, CvTerm type, Organism organism) {
        super(name, type, organism);
    }

    public GraphMolecule(String name, String fullName, CvTerm type, Organism organism) {
        super(name, fullName, type, organism);
    }

    public GraphMolecule(String name, CvTerm type, Xref uniqueId) {
        super(name, type, uniqueId);
    }

    public GraphMolecule(String name, String fullName, CvTerm type, Xref uniqueId) {
        super(name, fullName, type, uniqueId);
    }

    public GraphMolecule(String name, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, type, organism, uniqueId);
    }

    public GraphMolecule(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, fullName, type, organism, uniqueId);
    }

    public GraphMolecule(String name) {
        super(name);
    }

    public GraphMolecule(String name, String fullName) {
        super(name, fullName);
    }

    public GraphMolecule(String name, Organism organism) {
        super(name, organism);
    }

    public GraphMolecule(String name, String fullName, Organism organism) {
        super(name, fullName, organism);
    }

    public GraphMolecule(String name, Xref uniqueId) {
        super(name, uniqueId);
    }

    public GraphMolecule(String name, String fullName, Xref uniqueId) {
        super(name, fullName, uniqueId);
    }

    public GraphMolecule(String name, Organism organism, Xref uniqueId) {
        super(name, organism, uniqueId);
    }

    public GraphMolecule(String name, String fullName, Organism organism, Xref uniqueId) {
        super(name, fullName, organism, uniqueId);
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

}
