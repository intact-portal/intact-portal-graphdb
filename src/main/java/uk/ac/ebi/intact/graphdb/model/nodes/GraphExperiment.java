package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class GraphExperiment implements Experiment {

    @GraphId
    private Long graphId;

    private GraphPublication publication;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private GraphCvTerm interactionDetectionMethod;
    private GraphOrganism hostOrganism;
    //private Collection<InteractionEvidence> interactions;

    private Collection<GraphConfidence> confidences;
    private Collection<GraphVariableParameter> variableParameters;

    public GraphExperiment() {
    }

    public GraphExperiment(Experiment experiment) {
        setPublication(experiment.getPublication());
        setXrefs(experiment.getXrefs());
        setAnnotations(experiment.getAnnotations());
        setInteractionDetectionMethod(experiment.getInteractionDetectionMethod());
        setHostOrganism(experiment.getHostOrganism());
        setConfidences(experiment.getConfidences());
        setVariableParameters(experiment.getVariableParameters());
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
                this.interactionDetectionMethod = new GraphCvTerm(interactionDetectionMethod);
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

    @Override
    public String toString() {
        return "Experiment: "
                + (getPublication() != null ? getPublication().toString() : "no publication")
                + "( " + getInteractionDetectionMethod().toString()
                + (getHostOrganism() != null ? ", " + getHostOrganism().toString() : "") + " )";
    }
}
