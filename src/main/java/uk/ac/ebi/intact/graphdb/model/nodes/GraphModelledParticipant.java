package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import psidev.psi.mi.jami.listener.EntityInteractorChangeListener;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.*;

/**
 * Created by anjali on 29/04/19.
 */
@NodeEntity
public class GraphModelledParticipant extends GraphModelledEntity implements ModelledParticipant {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;

    @Relationship(type = RelationshipTypes.IC_PARTICIPANT, direction = Relationship.INCOMING)
    private GraphModelledInteraction interaction;

    @Relationship(type = RelationshipTypes.BIOLOGICAL_ROLE)
    private GraphCvTerm biologicalRole;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.ALIASES)
    private Collection<GraphAlias> aliases;

    @Transient
    private EntityInteractorChangeListener changeListener;

    @Transient
    private boolean isAlreadyCreated;

    public GraphModelledParticipant() {

    }

    public GraphModelledParticipant(ModelledParticipant modelledParticipant) {
        super(modelledParticipant, true);
        String callingClasses = Arrays.toString(Thread.currentThread().getStackTrace());
        setBiologicalRole(modelledParticipant.getBiologicalRole());


        if (!callingClasses.contains("GraphModelledInteraction") && !(modelledParticipant.getInteraction() instanceof Complex)) {
            setInteraction(modelledParticipant.getInteraction());
        }
        /*if (!callingClasses.contains("GraphBinaryInteractionEvidence")) {
            setBinaryInteractionEvidence(participantEvidence.getInteraction());
        }*/
        setAc(CommonUtility.extractAc(modelledParticipant));
        setUniqueKey(createUniqueKey(modelledParticipant));

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }

        setXrefs(modelledParticipant.getXrefs());
        setAnnotations(modelledParticipant.getAnnotations());
        setAliases(modelledParticipant.getAliases());

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
            super.getNodeProperties();

            Label[] labels = CommonUtility.getLabels(GraphModelledParticipant.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(biologicalRole, this.getGraphId(), RelationshipTypes.BIOLOGICAL_ROLE);
        CommonUtility.createRelationShip(interaction, this.getGraphId(), RelationshipTypes.IC_PARTICIPANT);
        CommonUtility.createXrefRelationShips(xrefs, this.getGraphId());
        CommonUtility.createAnnotationRelationShips(annotations, this.getGraphId());
        CommonUtility.createAliasRelationShips(aliases, this.getGraphId());
    }


    public CvTerm getBiologicalRole() {
        return this.biologicalRole;
    }

    public void setBiologicalRole(CvTerm biologicalRole) {
        if (biologicalRole != null) {
            if (biologicalRole instanceof GraphCvTerm) {
                this.biologicalRole = (GraphCvTerm) biologicalRole;
            } else {
                this.biologicalRole = new GraphCvTerm(biologicalRole, false);
            }
        } else {
            this.biologicalRole = null;
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
    public ModelledInteraction getInteraction() {
        return interaction;
    }

    public void setInteraction(ModelledInteraction interaction) {
        if (interaction != null) {
            if (interaction instanceof GraphModelledInteraction) {
                this.interaction = (GraphModelledInteraction) interaction;
            } else {
                this.interaction = new GraphModelledInteraction(interaction);
            }
        } else {
            this.interaction = null;
        }
    }

    @Override
    public EntityInteractorChangeListener getChangeListener() {
        return changeListener;
    }

    @Override
    public void setChangeListener(EntityInteractorChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void setInteractionAndAddParticipant(ModelledInteraction interaction) {

        if (this.interaction != null) {
            this.interaction.removeParticipant(this);
        }

        if (interaction != null) {
            interaction.addParticipant(this);
        }
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
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

    public String createUniqueKey(ModelledParticipant modelledParticipant) {
        return UniqueKeyGenerator.createModelledParticipantKey(modelledParticipant);
    }

    @Override
    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    @Override
    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }
}
