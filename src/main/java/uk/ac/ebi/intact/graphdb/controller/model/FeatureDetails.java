package uk.ac.ebi.intact.graphdb.controller.model;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
public class FeatureDetails {

    private String participantAc; //participant.ac
    private String name; //Shortname
    private CvTerm regionType;
    private Xref interactor; // participant.interactor.preferredIdentifierStr
    private String interactorName; // participant.interactor.shortName
    private Collection<String> range; //ranges.rangeString

    public FeatureDetails(String participantAc, String name, CvTerm regionType, Xref interactorId, String interactorName,
                          Collection<String> range) {
        this.participantAc = participantAc;
        this.name = name;
        this.regionType = regionType;
        this.interactor = interactorId;
        this.interactorName = interactorName;
        this.range = range;
    }

    public String getParticipantAc() {
        return participantAc;
    }

    public void setParticipantAc(String participantAc) {
        this.participantAc = participantAc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CvTerm getRegionType() {
        return regionType;
    }

    public void setRegionType(CvTerm regionType) {
        this.regionType = regionType;
    }

    public Xref getInteractor() {
        return interactor;
    }

    public void setInteractor(Xref interactor) {
        this.interactor = interactor;
    }

    public String getInteractorName() {
        return interactorName;
    }

    public void setInteractorName(String interactorName) {
        this.interactorName = interactorName;
    }

    public Collection<String> getRange() {
        return range;
    }

    public void setRange(Collection<String> range) {
        this.range = range;
    }
}
