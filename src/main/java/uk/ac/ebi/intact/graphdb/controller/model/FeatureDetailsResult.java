package uk.ac.ebi.intact.graphdb.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author Elisabet Barrera
 */
public class FeatureDetailsResult implements Page<FeatureDetails> {

    private final Page<GraphFeatureEvidence> featureEvidencePage;

    public FeatureDetailsResult(Page<GraphFeatureEvidence> featureEvidencePage) {
        this.featureEvidencePage = featureEvidencePage;
    }

    @Override
    public int getTotalPages() {
        return featureEvidencePage.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return featureEvidencePage.getTotalElements();
    }

    @Override
    public int getNumber() {
        return featureEvidencePage.getNumber();
    }

    @Override
    public int getSize() {
        return featureEvidencePage.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return featureEvidencePage.getNumberOfElements();
    }

    @Override
    public List<FeatureDetails> getContent() {
        return createFeatureDetails(featureEvidencePage.getContent());
    }

    @Override
    public boolean hasContent() {
        return featureEvidencePage.hasContent();
    }

    @Override
    public Sort getSort() {
        return featureEvidencePage.getSort();
    }

    @Override
    public boolean isFirst() {
        return featureEvidencePage.isFirst();
    }

    @Override
    public boolean isLast() {
        return featureEvidencePage.isLast();
    }

    @Override
    public boolean hasNext() {
        return featureEvidencePage.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return featureEvidencePage.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return featureEvidencePage.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return featureEvidencePage.previousPageable();
    }

    @Override
    @JsonIgnore
    public <U> Page<U> map(Function<? super FeatureDetails, ? extends U> converter) {
        //TODO
        return null;
    }

    @Override
    @JsonIgnore
    public Iterator<FeatureDetails> iterator() {
        //TODO
        return null;
    }

    private List<FeatureDetails> createFeatureDetails(List<GraphFeatureEvidence> featureEvidences) {

        List<FeatureDetails> featureDetailsList = new ArrayList<>();


        featureEvidences.forEach(feature -> {

            GraphParticipantEvidence participantEvidence = (GraphParticipantEvidence) feature.getParticipant();
            String participantAc = participantEvidence.getAc();

            String shortname = feature.getShortName();

            CvTerm database = new CvTerm(feature.getParticipant().getInteractor().getPreferredName(), feature.getParticipant().getInteractor().getPreferredIdentifier().getDatabase().getMIIdentifier());
            Xref interactorIdentifier = new Xref(database, feature.getParticipant().getInteractor().getPreferredIdentifier().getId());

            String interactorName = null;
            if (feature.getParticipant() != null && feature.getParticipant().getInteractor() != null ) {
                interactorName = feature.getParticipant().getInteractor().getShortName();
            }

            CvTerm regionType = new CvTerm(feature.getType().getShortName(), feature.getType().getMIIdentifier());

            Collection<String> range = new ArrayList<>();
            feature.getRanges().forEach(graphRange -> range.add(graphRange.getRangeString()));

            FeatureDetails featureDetails = new FeatureDetails(participantAc, shortname, regionType, interactorIdentifier,
                    interactorName, range);

            featureDetailsList.add(featureDetails);
        });

        return featureDetailsList;
    }
}
