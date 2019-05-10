package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.AliasUtils;
import psidev.psi.mi.jami.utils.ChecksumUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 26/03/2014
 * Time: 20:26
 */

@NodeEntity
public class GraphProtein extends GraphPolymer implements Protein {

    @GraphId
    private Long graphId;
    private String uniqueKey;

    private String uniprotName;

    @Relationship(type = RelationshipTypes.UNIPROTKB)
    private GraphXref uniprotkb;

    @Relationship(type = RelationshipTypes.REFSEQ)
    private GraphXref refseq;

    @Relationship(type = RelationshipTypes.GENE_NAME)
    private GraphAlias geneName;

    @Relationship(type = RelationshipTypes.ROGID)
    private GraphChecksum rogid;

    @Transient
    private boolean isAlreadyCreated;

    public GraphProtein() {
        super();
    }

    public GraphProtein(Protein protein) {
        super(protein, true);
        setUniprotkb(protein.getUniprotkb());
        setUniprotName(protein.getUniprotkb());
        setRefseq(protein.getRefseq());
        setChecksums(protein.getChecksums());
        setGeneName(protein.getGeneName());
        setUniqueKey(createUniqueKey(protein));

        if (CreationConfig.createNatively) {
            createNodeNatively();
            /*if (!isAlreadyCreated()) {*/
            createRelationShipNatively();
            //}
        }
    }

    public GraphProtein(String name, CvTerm type) {
        super(name, type != null ? type : CvTermUtils.createProteinInteractorType());
    }

    public GraphProtein(String name, String fullName, CvTerm type) {
        super(name, fullName, type != null ? type : CvTermUtils.createProteinInteractorType());
    }

    public GraphProtein(String name, CvTerm type, Organism organism) {
        super(name, type != null ? type : CvTermUtils.createProteinInteractorType(), organism);
    }

    public GraphProtein(String name, String fullName, CvTerm type, Organism organism) {
        super(name, fullName, type != null ? type : CvTermUtils.createProteinInteractorType(), organism);
    }

    public GraphProtein(String name, CvTerm type, Xref uniqueId) {
        super(name, type != null ? type : CvTermUtils.createProteinInteractorType(), uniqueId);
    }

    public GraphProtein(String name, String fullName, CvTerm type, Xref uniqueId) {
        super(name, fullName, type != null ? type : CvTermUtils.createProteinInteractorType(), uniqueId);
    }

