package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphInteractor implements Interactor {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;
    private String ac;

    private String shortName;
    private String fullName;
    private GraphOrganism organism;
    private GraphCvTerm interactorType;
    private Collection<GraphXref> identifiers;
    private Collection<GraphChecksum> checksums;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private Collection<GraphAlias> aliases;

    @Transient
    private boolean isAlreadyCreated;
    @Transient
    private Map<String, Object> nodeProperties = new HashMap<String, Object>();


    @Relationship(type = RelationshipTypes.INTERACTS_IN, direction = Relationship.OUTGOING)
    private Collection<GraphBinaryInteractionEvidence> interactions;

    public GraphInteractor() {
    }

    public GraphInteractor(Interactor interactor,boolean childAlreadyCreated) {
        setShortName(interactor.getShortName());
        setFullName(interactor.getFullName());
        setOrganism(interactor.getOrganism());
        setInteractorType(interactor.getInteractorType());
        setAc(CommonUtility.extractAc(interactor));
        setUniqueKey(this.getAc());

        if (CreationConfig.createNatively) {
            initialzeNodeProperties();
            if(!childAlreadyCreated) {
                createNodeNatively();
            }
        }

        setIdentifiers(interactor.getIdentifiers());
        setChecksums(interactor.getChecksums());
        setAnnotations(interactor.getAnnotations());
        setAliases(interactor.getAliases());

        if (CreationConfig.createNatively) {
            if(!isAlreadyCreated()&&!childAlreadyCreated) {
                createRelationShipNatively(this.getGraphId());
            }
        }
    }

    public void initialzeNodeProperties(){
        nodeProperties.put("uniqueKey", this.getUniqueKey());
        if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
        if (this.getShortName() != null) nodeProperties.put("shortName", this.getShortName());
        if (this.getFullName() != null) nodeProperties.put("fullName", this.getFullName());
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Label[] labels = CommonUtility.getLabels(GraphInteractor.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively(Long graphId) {
        CommonUtility.createRelationShip(organism, graphId, "organism");
        CommonUtility.createRelationShip(interactorType, graphId, "interactorType");
        CommonUtility.createXrefRelationShips(identifiers, graphId, "identifiers");
        CommonUtility.createCheckSumRelationShips(checksums, graphId, "checksums");
        CommonUtility.createXrefRelationShips(xrefs, graphId, "xrefs");
        CommonUtility.createAnnotationRelationShips(annotations, graphId, "annotations");
        CommonUtility.createAliasRelationShips(aliases, graphId, "aliases");
    }

    public GraphInteractor(String name, CvTerm type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        setShortName(name);
        setInteractorType(type);
    }

    public GraphInteractor(String name, String fullName, CvTerm type) {
        this(name, type);
        setFullName(fullName);
    }

    public GraphInteractor(String name, CvTerm type, Organism organism) {
        this(name, type);
        setOrganism(organism);
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Organism organism) {
        this(name, fullName, type);
        setOrganism(organism);
    }

    public GraphInteractor(String name, CvTerm type, Xref uniqueId) {
        this(name, type);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Xref uniqueId) {
        this(name, fullName, type);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name, CvTerm type, Organism organism, Xref uniqueId) {
        this(name, type, organism);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId) {
        this(name, fullName, type, organism);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        setShortName(name);
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName) {
        this(name);
        setFullName(fullName);
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, Organism organism) {
        this(name);
        setOrganism(organism);
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName, Organism organism) {
        this(name, fullName);
        setOrganism(organism);
    }

    public GraphInteractor(String name, Xref uniqueId) {
        this(name);
        getIdentifiers().add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName, Xref uniqueId) {
        this(name, fullName);
        getIdentifiers().add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, Organism organism, Xref uniqueId) {
        this(name, organism);
        identifiers.add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName, Organism organism, Xref uniqueId) {
        this(name, fullName, organism);
        getIdentifiers().add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public void initializeAc(Collection<Xref> xrefs) {
        try {
            CommonUtility commonUtility = Constants.COMMON_UTILITY_OBJECT_POOL.borrowObject();
            setAc(commonUtility.extractAc(xrefs));
            Constants.COMMON_UTILITY_OBJECT_POOL.returnObject(commonUtility);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        this.shortName = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public Collection<GraphXref> getIdentifiers() {
        if (identifiers == null) {
            this.identifiers = new ArrayList<GraphXref>();
        }
        return this.identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        if (identifiers != null) {
            this.identifiers = CollectionAdaptor.convertXrefIntoGraphModel(identifiers);
        } else {
            this.identifiers = new ArrayList<GraphXref>();
        }
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    /**
     * @return the first identifier in the list of identifiers or null if the list is empty
     */
    public Xref getPreferredIdentifier() {
        return !getIdentifiers().isEmpty() ? getIdentifiers().iterator().next() : null;
    }

    public Collection<GraphChecksum> getChecksums() {
        if (checksums == null) {
            this.checksums = new ArrayList<GraphChecksum>();
        }
        return this.checksums;
    }

    public void setChecksums(Collection<Checksum> checksums) {
        if (checksums != null) {
            this.checksums = CollectionAdaptor.convertChecksumIntoGraphModel(checksums);
        } else {
            this.checksums = new ArrayList<GraphChecksum>();
        }
    }

    public Collection<GraphXref> getXrefs() {
        if (xrefs == null) {
            this.xrefs = new ArrayList<GraphXref>();
        }
        return this.xrefs;
    }

    public void setXrefs(Collection<Xref> xrefs) {
        if (xrefs != null) {
            this.xrefs = CollectionAdaptor.convertXrefIntoGraphModel(xrefs);
        } else {
            this.xrefs = new ArrayList<GraphXref>();
        }
    }

    public Collection<GraphAnnotation> getAnnotations() {
        if (annotations == null) {
            this.annotations = new ArrayList<GraphAnnotation>();
        }
        return this.annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        if (annotations != null) {
            this.annotations = CollectionAdaptor.convertAnnotationIntoGraphModel(annotations);
        } else {
            this.annotations = new ArrayList<GraphAnnotation>();
        }
    }

    public Collection<GraphAlias> getAliases() {
        if (aliases == null) {
            this.aliases = new ArrayList<GraphAlias>();
        }
        return this.aliases;
    }

    public void setAliases(Collection<Alias> aliases) {
        if (aliases != null) {
            this.aliases = CollectionAdaptor.convertAliasIntoGraphModel(aliases);
        } else {
            this.aliases = new ArrayList<GraphAlias>();
        }
    }

    public Organism getOrganism() {
        return this.organism;
    }

    public void setOrganism(Organism organism) {
        if (organism != null) {
            if (organism instanceof GraphOrganism) {
                this.organism = (GraphOrganism) organism;
            } else {
                this.organism = new GraphOrganism(organism);
            }
        } else {
            this.organism = null;
        }
        //TODO login it
    }

    public CvTerm getInteractorType() {
        return this.interactorType;
    }

    public void setInteractorType(CvTerm interactorType) {
        if (interactorType != null) {
            if (interactorType instanceof GraphCvTerm) {
                this.interactorType = (GraphCvTerm) interactorType;
            } else {
                this.interactorType = new GraphCvTerm(interactorType,false);
            }
        } else {
            initialiseDefaultInteractorType();
        }
        //TODO login it
    }

    public Collection<GraphBinaryInteractionEvidence> getInteractions() {
        return interactions;
    }

    public void setInteractions(Collection<GraphBinaryInteractionEvidence> interactions) {
        this.interactions = interactions;
    }

    private void initialiseDefaultInteractorType() {
        this.interactorType = GraphUtils.createGraphMITerm(
                Interactor.UNKNOWN_INTERACTOR,
                Interactor.UNKNOWN_INTERACTOR_MI,
                GraphUtils.INTERACTOR_TYPE_OBJCLASS);
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    @Override
    public String toString() {
        return getShortName()
                + (this.organism != null ? ", " + this.organism.toString() : "")
                + (this.interactorType != null ? ", " + this.interactorType.toString() : "");
    }


    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }


}
