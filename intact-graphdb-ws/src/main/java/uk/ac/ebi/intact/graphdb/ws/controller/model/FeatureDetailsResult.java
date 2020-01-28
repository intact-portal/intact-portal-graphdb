package uk.ac.ebi.intact.graphdb.ws.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeature;
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

            String featureAc = feature.getAc();
            String shortname = feature.getShortName();
            CvTerm type = new CvTerm(feature.getType().getShortName(), feature.getType().getMIIdentifier());

            CvTerm role = null;
            if (feature.getRole() != null) {
                role = new CvTerm(feature.getRole().getShortName(), feature.getRole().getMIIdentifier());
            }

            Collection<String> ranges = new ArrayList<>();
            feature.getRanges().forEach(graphRange -> ranges.add(graphRange.getRangeString()));

            Collection<GraphFeature> linkedFeatures = new ArrayList<>();
            feature.getLinkedFeatures().forEach(linkfeature -> {
                if (linkfeature != null) {
                    linkedFeatures.add(linkfeature);
                }
            });

            String participantName = null;
            if (feature.getParticipant() != null && feature.getParticipant().getInteractor() != null) {
                participantName = feature.getParticipant().getInteractor().getShortName();
            }

            CvTerm participantDatabase = new CvTerm(feature.getParticipant().getInteractor().getPreferredName(),
                    feature.getParticipant().getInteractor().getPreferredIdentifier().getDatabase().getMIIdentifier());
            Xref participantIdentifier = new Xref(participantDatabase, feature.getParticipant().getInteractor().getPreferredIdentifier().getId());

            GraphParticipantEvidence participantEvidence = (GraphParticipantEvidence) feature.getParticipant();
            String participantAc = participantEvidence.getAc();


            Collection<CvTerm> detectionMethods = new ArrayList<>();
            feature.getDetectionMethods().forEach(detectionMethod -> {
                if (detectionMethod != null) {
                    detectionMethods.add(new CvTerm(detectionMethod.getShortName(), detectionMethod.getMIIdentifier()));
                }
            });


            Collection<Parameter> parameters = new ArrayList<>();
            feature.getParameters().forEach(parameter -> {
                if (parameter.getType() != null) {
                    CvTerm paramType = new CvTerm(parameter.getType().getShortName(), parameter.getType().getMIIdentifier());
                    CvTerm paramUnit = new CvTerm(parameter.getUnit().getShortName(), parameter.getUnit().getMIIdentifier());
                    parameters.add(new Parameter(paramType, paramUnit, parameter.getValue().toString()));
                }
            });

            Collection<Xref> identifiers = new ArrayList<>();
            feature.getIdentifiers().forEach(identifier -> {
                if (identifier != null) {
                    CvTerm cvTerm = new CvTerm(identifier.getDatabase().getShortName(), identifier.getDatabase().getMIIdentifier());
                    identifiers.add(new Xref(cvTerm, identifier.getId()));
                }
            });


            Collection<Xref> xrefs = new ArrayList<>();
            feature.getXrefs().forEach(xref -> {
                if (xref != null) {
                    CvTerm xrefDatabase = new CvTerm(xref.getDatabase().getShortName(), xref.getDatabase().getMIIdentifier());
                    CvTerm xrefQualifier = new CvTerm(xref.getQualifier().getShortName(), xref.getQualifier().getMIIdentifier());
                    xrefs.add(new Xref(xrefDatabase, xref.getId(), xrefQualifier));
                }
            });


            Collection<Annotation> annotations = new ArrayList<>();
            feature.getAnnotations().forEach(graphAnnotation -> {
                if (graphAnnotation != null) {
                    CvTerm cvTerm = new CvTerm(graphAnnotation.getTopic().getShortName(), graphAnnotation.getTopic().getMIIdentifier());
                    annotations.add(new Annotation(cvTerm, graphAnnotation.getValue()));
                }
            });

            FeatureDetails featureDetails = new FeatureDetails(featureAc, shortname, type, role, ranges, linkedFeatures,
                    participantName, participantIdentifier, participantAc, detectionMethods, parameters, identifiers,
                    xrefs, annotations);

            featureDetailsList.add(featureDetails);
        });

        return featureDetailsList;
    }
}
