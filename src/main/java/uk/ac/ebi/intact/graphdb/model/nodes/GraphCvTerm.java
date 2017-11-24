package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NodeEntity
public class GraphCvTerm implements CvTerm {

    @GraphId
    private Long id;

    private String shortName;
    private String fullName;
    private Collection<GraphXref> xrefs;
    private Collection<GraphXref> identifiers;
    private Collection<GraphAnnotation> annotations;
    private Collection<GraphAlias> synonyms;

    private String mIIdentifier;
    private String mODIdentifier;
    private String pARIdentifier;

    @Labels
    private List<String> typeLabels = new ArrayList<>();

    public GraphCvTerm() {
    }

    public GraphCvTerm(String shortName) {
        if(shortName == null) {
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
        if(ontologyId != null) {
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

    public GraphCvTerm(String shortName, String fullName, Xref ontologyId,String label) {
        this(shortName,fullName,ontologyId);
        this.getTypeLabels().add(label);
    }

    public GraphCvTerm(String shortName, String fullName, String miIdentifier,String label) {
        this(shortName, fullName,miIdentifier);
        this.getTypeLabels().add(label);
    }

    public GraphCvTerm(CvTerm cvTerm) {
        setShortName(cvTerm.getShortName());
        setFullName(cvTerm.getFullName());
        setXrefs(cvTerm.getXrefs());
        setIdentifiers(cvTerm.getIdentifiers());
        setAnnotations(cvTerm.getAnnotations());
        setSynonyms(cvTerm.getSynonyms());
        setMIIdentifier(cvTerm.getMIIdentifier());
        setMODIdentifier(cvTerm.getMODIdentifier());
        setPARIdentifier(cvTerm.getPARIdentifier());
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
        return this.identifiers;
    }

    public void setIdentifiers(Collection<Xref> identifiers) {
        if (identifiers != null) {
            this.identifiers = CollectionAdaptor.convertXrefIntoGraphModel(identifiers);
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
        return UnambiguousCvTermComparator.hashCode(this);
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

}
