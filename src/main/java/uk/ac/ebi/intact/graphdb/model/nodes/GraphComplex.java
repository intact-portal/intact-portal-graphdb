package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultAlias;
import psidev.psi.mi.jami.model.impl.DefaultAnnotation;
import psidev.psi.mi.jami.model.impl.DefaultChecksum;
import psidev.psi.mi.jami.model.impl.DefaultXref;
import psidev.psi.mi.jami.utils.*;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Created by anjali on 29/04/19.
 */
public class GraphComplex extends GraphInteractor implements Complex {


    private Date updatedDate;
    private Date createdDate;
    private GraphAnnotation physicalProperties;
    private GraphSource source;
    private GraphChecksum rigid;
    private GraphCvTerm interactionType;
    private GraphAlias recommendedName;
    private GraphAlias systematicName;
    private GraphCvTerm evidenceType;
    private GraphXref complexAcXref;
    private Collection<GraphModelledConfidence> confidences;
    private Collection<GraphModelledParameter> parameters;
    private Collection<GraphInteractionEvidence> interactionEvidences;
    private Collection<GraphModelledParticipant> components;
    private Collection<GraphCooperativeEffect> cooperativeEffects;


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


    public Collection<GraphModelledParticipant> getParticipants() {
        if (this.components == null) {
            this.components = new ArrayList<GraphModelledParticipant>();
        }
        return components;
    }

    public void setParticipants(Collection<ModelledParticipant> components) {
        if (components != null) {
            this.components = CollectionAdaptor.convertModelledParticipantIntoGraphModel(components);
        } else {
            this.components = new ArrayList<GraphModelledParticipant>();
        }
    }


    public boolean addParticipant(ModelledParticipant part) {
        if (part == null) {
            return false;
        }
        if (components == null) {
            initialiseComponents();
        }
        part.setInteraction(this);
        return components.add(new GraphModelledParticipant(part));
    }


    public boolean removeParticipant(ModelledParticipant part) {
        if (part == null) {
            return false;
        }
        if (components == null) {
            initialiseComponents();
        }
        part.setInteraction(null);
        if (components.remove(part)) {
            return true;
        }
        return false;
    }


    public boolean addAllParticipants(Collection<? extends ModelledParticipant> participants) {
        if (participants == null) {
            return false;
        }

        boolean added = false;
        for (ModelledParticipant p : participants) {
            if (addParticipant(p)) {
                added = true;
            }
        }
        return added;
    }


    public boolean removeAllParticipants(Collection<? extends ModelledParticipant> participants) {
        if (participants == null) {
            return false;
        }

        boolean removed = false;
        for (ModelledParticipant p : participants) {
            if (removeParticipant(p)) {
                removed = true;
            }
        }
        return removed;
    }


    public Collection<GraphInteractionEvidence> getInteractionEvidences() {
        if (interactionEvidences == null) {
            this.interactionEvidences = new ArrayList<GraphInteractionEvidence>();
        }
        return this.interactionEvidences;
    }

    public void setInteractionEvidences(Collection<InteractionEvidence> interactionEvidences) {
        if (interactionEvidences != null) {
            this.interactionEvidences = CollectionAdaptor.convertInteractionEvidenceIntoGraphModel(interactionEvidences);
        } else {
            this.interactionEvidences = new ArrayList<GraphInteractionEvidence>();
        }
    }


    public Collection<GraphModelledConfidence> getModelledConfidences() {
        if (confidences == null) {
            this.confidences = new ArrayList<GraphModelledConfidence>();
        }
        return this.confidences;
    }

    public void setModelledConfidences(Collection<ModelledConfidence> confidences) {
        if (confidences != null) {
            this.confidences = CollectionAdaptor.convertModelledConfidenceIntoGraphModel(confidences);
        } else {
            this.confidences = new ArrayList<GraphModelledConfidence>();
        }
    }


    public Collection<GraphModelledParameter> getModelledParameters() {
        if (parameters == null) {
            this.parameters = new ArrayList<GraphModelledParameter>();
        }
        return this.parameters;
    }

