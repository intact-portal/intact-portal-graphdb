package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Molecule;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphMolecule extends GraphInteractor implements Molecule {

    private String uniqueKey;

    @Transient
    private boolean isAlreadyCreated;

    public GraphMolecule() {
        super();
    }

    public GraphMolecule(Molecule molecule, boolean childAlreadyCreated) {
        super(molecule, true);
        setUniqueKey(createUniqueKey(molecule));
        if (CreationConfig.createNatively) {
            if (!childAlreadyCreated) {
                this.createNodeNatively();
            }
            if (!childAlreadyCreated) {
                this.createRelationShipNatively();
            }
        }
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

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            nodeProperties.putAll(super.getNodeProperties());
            Label[] labels = CommonUtility.getLabels(GraphMolecule.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }


    public String createUniqueKey(Molecule molecule) {
        return UniqueKeyGenerator.createInteractorKey(molecule);
    }

}
