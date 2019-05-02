package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import psidev.psi.mi.jami.model.CooperativityEvidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Publication;
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
public class GraphCooperativityEvidence implements CooperativityEvidence {

    @GraphId
    private Long graphId;

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;

    @Relationship(type = RelationshipTypes.PUBLICATION)
    private GraphPublication publication;

    @Relationship(type = RelationshipTypes.EVIDENCE_METHODS)
    private Collection<GraphCvTerm> evidenceMethods;

    @Transient
    private boolean isAlreadyCreated;

    public GraphCooperativityEvidence() {

    }

    public GraphCooperativityEvidence(CooperativityEvidence cooperativityEvidence) {
        setAc(CommonUtility.extractAc(cooperativityEvidence));
        setPublication(cooperativityEvidence.getPublication());
        setUniqueKey(createUniqueKey(cooperativityEvidence));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setEvidenceMethods(cooperativityEvidence.getEvidenceMethods());

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

            Label[] labels = CommonUtility.getLabels(GraphCooperativityEvidence.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(publication, this.graphId, RelationshipTypes.PUBLICATION);
        CommonUtility.createEvidenceMethodRelationShips(evidenceMethods, this.graphId);
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

    public Collection<GraphCvTerm> getEvidenceMethods() {
        if (this.evidenceMethods == null) {
            this.evidenceMethods = new ArrayList<GraphCvTerm>();
        }
        return this.evidenceMethods;
    }

    public void setEvidenceMethods(Collection<CvTerm> detectionMethods) {
        if (detectionMethods != null) {
            this.evidenceMethods = CollectionAdaptor.convertCvTermIntoGraphModel(detectionMethods);
        } else {
            this.evidenceMethods = new ArrayList<GraphCvTerm>();
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

    public String createUniqueKey(CooperativityEvidence cooperativityEvidence) {
        return UniqueKeyGenerator.createCooperativeEvidenceKey(cooperativityEvidence);
    }
}