    public void setModelledParameters(Collection<ModelledParameter> modelledParameters) {
        if (parameters != null) {
            this.parameters = CollectionAdaptor.convertModelledParameterIntoGraphModel(modelledParameters);
        } else {
            this.parameters = new ArrayList<GraphModelledParameter>();
        }
    }


    public Collection<GraphCooperativeEffect> getCooperativeEffects() {
        if (cooperativeEffects == null) {
            this.cooperativeEffects = new ArrayList<GraphCooperativeEffect>();
        }
        return this.cooperativeEffects;
    }

    public void setCooperativeEffects(Collection<CooperativeEffect> cooperativeEffects) {
        if (cooperativeEffects != null) {
            this.cooperativeEffects = CollectionAdaptor.convertCooperativeEffectIntoGraphModel(cooperativeEffects);
        } else {
            this.cooperativeEffects = new ArrayList<GraphCooperativeEffect>();
        }
    }

    public String getComplexAc() {
        return this.complexAcXref != null ? this.complexAcXref.getId() : null;
    }


    public String getComplexVersion() {
        return this.complexAcXref != null ? this.complexAcXref.getVersion() : null;
    }


    public void assignComplexAc(String accession, String version) {
        // add new complex ac if not null
        if (accession != null) {
            ComplexXrefList complexXrefList = (ComplexXrefList) getXrefs();

            CvTerm complexPortalDatabase = CvTermUtils.createComplexPortalDatabase();
            CvTerm complexPortalPrimaryQualifier = CvTermUtils.createComplexPortalPrimaryQualifier();
            // first remove old ac if not null
            if (this.complexAcXref != null) {
                if (!accession.equals(complexAcXref.getId())) {
                    // first remove old complexAcXref and creates the new one;
                    complexXrefList.remove(this.complexAcXref);
                    this.complexAcXref = new GraphXref(new GraphXref(complexPortalDatabase, accession, version, complexPortalPrimaryQualifier));
                    complexXrefList.add(this.complexAcXref);
                }
            } else {
                this.complexAcXref = new GraphXref(new GraphXref(complexPortalDatabase, accession, version, complexPortalPrimaryQualifier));
                complexXrefList.add(this.complexAcXref);
            }
        } else {
            throw new IllegalArgumentException("The complex ac has to be non null.");
        }
    }


    public void assignComplexAc(String accession) {

        // add new complex ac if not null
        if (accession != null) {
            String id;
            String version;

            //It checks if the accession is valid and split the version if it is provided
            String[] splittedComplexAc = accession.split("\\.");
            if (splittedComplexAc.length == 1) {
                id = splittedComplexAc[0];
                version = "1";
            } else if (splittedComplexAc.length == 2) {
                {
                    id = splittedComplexAc[0];
                    version = splittedComplexAc[1];
                }
            } else {
                throw new IllegalArgumentException("The complex ac has a non valid format (e.g. CPX-12345.1)");
            }
            assignComplexAc(id, version);

        } else {
            throw new IllegalArgumentException("The complex ac has to be non null.");
        }
    }


    public String getPhysicalProperties() {
        return this.physicalProperties != null ? this.physicalProperties.getValue() : null;
    }


    public void setPhysicalProperties(String properties) {
        ComplexAnnotationList complexAnnotationList = (ComplexAnnotationList) getAnnotations();

        // add new physical properties if not null
        if (properties != null) {

            CvTerm complexPhysicalProperties = CvTermUtils.createComplexPhysicalProperties();
            // first remove old physical property if not null
            if (this.physicalProperties != null) {
                complexAnnotationList.removeOnly(this.physicalProperties);
            }
            this.physicalProperties = new GraphAnnotation(new GraphAnnotation(complexPhysicalProperties, properties));
            complexAnnotationList.addOnly(this.physicalProperties);
        }
        // remove all physical properties if the collection is not empty
        else if (!complexAnnotationList.isEmpty()) {
            AnnotationUtils.removeAllAnnotationsWithTopic(complexAnnotationList, Annotation.COMPLEX_PROPERTIES_MI, Annotation.COMPLEX_PROPERTIES);
            physicalProperties = null;
        }
    }