    public GraphProtein(String name, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, type != null ? type : CvTermUtils.createProteinInteractorType(), organism, uniqueId);
    }

    public GraphProtein(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId) {
        super(name, fullName, type != null ? type : CvTermUtils.createProteinInteractorType(), organism, uniqueId);
    }

    public GraphProtein(String name) {
        super(name, CvTermUtils.createProteinInteractorType());
    }

    public GraphProtein(String name, String fullName) {
        super(name, fullName, CvTermUtils.createProteinInteractorType());
    }

    public GraphProtein(String name, Organism organism) {
        super(name, CvTermUtils.createProteinInteractorType(), organism);
    }

    public GraphProtein(String name, String fullName, Organism organism) {
        super(name, fullName, CvTermUtils.createProteinInteractorType(), organism);
    }

    public GraphProtein(String name, Xref uniqueId) {
        super(name, CvTermUtils.createProteinInteractorType(), uniqueId);
    }

    public GraphProtein(String name, String fullName, Xref uniqueId) {
        super(name, fullName, CvTermUtils.createProteinInteractorType(), uniqueId);
    }

    public GraphProtein(String name, Organism organism, Xref uniqueId) {
        super(name, CvTermUtils.createProteinInteractorType(), organism, uniqueId);
    }

    public GraphProtein(String name, String fullName, Organism organism, Xref uniqueId) {
        super(name, fullName, CvTermUtils.createProteinInteractorType(), organism, uniqueId);
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getUniprotName() != null) nodeProperties.put("uniprotName", this.getUniprotName());
            nodeProperties.putAll(super.getNodeProperties());
            Label[] labels = CommonUtility.getLabels(GraphProtein.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        super.createRelationShipNatively(this.getGraphId());
        CommonUtility.createRelationShip(uniprotkb, this.graphId, RelationshipTypes.UNIPROTKB);
        CommonUtility.createRelationShip(refseq, this.graphId, RelationshipTypes.REFSEQ);
        CommonUtility.createRelationShip(geneName, this.graphId, RelationshipTypes.GENE_NAME);
        CommonUtility.createRelationShip(rogid, this.graphId, RelationshipTypes.ROGID);
    }

    /**
     * @return The first uniprokb if provided, then the first refseq identifier if provided, otherwise the first identifier in the list
     */
    @Override
    public Xref getPreferredIdentifier() {
        return uniprotkb != null ? uniprotkb : (refseq != null ? refseq : super.getPreferredIdentifier());
    }

    public String getUniprotName() {
        return uniprotName;
    }

    public void setUniprotName(String uniprotName) {
        this.uniprotName = uniprotName;
    }

    public String getUniprotkb() {
        return this.uniprotkb != null ? this.uniprotkb.getId() : null;
    }

    public void setUniprotkb(String ac) {

        setUniprotName(ac);
        GraphProtein.ProteinIdentifierList proteinIdentifiers = (GraphProtein.ProteinIdentifierList) getIdentifiers();

        // add new uniprotkb if not null
        if (ac != null) {
            CvTerm uniprotkbDatabase = CvTermUtils.createUniprotkbDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old uniprotkb if not null
            if (this.uniprotkb != null) {
                proteinIdentifiers.removeOnly(this.uniprotkb);
            }
            this.uniprotkb = new GraphXref(new GraphXref(uniprotkbDatabase, ac, identityQualifier));
            proteinIdentifiers.addOnly(this.uniprotkb);
        }
        // remove all uniprotkb if the collection is not empty
        else if (!proteinIdentifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(proteinIdentifiers, Xref.UNIPROTKB_MI, Xref.UNIPROTKB);
            this.uniprotkb = null;
        }
    }

    public String getRefseq() {
        return this.refseq != null ? this.refseq.getId() : null;
    }

    public void setRefseq(String ac) {
        GraphProtein.ProteinIdentifierList proteinIdentifiers = (GraphProtein.ProteinIdentifierList) getIdentifiers();

        // add new refseq if not null
        if (ac != null) {
            CvTerm refseqDatabase = CvTermUtils.createRefseqDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old refseq if not null
            if (this.refseq != null) {
                proteinIdentifiers.removeOnly(this.refseq);
            }
            this.refseq = new GraphXref(new GraphXref(refseqDatabase, ac, identityQualifier));
            proteinIdentifiers.addOnly(this.refseq);
        }
        // remove all refseq if the collection is not empty
        else if (!proteinIdentifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(proteinIdentifiers, Xref.REFSEQ_MI, Xref.REFSEQ);
            this.refseq = null;
        }
    }

    public String getGeneName() {
        return this.geneName != null ? this.geneName.getName() : null;
    }

    public void setGeneName(String name) {
        GraphProtein.ProteinAliasList proteinAliases = (GraphProtein.ProteinAliasList) getAliases();

        // add new gene name if not null
        if (name != null) {
            CvTerm geneNameType = CvTermUtils.createGeneNameAliasType();
            // first remove old gene name if not null
            if (this.geneName != null) {
                proteinAliases.removeOnly(this.geneName);
            }
            this.geneName = new GraphAlias(new GraphAlias(geneNameType, name));
            proteinAliases.addOnly(this.geneName);
        }
        // remove all gene names if the collection is not empty
        else if (!proteinAliases.isEmpty()) {
            AliasUtils.removeAllAliasesWithType(proteinAliases, Alias.GENE_NAME_MI, Alias.GENE_NAME);
            this.geneName = null;
        }
    }

    public String getRogid() {
        return this.rogid != null ? this.rogid.getValue() : null;
    }

    public void setRogid(String rogid) {
        GraphProtein.ProteinChecksumList proteinChecksums = (GraphProtein.ProteinChecksumList) getChecksums();

        if (rogid != null) {
            CvTerm rogidMethod = CvTermUtils.createRogid();
            // first remove old rogid
            if (this.rogid != null) {
                proteinChecksums.removeOnly(this.rogid);
            }
            this.rogid = new GraphChecksum(new GraphChecksum(rogidMethod, rogid));
            proteinChecksums.addOnly(this.rogid);
        }
        // remove all smiles if the collection is not empty
        else if (!proteinChecksums.isEmpty()) {
            ChecksumUtils.removeAllChecksumWithMethod(proteinChecksums, Checksum.ROGID_MI, Checksum.ROGID);
            this.rogid = null;
        }
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    protected void processAddedAliasEvent(Alias added) {
        // the added alias is gene name and it is not the current gene name
        if (geneName == null && AliasUtils.doesAliasHaveType(added, Alias.GENE_NAME_MI, Alias.GENE_NAME)) {
            geneName = new GraphAlias(added);
        }
    }

    protected void processRemovedAliasEvent(Alias removed) {
        if (geneName != null && geneName.equals(removed)) {
            geneName = new GraphAlias(AliasUtils.collectFirstAliasWithType(getAliases(), Alias.GENE_NAME_MI, Alias.GENE_NAME));
        }
    }

    protected void clearPropertiesLinkedToAliases() {
        geneName = null;
    }

    protected void processAddedChecksumEvent(Checksum added) {
        if (rogid == null && ChecksumUtils.doesChecksumHaveMethod(added, Checksum.ROGID_MI, Checksum.ROGID)) {
            // the rogid is not set, we can set the rogid
            rogid = new GraphChecksum(added);
        }
    }

    protected void processRemovedChecksumEvent(Checksum removed) {
        if (rogid != null && rogid.equals(removed)) {
            rogid = new GraphChecksum(ChecksumUtils.collectFirstChecksumWithMethod(getChecksums(), Checksum.ROGID_MI, Checksum.ROGID));
        }
    }

    protected void clearPropertiesLinkedToChecksums() {
        rogid = null;
    }

    protected void processAddedIdentifierEvent(Xref added) {
        // the added identifier is uniprotkb and it is not the current uniprotkb identifier
        if (uniprotkb != added && (XrefUtils.isXrefFromDatabase(added, Xref.UNIPROTKB_MI, Xref.UNIPROTKB)
                || XrefUtils.isXrefFromDatabase(added, Xref.UNIPROTKB_SWISSPROT_MI, Xref.UNIPROTKB_SWISSPROT)
                || XrefUtils.isXrefFromDatabase(added, Xref.UNIPROTKB_TREMBL_MI, Xref.UNIPROTKB_TREMBL))) {
            // the current uniprotkb identifier is not identity, we may want to set uniprotkb Identifier
            if (!XrefUtils.doesXrefHaveQualifier(uniprotkb, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                // the uniprotkb identifier is not set, we can set the uniprotkb identifier
                if (uniprotkb == null) {
                    uniprotkb = new GraphXref(added);
                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                    uniprotkb = new GraphXref(added);
                }
                // the added xref is secondary object and the current uniprotkb identifier is not a secondary object, we reset uniprotkb identifier
                else if (!XrefUtils.doesXrefHaveQualifier(uniprotkb, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    uniprotkb = new GraphXref(added);
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
        if (uniprotkb != null && uniprotkb.equals(removed)) {
            Xref uniprotkbXref=XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), Xref.UNIPROTKB_MI, Xref.UNIPROTKB);
            if(uniprotkbXref!=null) {
                uniprotkb = new GraphXref(uniprotkbXref);
            }else{
                uniprotkb=null;
            }
            if (uniprotkb == null) {
                Xref uniprotkbSwissProtXref=XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), Xref.UNIPROTKB_SWISSPROT_MI, Xref.UNIPROTKB_SWISSPROT);
                if(uniprotkbSwissProtXref!=null) {
                    uniprotkb = new GraphXref(uniprotkbSwissProtXref);
                }else {
                    uniprotkb=null;
                }
                if (uniprotkb == null) {
                    Xref uniprotkbTremblXref= XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), Xref.UNIPROTKB_TREMBL_MI, Xref.UNIPROTKB_TREMBL);
                    if(uniprotkbTremblXref!=null) {
                        uniprotkb = new GraphXref(uniprotkbTremblXref);
                    }else {
                        uniprotkbTremblXref=null;
                    }
                }
            }
        } else if (refseq != null && refseq.equals(removed)) {
            Xref refseqXref=XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), Xref.REFSEQ_MI, Xref.REFSEQ);
            if(refseqXref!=null) {
                refseq = new GraphXref(refseqXref);
            }else{
                refseq=null;
            }
        }
    }

    protected void clearPropertiesLinkedToIdentifiers() {
        uniprotkb = null;
        refseq = null;
    }

    @Override
    /**
     * Sets the interactor type of this protein.
     * If the given interactorType is null, it will set the interactor type to 'protein' (MI:0326)
     */
    public void setInteractorType(CvTerm interactorType) {
        if (interactorType == null) {
            super.setInteractorType(CvTermUtils.createProteinInteractorType());
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

    @Override
    public String toString() {
        return "Protein"
                + (getGeneName() != null ? getGeneName() :
                (getUniprotkb() != null ? getUniprotkb() :
                        (getRefseq() != null ? getRefseq() : super.toString())));
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialiseIdentifiers() {
        initialiseIdentifiersWith(new ProteinIdentifierList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialiseChecksums() {
        initialiseChecksumsWith(new ProteinChecksumList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialiseAliases() {
        initialiseAliasesWith(new ProteinAliasList());
    }

    @Override
    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(Protein protein) {
        return UniqueKeyGenerator.createInteractorKey(protein);
    }

    @Transient
    private class ProteinIdentifierList extends AbstractListHavingProperties<GraphXref> {
        public ProteinIdentifierList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(GraphXref added) {
            processAddedIdentifierEvent(added);
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

    @Transient
    private class ProteinChecksumList extends AbstractListHavingProperties<GraphChecksum> {
        public ProteinChecksumList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(GraphChecksum added) {
            processAddedChecksumEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(GraphChecksum removed) {
            processRemovedChecksumEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToChecksums();
        }
    }

    @Transient
    private class ProteinAliasList extends AbstractListHavingProperties<GraphAlias> {
        public ProteinAliasList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(GraphAlias added) {
            processAddedAliasEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(GraphAlias removed) {
            processRemovedAliasEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToAliases();
        }
    }

}