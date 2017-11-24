package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;
import uk.ac.ebi.intact.graphdb.utils.GraphUtils;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class GraphInteractor implements Interactor {

    @GraphId
    private Long graphId;

    private String shortName;
    private String fullName;
    private Collection<GraphXref> identifiers;
    private Collection<GraphChecksum> checksums;
    private Collection<GraphXref> xrefs;
    private Collection<GraphAnnotation> annotations;
    private Collection<GraphAlias> aliases;
    private GraphOrganism organism;
    private GraphCvTerm interactorType;

    @Relationship(type = RelationshipTypes.INTERACTS_IN, direction = Relationship.OUTGOING)
    private Collection<GraphBinaryInteractionEvidence> interactions;

    public GraphInteractor() {
    }

    public GraphInteractor(Interactor interactor) {
        setShortName(interactor.getShortName());
        setFullName(interactor.getFullName());
        setIdentifiers(interactor.getIdentifiers());
        setChecksums(interactor.getChecksums());
        setAnnotations(interactor.getAnnotations());
        setAliases(interactor.getAliases());
        setOrganism(interactor.getOrganism());
        setInteractorType(interactor.getInteractorType());
    }

    public GraphInteractor(String name, CvTerm type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        setShortName(name);
        setInteractorType(type);
    }

    public GraphInteractor(String name, String fullName, CvTerm type) {
        this(name, type);
        setFullName(fullName);
    }

    public GraphInteractor(String name, CvTerm type, Organism organism) {
        this(name, type);
        setOrganism(organism);
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Organism organism) {
        this(name, fullName, type);
        setOrganism(organism);
    }

    public GraphInteractor(String name, CvTerm type, Xref uniqueId) {
        this(name, type);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Xref uniqueId) {
        this(name, fullName, type);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name, CvTerm type, Organism organism, Xref uniqueId) {
        this(name, type, organism);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId) {
        this(name, fullName, type, organism);
        getIdentifiers().add(new GraphXref(uniqueId));
    }

    public GraphInteractor(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        setShortName(name);
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName) {
        this(name);
        setFullName(fullName);
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, Organism organism) {
        this(name);
        setOrganism(organism);
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName, Organism organism) {
        this(name, fullName);
        setOrganism(organism);
    }

    public GraphInteractor(String name, Xref uniqueId) {
        this(name);
        getIdentifiers().add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName, Xref uniqueId) {
        this(name, fullName);
        getIdentifiers().add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, Organism organism, Xref uniqueId) {
        this(name, organism);
        identifiers.add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public GraphInteractor(String name, String fullName, Organism organism, Xref uniqueId) {
        this(name, fullName, organism);
        getIdentifiers().add(new GraphXref(uniqueId));
        initialiseDefaultInteractorType();
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String name) {
        if (name == null || name.isEmpty()) {
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

    /**
     * @return the first identifier in the list of identifiers or null if the list is empty
     */
    public Xref getPreferredIdentifier() {
        return !getIdentifiers().isEmpty() ? getIdentifiers().iterator().next() : null;
    }

    public Collection<GraphChecksum> getChecksums() {
        if (checksums == null) {
            this.checksums = new ArrayList<GraphChecksum>();
        }
        return this.checksums;
    }

    public void setChecksums(Collection<Checksum> checksums) {
        if (checksums != null) {
            this.checksums = CollectionAdaptor.convertChecksumIntoGraphModel(checksums);
        } else {
            this.checksums = new ArrayList<GraphChecksum>();
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

    public Organism getOrganism() {
        return this.organism;
    }

    public void setOrganism(Organism organism) {
        if (organism != null) {
            if (organism instanceof GraphOrganism) {
                this.organism = (GraphOrganism) organism;
            } else {
                this.organism = new GraphOrganism(organism);
            }
        } else {
            this.organism = null;
        }
        //TODO login it
    }

    public CvTerm getInteractorType() {
        return this.interactorType;
    }

    public void setInteractorType(CvTerm interactorType) {
        if (interactorType != null) {
            if (interactorType instanceof GraphCvTerm) {
                this.interactorType = (GraphCvTerm) interactorType;
            } else {
                this.interactorType = new GraphCvTerm(interactorType);
            }
        } else {
            initialiseDefaultInteractorType();
        }
        //TODO login it
    }

    public Collection<GraphBinaryInteractionEvidence> getInteractions() {
        return interactions;
    }

    public void setInteractions(Collection<GraphBinaryInteractionEvidence> interactions) {
        this.interactions = interactions;
    }

    private void initialiseDefaultInteractorType() {
        this.interactorType = GraphUtils.createGraphMITerm(
                Interactor.UNKNOWN_INTERACTOR,
                Interactor.UNKNOWN_INTERACTOR_MI,
                GraphUtils.INTERACTOR_TYPE_OBJCLASS);
    }

    @Override
    public String toString() {
        return getShortName()
                + (getOrganism() != null ? ", " + getOrganism().toString() : "")
                + (getInteractorType() != null ? ", " + getInteractorType().toString() : "");
    }
}
