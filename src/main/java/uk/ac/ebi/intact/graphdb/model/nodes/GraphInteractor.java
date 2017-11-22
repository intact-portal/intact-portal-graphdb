package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class GraphInteractor implements Interactor {

    @GraphId
    protected Long id;

    private String shortName;
    private String fullName;
    private Collection<GraphXref> identifiers;
    private Collection<GraphChecksum> checksums;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private Collection<GraphAlias> aliases;
    private GraphOrganism organism;
    private GraphCvTerm interactorType;




        @Relationship(type = "INTERACT_IN", direction = Relationship.OUTGOING)
        private Collection<GraphBinaryInteraction> interactions;

        public GraphInteractor() {
        }

        public GraphInteractor(Interactor interactor) {
            setShortName(interactor.getShortName());
            setFullName(interactor.getFullName());
            setIdentifiers(CollectionAdaptor.convertXrefIntoGraphModel(interactor.getIdentifiers()));
            setChecksums(CollectionAdaptor.convertChecksumIntoGraphModel(interactor.getChecksums()));
            setAnnotations(CollectionAdaptor.convertAnnotationIntoGraphModel(interactor.getAnnotations()));
            setAliases(CollectionAdaptor.convertAliasIntoGraphModel(interactor.getAliases()));
            setOrganism(new GraphOrganism(interactor.getOrganism()));
            setInteractorType(new GraphCvTerm(interactor.getInteractorType()));
    }


    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String name) {
        if (name == null || (name != null && name.length() == 0)) {
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        this.shortName = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public Collection<? extends Xref> getIdentifiers() {
        if (identifiers == null) {
            this.identifiers = new ArrayList<GraphXref>();
        }
        return this.identifiers;
    }

    public void setIdentifiers(Collection<GraphXref> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * @return the first identifier in the list of identifiers or null if the list is empty
     */
    public Xref getPreferredIdentifier() {
        return !getIdentifiers().isEmpty() ? getIdentifiers().iterator().next() : null;
    }

    public Collection<? extends Checksum> getChecksums() {
        if (checksums == null) {
            this.checksums = new ArrayList<GraphChecksum>();
        }
        return this.checksums;
    }

    public void setChecksums(Collection<GraphChecksum> checksums) {
        this.checksums = checksums;
    }

    public Collection<? extends Xref> getXrefs() {
        if (xrefs == null) {
            this.xrefs = new ArrayList<GraphXref>();
        }
        return this.xrefs;
    }

    public void setXrefs(Collection<GraphXref> xrefs) {
        this.xrefs = xrefs;
    }

    public Collection<? extends Annotation> getAnnotations() {
        if (annotations == null) {
            this.annotations = new ArrayList<GraphAnnotation>();
        }
        return this.annotations;
    }

    public void setAnnotations(Collection<GraphAnnotation> annotations) {
        this.annotations = annotations;
    }

    public Collection<? extends Alias> getAliases() {
        if (aliases == null) {
            this.aliases = new ArrayList<GraphAlias>();
        }
        return this.aliases;
    }

    public void setAliases(Collection<GraphAlias> aliases) {
        this.aliases = aliases;
    }

    public Organism getOrganism() {
        return this.organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = (GraphOrganism) organism;
    }

    public CvTerm getInteractorType() {
        return this.interactorType;
    }

    public void setInteractorType(CvTerm interactorType) {
        if (interactorType == null) {
            this.interactorType = CvTermUtils.createUnknownInteractorType();
        } else {
            this.interactorType = interactorType;
        }
    }

    public Collection<GraphBinaryInteraction> getInteractions() {
        return interactions;
    }

    @Override
    public String toString() {
        return getShortName()
                + (getOrganism() != null ? ", " + getOrganism().toString() : "")
                + (getInteractorType() != null ? ", " + getInteractorType().toString() : "");
    }
}
