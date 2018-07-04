package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.NucleicAcid;
import psidev.psi.mi.jami.model.Organism;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@NodeEntity
public class GraphNucleicAcid extends GraphPolymer implements NucleicAcid {

    @GraphId
    private Long graphId;


    private String uniqueKey;

    private GraphXref ddbjEmblGenbank;
    private GraphXref refseq;

    @Transient
    private boolean isAlreadyCreated;

    public GraphNucleicAcid() {
        super();
    }

    public GraphNucleicAcid(NucleicAcid nucleicAcid) {
        super(nucleicAcid,true);
        setDdbjEmblGenbank(nucleicAcid.getDdbjEmblGenbank());
        setRefseq(nucleicAcid.getRefseq());
        setUniqueKey(createUniqueKey());

        if (CreationConfig.createNatively) {
            createNodeNatively();
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
            nodeProperties.putAll(super.getNodeProperties());
            Label[] labels = CommonUtility.getLabels(GraphNucleicAcid.class);

            NodeDataFeed nodeDataFeed=CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(ddbjEmblGenbank, this.graphId, "ddbjEmblGenbank");
        CommonUtility.createRelationShip(refseq, this.graphId, "refseq");
    }

    public GraphNucleicAcid(String name, CvTerm type) {
        super(name, type != null ? type : CvTermUtils.createNucleicAcidInteractorType());
    }

    public GraphNucleicAcid(String name, String fullName, CvTerm type) {
        super(name, fullName, type != null ? type : CvTermUtils.createNucleicAcidInteractorType());
    }

    public GraphNucleicAcid(String name, CvTerm type, Organism organism) {
        super(name, type != null ? type : CvTermUtils.createNucleicAcidInteractorType(), organism);
    }

    public GraphNucleicAcid(String name, String fullName, CvTerm type, Organism organism) {
        super(name, fullName, type != null ? type : CvTermUtils.createNucleicAcidInteractorType(), organism);
    }

    public GraphNucleicAcid(String name, CvTerm type, Xref uniqueId) {
        super(name, type != null ? type : CvTermUtils.createNucleicAcidInteractorType(), uniqueId);
    }

    public GraphNucleicAcid(String name, String fullName, CvTerm type, Xref uniqueId) {
        super(name, fullName, type != null ? type : CvTermUtils.createNucleicAcidInteractorType(), uniqueId);
    }

    public GraphNucleicAcid(String name, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, type != null ? type : CvTermUtils.createNucleicAcidInteractorType(), organism, uniqueId);
    }

