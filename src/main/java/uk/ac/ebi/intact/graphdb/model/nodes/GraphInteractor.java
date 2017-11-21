package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class GraphInteractor implements psidev.psi.mi.jami.model.Interactor  {

    @GraphId
    protected Long id;

    private String shortName;
    private String fullName;
    private Collection<Xref> identifiers;
    private Collection<Checksum> checksums;
    private Collection<Xref> xrefs;
    private Collection<Annotation> annotations;
    private Collection<Alias> aliases;
    private Organism organism;
    private CvTerm interactorType;


    @Relationship(type = "INTERACT_IN", direction = Relationship.OUTGOING)
    private Collection<GraphBinaryInteraction> interactions;

    public GraphInteractor() {
    }


    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String name) {
        if (name == null || (name != null && name.length() == 0)){
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

    public Collection<Xref> getIdentifiers() {
        if (identifiers == null){
            this.identifiers = new ArrayList<Xref>();
        }
        return this.identifiers;
    }

    /**
     *
     * @return the first identifier in the list of identifiers or null if the list is empty
     */
    public Xref getPreferredIdentifier() {
        return !getIdentifiers().isEmpty() ? getIdentifiers().iterator().next() : null;
    }

    public Collection<Checksum> getChecksums() {
        if (checksums == null){
            this.checksums = new ArrayList<Checksum>();
        }
        return this.checksums;
    }

    public Collection<Xref> getXrefs() {
        if (xrefs == null){
            this.xrefs = new ArrayList<Xref>();
        }
        return this.xrefs;
    }

    public Collection<Annotation> getAnnotations() {
        if (annotations == null){
            this.annotations = new ArrayList<Annotation>();
        }
        return this.annotations;
    }

    public Collection<Alias> getAliases() {
        if (aliases == null){
            this.aliases = new ArrayList<Alias>();
        }
        return this.aliases;
    }

    public Organism getOrganism() {
        return this.organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public CvTerm getInteractorType() {
        return this.interactorType;
    }

    public void setInteractorType(CvTerm interactorType) {
        if (interactorType == null){
            this.interactorType = CvTermUtils.createUnknownInteractorType();
        }
        else {
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
                + (getInteractorType() != null ? ", " + getInteractorType().toString() : "")  ;
    }
}
