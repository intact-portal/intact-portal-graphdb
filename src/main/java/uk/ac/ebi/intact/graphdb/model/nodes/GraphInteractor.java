package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.*;

import java.util.*;

@NodeEntity
public class GraphInteractor implements Interactor {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;
    private String ac;

    private String shortName;
    private String fullName;
    private String preferredName;

    private String preferredIdentifierStr;

    @Relationship(type = RelationshipTypes.ORGANISM)
    private GraphOrganism organism;

    @Relationship(type = RelationshipTypes.INTERACTOR_TYPE)
    private GraphCvTerm interactorType;

    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.CHECKSUMS)
    private Collection<GraphChecksum> checksums;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    @Relationship(type = RelationshipTypes.INTERACTOR_A, direction = Relationship.INCOMING)
    private Collection<GraphBinaryInteractionEvidence> interactionsA;

    @Relationship(type = RelationshipTypes.INTERACTOR_B, direction = Relationship.INCOMING)
    private Collection<GraphBinaryInteractionEvidence> interactionsB;

    @Relationship(type = RelationshipTypes.HAS, direction = Relationship.INCOMING)
    private Collection<GraphInteractionEvidence> interactionEvidence;

    @Relationship(type = RelationshipTypes.INTERACTOR, direction = Relationship.INCOMING)
    private Collection<GraphParticipantEvidence> participantEvidences;

    @Transient
    private Collection<GraphBinaryInteractionEvidence> interactions;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private Map<String, Object> nodeProperties = new HashMap<String, Object>();

    public GraphInteractor() {
    }

    public GraphInteractor(Interactor interactor, boolean childAlreadyCreated) {
        setShortName(interactor.getShortName());
        setFullName(interactor.getFullName());
        setOrganism(interactor.getOrganism());
        setInteractorType(interactor.getInteractorType());
        setAc(CommonUtility.extractAc(interactor));
        setPreferredName(interactor.getPreferredName());
        setPreferredIdentifierStr(interactor.getPreferredIdentifier().getId());
        setUniqueKey(createUniqueKey());

        if (CreationConfig.createNatively) {
            initializeNodeProperties();
            if (!childAlreadyCreated) {
                createNodeNatively();
            }
        }

        setIdentifiers(interactor.getIdentifiers());
        setChecksums(interactor.getChecksums());
        setAnnotations(interactor.getAnnotations());
        setAliases(interactor.getAliases());
        setXrefs(interactor.getXrefs());

        if (CreationConfig.createNatively) {
            if (!childAlreadyCreated) {
                createRelationShipNatively(this.getGraphId());
            }
        }
    }

    public void initializeNodeProperties() {

        if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
        if (this.getPreferredName() != null) nodeProperties.put("preferredName", this.getPreferredName());
        if (this.getPreferredIdentifierStr() != null) nodeProperties.put("preferredIdentifierStr", this.getPreferredIdentifierStr());
        if (this.getShortName() != null) nodeProperties.put("shortName", this.getShortName());
        if (this.getFullName() != null) nodeProperties.put("fullName", this.getFullName());
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Label[] labels = CommonUtility.getLabels(GraphInteractor.class);
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively(Long graphId) {
        CommonUtility.createRelationShip(organism, graphId, RelationshipTypes.ORGANISM);
        CommonUtility.createRelationShip(interactorType, graphId, RelationshipTypes.INTERACTOR_TYPE);
        CommonUtility.createIdentifierRelationShips(identifiers, graphId);
        CommonUtility.createChecksumRelationShips(checksums, graphId);
        CommonUtility.createXrefRelationShips(xrefs, graphId);
        CommonUtility.createAnnotationRelationShips(annotations, graphId);
        CommonUtility.createAliasRelationShips(aliases, graphId);
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

    public void initializeAc(Collection<Xref> identifiers) {
        try {
            CommonUtility commonUtility = Constants.COMMON_UTILITY_OBJECT_POOL.borrowObject();
            setAc(commonUtility.extractAc(identifiers));
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

    /**
     * <p>initialiseIdentifiersWith</p>
     *
     * @param identifiers a {@link java.util.Collection} object.
     */
    protected void initialiseIdentifiersWith(Collection<GraphXref> identifiers) {
        if (identifiers == null) {
            this.identifiers = Collections.EMPTY_LIST;
        } else {
            this.identifiers = identifiers;
        }
    }

    /**
     * <p>initialiseIdentifiers</p>
     */
    protected void initialiseIdentifiers() {
        this.identifiers = new ArrayList<GraphXref>();
    }

    /**
     * <p>initialiseAliases</p>
     */
    protected void initialiseAliases() {
        this.aliases = new ArrayList<GraphAlias>();
    }

    /**
     * <p>initialiseChecksums</p>
     */
    protected void initialiseChecksums() {
        this.checksums = new ArrayList<GraphChecksum>();
    }

    /**
     * <p>initialiseChecksumsWith</p>
     *
     * @param checksums a {@link java.util.Collection} object.
     */
    protected void initialiseChecksumsWith(Collection<GraphChecksum> checksums) {
        if (checksums == null) {
            this.checksums = Collections.EMPTY_LIST;
        } else {
            this.checksums = checksums;
        }
    }


    /**
     * <p>initialiseAliasesWith</p>
     *
     * @param aliases a {@link java.util.Collection} object.
     */
    protected void initialiseAliasesWith(Collection<GraphAlias> aliases) {
        if (aliases == null) {
            this.aliases = Collections.EMPTY_LIST;
        } else {
            this.aliases = aliases;
        }
    }

    public Collection<GraphXref> getIdentifiers() {
        if (identifiers == null) {
            initialiseIdentifiers();
        }
        return this.identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        initialiseIdentifiers();
        if (identifiers != null) {
            this.getIdentifiers().addAll(CollectionAdaptor.convertXrefIntoGraphModel(identifiers));
        } else {
            this.identifiers = getIdentifiers();
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
            initialiseChecksums();
        }
        return this.checksums;
    }

    public void setChecksums(Collection<Checksum> checksums) {
        initialiseChecksums();
        if (checksums != null) {
            this.getChecksums().addAll(CollectionAdaptor.convertChecksumIntoGraphModel(checksums));
        } else {
            this.checksums = getChecksums();
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
            initialiseAliases();
        }
        return this.aliases;
    }

    public void setAliases(Collection<Alias> aliases) {
        initialiseAliases();
        if (aliases != null) {
            this.getAliases().addAll(CollectionAdaptor.convertAliasIntoGraphModel(aliases));
        } else {
            this.aliases = getAliases();
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
                this.interactorType = new GraphCvTerm(interactorType, false);
            }
        } else {
            initialiseDefaultInteractorType();
        }
        //TODO login it
    }

    //TODO improve this part
    public Collection<GraphBinaryInteractionEvidence> getInteractions() {
        if (interactions == null) {
            interactions = new ArrayList<>();
            if (interactionsA != null) {
                interactions.addAll(interactionsA);
            }
            if (interactionsB != null) {
                interactions.addAll(interactionsB);
            }
        }
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

    public int hashCode() {
        int hashcode = 31;
        if (this.getPreferredIdentifierStr() != null) {
            hashcode = 31 * hashcode + this.getPreferredIdentifierStr().hashCode();
        }
        return hashcode;
    }

    public String createUniqueKey() {
        return hashCode() + "";
    }


    public Collection<GraphParticipantEvidence> getParticipantEvidences() {
        return participantEvidences;
    }

    public void setParticipantEvidences(Collection<GraphParticipantEvidence> participantEvidences) {
        this.participantEvidences = participantEvidences;
    }

    @Override
    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getPreferredIdentifierStr() {
        return preferredIdentifierStr;
    }

    public void setPreferredIdentifierStr(String preferredIdentifierStr) {
        this.preferredIdentifierStr = preferredIdentifierStr;
    }
}
