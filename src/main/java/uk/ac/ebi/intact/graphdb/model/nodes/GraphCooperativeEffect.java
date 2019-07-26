package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import psidev.psi.mi.jami.model.CooperativeEffect;
import psidev.psi.mi.jami.model.CooperativityEvidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.ModelledInteraction;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjali on 30/04/19.
 */
@NodeEntity
public class GraphCooperativeEffect  implements CooperativeEffect {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;

    @Relationship(type = RelationshipTypes.OUTCOME)
    private GraphCvTerm outCome;

    @Relationship(type = RelationshipTypes.RESPONSE)
    private GraphCvTerm response;

    @Relationship(type = RelationshipTypes.COOPERATIVE_EVIDENCES)
    private Collection<GraphCooperativityEvidence> cooperativityEvidences;

    @Relationship(type = RelationshipTypes.AFFECTED_INTERACTIONS)
    private Collection<GraphModelledInteraction> affectedInteractions;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Transient
    private boolean isAlreadyCreated;

    public GraphCooperativeEffect() {

    }

    public GraphCooperativeEffect(CooperativeEffect cooperativeEffect) {
        setAc(CommonUtility.extractAc(cooperativeEffect));
        setOutCome(cooperativeEffect.getOutCome());
        setResponse(cooperativeEffect.getResponse());
        setUniqueKey(createUniqueKey(cooperativeEffect));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setCooperativityEvidences(cooperativeEffect.getCooperativityEvidences());
        setAffectedInteractions(cooperativeEffect.getAffectedInteractions());
        setAnnotations(cooperativeEffect.getAnnotations());

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated()) {
                createRelationShipNatively();
            }
        }
    }

    public void createNodeNatively() {
        try {
            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());

            Label[] labels = CommonUtility.getLabels(GraphCooperativeEffect.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(outCome, this.graphId, RelationshipTypes.OUTCOME);
        CommonUtility.createRelationShip(response, this.graphId, RelationshipTypes.RESPONSE);
        CommonUtility.createCooperativeEvidenceRelationShips(cooperativityEvidences, this.graphId);
        CommonUtility.createAffectedInteractionRelationShips(affectedInteractions, this.graphId);
        CommonUtility.createAnnotationRelationShips(annotations, this.graphId);
    }

    public Collection<GraphCooperativityEvidence> getCooperativityEvidences() {
        if (this.cooperativityEvidences == null) {
            this.cooperativityEvidences = new ArrayList<GraphCooperativityEvidence>();
        }
        return this.cooperativityEvidences;
    }

    public void setCooperativityEvidences(Collection<CooperativityEvidence> cooperativityEvidences) {
        if (cooperativityEvidences != null) {
            this.cooperativityEvidences = CollectionAdaptor.convertCooperativityEvidenceIntoGraphModel(cooperativityEvidences);
        } else {
            this.cooperativityEvidences = new ArrayList<GraphCooperativityEvidence>();
        }
    }

    public Collection<GraphModelledInteraction> getAffectedInteractions() {
        if (this.affectedInteractions == null) {
            this.affectedInteractions = new ArrayList<GraphModelledInteraction>();
        }
        return this.affectedInteractions;
    }

    public void setAffectedInteractions(Collection<ModelledInteraction> affectedInteractions) {
        if (affectedInteractions != null) {
            this.affectedInteractions = CollectionAdaptor.convertModelledInteractionIntoGraphModel(affectedInteractions);
        } else {
            this.affectedInteractions = new ArrayList<GraphModelledInteraction>();
        }
    }

    public Collection<GraphAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<GraphAnnotation> annotations) {
        this.annotations = annotations;
    }


    public CvTerm getOutCome() {
        return outCome;
    }

    public void setOutCome(CvTerm outcoutComeome) {
        if (outCome != null) {
            if (outCome instanceof GraphCvTerm) {
                this.outCome = (GraphCvTerm) outCome;
            } else {
                this.outCome = new GraphCvTerm(outCome, false);
            }
        } else {
            this.outCome = null;
        }
    }

    @Override
    public CvTerm getResponse() {
        return response;
    }

    public void setResponse(CvTerm response) {
        if (response != null) {
            if (response instanceof GraphCvTerm) {
                this.response = (GraphCvTerm) response;
            } else {
                this.response = new GraphCvTerm(response, false);
            }
        } else {
            this.response = null;
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

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(CooperativeEffect cooperativeEffect) {
        return UniqueKeyGenerator.createCooperativeEffectKey(cooperativeEffect);
    }
}
