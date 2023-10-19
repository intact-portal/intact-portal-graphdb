package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.graphdb.Label;
import org.neo4j.ogm.annotation.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import uk.ac.ebi.intact.graphdb.beans.NodeDataFeed;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;
import uk.ac.ebi.intact.graphdb.utils.CreationConfig;
import uk.ac.ebi.intact.graphdb.utils.UniqueKeyGenerator;
import uk.ac.ebi.intact.graphdb.utils.cache.GraphEntityCache;

import java.util.*;

@NodeEntity
public class GraphCvTerm extends GraphDatabaseObject implements CvTerm {

    @Index(unique = true, primary = true)
    private String uniqueKey;

    private String ac;
    private String shortName;
    private String fullName;

    @Relationship(type = RelationshipTypes.XREFS)
    private Collection<GraphXref> xrefs;

    @Relationship(type = RelationshipTypes.IDENTIFIERS)
    private Collection<GraphXref> identifiers;

    @Relationship(type = RelationshipTypes.ANNOTATIONS)
    private Collection<GraphAnnotation> annotations;

    @Relationship(type = RelationshipTypes.SYNONYMS)
    private Collection<GraphAlias> synonyms;

    private String mIIdentifier;
    private String mODIdentifier;
    private String pARIdentifier;

    @Transient
    private boolean isAlreadyCreated;
    @Transient
    private Map<String, Object> nodeProperties = new HashMap<String, Object>();

    @Labels
    private List<String> typeLabels = new ArrayList<>();

    public GraphCvTerm() {
    }

    public GraphCvTerm(CvTerm cvTerm, boolean childAlreadyCreated) {

        if (GraphEntityCache.cvTermCacheMap.get(cvTerm.getShortName()) == null) {
            GraphEntityCache.cvTermCacheMap.put(cvTerm.getShortName(), this);
        }
        setShortName(cvTerm.getShortName());
        setFullName(cvTerm.getFullName());
        setMIIdentifier(cvTerm.getMIIdentifier());
        setMODIdentifier(cvTerm.getMODIdentifier());
        setPARIdentifier(cvTerm.getPARIdentifier());
        setAc(CommonUtility.extractAc(cvTerm));
        setUniqueKey(createUniqueKey(cvTerm));

        if (CreationConfig.createNatively) {
            initialzeNodeProperties();
            if (!childAlreadyCreated) {
                createNodeNatively();
            }
        }


        setXrefs(cvTerm.getXrefs());
        setIdentifiers(cvTerm.getIdentifiers());
        setAnnotations(cvTerm.getAnnotations());
        setSynonyms(cvTerm.getSynonyms());

        if (CreationConfig.createNatively) {
            if (!isAlreadyCreated() && !childAlreadyCreated) {
                createRelationShipNatively(this.getGraphId());
            }
        }
    }


    public GraphCvTerm(String shortName) {
        if (shortName == null) {
            throw new IllegalArgumentException("The short name is required and cannot be null");
        } else {
            this.shortName = shortName;
        }
    }

    public GraphCvTerm(String shortName, String miIdentifier) {
        this(shortName);
        this.setMIIdentifier(miIdentifier);
    }

    public GraphCvTerm(String shortName, String fullName, String miIdentifier) {
        this(shortName, miIdentifier);
        this.fullName = fullName;
    }

    public GraphCvTerm(String shortName, Xref ontologyId) {
        this(shortName);
        if (ontologyId != null) {
            this.getIdentifiers().add(new GraphXref(ontologyId));
        }

    }

    public GraphCvTerm(String shortName, String fullName, Xref ontologyId) {
        this(shortName, ontologyId);
        this.fullName = fullName;
    }

    public GraphCvTerm(String shortName, Xref ontologyId, String label) {
        this(shortName, ontologyId);
        getTypeLabels().add(label);
    }

    public GraphCvTerm(String shortName, String fullName, Xref ontologyId, String label) {
        this(shortName, fullName, ontologyId);
        this.getTypeLabels().add(label);
    }

    public GraphCvTerm(String shortName, String fullName, String miIdentifier, String label) {
        this(shortName, fullName, miIdentifier);
        this.getTypeLabels().add(label);
    }