    public String getRecommendedName() {
        return this.recommendedName != null ? this.recommendedName.getName() : null;
    }

    public void setRecommendedName(String name) {
        ComplexAliasList complexAliasList = (ComplexAliasList) getAliases();

        // add new recommended name if not null
        if (name != null) {

            CvTerm recommendedName = CvTermUtils.createComplexRecommendedName();
            // first remove old recommended name if not null
            if (this.recommendedName != null) {
                complexAliasList.removeOnly(this.recommendedName);
            }
            this.recommendedName = new GraphAlias(new GraphAlias(recommendedName, name));
            complexAliasList.addOnly(this.recommendedName);
        }
        // remove all recommended name if the collection is not empty
        else if (!complexAliasList.isEmpty()) {
            AliasUtils.removeAllAliasesWithType(complexAliasList, Alias.COMPLEX_RECOMMENDED_NAME_MI, Alias.COMPLEX_RECOMMENDED_NAME);
            recommendedName = null;
        }
    }


    public String getSystematicName() {
        return this.systematicName != null ? this.systematicName.getName() : null;
    }


    public void setSystematicName(String name) {
        ComplexAliasList complexAliasList = (ComplexAliasList) getAliases();

        // add new systematic name if not null
        if (name != null) {

            CvTerm systematicName = CvTermUtils.createComplexSystematicName();
            // first remove systematic name  if not null
            if (this.systematicName != null) {
                complexAliasList.removeOnly(this.systematicName);
            }
            this.systematicName = new GraphAlias(new GraphAlias(systematicName, name));
            complexAliasList.addOnly(this.systematicName);
        }
        // remove all systematic name  if the collection is not empty
        else if (!complexAliasList.isEmpty()) {
            AliasUtils.removeAllAliasesWithType(complexAliasList, Alias.COMPLEX_SYSTEMATIC_NAME_MI, Alias.COMPLEX_SYSTEMATIC_NAME);
            systematicName = null;
        }
    }


    public CvTerm getEvidenceType() {
        return this.evidenceType;
    }


    public void setEvidenceType(CvTerm evidenceType) {
        if (evidenceType != null) {
            if (evidenceType instanceof GraphCvTerm) {
                this.evidenceType = (GraphCvTerm) evidenceType;
            } else {
                this.evidenceType = new GraphCvTerm(evidenceType,false);
            }
        } else {
            this.evidenceType = null;
        }
    }


    @Override
    public void setInteractorType(CvTerm interactorType) {
        if (interactorType == null) {
            super.setInteractorType(CvTermUtils.createComplexInteractorType());
        } else {
            super.setInteractorType(interactorType);
        }
    }


    public String getRigid() {
        return this.rigid != null ? this.rigid.getValue() : null;
    }


    public void setRigid(String rigid) {
        Collection<GraphChecksum> checksums = getChecksums();
        if (rigid != null) {
            CvTerm rigidMethod = CvTermUtils.createRigid();
            // first remove old rigid
            if (this.rigid != null) {
                checksums.remove(this.rigid);
            }
            this.rigid = new GraphChecksum(new GraphChecksum(rigidMethod, rigid));
            checksums.add(this.rigid);
        }
        // remove all smiles if the collection is not empty
        else if (!checksums.isEmpty()) {
            ChecksumUtils.removeAllChecksumWithMethod(checksums, Checksum.RIGID_MI, Checksum.RIGID);
            this.rigid = null;
        }
    }


    public Date getUpdatedDate() {
        return this.updatedDate;
    }


    public void setUpdatedDate(Date updated) {
        this.updatedDate = updated;
    }


    public Date getCreatedDate() {
        return this.createdDate;
    }


