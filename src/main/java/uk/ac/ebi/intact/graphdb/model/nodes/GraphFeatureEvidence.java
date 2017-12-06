package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphFeatureEvidence implements FeatureEvidence {

    @GraphId
    private Long id;

    @Index(unique = true,primary = true)
    private String uniqueKey;

    private Collection<GraphCvTerm> detectionMethods;
    private Collection<GraphParameter> parameters;
    private String shortName;
    private String fullName;
    // private GraphXref interpro; // To do
    private String interpro;
    private Collection<GraphXref> identifiers;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private GraphCvTerm type;
    private Collection<GraphRange> ranges;
    private Collection<GraphAlias> aliases;
    private GraphCvTerm role;
    private GraphExperimentalEntity participant;
    private Collection<GraphFeatureEvidence> linkedFeatures;

    public GraphFeatureEvidence() {

    }

    public GraphFeatureEvidence(FeatureEvidence featureEvidence) {
        setDetectionMethods(featureEvidence.getDetectionMethods());
        setParameters(featureEvidence.getParameters());
        setShortName(featureEvidence.getShortName());
        setFullName(featureEvidence.getFullName());
        setInterpro(featureEvidence.getInterpro());
        setIdentifiers(featureEvidence.getIdentifiers());
        setXrefs(featureEvidence.getXrefs());
        setAnnotations(featureEvidence.getAnnotations());
        setType(featureEvidence.getType());
        setRanges(featureEvidence.getRanges());
        setAliases(featureEvidence.getAliases());
        setRole(featureEvidence.getRole());
        setParticipant(featureEvidence.getParticipant());
        setLinkedFeatures(featureEvidence.getLinkedFeatures());
        setUniqueKey(featureEvidence.toString());
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
        if (this.participant != null){
            this.participant.removeFeature(this);
        }

        if (participant != null){
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

    public String toString() {
        return "Feature: " + (this.getShortName() != null?this.getShortName() + " ":"") + (this.getType() != null?this.getType().toString() + " ":"") + (!this.getRanges().isEmpty()?"(" + this.getRanges().toString() + ")":" (-)");
    }

}
