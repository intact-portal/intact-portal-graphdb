package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.*;

/**
 * Created by anjali on 30/04/19.
 */
@NodeEntity
public class GraphModelledFeature implements ModelledFeature {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private String shortName;
    private String fullName;

    @Relationship(type = RelationshipTypes.INTERPRO)
    private GraphXref interpro;

    @Relationship(type = RelationshipTypes.TYPE)
    private GraphCvTerm type;

    @Relationship(type = RelationshipTypes.ROLE)
    private GraphCvTerm role;

    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.RANGES)
    private Collection<GraphModelledRange> ranges;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    @Relationship(type = RelationshipTypes.PARTICIPANT_FEATURE, direction = Relationship.INCOMING)
    private GraphModelledEntity participant;

    @Relationship(type = RelationshipTypes.LINKED_FEATURES, direction = Relationship.UNDIRECTED)
    private Collection<GraphModelledFeature> linkedFeatures;

    @Transient
    private boolean isAlreadyCreated;

    public GraphModelledFeature() {

    }

    public GraphModelledFeature(ModelledFeature modelledFeature) {
        boolean wasInitializedBefore = false;
        String callingClass = Arrays.toString(Thread.currentThread().getStackTrace());
        if (GraphEntityCache.modelledFeatureCacheMap.get(modelledFeature.getShortName()) == null) {
            GraphEntityCache.modelledFeatureCacheMap.put(modelledFeature.getShortName(), this);
        } else {
            wasInitializedBefore = true;
        }
        setShortName(modelledFeature.getShortName());
        setFullName(modelledFeature.getFullName());
        setInterpro(modelledFeature.getInterpro());
        setType(modelledFeature.getType());
        setRole(modelledFeature.getRole());
        setAc(CommonUtility.extractAc(modelledFeature));
        setUniqueKey(createUniqueKey(modelledFeature));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        if (!callingClass.contains("GraphModelledEntity")) {
            setParticipant(modelledFeature.getParticipant());
        }

        setIdentifiers(modelledFeature.getIdentifiers());
        setXrefs(modelledFeature.getXrefs());
        setAnnotations(modelledFeature.getAnnotations());
        setRanges(modelledFeature.getRanges());
        setAliases(modelledFeature.getAliases());

        if (!wasInitializedBefore) {
            setLinkedFeatures(modelledFeature.getLinkedFeatures());
        }

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getShortName() != null) nodeProperties.put("shortName", this.getShortName());
            if (this.getFullName() != null) nodeProperties.put("fullName", this.getFullName());
            if (this.getInterpro() != null) nodeProperties.put("interpro", this.getInterpro());

            Label[] labels = CommonUtility.getLabels(GraphModelledFeature.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(type, this.graphId, RelationshipTypes.TYPE);
        CommonUtility.createRelationShip(interpro, this.graphId, RelationshipTypes.INTERPRO);
        CommonUtility.createRelationShip(role, this.graphId, RelationshipTypes.ROLE);
        CommonUtility.createRelationShip(participant, this.graphId, RelationshipTypes.PARTICIPANT_FEATURE);
        CommonUtility.createIdentifierRelationShips(identifiers, this.graphId);
        CommonUtility.createXrefRelationShips(xrefs, this.graphId);
        CommonUtility.createAnnotationRelationShips(annotations, this.graphId);
        CommonUtility.createModelledRangeRelationShips(ranges, this.graphId);
        CommonUtility.createAliasRelationShips(aliases, this.graphId);
        CommonUtility.createModelledFeatureRelationShips(linkedFeatures, this.graphId, RelationshipTypes.LINKED_FEATURES);
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInterpro() {
        return this.interpro != null ? this.interpro.getId() : null;
    }

    /**
     * {@inheritDoc}
     */
    public void setInterpro(String interpro) {
        Collection<GraphXref> featureIdentifiers = getIdentifiers();

        // add new interpro if not null
        if (interpro != null) {
            CvTerm interproDatabase = CvTermUtils.createInterproDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old chebi if not null
            if (this.interpro != null) {
                featureIdentifiers.remove(this.interpro);
            }
            this.interpro = new GraphXref(new GraphXref(interproDatabase, interpro, identityQualifier));
            featureIdentifiers.add(this.interpro);
        }
        // remove all interpro if the collection is not empty
        else if (!featureIdentifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(featureIdentifiers, Xref.INTERPRO_MI, Xref.INTERPRO);
            this.interpro = null;
        }
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

    public Collection<GraphXref> getXrefs() {
        if (xrefs == null) {
            this.xrefs = new ArrayList<GraphXref>();
        }
        return this.xrefs;
    }

    //TODO Review it shoudl use PublicationXrefList I guess
    public void setXrefs(Collection<Xref> xrefs) {
        if (xrefs != null) {
            this.xrefs = CollectionAdaptor.convertXrefIntoGraphModel(xrefs);
        } else {
            this.xrefs = new ArrayList<GraphXref>();
        }
    }

    public Collection<GraphAnnotation> getAnnotations() {
        if (this.annotations == null) {
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

    public CvTerm getType() {
        return this.type;
    }

    public void setType(CvTerm type) {
        if (type != null) {
            if (type instanceof GraphCvTerm) {
                this.type = (GraphCvTerm) type;
            } else {
                this.type = new GraphCvTerm(type, false);
            }
        } else {
            this.type = null;
        }
    }

    public Collection<GraphModelledRange> getRanges() {
        if (this.ranges == null) {
            this.ranges = new ArrayList<GraphModelledRange>();
        }
        return this.ranges;
    }

    public void setRanges(Collection<Range> ranges) {
        if (ranges != null) {
            this.ranges = CollectionAdaptor.convertModelledRangeIntoGraphModel(ranges, this.getUniqueKey());
        } else {
            this.ranges = new ArrayList<GraphModelledRange>();
        }
    }

    public CvTerm getRole() {
        return this.role;
    }

    public void setRole(CvTerm role) {
        if (role != null) {
            if (role instanceof GraphCvTerm) {
                this.role = (GraphCvTerm) role;
            } else {
                this.role = new GraphCvTerm(role, false);
            }
        } else {
            this.role = null;
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

    @Override
    public ModelledEntity getParticipant() {
        return this.participant;
    }

    @Override
    public void setParticipant(ModelledEntity participant) {
        if (participant != null) {
            if (participant instanceof GraphModelledEntity) {
                this.participant = (GraphModelledEntity) participant;
            } else if (participant instanceof ModelledParticipant) {
                this.participant = new GraphModelledParticipant((ModelledParticipant) participant);
            }
        } else {
            this.participant = null;
        }
    }

    @Override
    public void setParticipantAndAddFeature(ModelledEntity participant) {
        if (this.participant != null) {
            this.participant.removeFeature(this);
        }

        if (participant != null) {
            participant.addFeature(this);
        }
    }

    public Collection<GraphModelledFeature> getLinkedFeatures() {
        if (this.linkedFeatures == null) {
            this.linkedFeatures = new ArrayList<GraphModelledFeature>();
        }
        return this.linkedFeatures;
    }

    public void setLinkedFeatures(Collection<ModelledFeature> linkedFeatures) {
        if (linkedFeatures != null) {
            this.linkedFeatures = CollectionAdaptor.convertModelledFeatureIntoGraphModel(linkedFeatures);
        } else {
            this.linkedFeatures = new ArrayList<GraphModelledFeature>();
        }
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }


    public String createUniqueKey(ModelledFeature modelledFeature) {
        return UniqueKeyGenerator.createModelledFeatureKey(modelledFeature);
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }
}