    public void setCreatedDate(Date created) {
        this.createdDate = created;
    }


    public CvTerm getInteractionType() {
        return this.interactionType;
    }


    public void setInteractionType(CvTerm interactionType) {
        if (interactionType != null) {
            if (interactionType instanceof GraphCvTerm) {
                this.interactionType = (GraphCvTerm) interactionType;
            } else {
                this.interactionType = new GraphCvTerm(interactionType, false);
            }
        } else {
            this.interactionType = null;
        }
    }


    public Collection<GraphAnnotation> getAnnotations() {
        return super.getAnnotations();
    }


    public Collection<GraphChecksum> getChecksums() {
        return super.getChecksums();
    }


    public Collection<GraphXref> getXrefs() {
        return super.getXrefs();
    }


    public Collection<Xref> getIdentifiers() {
        return super.getIdentifiers();
    }


    public Collection<GraphAlias> getAliases() {
        return super.getAliases();
    }


    @Override
    public String toString() {
        return "Complex: " + super.toString();
    }


    protected void processAddedAnnotationEvent(Annotation added) {
        if (physicalProperties == null && AnnotationUtils.doesAnnotationHaveTopic(added, Annotation.COMPLEX_PROPERTIES_MI, Annotation.COMPLEX_PROPERTIES)) {
            physicalProperties = new GraphAnnotation(added);
        }
    }


    protected void processRemovedAnnotationEvent(Annotation removed) {
        if (physicalProperties != null && physicalProperties.equals(removed)) {
            physicalProperties = new GraphAnnotation(AnnotationUtils.collectFirstAnnotationWithTopic(getAnnotations(), Annotation.COMPLEX_PROPERTIES_MI, Annotation.COMPLEX_PROPERTIES));
        }
    }


    protected void clearPropertiesLinkedToAnnotations() {
        physicalProperties = null;
    }


    protected void processAddedChecksumEvent(Checksum added) {
        if (rigid == null && ChecksumUtils.doesChecksumHaveMethod(added, Checksum.RIGID_MI, Checksum.RIGID)) {
            // the rigid is not set, we can set the rigid
            rigid = new GraphChecksum(added);
        }
    }


    protected void processRemovedChecksumEvent(Checksum removed) {
        if (rigid == removed) {
            rigid = new GraphChecksum(ChecksumUtils.collectFirstChecksumWithMethod(getChecksums(), Checksum.RIGID_MI, Checksum.RIGID));
        }
    }


    protected void clearPropertiesLinkedToChecksums() {
        rigid = null;
    }

    private class ComplexAnnotationList extends AbstractListHavingProperties<GraphAnnotation> {
        public ComplexAnnotationList() {
            super();
        }

        @Override
        protected void processAddedObjectEvent(GraphAnnotation added) {
            processAddedAnnotationEvent(added);
        }

        @Override
        protected void processRemovedObjectEvent(GraphAnnotation removed) {
            processRemovedAnnotationEvent(removed);
        }

        @Override
        protected void clearProperties() {
            clearPropertiesLinkedToAnnotations();
        }
    }

