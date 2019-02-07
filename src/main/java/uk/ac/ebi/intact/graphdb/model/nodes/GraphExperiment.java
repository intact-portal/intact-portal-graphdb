package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.comparator.experiment.UnambiguousExperimentComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.*;

import static uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes.INTERACTION_DETECTION_METHOD;

@NodeEntity
public class GraphExperiment implements Experiment {

    @GraphId
    private Long graphId;

    @Index(unique = true,primary = true)
    private String uniqueKey;

    private String ac;

    @Relationship(type = RelationshipTypes.PUB_EXP, direction = Relationship.INCOMING)
    @JsonManagedReference
    private GraphPublication publication;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = INTERACTION_DETECTION_METHOD)
    private GraphCvTerm interactionDetectionMethod;

    @Relationship(type = RelationshipTypes.HOST_ORGANISM)
    private GraphOrganism hostOrganism;

    //private Collection<InteractionEvidence> interactions;

    @Relationship(type = RelationshipTypes.CONFIDENCE)
    private Collection<GraphConfidence> confidences;

    @Relationship(type = RelationshipTypes.VARIABLE_PARAMETER)
    private Collection<GraphVariableParameter> variableParameters;

    private String pubmedId;

    @Transient
    private boolean isAlreadyCreated;

    @Transient
    private boolean forceHashCodeGeneration;

    public GraphExperiment() {
    }

    public GraphExperiment(Experiment experiment) {

        setForceHashCodeGeneration(true);
        String callingClass= Arrays.toString(Thread.currentThread().getStackTrace());

        if(!callingClass.contains("GraphPublication")){
            setPublication(experiment.getPublication());
        }
        setInteractionDetectionMethod(experiment.getInteractionDetectionMethod());
        setHostOrganism(experiment.getHostOrganism());
        setPubmedId(experiment.getPublication().getPubmedId());
        setAc(CommonUtility.extractAc(experiment));
        setUniqueKey(createUniqueKey(experiment));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setXrefs(experiment.getXrefs());
        setAnnotations(experiment.getAnnotations());
        setConfidences(experiment.getConfidences());

        boolean wasInitializedBefore=false;
        if (GraphEntityCache.experimentCacheMap.get(this.getUniqueKey()) == null) {
            GraphEntityCache.experimentCacheMap.put(this.getUniqueKey(), this);
        }else{
            wasInitializedBefore=true;
        }

        if (!wasInitializedBefore) {
            setVariableParameters(experiment.getVariableParameters());
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
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getPubmedId() != null) nodeProperties.put("pubmedId", this.getPubmedId());

            Label[] labels = CommonUtility.getLabels(GraphExperiment.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(interactionDetectionMethod, this.graphId,  RelationshipTypes.INTERACTION_DETECTION_METHOD);
        CommonUtility.createRelationShip(hostOrganism, this.graphId,  RelationshipTypes.HOST_ORGANISM);
        CommonUtility.createRelationShip(publication, this.graphId, RelationshipTypes.PUB_EXP);
        CommonUtility.createXrefRelationShips(xrefs, this.graphId);
        CommonUtility.createAnnotationRelationShips(annotations, this.graphId);
        CommonUtility.createConfidenceRelationShips(confidences,this.graphId);
        CommonUtility.createVariableParameterRelationShips(variableParameters,this.graphId);
    }

/*    public GraphExperiment(Publication publication) {
        this.publication = publication;
        this.interactionDetectionMethod = CvTermUtils.createUnspecifiedMethod();
    }

    public GraphExperiment(Publication publication, CvTerm interactionDetectionMethod) {
        this.publication = publication;
        if (interactionDetectionMethod == null) {
            this.interactionDetectionMethod = CvTermUtils.createUnspecifiedMethod();
        } else {
            this.interactionDetectionMethod = interactionDetectionMethod;
        }
    }

    public GraphExperiment(Publication publication, CvTerm interactionDetectionMethod, Organism organism) {
        this(publication, interactionDetectionMethod);
        this.hostOrganism = organism;
    }*/

    /*protected void initialiseXrefs() {
        this.xrefs = new ArrayList<Xref>();
    }

    protected void initialiseAnnotations() {
        this.annotations = new ArrayList<Annotation>();
    }

    *//*protected void initialiseInteractions() {
        this.interactions = new ArrayList<InteractionEvidence>();
    }*//*


    protected void initialiseConfidences() {
        this.confidences = new ArrayList<Confidence>();
    }

    protected void initialiseVariableParameters() {
        this.variableParameters = new ArrayList<VariableParameter>();
    }
*/

    public String getPubmedId() {
        return pubmedId;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Publication getPublication() {
        return this.publication;
    }

    public void setPublication(Publication publication) {
        if (publication != null) {
            if (publication instanceof GraphPublication) {
                this.publication = (GraphPublication) publication;
            } else {
                this.publication = new GraphPublication(publication);
            }
        } else {
            this.publication = null;
        }
    }

    public void setPublicationAndAddExperiment(Publication publication) {
        if (this.publication != null) {
            this.publication.removeExperiment(this);
        }

        if (publication != null) {
            publication.addExperiment(this);
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

    public Collection<GraphConfidence> getConfidences() {
        if (this.confidences == null) {
            this.confidences = new ArrayList<GraphConfidence>();
        }
        return this.confidences;
    }

    public void setConfidences(Collection<Confidence> confidences) {
        if (confidences != null) {
            this.confidences = CollectionAdaptor.convertConfidenceIntoGraphModel(confidences);
        } else {
            this.confidences = new ArrayList<GraphConfidence>();
        }
    }

    public CvTerm getInteractionDetectionMethod() {
        return this.interactionDetectionMethod;
    }

    public void setInteractionDetectionMethod(CvTerm interactionDetectionMethod) {
        if (interactionDetectionMethod != null) {
            if (interactionDetectionMethod instanceof GraphCvTerm) {
                this.interactionDetectionMethod = (GraphCvTerm) interactionDetectionMethod;
            } else {
                this.interactionDetectionMethod = new GraphCvTerm(interactionDetectionMethod,false);
            }
        } else {
            this.interactionDetectionMethod = null;
        }
    }

    public Organism getHostOrganism() {
        return this.hostOrganism;
    }

    public void setHostOrganism(Organism hostOrganism) {
        if (hostOrganism != null) {
            if (hostOrganism instanceof GraphOrganism) {
                this.hostOrganism = (GraphOrganism) hostOrganism;
            } else {
                this.hostOrganism = new GraphOrganism(hostOrganism);
            }
        } else {
            this.hostOrganism = null;
        }
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public Collection<InteractionEvidence> getInteractionEvidences() {
        /*if (interactions == null) {
            initialiseInteractions();
        }*/
        //  return this.interactions;
        return null;
    }

    public boolean addInteractionEvidence(InteractionEvidence evidence) {
        if (evidence == null) {
            return false;
        }

        if (getInteractionEvidences().add(evidence)) {
            evidence.setExperiment(this);
            return true;
        }
        return false;
    }

    public boolean removeInteractionEvidence(InteractionEvidence evidence) {
        if (evidence == null) {
            return false;
        }

        if (getInteractionEvidences().remove(evidence)) {
            evidence.setExperiment(null);
            return true;
        }
        return false;
    }

    public boolean addAllInteractionEvidences(Collection<? extends InteractionEvidence> evidences) {
        if (evidences == null) {
            return false;
        }

        boolean added = false;
        for (InteractionEvidence ev : evidences) {
            if (addInteractionEvidence(ev)) {
                added = true;
            }
        }
        return added;
    }

    public boolean removeAllInteractionEvidences(Collection<? extends InteractionEvidence> evidences) {
        if (evidences == null) {
            return false;
        }

        boolean removed = false;
        for (InteractionEvidence ev : evidences) {
            if (removeInteractionEvidence(ev)) {
                removed = true;
            }
        }
        return removed;
    }

    public Collection<GraphVariableParameter> getVariableParameters() {
        if (this.variableParameters == null) {
            this.variableParameters = new ArrayList<GraphVariableParameter>();
        }
        return variableParameters;
    }

    public void setVariableParameters(Collection<VariableParameter> variableParameters) {
        if (variableParameters != null) {
            this.variableParameters = CollectionAdaptor.convertVariableParameterIntoGraphModel(variableParameters);
        } else {
            this.variableParameters = new ArrayList<GraphVariableParameter>();
        }
    }

    public boolean addVariableParameter(VariableParameter variableParameter) {
        /*if (variableParameter == null) {
            return false;
        }

        if (getVariableParameters().add(variableParameter)) {
            variableParameter.setExperiment(this);
            return true;
        }*/
        return false;
    }

    public boolean removeVariableParameter(VariableParameter variableParameter) {
        if (variableParameter == null) {
            return false;
        }

        if (getVariableParameters().remove(variableParameter)) {
            variableParameter.setExperiment(null);
            return true;
        }
        return false;
    }

    public boolean addAllVariableParameters(Collection<? extends VariableParameter> variableParameters) {
        if (variableParameters == null) {
            return false;
        }

        boolean added = false;
        for (VariableParameter param : variableParameters) {
            if (addVariableParameter(param)) {
                added = true;
            }
        }
        return added;
    }

    public boolean removeAllVariableParameters(Collection<? extends VariableParameter> variableParameters) {
        if (variableParameters == null) {
            return false;
        }

        boolean removed = false;
        for (VariableParameter param : variableParameters) {
            if (removeVariableParameter(param)) {
                removed = true;
            }
        }
        return removed;
    }


    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    @Override
    public int hashCode() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode;
        try{
            hashcode=UnambiguousExperimentComparator.hashCode(this);
        }catch(Exception e){
            //Hash Code Could not be created, creating default ; this was needed for the cases where all values are not initialized by neo4j
            hashcode=super.hashCode();
        }
        return hashcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Experiment)) {
            return false;
        }

        return UnambiguousExperimentComparator.areEquals(this, (Experiment) o);
    }
    @Override
    public String toString() {
        return "Experiment: "
                + (getPubmedId() != null ? getPubmedId() : "no publication")
                + "( " + getInteractionDetectionMethod().toString()
                + (getHostOrganism() != null ? ", " + getHostOrganism().toString() : "") + " )";
    }

    public String createUniqueKey(Experiment experiment){
        return experiment != null ? UnambiguousExperimentComparator.hashCode(experiment) + "" : "";
    }

    public boolean isForceHashCodeGeneration() {
        return forceHashCodeGeneration;
    }

    public void setForceHashCodeGeneration(boolean forceHashCodeGeneration) {
        this.forceHashCodeGeneration = forceHashCodeGeneration;
    }
}