    public GraphNucleicAcid(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, fullName, type != null ? type : CvTermUtils.createNucleicAcidInteractorType(), organism, uniqueId);
    }

    public GraphNucleicAcid(String name) {
        super(name, CvTermUtils.createNucleicAcidInteractorType());
    }

    public GraphNucleicAcid(String name, String fullName) {
        super(name, fullName, CvTermUtils.createNucleicAcidInteractorType());
    }

    public GraphNucleicAcid(String name, Organism organism) {
        super(name, CvTermUtils.createNucleicAcidInteractorType(), organism);
    }

    public GraphNucleicAcid(String name, String fullName, Organism organism) {
        super(name, fullName, CvTermUtils.createNucleicAcidInteractorType(), organism);
    }

    public GraphNucleicAcid(String name, Xref uniqueId) {
        super(name, CvTermUtils.createNucleicAcidInteractorType(), uniqueId);
    }

    public GraphNucleicAcid(String name, String fullName, Xref uniqueId) {
        super(name, fullName, CvTermUtils.createNucleicAcidInteractorType(), uniqueId);
    }

    public GraphNucleicAcid(String name, Organism organism, Xref uniqueId) {
        super(name, CvTermUtils.createNucleicAcidInteractorType(), organism, uniqueId);
    }

    public GraphNucleicAcid(String name, String fullName, Organism organism, Xref uniqueId) {
        super(name, fullName, CvTermUtils.createNucleicAcidInteractorType(), organism, uniqueId);
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    /**
     * @return The first ddbjEmblGenbank if provided, then the first refseq identifier if provided, otherwise the first identifier in the list
     */
    @Override
    public Xref getPreferredIdentifier() {
        return ddbjEmblGenbank != null ? ddbjEmblGenbank : (refseq != null ? refseq : super.getPreferredIdentifier());
    }

    public String getDdbjEmblGenbank() {
        return this.ddbjEmblGenbank != null ? this.ddbjEmblGenbank.getId() : null;
    }


    public void setDdbjEmblGenbank(String id) {
        NucleicAcidIdentifierList nucleicAcidIdentifiers = (NucleicAcidIdentifierList) getIdentifiers();

        // add new ddbj/embl/genbank if not null
        if (id != null) {
            CvTerm ddbjEmblGenbankDatabase = CvTermUtils.createDdbjEmblGenbankDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old ddbj/embl/genbank if not null
            if (this.ddbjEmblGenbank != null) {
                nucleicAcidIdentifiers.removeOnly(this.ddbjEmblGenbank);
            }
            this.ddbjEmblGenbank = new GraphXref(ddbjEmblGenbankDatabase, id, identityQualifier);
            nucleicAcidIdentifiers.addOnly(this.ddbjEmblGenbank);
        }
        // remove all ddbj/embl/genbank if the collection is not empty
        else if (!nucleicAcidIdentifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(nucleicAcidIdentifiers, Xref.DDBJ_EMBL_GENBANK_MI, Xref.DDBJ_EMBL_GENBANK);
            this.ddbjEmblGenbank = null;
        }
    }

    public String getRefseq() {
        return this.refseq != null ? this.refseq.getId() : null;
    }

    public void setRefseq(String id) {
        NucleicAcidIdentifierList nucleicAcidIdentifiers = (NucleicAcidIdentifierList) getIdentifiers();

        // add new refseq if not null
        if (id != null) {
            CvTerm refseqDatabase = CvTermUtils.createRefseqDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove refseq if not null
            if (this.refseq != null) {
                nucleicAcidIdentifiers.removeOnly(this.refseq);
            }
            this.refseq = new GraphXref(refseqDatabase, id, identityQualifier);
            nucleicAcidIdentifiers.addOnly(this.refseq);
        }
        // remove all ensembl genomes if the collection is not empty
        else if (!nucleicAcidIdentifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(nucleicAcidIdentifiers, Xref.REFSEQ_MI, Xref.REFSEQ);
            this.refseq = null;
        }
    }

    protected void processAddedIdentifiersEvent(Xref added) {
        // the added identifier is ddbj/embl/genbank and it is not the current ddbj/embl/genbank identifier
        if (ddbjEmblGenbank != added && XrefUtils.isXrefFromDatabase(added, Xref.DDBJ_EMBL_GENBANK_MI, Xref.DDBJ_EMBL_GENBANK)) {
            // the current ddbj/embl/genbank identifier is not identity, we may want to set ddbj/embl/genbank Identifier
            if (!XrefUtils.doesXrefHaveQualifier(ddbjEmblGenbank, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                // the ddbj/embl/genbank identifier is not set, we can set the ddbj/embl/genbank identifier
                if (ddbjEmblGenbank == null) {
                    ddbjEmblGenbank = new GraphXref(added);
                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                    ddbjEmblGenbank = new GraphXref(added);
                }
                // the added xref is secondary object and the current ddbj/embl/genbank identifier is not a secondary object, we reset ddbj/embl/genbank identifier
                else if (!XrefUtils.doesXrefHaveQualifier(ddbjEmblGenbank, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    ddbjEmblGenbank = new GraphXref(added);
                }
            }
        }
        // the added identifier is refseq id and it is not the current refseq id
        else if (refseq != added && XrefUtils.isXrefFromDatabase(added, Xref.REFSEQ_MI, Xref.REFSEQ)) {
            // the current refseq id is not identity, we may want to set refseq id
            if (!XrefUtils.doesXrefHaveQualifier(refseq, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                // the refseq id is not set, we can set the refseq id
                if (refseq == null) {
                    refseq = new GraphXref(added);

                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                    refseq = new GraphXref(added);
                }
                // the added xref is secondary object and the current refseq id is not a secondary object, we reset refseq id
                else if (!XrefUtils.doesXrefHaveQualifier(refseq, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    refseq = new GraphXref(added);
                }
            }
        }
    }

    protected void processRemovedIdentifierEvent(Xref removed) {
        if (ddbjEmblGenbank != null && ddbjEmblGenbank.equals(removed)) {
            ddbjEmblGenbank = new GraphXref(XrefUtils.collectFirstIdentifierWithDatabase(
                    getIdentifiers(),
                    Xref.DDBJ_EMBL_GENBANK_MI,
                    Xref.DDBJ_EMBL_GENBANK));
        } else if (refseq != null && refseq.equals(removed)) {
            refseq = new GraphXref(XrefUtils.collectFirstIdentifierWithDatabase(
                    getIdentifiers(),
                    Xref.REFSEQ_MI,
                    Xref.REFSEQ));
        }
    }

    protected void clearPropertiesLinkedToIdentifiers() {
        ddbjEmblGenbank = null;
        refseq = null;
    }

    @Override
    /**
     * Sets the interactor type of this NucleicAcid.
     * If the interactor type is null, it will set the interactor type to nucleic acid (MI:0318)
     */
    public void setInteractorType(CvTerm interactorType) {
        if (interactorType == null) {
            super.setInteractorType(CvTermUtils.createNucleicAcidInteractorType());
        } else {
            super.setInteractorType(interactorType);
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

    @Override
    public String toString() {
        return "Nucleic acid: "
                + (getDdbjEmblGenbank() != null ? getDdbjEmblGenbank()
                : (getRefseq() != null ? getRefseq() : super.toString()));
    }

    @Override
    public int hashCode() {
        int hashcode = 31;
        hashcode = 31 * hashcode + "Nucleic acid".hashCode();
        if (this.getDdbjEmblGenbank() != null) {
            hashcode = 31 * hashcode + this.getDdbjEmblGenbank().hashCode();
        }
        if (this.getRefseq() != null) {
            hashcode = 31 * hashcode + this.getRefseq().hashCode();
        }
        return hashcode;
    }


    public String createUniqueKey(){
        return hashCode() + "";
    }

    @Transient
    private class NucleicAcidIdentifierList extends AbstractListHavingProperties<GraphXref> {
        public NucleicAcidIdentifierList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(GraphXref added) {
            processAddedIdentifiersEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(GraphXref removed) {
            processRemovedIdentifierEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToIdentifiers();
        }
    }
}
