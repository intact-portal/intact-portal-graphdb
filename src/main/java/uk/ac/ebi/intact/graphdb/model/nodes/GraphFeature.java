package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.HashCode;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 07/09/18.
 */
@NodeEntity
public class GraphFeature implements Feature {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private String shortName;
    private String fullName;
    private String interpro;

    @Relationship(type = RelationshipTypes.TYPE)
    private GraphCvTerm type;

    @Relationship(type = RelationshipTypes.ROLE)
    private GraphCvTerm role;

    @Relationship(type = RelationshipTypes.PARTICIPANT)
    private GraphEntity participant;

    // private GraphXref interpro; // To do
    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.RANGES)
    private Collection<GraphRange> ranges;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    @Relationship(type = RelationshipTypes.LINKED_FEATURES)
    private Collection<GraphFeature> linkedFeatures;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private boolean forceHashCodeGeneration;

    @Transient
    private Map<String, Object> nodeProperties = new HashMap<String, Object>();

    public GraphFeature() {

    }

    public GraphFeature(Feature featureEvidence, boolean childAlreadyCreated) {

        setForceHashCodeGeneration(true);
        boolean wasInitializedBefore = false;

        if (GraphEntityCache.featureCacheMap.get(featureEvidence.getShortName()) == null) {
            if (!childAlreadyCreated) {
                GraphEntityCache.featureCacheMap.put(featureEvidence.getShortName(), this);
            }
        } else {
            wasInitializedBefore = true;
        }
        setShortName(featureEvidence.getShortName());
        setFullName(featureEvidence.getFullName());
        setInterpro(featureEvidence.getInterpro());
        setType(featureEvidence.getType());
        setRole(featureEvidence.getRole());
        setAc(CommonUtility.extractAc(featureEvidence));
        setUniqueKey(createUniqueKey(featureEvidence));

        if (CreationConfig.createNatively) {
            initializeNodeProperties();
            if (!childAlreadyCreated) {
                createNodeNatively();
                if (!wasInitializedBefore) {
                    setLinkedFeatures(featureEvidence.getLinkedFeatures());// to avoid looping
                    setParticipant(featureEvidence.getParticipant());// to avoid looping
                }

            }
        }

        setIdentifiers(featureEvidence.getIdentifiers());
        setXrefs(featureEvidence.getXrefs());
        setAnnotations(featureEvidence.getAnnotations());
        setRanges(featureEvidence.getRanges());
        setAliases(featureEvidence.getAliases());

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated() && !childAlreadyCreated) {
                createRelationShipNatively(this.getGraphId());
            }
        }
    }

    public void initializeNodeProperties() {

        if (this.getAc() != null) getNodeProperties().put("ac", this.getAc());
        if (this.getShortName() != null) getNodeProperties().put("shortName", this.getShortName());
        if (this.getFullName() != null) getNodeProperties().put("fullName", this.getFullName());
        if (this.getInterpro() != null) getNodeProperties().put("interpro", this.getInterpro());
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());

            Label[] labels = CommonUtility.getLabels(GraphFeature.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively(Long graphId) {
        CommonUtility.createRelationShip(type, graphId, RelationshipTypes.TYPE);
        CommonUtility.createRelationShip(role, graphId, RelationshipTypes.ROLE);
        CommonUtility.createRelationShip(participant, graphId, RelationshipTypes.PARTICIPANT);
        CommonUtility.createIdentifierRelationShips(identifiers, graphId);
        CommonUtility.createXrefRelationShips(xrefs, graphId);
        CommonUtility.createAnnotationRelationShips(annotations, graphId);
        CommonUtility.createRangeRelationShips(ranges, graphId);
        CommonUtility.createAliasRelationShips(aliases, graphId);
        CommonUtility.createFeatureRelationShips(linkedFeatures, graphId, RelationshipTypes.LINKED_FEATURES);
    }


    public String getShortName() {
        return this.shortName;
    }
/*protected void initialiseParameters() {
        this.parameters = new ArrayList<Parameter>();
    }

    protected void initialiseDetectionMethods(){
        this.detectionMethods = new ArrayList<CvTerm>();
    }

    protected void initialiseDetectionMethodsWith(Collection<CvTerm> methods){
        if (methods == null){
            this.detectionMethods = Collections.EMPTY_LIST;
        }
        else {
            this.detectionMethods = methods;
        }
    }

    protected void initialiseParametersWith(Collection<Parameter> parameters) {
        if (parameters == null){
            this.parameters = Collections.EMPTY_LIST;
        }
        else {
            this.parameters = parameters;
        }
    }*/

    public void setShortName(String name) {
        this.shortName = name;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getInterpro() {
        return this.interpro != null ? this.interpro : null;
    }

    public void setInterpro(String interpro) {
       /* Collection<Xref> featureIdentifiers = getIdentifiers();

        // add new interpro if not null
        if (interpro != null){
            CvTerm interproDatabase = CvTermUtils.createInterproDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old chebi if not null
            if (this.interpro != null){
                featureIdentifiers.remove(this.interpro);
            }
            this.interpro = new DefaultXref(interproDatabase, interpro, identityQualifier);
            featureIdentifiers.add(this.interpro);
        }
        // remove all interpro if the collection is not empty
        else if (!featureIdentifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(featureIdentifiers, Xref.INTERPRO_MI, Xref.INTERPRO);
            this.interpro = null;
        }*/

        this.interpro = interpro;
    }

    /*protected void initialiseIdentifiers(){
       // this.identifiers = new AbstractFeature.FeatureIdentifierList();
    }

    protected void initialiseAnnotations(){
        this.annotations = new ArrayList<Annotation>();
    }

    protected void initialiseXrefs(){
        this.xrefs = new ArrayList<Xref>();
    }

    protected void initialiseRanges(){
        this.ranges = new ArrayList<Range>();
    }

    protected void initialiseIdentifiersWith(Collection<Xref> identifiers){
        if (identifiers == null){
            this.identifiers = Collections.EMPTY_LIST;
        }
        else {
            this.identifiers = identifiers;
        }
    }

    protected void initialiseAnnotationsWith(Collection<Annotation> annotations){
        if (annotations == null){
            this.annotations = Collections.EMPTY_LIST;
        }
        else {
            this.annotations = annotations;
        }
    }

    protected void initialiseXrefsWith(Collection<Xref> xrefs){
        if (xrefs == null){
            this.xrefs = Collections.EMPTY_LIST;
        }
        else {
            this.xrefs = xrefs;
        }
    }

    protected void initialiseRangesWith(Collection<Range> ranges){
        if (ranges == null){
            this.ranges = Collections.EMPTY_LIST;
        }
        else {
            this.ranges = ranges;
        }
    }

    protected void initialiseLinkedFeatures(){
        this.linkedFeatures = new ArrayList<FeatureEvidence>();
    }

    protected void initialiseLinkedFeaturesWith(Collection<FeatureEvidence> features){
        if (features == null){
            this.linkedFeatures = Collections.EMPTY_LIST;
        }
        else {
            this.linkedFeatures = features;
        }
    }

    protected void initialiseAliases(){
        this.aliases = new ArrayList<Alias>();
    }

    protected void initialiseAliasesWith(Collection<Alias> aliases){
        if (aliases == null){
            this.aliases = Collections.EMPTY_LIST;
        }
        else {
            this.aliases = aliases;
        }
    }*/

    public Collection<GraphXref> getIdentifiers() {
        if (this.identifiers == null) {
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
        if (this.xrefs == null) {
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

   /* public String getInterpro() {
        return this.interpro != null ? this.interpro.getId() : null;
    }*/

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

    public Collection<GraphRange> getRanges() {
        if (this.ranges == null) {
            this.ranges = new ArrayList<GraphRange>();
        }
        return this.ranges;
    }

    public void setRanges(Collection<Range> ranges) {
        if (ranges != null) {
            this.ranges = CollectionAdaptor.convertRangeIntoGraphModel(ranges);
        } else {
            this.ranges = new ArrayList<GraphRange>();
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

    @Override
    public Entity getParticipant() {
        return this.participant;
    }

    @Override
    public void setParticipant(Entity participant) {
        if (participant != null) {
            if (participant instanceof GraphEntity) {
                this.participant = (GraphEntity) participant;
            } else {
                this.participant = new GraphEntity(participant, false);
            }
        } else {
            this.participant = null;
        }
    }

    @Override
    public void setParticipantAndAddFeature(Entity participant) {
        if (this.participant != null) {
            this.participant.removeFeature(this);
        }

        if (participant != null) {
            participant.addFeature(this);
        }
    }

    public Collection<GraphFeature> getLinkedFeatures() {
        if (this.linkedFeatures == null) {
            this.linkedFeatures = new ArrayList<GraphFeature>();
        }
        return this.linkedFeatures;
    }

    public void setLinkedFeatures(Collection<Feature> linkedFeatures) {
        if (linkedFeatures != null) {
            this.linkedFeatures = CollectionAdaptor.convertFeatureIntoGraphModel(linkedFeatures);
        } else {
            this.linkedFeatures = new ArrayList<GraphFeature>();
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

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String toString() {
        return "Feature: " + (this.getShortName() != null ? this.getShortName() + " " : "") + (this.getType() != null ? this.getType().toString() + " " : "") + (!this.getRanges().isEmpty() ? "(" + this.getRanges().toString() + ")" : " (-)");
    }

    public int hashCode() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + "Feature:".hashCode();

        if (this.getShortName() != null) {
            hashcode = 31 * hashcode + this.getShortName().hashCode();
        }

        if (this.getType() != null) {
            hashcode = 31 * hashcode + this.getType().hashCode();
        }

        if (this.getRole() != null) {
            hashcode = 31 * hashcode + this.getRole().hashCode();
        }

        if (this.getInterpro() != null) {
            hashcode = 31 * hashcode + this.getInterpro().hashCode();
        }

        if (this.getIdentifiers() != null) {
            hashcode = 31 * hashcode + HashCode.identifiersGraphHashCode(this.getIdentifiers());
        }

        if (this.getRanges() != null) {
            hashcode = 31 * hashcode + HashCode.rangesGraphHashCode(this.getRanges());
        }


        return hashcode;
    }


    public String createUniqueKey(Feature featureEvidence) {
        int hashcode = HashCode.featureHashCode(featureEvidence);

        return hashcode + "";
    }

    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    public boolean isForceHashCodeGeneration() {
        return forceHashCodeGeneration;
    }

    public void setForceHashCodeGeneration(boolean forceHashCodeGeneration) {
        this.forceHashCodeGeneration = forceHashCodeGeneration;
    }
}
