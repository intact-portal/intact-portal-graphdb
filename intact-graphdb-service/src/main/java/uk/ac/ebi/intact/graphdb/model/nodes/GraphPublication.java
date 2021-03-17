package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@NodeEntity
public class GraphPublication extends GraphDatabaseObject implements Publication {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private String pubmedIdStr;
    private String title;
    private String journal;
    private Date publicationDate;

    @Transient
    private CurationDepth curationDepth;

    @Relationship(type = RelationshipTypes.GRAPH_CURATION_DEPTH)
    private GraphCurationDepth graphCurationDepth;

    private Date releasedDate;

    @Relationship(type = RelationshipTypes.SOURCE)
    private GraphSource source;

    @Relationship(type = RelationshipTypes.PMID)
    private GraphXref pubmedId;

    @Relationship(type = RelationshipTypes.DOI)
    private GraphXref doi;

    @Relationship(type = RelationshipTypes.IMEX_ID)
    private GraphXref imexId;

    private List<String> authors;

    @Relationship(type = RelationshipTypes.GRAPH_AUTHORS)
    private Collection<GraphAuthor> graphAuthors;

    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.PUB_EXP, direction = Relationship.OUTGOING)
    @JsonBackReference
    private Collection<GraphExperiment> experiments;

    @Transient
    private boolean isAlreadyCreated;

    public GraphPublication() {
        this.curationDepth = CurationDepth.undefined;
    }

    public GraphPublication(Publication publication) {
        setTitle(publication.getTitle());
        setJournal(publication.getJournal());
        setPublicationDate(publication.getPublicationDate());
        setCurationDepth(publication.getCurationDepth());
        setGraphCurationDepth(new GraphCurationDepth(curationDepth));
        setReleasedDate(publication.getReleasedDate());
        setSource(publication.getSource());
//        setPubmedId(publication.getPubmedId());
        setDoi(publication.getDoi());
        assignImexId(publication.getImexId());
        setAc(CommonUtility.extractAc(publication));
        setUniqueKey(createUniqueKey(publication));
        setAuthors(publication.getAuthors());

        if (CreationConfig.createNatively) {
            createNodeNatively();
        }


        initializeGraphAuthors(this.getAuthors());
        setIdentifiers(publication.getIdentifiers());
        setXrefs(publication.getXrefs());
        setAnnotations(publication.getAnnotations());
        setExperiments(publication.getExperiments());


        if (CreationConfig.createNatively) {
            createRelationShipNatively();
        }
    }

    public GraphPublication(Xref identifier) {
        this();

        if (identifier != null) {
            getIdentifiers().add(new GraphXref(identifier));
        }
    }

    public GraphPublication(Xref identifier, CurationDepth curationDepth, Source source) {
        this(identifier);
        if (curationDepth != null) {
            this.curationDepth = curationDepth;
        }
        setSource(source);
    }

    public GraphPublication(Xref identifier, String imexId, Source source) {
        this(identifier, CurationDepth.IMEx, source);
        assignImexId(imexId);
    }

    public GraphPublication(String pubmed) {
        this.curationDepth = CurationDepth.undefined;

        if (pubmed != null) {
            setPubmedId(pubmed);
        }
    }

    public GraphPublication(String pubmed, CurationDepth curationDepth, Source source) {
        this(pubmed);
        if (curationDepth != null) {
            this.curationDepth = curationDepth;
        }
        setSource(source);
    }

    public GraphPublication(String pubmed, String imexId, Source source) {
        this(pubmed, CurationDepth.IMEx, source);
        assignImexId(imexId);
    }

    public GraphPublication(String title, String journal, Date publicationDate) {
        this.title = title;
        this.journal = journal;
        this.publicationDate = publicationDate;
        this.curationDepth = CurationDepth.undefined;
    }

    public GraphPublication(String title, String journal, Date publicationDate, CurationDepth curationDepth, Source source) {
        this(title, journal, publicationDate);
        if (curationDepth != null) {
            this.curationDepth = curationDepth;
        }
        setSource(source);
    }

    public GraphPublication(String title, String journal, Date publicationDate, String imexId, Source source) {
        this(title, journal, publicationDate, CurationDepth.IMEx, source);
        assignImexId(imexId);
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;

            Map<String, Object> nodeProperties = new HashMap<String, Object>();
            if (this.getPubmedIdStr() != null) nodeProperties.put("pubmedIdStr", this.getPubmedIdStr());
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
            if (this.getTitle() != null) nodeProperties.put("title", this.getTitle());
            if (this.getJournal() != null) nodeProperties.put("journal", this.getJournal());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.UK);
            //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            if (this.getPublicationDate() != null)
                nodeProperties.put("publicationDate", dateFormat.format(this.getPublicationDate()));
            if (this.getReleasedDate() != null)
                nodeProperties.put("releasedDate", dateFormat.format(this.getReleasedDate()));

            if (this.getAuthors() != null) {
                String[] authorArray = new String[this.getAuthors().size()];
                authorArray = this.getAuthors().toArray(authorArray);
                nodeProperties.put("authors", authorArray);
            }


            Label[] labels = CommonUtility.getLabels(GraphPublication.class);

            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively() {
        CommonUtility.createRelationShip(graphCurationDepth, this.getGraphId(), RelationshipTypes.GRAPH_CURATION_DEPTH);
        CommonUtility.createRelationShip(imexId, this.getGraphId(), RelationshipTypes.IMEX_ID);
        CommonUtility.createRelationShip(source, this.getGraphId(), RelationshipTypes.SOURCE);
        CommonUtility.createRelationShip(pubmedId, this.getGraphId(), RelationshipTypes.PMID);
        CommonUtility.createRelationShip(doi, this.getGraphId(), RelationshipTypes.DOI);
        CommonUtility.createIdentifierRelationShips(identifiers, this.getGraphId());
        CommonUtility.createXrefRelationShips(xrefs, this.getGraphId());
        CommonUtility.createAnnotationRelationShips(annotations, this.getGraphId());
        CommonUtility.createExperimentRelationShips(experiments, this.getGraphId());
        CommonUtility.createAuthorRelationShips(this.getGraphAuthors(), this.getGraphId());
    }

    public void initializeAc() {
        String ac = null;

        for (Xref xref : xrefs) {
            if (xref.getDatabase() != null && xref.getDatabase().getShortName() != null & xref.getDatabase().getShortName().equals(Constants.INTACT_DB)) {
                ac = xref.getId();
            }
        }


    }


    public String getPubmedIdStr() {
        return pubmedIdStr;
    }

    public void setPubmedIdStr(String pubmedIdStr) {
        this.pubmedIdStr = pubmedIdStr;
    }

    public String getPubmedId() {
        return this.pubmedId != null ? this.pubmedId.getId() : null;
    }

    public void setPubmedId(String pubmedId) {

        setPubmedIdStr(pubmedId);
        //changed this method as it was giving problems
        Collection<GraphXref> identifiers = getIdentifiers();

        // add new pubmed if not null
        if (pubmedId != null) {
            CvTerm pubmedDatabase = CvTermUtils.createPubmedDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old pubmed if not null
            if (this.pubmedId != null) {
                identifiers.remove(this.pubmedId);
            }
            this.pubmedId = new GraphXref(new GraphXref(pubmedDatabase, pubmedId, identityQualifier));
            identifiers.add(this.pubmedId);
        }
        // remove all pubmed if the collection is not empty
        else if (!identifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(identifiers, Xref.PUBMED_MI, Xref.PUBMED);
            this.pubmedId = null;
        }
    }

    public String getDoi() {
        return this.doi != null ? this.doi.getId() : null;
    }

    public void setDoi(String doi) {
        //changed this method as it was giving problems
        Collection<GraphXref> identifiers = getIdentifiers();
        // add new doi if not null
        if (doi != null) {
            CvTerm doiDatabase = CvTermUtils.createDoiDatabase();
            CvTerm identityQualifier = CvTermUtils.createIdentityQualifier();
            // first remove old doi if not null
            if (this.doi != null) {
                identifiers.remove(this.doi);
            }
            this.doi = new GraphXref(new GraphXref(doiDatabase, doi, identityQualifier));
            identifiers.add(this.doi);
        }
        // remove all doi if the collection is not empty
        else if (!identifiers.isEmpty()) {
            XrefUtils.removeAllXrefsWithDatabase(identifiers, Xref.DOI_MI, Xref.DOI);
            this.doi = null;
        }
    }

    public Collection<GraphXref> getIdentifiers() {
        if (identifiers == null) {
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

    public String getImexId() {
        return this.imexId != null ? this.imexId.getId() : null;
    }

    public void setImexId(GraphXref imexId) {
        this.imexId = imexId;
    }

    public void assignImexId(String identifier) {
        //changed this method as it was giving problems
        Collection<GraphXref> identifiers = getXrefs();

        // add new imex if not null
        if (identifier != null) {
            CvTerm imexDatabase = CvTermUtils.createImexDatabase();
            CvTerm imexPrimaryQualifier = CvTermUtils.createImexPrimaryQualifier();
            // first remove old imex if not null
            if (this.imexId != null) {
                xrefs.remove(this.imexId);
            }
            this.imexId = new GraphXref(new GraphXref(imexDatabase, identifier, imexPrimaryQualifier));
            xrefs.add(this.imexId);

            this.curationDepth = CurationDepth.IMEx;
        } else if (this.imexId != null) {
            throw new IllegalArgumentException("The imex id has to be non null.");
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return this.journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public Date getPublicationDate() {
        return this.publicationDate;
    }

    public void setPublicationDate(Date date) {
        this.publicationDate = date;
    }


    public Collection<GraphXref> getXrefs() {
        if (xrefs == null) {
            this.xrefs = new PublicationXrefList();
        }
        return this.xrefs;
    }

    //TODO Review it shoudl use PublicationXrefList I guess
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

    public Collection<GraphExperiment> getExperiments() {
        if (experiments == null) {
            this.experiments = new ArrayList<GraphExperiment>();
        }
        return this.experiments;
    }

    public void setExperiments(Collection<Experiment> experiments) {
        if (experiments != null) {
            addAllExperiments(experiments);
        } else {
            this.experiments = new ArrayList<GraphExperiment>();
        }
    }

    public CurationDepth getCurationDepth() {
        if (this.curationDepth == null && this.getGraphCurationDepth() != null) {
            this.curationDepth = CurationDepth.valueOf(this.getGraphCurationDepth().getCurationDepth());
        }
        return this.curationDepth;
    }

    public void setCurationDepth(CurationDepth curationDepth) {

        if (imexId != null && curationDepth != null && !curationDepth.equals(CurationDepth.IMEx)) {
            throw new IllegalArgumentException("The curationDepth " + curationDepth.toString() + " is not allowed because the publication has an IMEx id so it has IMEx curation depth.");
        } else if (imexId != null && curationDepth == null) {
            throw new IllegalArgumentException("The curationDepth cannot be null/not specified because the publication has an IMEx id so it has IMEx curation depth.");
        }

        if (curationDepth == null) {
            this.curationDepth = CurationDepth.undefined;
        } else {
            this.curationDepth = curationDepth;
        }
    }

    public Date getReleasedDate() {
        return this.releasedDate;
    }

    public void setReleasedDate(Date released) {
        this.releasedDate = released;
    }

    public Source getSource() {
        return this.source;
    }

    public void setSource(Source source) {
        if (source != null) {
            if (source instanceof GraphSource) {
                this.source = (GraphSource) source;
            } else {
                this.source = new GraphSource(source);
            }
        } else {
            this.source = null;
        }
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public boolean addExperiment(Experiment exp) {
        if (exp == null) {
            return false;
        } else {
            if (getExperiments().add(new GraphExperiment(exp))) {
                exp.setPublication(this);
                return true;
            }
            return false;
        }
    }

    public boolean removeExperiment(Experiment exp) {
        if (exp == null) {
            return false;
        } else {
            if (getExperiments().remove(exp)) {
                exp.setPublication(null);
                return true;
            }
            return false;
        }
    }

    public boolean addAllExperiments(Collection<? extends Experiment> exps) {
        if (exps == null) {
            return false;
        } else {
            boolean added = false;

            for (Experiment exp : exps) {
                if (addExperiment(exp)) {
                    added = true;
                }
            }
            return added;
        }
    }

    public boolean removeAllExperiments(Collection<? extends Experiment> exps) {
        if (exps == null) {
            return false;
        } else {
            boolean removed = false;

            for (Experiment exp : exps) {
                if (removeExperiment(exp)) {
                    removed = true;
                }
            }
            return removed;
        }
    }

    protected void processAddedIdentifierEvent(Xref added) {

        // the added identifier is pubmed and it is not the current pubmed identifier
        if (pubmedId != added && XrefUtils.isXrefFromDatabase(added, Xref.PUBMED_MI, Xref.PUBMED)) {
            // the current pubmed identifier is not identity, we may want to set pubmed Identifier
            if (!XrefUtils.doesXrefHaveQualifier(pubmedId, Xref.IDENTITY_MI, Xref.IDENTITY) && !XrefUtils.doesXrefHaveQualifier(pubmedId, Xref.PRIMARY_MI, Xref.PRIMARY)) {
                // the pubmed identifier is not set, we can set the pubmed
                if (pubmedId == null) {
                    pubmedId = new GraphXref(added);
                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY) || XrefUtils.doesXrefHaveQualifier(added, Xref.PRIMARY_MI, Xref.PRIMARY)) {
                    pubmedId = new GraphXref(added);
                }
                // the added xref is secondary object and the current pubmed is not a secondary object, we reset pubmed identifier
                else if (!XrefUtils.doesXrefHaveQualifier(pubmedId, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    pubmedId = new GraphXref(added);
                }
            }
        }
        // the added identifier is doi and it is not the current doi identifier
        else if (doi != added && XrefUtils.isXrefFromDatabase(added, Xref.DOI_MI, Xref.DOI)) {
            // the current doi identifier is not identity, we may want to set doi
            if (!XrefUtils.doesXrefHaveQualifier(doi, Xref.IDENTITY_MI, Xref.IDENTITY) && !XrefUtils.doesXrefHaveQualifier(doi, Xref.PRIMARY_MI, Xref.PRIMARY)) {
                // the doi is not set, we can set the doi
                if (doi == null) {
                    doi = new GraphXref(added);
                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY) || XrefUtils.doesXrefHaveQualifier(added, Xref.PRIMARY_MI, Xref.PRIMARY)) {
                    doi = new GraphXref(added);
                }
                // the added xref is secondary object and the current doi is not a secondary object, we reset doi
                else if (!XrefUtils.doesXrefHaveQualifier(doi, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    doi = new GraphXref(added);
                }
            }
        }
    }

    protected void processRemovedIdentifierEvent(Xref removed) {
        // the removed identifier is pubmed
        if (pubmedId != null && pubmedId.equals(removed)) {
            Xref pubmedIdXref = XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), Xref.PUBMED_MI, Xref.PUBMED);
            if (pubmedIdXref != null) {
                pubmedId = new GraphXref(pubmedIdXref);
            } else {
                pubmedId = null;
            }
        }
        // the removed identifier is doi
        else if (doi != null && doi.equals(removed)) {
            Xref doiXref = XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), Xref.DOI_MI, Xref.DOI);
            if (doiXref != null) {
                doi = new GraphXref(doiXref);
            } else {
                doi = null;
            }
        }
    }

    protected void clearPropertiesLinkedToIdentifiers() {
        pubmedId = null;
        doi = null;
    }

    protected void processAddedXrefEvent(Xref added) {

        // the added identifier is imex and the current imex is not set
        if (imexId == null && XrefUtils.isXrefFromDatabase(added, Xref.IMEX_MI, Xref.IMEX)) {
            // the added xref is imex-primary
            if (XrefUtils.doesXrefHaveQualifier(added, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY)) {
                imexId = new GraphXref(added);
            }
        }
    }

    protected void processRemovedXrefEvent(Xref removed) {
        // the removed identifier is pubmed
        if (imexId != null && imexId.equals(removed)) {
            Collection<Xref> existingImex = XrefUtils.collectAllXrefsHavingDatabaseAndQualifier(getXrefs(), Xref.IMEX_MI, Xref.IMEX, Xref.IMEX_PRIMARY_MI, Xref.IMEX_PRIMARY);
            if (!existingImex.isEmpty()) {
                imexId = new GraphXref(existingImex.iterator().next());
            }
        }
    }

    protected void clearPropertiesLinkedToXrefs() {
        imexId = null;
    }

    @Override
    public String toString() {
        return "GraphPublication: " +
                (getImexId() != null ? getImexId() :
                        (getPubmedId() != null ? getPubmedId() :
                                (getDoi() != null ? getDoi() :
                                        (getTitle() != null ? getTitle() : "-"))));
    }

    public Collection<GraphAuthor> getGraphAuthors() {
        return graphAuthors;
    }

    public void setGraphAuthors(Collection<GraphAuthor> graphAuthors) {
        this.graphAuthors = graphAuthors;
    }

    @Override
    public List<String> getAuthors() {
        if (this.authors == null && this.graphAuthors != null) {
            List<String> authorsList = new ArrayList<String>();
            for (GraphAuthor graphAuthor : graphAuthors) {
                authorsList.add(graphAuthor.getAuthorName());
            }

            return authorsList;
        }
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    private void initializeGraphAuthors(List<String> authors) {
        if (graphAuthors != null) {
            this.graphAuthors = CollectionAdaptor.convertAuthorStringIntoGraphModel(authors);
        } else {
            this.graphAuthors = new ArrayList<GraphAuthor>();
        }
    }

    public GraphCurationDepth getGraphCurationDepth() {
        return graphCurationDepth;
    }

    public void setGraphCurationDepth(GraphCurationDepth graphCurationDepth) {
        this.graphCurationDepth = graphCurationDepth;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public int hashCode() {

        if (this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return this.getUniqueKey().hashCode();
        }
        return super.hashCode();
    }

    public String createUniqueKey(Publication publication) {
        return UniqueKeyGenerator.createPublicationKey(publication);
    }

    @Transient
    private class PublicationIdentifierList extends AbstractListHavingProperties<GraphXref> {
        public PublicationIdentifierList() {
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
    private class PublicationXrefList extends AbstractListHavingProperties<GraphXref> {
        public PublicationXrefList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(GraphXref added) {
            processAddedXrefEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(GraphXref removed) {
            processRemovedXrefEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToXrefs();
        }
    }

}
