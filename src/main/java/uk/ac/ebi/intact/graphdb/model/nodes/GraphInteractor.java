package uk.ac.ebi.intact.graphdb.model.nodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.CvTermUtils;

import java.util.ArrayList;
import java.util.Collection;

@NodeEntity
public class GraphInteractor implements Interactor  {

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

    public GraphInteractor(String name, CvTerm type){
        if (name == null || (name != null && name.length() == 0)){
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        this.shortName = name;
        if (type == null){
            this.interactorType = CvTermUtils.createUnknownInteractorType();
        }
        else {
            this.interactorType = type;
        }
    }

    public GraphInteractor(String name, String fullName, CvTerm type){
        this(name, type);
        this.fullName = fullName;
    }

    public GraphInteractor(String name, CvTerm type, Organism organism){
        this(name, type);
        this.organism = organism;
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Organism organism){
        this(name, fullName, type);
        this.organism = organism;
    }

    public GraphInteractor(String name, CvTerm type, Xref uniqueId){
        this(name, type);
        getIdentifiers().add(uniqueId);
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Xref uniqueId){
        this(name, fullName, type);
        getIdentifiers().add(uniqueId);
    }

    public GraphInteractor(String name, CvTerm type, Organism organism, Xref uniqueId){
        this(name, type, organism);
        getIdentifiers().add(uniqueId);
    }

    public GraphInteractor(String name, String fullName, CvTerm type, Organism organism, Xref uniqueId){
        this(name, fullName, type, organism);
        getIdentifiers().add(uniqueId);
    }

    public GraphInteractor(String name){
        if (name == null || (name != null && name.length() == 0)){
            throw new IllegalArgumentException("The short name cannot be null or empty.");
        }
        this.shortName = name;
        this.interactorType = CvTermUtils.createUnknownInteractorType();
    }

    public GraphInteractor(String name, String fullName){
        this(name);
        this.fullName = fullName;
    }

    public GraphInteractor(String name, Organism organism){
        this(name);
        this.organism = organism;
        this.interactorType = CvTermUtils.createUnknownInteractorType();
    }

    public GraphInteractor(String name, String fullName, Organism organism){
        this(name, fullName);
        this.organism = organism;
    }

    public GraphInteractor(String name, Xref uniqueId){
        this(name);
        getIdentifiers().add(uniqueId);
        this.interactorType = CvTermUtils.createUnknownInteractorType();
    }

    public GraphInteractor(String name, String fullName, Xref uniqueId){
        this(name, fullName);
        getIdentifiers().add(uniqueId);
        this.interactorType = CvTermUtils.createUnknownInteractorType();
    }

    public GraphInteractor(String name, Organism organism, Xref uniqueId){
        this(name, organism);
        getIdentifiers().add(uniqueId);
        this.interactorType = CvTermUtils.createUnknownInteractorType();
    }

    public GraphInteractor(String name, String fullName, Organism organism, Xref uniqueId){
        this(name, fullName, organism);
        getIdentifiers().add(uniqueId);
        this.interactorType = CvTermUtils.createUnknownInteractorType();
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
