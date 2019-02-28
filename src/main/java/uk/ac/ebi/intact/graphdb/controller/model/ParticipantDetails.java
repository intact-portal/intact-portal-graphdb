package uk.ac.ebi.intact.graphdb.controller.model;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class ParticipantDetails {
    private String participantAc;
    private CvTerm type;
    private Xref participantId;
    private Collection<Alias> aliases;
    private String description;
    private Organism species;
    private Organism expressionSystem;
    private Collection<CvTerm> detectionMethod;
    private CvTerm experimentalRole;
    private CvTerm biologicalRole;
//    private Integer featureCount;
    private Collection<CvTerm> experimentalPreparations;
    private Collection<TermType> parameters;
    private Collection<TermType> confidences;

    public ParticipantDetails(String participantAc, CvTerm type, Xref participantId,
                              Collection<Alias> aliases, String description, Organism species,
                              Organism expressionSystem, Collection<CvTerm> detectionMethod,
                              CvTerm experimentalRole, CvTerm biologicalRole, Collection<CvTerm> experimentalPreparations,
                              Collection<TermType> parameters, Collection<TermType> confidences) {
        this.participantAc = participantAc;
        this.type = type;
        this.participantId = participantId;
        this.aliases = aliases;
        this.description = description;
        this.species = species;
        this.expressionSystem = expressionSystem;
        this.detectionMethod = detectionMethod;
        this.experimentalRole = experimentalRole;
        this.biologicalRole = biologicalRole;
//        this.featureCount = featureCount;
        this.experimentalPreparations = experimentalPreparations;
        this.parameters = parameters;
        this.confidences = confidences;
    }

    public String getParticipantAc() {
        return participantAc;
    }

    public void setParticipantAc(String participantAc) {
        this.participantAc = participantAc;
    }

    public CvTerm getType() {
        return type;
    }

    public void setType(CvTerm type) {
        this.type = type;
    }

    public Xref getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Xref participantId) {
        this.participantId = participantId;
    }

    public Collection<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(Collection<Alias> aliases) {
        this.aliases = aliases;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organism getSpecies() {
        return species;
    }

    public void setSpecies(Organism species) {
        this.species = species;
    }

    public Organism getExpressionSystem() {
        return expressionSystem;
    }

    public void setExpressionSystem(Organism expressionSystem) {
        this.expressionSystem = expressionSystem;
    }

    public Collection<CvTerm> getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(Collection<CvTerm> detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public CvTerm getExperimentalRole() {
        return experimentalRole;
    }

    public void setExperimentalRole(CvTerm experimentalRole) {
        this.experimentalRole = experimentalRole;
    }

    public CvTerm getBiologicalRole() {
        return biologicalRole;
    }

    public void setBiologicalRole(CvTerm biologicalRole) {
        this.biologicalRole = biologicalRole;
    }

//    public Integer getFeatureCount() {
//        return featureCount;
//    }
//
//    public void setFeatureCount(Integer featureCount) {
//        this.featureCount = featureCount;
//    }

    public Collection<CvTerm> getExperimentalPreparations() {
        return experimentalPreparations;
    }

    public void setExperimentalPreparations(Collection<CvTerm> experimentalPreparations) {
        this.experimentalPreparations = experimentalPreparations;
    }

    public Collection<TermType> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<TermType> parameters) {
        this.parameters = parameters;
    }

    public Collection<TermType> getConfidences() {
        return confidences;
    }

    public void setConfidences(Collection<TermType> confidences) {
        this.confidences = confidences;
    }
}
