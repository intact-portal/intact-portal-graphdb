package uk.ac.ebi.intact.graphdb.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphParticipantEvidence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author Elisabet Barrera
 */
public class ParticipantDetailsResult implements Page<ParticipantDetails> {

    private final Page<GraphParticipantEvidence> participantEvidencePage;

    public ParticipantDetailsResult(Page<GraphParticipantEvidence> participantEvidencePage) {
        this.participantEvidencePage = participantEvidencePage;
    }

    @Override
    public int getTotalPages() {
        return participantEvidencePage.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return participantEvidencePage.getTotalElements();
    }

    @Override
    public int getNumber() {
        return participantEvidencePage.getNumber();
    }

    @Override
    public int getSize() {
        return participantEvidencePage.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return participantEvidencePage.getNumberOfElements();
    }

    @Override
    public List<ParticipantDetails> getContent() {
        return createParticipantsDetails(participantEvidencePage.getContent());
    }

    @Override
    public boolean hasContent() {
        return participantEvidencePage.hasContent();
    }

    @Override
    public Sort getSort() {
        return participantEvidencePage.getSort();
    }

    @Override
    public boolean isFirst() {
        return participantEvidencePage.isFirst();
    }

    @Override
    public boolean isLast() {
        return participantEvidencePage.isLast();
    }

    @Override
    public boolean hasNext() {
        return participantEvidencePage.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return participantEvidencePage.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return participantEvidencePage.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return participantEvidencePage.previousPageable();
    }

    @Override
    @JsonIgnore
    public <U> Page<U> map(Function<? super ParticipantDetails, ? extends U> function) {
        //TODO
        return null;
    }

    @Override
    @JsonIgnore
    public Iterator<ParticipantDetails> iterator() {
        //TODO
        return null;
    }

    private List<ParticipantDetails> createParticipantsDetails(List<GraphParticipantEvidence> participantEvidences) {

        List<ParticipantDetails> participantDetailsList = new ArrayList<>();

        participantEvidences.forEach(participant -> {
            String participantAc = participant.getAc();
            CvTerm type = new CvTerm(participant.getInteractor().getInteractorType().getShortName(), participant.getInteractor().getInteractorType().getMIIdentifier());

            CvTerm database = new CvTerm(participant.getInteractor().getPreferredName(), participant.getInteractor().getPreferredIdentifier().getDatabase().getMIIdentifier());
            Xref participantIdentifier = new Xref(database, participant.getInteractor().getPreferredIdentifier().getId());

            /* TODO: CVTERMS shouldn't have type null, but until this is fixed I need to create a patch and add a CVterm with not available
             * TODO: TO fix it ones the type are correct in the graphdb
             */
            Collection<Alias> aliases = new ArrayList<>();
            participant.getAliases().forEach(alias -> {
                if (alias.getType() != null) {
                    aliases.add(new Alias(alias.getName(), new CvTerm(alias.getType().getShortName(), alias.getType().getMIIdentifier())));
                } else {
                    aliases.add(new Alias(alias.getName(), null));
                }
            });

            String description = null;
            if (participant.getInteractor() != null ) {
                description = participant.getInteractor().getFullName();
            }
            Organism species = null;
            if (participant.getInteractor() != null && participant.getInteractor().getOrganism() != null ) {
                species = new Organism(participant.getInteractor().getOrganism().getScientificName(), participant.getInteractor().getOrganism().getTaxId());

            }
            // TODO: EXPRESSED IN ORGANISM is also retrieving nulls, to fix it
            Organism expressionSystem = null;
            if (participant.getExpressedInOrganism() != null) {
                expressionSystem = new Organism(participant.getExpressedInOrganism().getScientificName(), participant.getExpressedInOrganism().getTaxId());
            }

            Collection<CvTerm> detectionMethods = new ArrayList<>();
            participant.getIdentificationMethods().forEach(detectionMethod -> {
                if (detectionMethod != null) {
                    detectionMethods.add(new CvTerm(detectionMethod.getShortName(), detectionMethod.getMIIdentifier()));
                }
                else {
                    detectionMethods.add(null);
                }
            });

            CvTerm experimentalRole = null;
            if (participant.getExperimentalRole() != null ) {
                experimentalRole = new CvTerm(participant.getExperimentalRole().getShortName(), participant.getExperimentalRole().getMIIdentifier());
            }

            CvTerm biologicalRole = null;
            if (participant.getBiologicalRole() != null ) {
                biologicalRole = new CvTerm(participant.getBiologicalRole().getShortName(), participant.getBiologicalRole().getMIIdentifier());
            }

            Collection<CvTerm> experimentalPreparations = new ArrayList<>();
            participant.getExperimentalPreparations().forEach(experimentalPreparation -> {
                if (experimentalPreparation != null) {
                    experimentalPreparations.add(new CvTerm(experimentalPreparation.getShortName(), experimentalPreparation.getMIIdentifier()));
                } else {
                    experimentalPreparations.add(null);
                }
            });

            Collection<TermType> parameters = new ArrayList<>();
            participant.getParameters().forEach(parameter -> {
                if (parameter.getType() != null) {
                    CvTerm cvTerm = new CvTerm(parameter.getType().getShortName(), parameter.getType().getMIIdentifier());
                    parameters.add(new TermType(cvTerm, parameter.getValue().toString()));

                } else {
                    parameters.add(new TermType(null, parameter.getValue().toString()));
                }
            });

            Collection<TermType> confidences = new ArrayList<>();
            participant.getConfidences().forEach(confidence -> {
                if (confidence.getType() != null) {
                    CvTerm cvTerm = new CvTerm(confidence.getType().getShortName(), confidence.getType().getMIIdentifier());
                    confidences.add(new TermType(cvTerm, confidence.getValue()));
                } else {
                    confidences.add(new TermType(null, confidence.getValue()));

                }
            });

            ParticipantDetails participantDetails = new ParticipantDetails(participantAc, type, participantIdentifier, aliases,
                    description, species, expressionSystem, detectionMethods, experimentalRole, biologicalRole, experimentalPreparations,
                    parameters, confidences);

            participantDetailsList.add(participantDetails);
        });

        return participantDetailsList;

    }
}
