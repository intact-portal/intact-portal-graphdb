package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Polymer;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.CvTermUtils;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

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
    private String uniqueKey;

    private String sequence;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private Map<String, Object> nodeProperties = new HashMap<String, Object>();

    public GraphPolymer() {
        super();
    }

    public GraphPolymer(Polymer polymer, boolean childAlreadyCreated) {
        super(polymer, true);
        setSequence(polymer.getSequence());
        setUniqueKey(createUniqueKey(polymer));

        if (CreationConfig.createNatively) {
            this.initialzeNodeProperties();
            if (!childAlreadyCreated) {
                createNodeNatively();
            }
        }

        if (CreationConfig.createNatively) {
            if (!childAlreadyCreated) {
                this.createRelationShipNatively();
            }
        }
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

    public void initialzeNodeProperties() {
        if (this.getSequence() != null) getNodeProperties().put("sequence", this.getSequence());
        this.getNodeProperties().putAll(super.getNodeProperties());
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Label[] labels = CommonUtility.getLabels(GraphPolymer.class);
            nodeProperties.put("uniqueKey", this.getUniqueKey());

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(getNodeProperties(), labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (interactorType == null) {
            super.setInteractorType(CvTermUtils.createPolymerInteractorType());
        } else {
            super.setInteractorType(interactorType);
        }
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    @Override
    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    @Override
    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(Polymer polymer) {
        return UniqueKeyGenerator.createInteractorKey(polymer);
    }

    @Override
    public Long getGraphId() {
        return graphId;
    }

    @Override
    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

}