    private class ComplexChecksumList extends AbstractListHavingProperties<GraphChecksum> {
        public ComplexChecksumList() {
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

    private class ComplexAliasList extends AbstractListHavingProperties<GraphAlias> {
        public ComplexAliasList() {
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

    private class ComplexXrefList extends AbstractListHavingProperties<GraphXref> {
        public ComplexXrefList() {
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


    /**
     * <p>processAddedAliasEvent</p>
     *
     * @param added a {@link psidev.psi.mi.jami.model.Alias} object.
     */
    protected void processAddedAliasEvent(Alias added) {
        if (recommendedName == null && AliasUtils.doesAliasHaveType(added, Alias.COMPLEX_RECOMMENDED_NAME_MI, Alias.COMPLEX_RECOMMENDED_NAME)) {
            recommendedName = new GraphAlias(added);
        } else if (systematicName == null && AliasUtils.doesAliasHaveType(added, Alias.COMPLEX_SYSTEMATIC_NAME_MI, Alias.COMPLEX_SYSTEMATIC_NAME)) {
            systematicName = new GraphAlias(added);;
        }
    }

    /**
     * <p>processRemovedAliasEvent</p>
     *
     * @param removed a {@link psidev.psi.mi.jami.model.Alias} object.
     */
    protected void processRemovedAliasEvent(Alias removed) {
        if (recommendedName != null && recommendedName.equals(removed)) {
            recommendedName = new GraphAlias(AliasUtils.collectFirstAliasWithType(getAliases(), Alias.COMPLEX_RECOMMENDED_NAME_MI, Alias.COMPLEX_RECOMMENDED_NAME));
        } else if (systematicName != null && systematicName.equals(removed)) {
            systematicName = new GraphAlias(AliasUtils.collectFirstAliasWithType(getAliases(), Alias.COMPLEX_SYSTEMATIC_NAME_MI, Alias.COMPLEX_SYSTEMATIC_NAME));
        }
    }

    /**
     * <p>clearPropertiesLinkedToAliases</p>
     */
    protected void clearPropertiesLinkedToAliases() {
        this.recommendedName = null;
        this.systematicName = null;
    }

    /**
     * <p>processAddedXrefEvent</p>
     *
     * @param added a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    protected void processAddedXrefEvent(Xref added) {
        // the added identifier is a complexAcXref and the current complexAcXref is not set
        if (complexAcXref == null && XrefUtils.isXrefFromDatabase(added, Xref.COMPLEX_PORTAL_MI, Xref.COMPLEX_PORTAL)) {
            // the added xref is complex-primary
            if (XrefUtils.doesXrefHaveQualifier(added, Xref.COMPLEX_PRIMARY_MI, Xref.COMPLEX_PRIMARY)) {
                complexAcXref = new GraphXref(added);
            }
        }
    }

    /**
     * <p>processRemovedXrefEvent</p>
     *
     * @param removed a {@link psidev.psi.mi.jami.model.Xref} object.
     */
    protected void processRemovedXrefEvent(Xref removed) {
        // the removed identifier is pubmed
        if (complexAcXref != null && complexAcXref.equals(removed)) {
            Collection<Xref> existingComplexAc = XrefUtils.collectAllXrefsHavingDatabaseAndQualifier(getXrefs(), Xref.COMPLEX_PORTAL_MI, Xref.COMPLEX_PORTAL, Xref.COMPLEX_PRIMARY_MI, Xref.COMPLEX_PRIMARY);
            if (!existingComplexAc.isEmpty()) {
                complexAcXref = new GraphXref(existingComplexAc.iterator().next());
            }
        }
    }

    protected void clearPropertiesLinkedToXrefs() {
        complexAcXref = null;
    }

    /**
     * <p>initialiseParameters</p>
     */
    protected void initialiseParameters() {
        this.parameters = new ArrayList<GraphModelledParameter>();
    }

    /**
     * <p>initialiseParametersWith</p>
     *
     * @param parameters a {@link java.util.Collection} object.
     */
    protected void initialiseParametersWith(Collection<ModelledParameter> parameters) {
        if (parameters != null) {
            this.parameters = CollectionAdaptor.convertModelledParameterIntoGraphModel(parameters);
        } else {
            this.parameters = new ArrayList<GraphModelledParameter>();
        }
    }

    /**
     * <p>initialiseComponents</p>
     */
    protected void initialiseComponents() {
        this.components = new ArrayList<GraphModelledParticipant>();
    }

    /**
     * <p>initialiseComponentsWith</p>
     *
     * @param components a {@link java.util.Collection} object.
     */
    protected void initialiseComponentsWith(Collection<ModelledParticipant> components) {
        if (components != null) {
            this.components = CollectionAdaptor.convertModelledParticipantIntoGraphModel(components);
        } else {
            this.components = new ArrayList<GraphModelledParticipant>();
        }
    }


}
