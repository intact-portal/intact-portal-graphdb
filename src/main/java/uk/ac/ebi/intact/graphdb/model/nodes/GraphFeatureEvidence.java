package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.*;

/**
 * Created by anjali on 21/11/17.
 */
@NodeEntity
public class GraphFeatureEvidence extends GraphFeature<ExperimentalEntity, FeatureEvidence> implements FeatureEvidence {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    @Relationship(type = RelationshipTypes.DETECTION_METHOD)
    private Collection<GraphCvTerm> detectionMethods;

    @Relationship(type = RelationshipTypes.PARAMETERS)
    private Collection<GraphParameter> parameters;

    @Transient
    private boolean isAlreadyCreated;

    public GraphFeatureEvidence() {

    }

    public GraphFeatureEvidence(FeatureEvidence featureEvidence) {
        super(featureEvidence, true);
        boolean wasInitializedBefore = false;
        String callingClass = Arrays.toString(Thread.currentThread().getStackTrace());
        setUniqueKey(createUniqueKey(featureEvidence));

        if (GraphEntityCache.featureCacheMap.get(this.getUniqueKey()) == null) {
            GraphEntityCache.featureCacheMap.put(this.getUniqueKey(), this);
        } else {
            wasInitializedBefore = true;
        }

        if (CreationConfig.createNatively) {
            this.createNodeNatively();
        }

        setDetectionMethods(featureEvidence.getDetectionMethods());
        setParameters(featureEvidence.getParameters());
        if (!wasInitializedBefore) {
            setParticipant(featureEvidence.getParticipant());// to avoid looping
            setLinkedFeatures(((Feature) featureEvidence).getLinkedFeatures());// to avoid looping
        }
        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated()) {
                this.createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            nodeProperties.putAll(super.getNodeProperties());

            Label[] labels = CommonUtility.getLabels(GraphFeatureEvidence.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createDetectionMethodRelationShips(detectionMethods, this.graphId);
        CommonUtility.createParameterRelationShips(parameters, this.graphId);
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

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
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

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }


    public String createUniqueKey(FeatureEvidence featureEvidence) {
        // TODO... Check and use the super one if possible
        // Ans: super won't exist later
        return UniqueKeyGenerator.createFeatureKey(featureEvidence);
    }

}
