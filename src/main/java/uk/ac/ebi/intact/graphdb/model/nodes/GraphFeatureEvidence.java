package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphFeatureEvidence implements FeatureEvidence {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String shortName;
    private String fullName;
    private String interpro;
    private GraphCvTerm type;
    private GraphCvTerm role;
    private GraphExperimentalEntity participant;

    private Collection<GraphCvTerm> detectionMethods;
    private Collection<GraphParameter> parameters;
    // private GraphXref interpro; // To do
    private Collection<GraphXref> identifiers;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private Collection<GraphRange> ranges;
    private Collection<GraphAlias> aliases;
    private Collection<GraphFeatureEvidence> linkedFeatures;

    @Transient
    private boolean isAlreadyCreated;

    public GraphFeatureEvidence() {

    }

    public GraphFeatureEvidence(FeatureEvidence featureEvidence) {

        setShortName(featureEvidence.getShortName());
        setFullName(featureEvidence.getFullName());
        setInterpro(featureEvidence.getInterpro());
        setType(featureEvidence.getType());
        setRole(featureEvidence.getRole());
        setParticipant(featureEvidence.getParticipant());
        setUniqueKey(this.toString());

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setDetectionMethods(featureEvidence.getDetectionMethods());
        setParameters(featureEvidence.getParameters());
        setIdentifiers(featureEvidence.getIdentifiers());
        setXrefs(featureEvidence.getXrefs());
        setAnnotations(featureEvidence.getAnnotations());
        setRanges(featureEvidence.getRanges());
        setAliases(featureEvidence.getAliases());

        if (GraphEntityCache.featureCacheMap.get(featureEvidence.getShortName()) == null) {
            GraphEntityCache.featureCacheMap.put(featureEvidence.getShortName(), this);
            setLinkedFeatures(featureEvidence.getLinkedFeatures());
        }



        if (CreationConfig.createNatively) {
            if(!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getShortName() != null) nodeProperties.put("shortName", this.getShortName());
            if (this.getFullName() != null) nodeProperties.put("fullName", this.getFullName());
            if (this.getInterpro() != null) nodeProperties.put("interpro", this.getInterpro());

            Label[] labels = CommonUtility.getLabels(GraphFeatureEvidence.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(type, this.graphId, "type");
        CommonUtility.createRelationShip(role, this.graphId, "role");
        CommonUtility.createRelationShip(participant, this.graphId, "participant");
        CommonUtility.createCvTermRelationShips(detectionMethods, this.graphId, "detectionMethods");
        CommonUtility.createParameterRelationShips(parameters, this.graphId, "parameters");
        CommonUtility.createXrefRelationShips(identifiers, this.graphId, "identifiers");
        CommonUtility.createXrefRelationShips(xrefs, this.graphId, "xrefs");
        CommonUtility.createAnnotationRelationShips(annotations, this.graphId, "annotations");
        CommonUtility.createRangeRelationShips(ranges, this.graphId, "ranges");
        CommonUtility.createAliasRelationShips(aliases, this.graphId, "aliases");
        CommonUtility.createFeatureEvidenceRelationShips(linkedFeatures, this.graphId, "linkedFeatures");
    }


    public Collection<GraphCvTerm> getDetectionMethods() {
        if (this.detectionMethods == null) {
            this.detectionMethods = new ArrayList<GraphCvTerm>();
        }
        return this.detectionMethods;
    }

    public void setDetectionMethods(Collection<CvTerm> detectionMethods) {
        if (detectionMethods != null) {
            this.detectionMethods = CollectionAdaptor.convertCvTermIntoGraphModel(detectionMethods);
        } else {
            this.detectionMethods = new ArrayList<GraphCvTerm>();
        }
    }

    public Collection<GraphParameter> getParameters() {
        if (this.parameters == null) {
            this.parameters = new ArrayList<GraphParameter>();
        }
        return this.parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        if (parameters != null) {
            this.parameters = CollectionAdaptor.convertParameterIntoGraphModel(parameters);
        } else {
            this.parameters = new ArrayList<GraphParameter>();
        }
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
                this.type = new GraphCvTerm(type);
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
                this.role = new GraphCvTerm(role);
            }
        } else {
            this.role = null;
        }
    }

    @Override
    public ExperimentalEntity getParticipant() {
        return this.participant;
    }

    @Override
    public void setParticipant(ExperimentalEntity participant) {
        if (participant != null) {
            if (participant instanceof GraphExperimentalEntity) {
                this.participant = (GraphExperimentalEntity) participant;
            } else {
                this.participant = new GraphExperimentalEntity(participant);
            }
        } else {
            this.participant = null;
        }
    }

    @Override
    public void setParticipantAndAddFeature(ExperimentalEntity participant) {
        if (this.participant != null) {
            this.participant.removeFeature(this);
        }

        if (participant != null) {
            participant.addFeature(this);
        }
    }

    public Collection<GraphFeatureEvidence> getLinkedFeatures() {
        if (this.linkedFeatures == null) {
            this.linkedFeatures = new ArrayList<GraphFeatureEvidence>();
        }
        return this.linkedFeatures;
    }

    public void setLinkedFeatures(Collection<FeatureEvidence> linkedFeatures) {
        if (linkedFeatures != null) {
            this.linkedFeatures = CollectionAdaptor.convertFeatureEvidenceIntoGraphModel(linkedFeatures);
        } else {
            this.linkedFeatures = new ArrayList<GraphFeatureEvidence>();
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

    public String toString() {
        return "Feature: " + (this.getShortName() != null ? this.getShortName() + " " : "") + (this.getType() != null ? this.getType().toString() + " " : "") + (!this.getRanges().isEmpty() ? "(" + this.getRanges().toString() + ")" : " (-)");
    }

}
