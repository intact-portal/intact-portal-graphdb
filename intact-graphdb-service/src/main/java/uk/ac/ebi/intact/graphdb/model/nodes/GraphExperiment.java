package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.comparator.experiment.UnambiguousExperimentComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.*;

import static uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes.INTERACTION_DETECTION_METHOD;
import static uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes.PARTICIPANT_DETECTION_METHOD;

@NodeEntity
public class GraphExperiment extends GraphDatabaseObject implements Experiment {

    @Index(unique = true, primary = true)
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

    @Relationship(type = PARTICIPANT_DETECTION_METHOD)
    private GraphCvTerm participantDetectionMethod;

    @Relationship(type = RelationshipTypes.HOST_ORGANISM)
    private GraphOrganism hostOrganism;

    @Relationship(type = RelationshipTypes.EXPERIMENT, direction = Relationship.INCOMING)
    @JsonBackReference
    private Collection<InteractionEvidence> interactions;

    @Relationship(type = RelationshipTypes.CONFIDENCE)
    private Collection<GraphConfidence> confidences;

    @Relationship(type = RelationshipTypes.VARIABLE_PARAMETER)
    private Collection<GraphVariableParameter> variableParameters;

    private String pubmedId;

    @Transient
    private boolean isAlreadyCreated;

    public GraphExperiment() {
    }

    public GraphExperiment(Experiment experiment) {

        String callingClass = Arrays.toString(Thread.currentThread().getStackTrace());

        if (!callingClass.contains("GraphPublication")) {
            setPublication(experiment.getPublication());
        }
        setInteractionDetectionMethod(experiment.getInteractionDetectionMethod());
        setParticipantDetectionMethod(CommonUtility.extractParticipantDetectionMethod(experiment));
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
        addAllInteractionEvidences(experiment.getInteractionEvidences());

        boolean wasInitializedBefore = false;
        if (GraphEntityCache.experimentCacheMap.get(this.getUniqueKey()) == null) {
            GraphEntityCache.experimentCacheMap.put(this.getUniqueKey(), this);
        } else {
            wasInitializedBefore = true;
        }

        if (!wasInitializedBefore) {
            setVariableParameters(experiment.getVariableParameters());
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
            if (this.getPubmedId() != null) nodeProperties.put("pubmedId", this.getPubmedId());

            Label[] labels = CommonUtility.getLabels(GraphExperiment.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(interactionDetectionMethod, this.getGraphId(), RelationshipTypes.INTERACTION_DETECTION_METHOD);
        CommonUtility.createRelationShip(participantDetectionMethod, this.getGraphId(), RelationshipTypes.PARTICIPANT_DETECTION_METHOD);
        CommonUtility.createRelationShip(hostOrganism, this.getGraphId(), RelationshipTypes.HOST_ORGANISM);
        CommonUtility.createIncomingRelationShip(publication, this.getGraphId(), RelationshipTypes.PUB_EXP);
        CommonUtility.createXrefRelationShips(xrefs, this.getGraphId());
        CommonUtility.createAnnotationRelationShips(annotations, this.getGraphId());
        CommonUtility.createConfidenceRelationShips(confidences, this.getGraphId());
        CommonUtility.createVariableParameterRelationShips(variableParameters, this.getGraphId());
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
                this.interactionDetectionMethod = new GraphCvTerm(interactionDetectionMethod, false);
            }
        } else {
            this.interactionDetectionMethod = null;
        }
    }

    public CvTerm getParticipantDetectionMethod() {
        return this.participantDetectionMethod;
    }

    public void setParticipantDetectionMethod(CvTerm participantDetectionMethod) {
        if (participantDetectionMethod != null) {
            if (participantDetectionMethod instanceof GraphCvTerm) {
                this.participantDetectionMethod = (GraphCvTerm) participantDetectionMethod;
            } else {
                this.participantDetectionMethod = new GraphCvTerm(participantDetectionMethod, false);
            }
        } else {
            this.participantDetectionMethod = null;
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
        if (interactions == null) {
            this.interactions = new ArrayList<InteractionEvidence>();
        }
          return this.interactions;
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

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
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

    public String createUniqueKey(Experiment experiment) {
        return UniqueKeyGenerator.createExperimentKey(experiment);
    }
}