    public void initialzeNodeProperties() {

        if (this.getAc() != null) nodeProperties.put("ac", this.getAc());
        if (this.getShortName() != null) nodeProperties.put("shortName", this.getShortName());
        if (this.getFullName() != null) nodeProperties.put("fullName", this.getFullName());
        if (this.getMIIdentifier() != null) nodeProperties.put("mIIdentifier", this.getMIIdentifier());
        if (this.getMODIdentifier() != null) nodeProperties.put("mODIdentifier", this.getMODIdentifier());
        if (this.getPARIdentifier() != null) nodeProperties.put("pARIdentifier", this.getPARIdentifier());
    }

    public void createNodeNatively() {
        try {
            BatchInserter batchInserter = CreationConfig.batchInserter;
            nodeProperties.put("uniqueKey", this.getUniqueKey());
            Label[] labels = CommonUtility.getLabels(GraphCvTerm.class);

            //create node
            NodeDataFeed nodeDataFeed = CommonUtility.createNode(nodeProperties, labels);
            setGraphId(nodeDataFeed.getGraphId());
            setAlreadyCreated(nodeDataFeed.isAlreadyCreated());

            //create relationships


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRelationShipNatively(Long graphId) {
        CommonUtility.createXrefRelationShips(xrefs, graphId);
        CommonUtility.createSynonymRelationShips(synonyms, graphId);
        CommonUtility.createAnnotationRelationShips(annotations, graphId);
        CommonUtility.createIdentifierRelationShips(identifiers, graphId);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The short name cannot be null");
        }
        this.shortName = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    @Override
    public String getMIIdentifier() {
        return this.mIIdentifier;
    }

    @Override
    public void setMIIdentifier(String mi) {
        this.mIIdentifier = mi;
    }

    @Override
    public String getMODIdentifier() {
        return this.mODIdentifier;
    }

    @Override
    public void setMODIdentifier(String mod) {
        this.mODIdentifier = mod;
    }

    @Override
    public String getPARIdentifier() {
        return this.pARIdentifier;
    }

    @Override
    public void setPARIdentifier(String par) {
        this.pARIdentifier = par;
    }

    public Collection<GraphXref> getIdentifiers() {
        if (this.identifiers == null) {
            this.identifiers = new ArrayList<GraphXref>();
        }
        /*This is needed because jami always puts ac identifier at last
        * and takes the first identifier from the list for various use cases
        * so we need to put this transient ac identifier at the end of list */
        moveAcIdentifierToLast(this.identifiers);
        return this.identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        if (identifiers != null) {
            this.identifiers = CollectionAdaptor.convertXrefIntoGraphModel(identifiers);
        /*This is needed because jami always puts ac identifier at last
        * and takes the first identifier from the list for various use cases
        * so we need to put this transient ac identifier at the end of list */
            moveAcIdentifierToLast(this.identifiers);
        } else {
            this.identifiers = new ArrayList<GraphXref>();
        }
    }


    public Collection<GraphXref> getXrefs() {
        if (xrefs == null) {
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
        if (annotations == null) {
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

    public Collection<GraphAlias> getSynonyms() {
        if (synonyms == null) {
            this.synonyms = new ArrayList<GraphAlias>();
        }
        return this.synonyms;
    }

    public void setSynonyms(Collection<Alias> synonyms) {
        if (synonyms != null) {
            this.synonyms = CollectionAdaptor.convertAliasIntoGraphModel(synonyms);
        } else {
            this.synonyms = new ArrayList<GraphAlias>();
        }
    }


/*
    protected void processAddedIdentifierEvent(Xref added) {

        // the added identifier is psi-mi and it is not the current mi identifier
        if (miIdentifier != added && XrefUtils.isXrefFromDatabase(added, CvTerm.PSI_MI_MI, CvTerm.PSI_MI)) {
            // the current psi-mi identifier is not identity, we may want to set miIdentifier
            if (!XrefUtils.doesXrefHaveQualifier(miIdentifier, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                // the miidentifier is not set, we can set the miidentifier
                if (miIdentifier == null) {
                    miIdentifier = added;
                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                    miIdentifier = added;
                }
                // the added xref is secondary object and the current mi is not a secondary object, we reset miidentifier
                else if (!XrefUtils.doesXrefHaveQualifier(miIdentifier, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    miIdentifier = added;
                }
            }
        }
        // the added identifier is psi-mod and it is not the current mod identifier
        else if (modIdentifier != added && XrefUtils.isXrefFromDatabase(added, CvTerm.PSI_MOD_MI, CvTerm.PSI_MOD)) {
            // the current psi-mod identifier is not identity, we may want to set modIdentifier
            if (!XrefUtils.doesXrefHaveQualifier(modIdentifier, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                // the modIdentifier is not set, we can set the modIdentifier
                if (modIdentifier == null) {
                    modIdentifier = added;
                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                    modIdentifier = added;
                }
                // the added xref is secondary object and the current mi is not a secondary object, we reset miidentifier
                else if (!XrefUtils.doesXrefHaveQualifier(modIdentifier, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    modIdentifier = added;
                }
            }
        }
        // the added identifier is psi-par and it is not the current par identifier
        else if (parIdentifier != added && XrefUtils.isXrefFromDatabase(added, null, CvTerm.PSI_PAR)) {
            // the current psi-par identifier is not identity, we may want to set parIdentifier
            if (!XrefUtils.doesXrefHaveQualifier(parIdentifier, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                // the parIdentifier is not set, we can set the parIdentifier
                if (parIdentifier == null) {
                    parIdentifier = added;
                } else if (XrefUtils.doesXrefHaveQualifier(added, Xref.IDENTITY_MI, Xref.IDENTITY)) {
                    parIdentifier = added;
                }
                // the added xref is secondary object and the current par is not a secondary object, we reset paridentifier
                else if (!XrefUtils.doesXrefHaveQualifier(parIdentifier, Xref.SECONDARY_MI, Xref.SECONDARY)
                        && XrefUtils.doesXrefHaveQualifier(added, Xref.SECONDARY_MI, Xref.SECONDARY)) {
                    parIdentifier = added;
                }
            }
        }
    }

    protected void processRemovedIdentifierEvent(Xref removed) {
        // the removed identifier is psi-mi
        if (miIdentifier != null && miIdentifier.equals(removed)) {
            miIdentifier = XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), CvTerm.PSI_MI_MI, CvTerm.PSI_MI);
        }
        // the removed identifier is psi-mod
        else if (modIdentifier != null && modIdentifier.equals(removed)) {
            modIdentifier = XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), CvTerm.PSI_MOD_MI, CvTerm.PSI_MOD);
        }
        // the removed identifier is psi-par
        else if (parIdentifier != null && parIdentifier.equals(removed)) {
            parIdentifier = XrefUtils.collectFirstIdentifierWithDatabase(getIdentifiers(), null, CvTerm.PSI_PAR);
        }
    }
*/
/*
    protected void clearPropertiesLinkedToIdentifiers() {
        miIdentifier = null;
        modIdentifier = null;
        parIdentifier = null;
    }*/

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

        if (!(o instanceof CvTerm)) {
            return false;
        }

        return UnambiguousCvTermComparator.areEquals(this, (CvTerm) o);
    }

    @Override
    public String toString() {
        return (getMIIdentifier() != null ? getMIIdentifier() :
                (getMODIdentifier() != null ? getMODIdentifier() :
                        (getPARIdentifier() != null ? getPARIdentifier() : "-"))) + " (" + getShortName() + ")";
    }

    /*    private class CvTermIdentifierList extends AbstractListHavingProperties<Xref> {
            public CvTermIdentifierList() {
                super();
            }

            @Override
            protected void processAddedObjectEvent(Xref added) {
                processAddedIdentifierEvent(added);
            }

            @Override
            protected void processRemovedObjectEvent(Xref removed) {
                processRemovedIdentifierEvent(removed);
            }

            @Override
            protected void clearProperties() {
                clearPropertiesLinkedToIdentifiers();
            }
        }

    */
    public List<String> getTypeLabels() {
        return typeLabels;
    }

    public void setTypeLabels(List<String> typeLabels) {
        this.typeLabels = typeLabels;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String createUniqueKey(CvTerm cvTerm) {
        return UniqueKeyGenerator.createCvTermKey(cvTerm);
    }

    public void moveAcIdentifierToLast(Collection<GraphXref> graphXrefs) {

        GraphXref graphAcXrefToBeMoved = null;

        int counter = 1;
        for (GraphXref graphXref : graphXrefs) {
            if (graphXref.getId() != null && graphXref.getId().startsWith("EBI-") && counter != graphXrefs.size()) {
                graphAcXrefToBeMoved = graphXref;
            }
            counter++;
        }

        if (graphAcXrefToBeMoved != null) {
            graphXrefs.remove(graphAcXrefToBeMoved);
            graphXrefs.add(graphAcXrefToBeMoved);
        }

    }

}
