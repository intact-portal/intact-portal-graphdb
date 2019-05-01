package uk.ac.ebi.intact.graphdb.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.PositionUtils;
import psidev.psi.mi.jami.utils.RangeUtils;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.experiment.UnambiguousVariableParameterComparator;
import psidev.psi.mi.jami.utils.comparator.feature.UnambiguousFeatureEvidenceComparator;
import psidev.psi.mi.jami.utils.comparator.interaction.UnambiguousInteractionEvidenceComparator;
import psidev.psi.mi.jami.utils.comparator.parameter.UnambiguousParameterComparator;
import psidev.psi.mi.jami.utils.comparator.range.UnambiguousRangeComparator;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphClusteredInteraction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by anjali on 27/03/19.
 */


public class UniqueKeyGenerator {

    private static final Log log = LogFactory.getLog(UniqueKeyGenerator.class);

    public static String createXrefKey(Xref xref) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "xref::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            CvTerm database1 = xref.getDatabase();
            String mi = database1.getMIIdentifier();
            String mod = database1.getMODIdentifier();
            String par = database1.getPARIdentifier();

            if (mi != null) {
                uniqueKeyStringBuilder.append(mi);
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            } else if (mod != null) {
                uniqueKeyStringBuilder.append(mod);
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            } else if (par != null) {
                uniqueKeyStringBuilder.append(par);
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            } else {
                uniqueKeyStringBuilder.append(database1.getShortName());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            uniqueKeyStringBuilder.append(xref.getId());

            CvTerm qualifier = xref.getQualifier();
            if (qualifier != null) {
                String qualifierMi = qualifier.getMIIdentifier();
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
                if (qualifierMi != null) {
                    uniqueKeyStringBuilder.append(qualifierMi);
                } else {
                    uniqueKeyStringBuilder.append(qualifier.getShortName());
                }
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createCvTermKey(CvTerm cvTerm) {

        UnambiguousCvTermComparator unambiguousCvTermComparator = new UnambiguousCvTermComparator();
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "cvterm::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            if (cvTerm.getMIIdentifier() != null) {
                uniqueKeyStringBuilder.append(cvTerm.getMIIdentifier());
            } else if (cvTerm.getMODIdentifier() != null) {
                uniqueKeyStringBuilder.append(cvTerm.getMODIdentifier());
            } else if (cvTerm.getPARIdentifier() != null) {
                uniqueKeyStringBuilder.append(cvTerm.getPARIdentifier());
            } else if (!cvTerm.getIdentifiers().isEmpty()) {
                List<Xref> list1 = new ArrayList<Xref>(cvTerm.getIdentifiers());
                Collections.sort(list1, unambiguousCvTermComparator.getIdentifierComparator());
                int counter = 1;
                for (Xref xref : list1) {
                    uniqueKeyStringBuilder.append(createXrefKey(xref));// ***calls xref unique key generation
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }
            } else {
                uniqueKeyStringBuilder.append(cvTerm.getShortName());
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createAliasKey(Alias alias) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "alias::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            CvTerm type = alias.getType();
            uniqueKeyStringBuilder.append(createCvTermKey(type));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(alias.getName());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createAnnotationKey(Annotation annotation) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "annotation::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            CvTerm topic = annotation.getTopic();
            uniqueKeyStringBuilder.append(createCvTermKey(topic));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(annotation.getValue());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createAuthorKey(String authorName) {

        // use orchid in future
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "author::";
        uniqueKeyStringBuilder.append(prefix);
        if (authorName == null) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        } else {
            uniqueKeyStringBuilder.append(authorName);
        }

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createOrganismKey(Organism organism) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "organism::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(organism.getTaxId());

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm cellType = organism.getCellType();
            uniqueKeyStringBuilder.append(createCvTermKey(cellType));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm tissue = organism.getTissue();
            uniqueKeyStringBuilder.append(createCvTermKey(tissue));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm compartment = organism.getCompartment();
            uniqueKeyStringBuilder.append(createCvTermKey(compartment));

        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createParameterKey(Parameter parameter) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "parameter::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            CvTerm type = parameter.getType();
            uniqueKeyStringBuilder.append(createCvTermKey(type));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm unit = parameter.getUnit();
            uniqueKeyStringBuilder.append(createCvTermKey(unit));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            ParameterValue value = parameter.getValue();
            uniqueKeyStringBuilder.append(createParameterValueKey(value));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            BigDecimal uncertainty = parameter.getUncertainty();
            if (uncertainty != null) {
                uniqueKeyStringBuilder.append(uncertainty.intValue());
            }

            // delete any trailing underscore
            uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createModelledParameterKey(ModelledParameter modelledParameter) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "modelled parameter::";
        uniqueKeyStringBuilder.append(prefix);

        try {

            uniqueKeyStringBuilder.append(modelledParameter.getPublication().getPubmedId());
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);

            CvTerm type = modelledParameter.getType();
            uniqueKeyStringBuilder.append(createCvTermKey(type));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm unit = modelledParameter.getUnit();
            uniqueKeyStringBuilder.append(createCvTermKey(unit));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            ParameterValue value = modelledParameter.getValue();
            uniqueKeyStringBuilder.append(createParameterValueKey(value));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            BigDecimal uncertainty = modelledParameter.getUncertainty();
            if (uncertainty != null) {
                uniqueKeyStringBuilder.append(uncertainty.intValue());
            }

            // delete any trailing underscore
            uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createParameterValueKey(ParameterValue parameterValue) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "parameter value::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            String parameterValueString = (parameterValue.getBase() != 0 &&
                    parameterValue.getFactor().doubleValue() != 0 ?
                    parameterValue.getFactor().toString() +
                            (parameterValue.getExponent() != 0 ? "x" +
                                    parameterValue.getBase() + "^(" + parameterValue.getExponent() + ")" : "") : "0");
            uniqueKeyStringBuilder.append(parameterValueString);
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createRangeKey(Range range, String featureUniqueString) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "range::";
        uniqueKeyStringBuilder.append(prefix);

        uniqueKeyStringBuilder.append(featureUniqueString);
        uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
        uniqueKeyStringBuilder.append(RangeUtils.convertRangeToString(range));

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createPositionKey(Position position, String rangeUniqueString) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "position::";
        uniqueKeyStringBuilder.append(prefix);

        uniqueKeyStringBuilder.append(rangeUniqueString);
        uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
        uniqueKeyStringBuilder.append(PositionUtils.convertPositionToString(position));

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createResultingSequenceKey(ResultingSequence resultingSequence, String rangeUniqueString) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "resultingSequence::";
        uniqueKeyStringBuilder.append(prefix);

        uniqueKeyStringBuilder.append(rangeUniqueString);
        uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
        if (resultingSequence.getOriginalSequence() != null) {
            uniqueKeyStringBuilder.append(resultingSequence.getOriginalSequence().hashCode());
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
        }
        if (resultingSequence.getNewSequence() != null) {
            uniqueKeyStringBuilder.append(resultingSequence.getNewSequence().hashCode());
        }

        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }


    public static String createFeatureKey(Feature featureEvidence) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "feature::";
        uniqueKeyStringBuilder.append(prefix);

        try {

            if (featureEvidence.getShortName() != null) {
                uniqueKeyStringBuilder.append(featureEvidence.getShortName());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getType() != null) {
                uniqueKeyStringBuilder.append(createCvTermKey(featureEvidence.getType()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getRole() != null) {
                uniqueKeyStringBuilder.append(createCvTermKey(featureEvidence.getRole()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getInterpro() != null) {
                uniqueKeyStringBuilder.append(featureEvidence.getInterpro());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getIdentifiers() != null) {
                uniqueKeyStringBuilder.append(createXrefListKey(featureEvidence.getIdentifiers()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getRanges() != null) {
                UnambiguousRangeComparator unambiguousXrefComparator = new UnambiguousRangeComparator();
                if (!featureEvidence.getRanges().isEmpty()) {
                    List<Range> list1 = new ArrayList<Range>(featureEvidence.getRanges());
                    Collections.sort(list1, unambiguousXrefComparator);
                    int counter = 1;
                    for (Range range : list1) {
                        uniqueKeyStringBuilder.append(createRangeKey(range, featureEvidence.getShortName()));
                        if (counter != list1.size()) {
                            uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                        }
                        counter++;
                    }
                }
            }

            // delete any trailing underscore
            uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createPolymerKey(Polymer polymer) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "polymer::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            if (polymer.getPreferredIdentifier() != null && polymer.getPreferredIdentifier().getId() != null) {
                uniqueKeyStringBuilder.append(polymer.getPreferredIdentifier().getId());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (polymer.getOrganism() != null) {
                uniqueKeyStringBuilder.append(createOrganismKey((polymer.getOrganism())));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (polymer.getSequence() != null) {
                uniqueKeyStringBuilder.append(polymer.getSequence().hashCode());// use hashcode (as it is)
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }

        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createProteinKey(Protein protein) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "protein::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(protein.getPreferredIdentifier().getId());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createNucleicAcidKey(NucleicAcid nucleicAcid) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "nucleic acid::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(nucleicAcid.getPreferredIdentifier().getId());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createGeneKey(Gene gene) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "gene::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(gene.getPreferredIdentifier().getId());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createMoleculeKey(Molecule molecule) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "molecule::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(molecule.getPreferredIdentifier().getId());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createInteractorKey(Interactor interactor) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "interactor::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            if (interactor instanceof Protein) {
                uniqueKeyStringBuilder.append(createProteinKey((Protein) interactor));
            } else if (interactor instanceof NucleicAcid) {
                uniqueKeyStringBuilder.append(createNucleicAcidKey((NucleicAcid) interactor));
            } else if (interactor instanceof Polymer) {
                uniqueKeyStringBuilder.append(createPolymerKey((Polymer) interactor));
            } else if (interactor instanceof Gene) {
                uniqueKeyStringBuilder.append(createGeneKey((Gene) interactor));
            } else if (interactor instanceof Molecule) {
                uniqueKeyStringBuilder.append(createMoleculeKey((Molecule) interactor));
            } else if (!interactor.getIdentifiers().isEmpty()) {
                uniqueKeyStringBuilder.append(createXrefListKey(interactor.getIdentifiers()));
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createParticipantEvidenceKey(ParticipantEvidence participantEvidence) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "participant::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            if (participantEvidence.getInteraction() != null) {
                uniqueKeyStringBuilder.append(createInteractionEvidenceKey(participantEvidence.getInteraction()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getInteractor() != null) {
                uniqueKeyStringBuilder.append(createInteractorKey(participantEvidence.getInteractor()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getBiologicalRole() != null) {
                uniqueKeyStringBuilder.append(createCvTermKey(participantEvidence.getBiologicalRole()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getExperimentalRole() != null) {
                uniqueKeyStringBuilder.append(createCvTermKey(participantEvidence.getExperimentalRole()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getIdentificationMethods() != null && !participantEvidence.getIdentificationMethods().isEmpty()) {
                UniqueKeyGenerator.createCvTermListKey(participantEvidence.getIdentificationMethods());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getExperimentalPreparations() != null && !participantEvidence.getExperimentalPreparations().isEmpty()) {
                UniqueKeyGenerator.createCvTermListKey(participantEvidence.getExperimentalPreparations());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getExpressedInOrganism() != null) {
                uniqueKeyStringBuilder.append(UniqueKeyGenerator.createOrganismKey(participantEvidence.getExpressedInOrganism()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getParameters() != null && !participantEvidence.getParameters().isEmpty()) {
                uniqueKeyStringBuilder.append(createParameterListKey(participantEvidence.getParameters()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (participantEvidence.getFeatures() != null && !participantEvidence.getFeatures().isEmpty()) {
                uniqueKeyStringBuilder.append(createFeatureListKey(participantEvidence.getFeatures()));
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createModelledParticipantKey(ModelledParticipant modelledParticipant) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "modelled participant::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            if (modelledParticipant.getInteraction() != null) {
                uniqueKeyStringBuilder.append(createModelledInteractionKey(modelledParticipant.getInteraction()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (modelledParticipant.getInteractor() != null) {
                uniqueKeyStringBuilder.append(createInteractorKey(modelledParticipant.getInteractor()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (modelledParticipant.getBiologicalRole() != null) {
                uniqueKeyStringBuilder.append(createCvTermKey(modelledParticipant.getBiologicalRole()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (modelledParticipant.getFeatures() != null && !modelledParticipant.getFeatures().isEmpty()) {
                uniqueKeyStringBuilder.append(createModelledFeatureKey(modelledParticipant.getFeatures()));
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createBinaryInteractionEvidenceKey(BinaryInteractionEvidence binaryInteractionEvidence) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "binary interaction evidence::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            if (binaryInteractionEvidence.getParticipantA() != null) {
                uniqueKeyStringBuilder.append(createParticipantEvidenceKey(binaryInteractionEvidence.getParticipantA()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (binaryInteractionEvidence.getParticipantB() != null) {
                uniqueKeyStringBuilder.append(createParticipantEvidenceKey(binaryInteractionEvidence.getParticipantB()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (binaryInteractionEvidence.getInteractionType() != null) {
                uniqueKeyStringBuilder.append(createCvTermKey(binaryInteractionEvidence.getInteractionType()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (binaryInteractionEvidence.getComplexExpansion() != null) {
                uniqueKeyStringBuilder.append(createCvTermKey(binaryInteractionEvidence.getComplexExpansion()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (binaryInteractionEvidence.getIdentifiers() != null && !binaryInteractionEvidence.getIdentifiers().isEmpty()) {
                uniqueKeyStringBuilder.append(createXrefListKey(binaryInteractionEvidence.getIdentifiers()));
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }

        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createInteractionEvidenceKey(InteractionEvidence interactionEvidence) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "interaction evidence::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(createXrefListKey(interactionEvidence.getIdentifiers()));
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createStoichiometryKey(Stoichiometry stc) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "stoichiometry::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            uniqueKeyStringBuilder.append(stc.getMinValue());
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(stc.getMaxValue());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }


    public static String createSourceKey(Source source) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "source::";
        uniqueKeyStringBuilder.append(prefix);

        uniqueKeyStringBuilder.append(createCvTermKey(source));

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createEntityKey(Entity entity) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "entity::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            if (entity.getInteractor() != null) {
                uniqueKeyStringBuilder.append(createInteractorKey(entity.getInteractor()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (entity.getStoichiometry() != null) {
                uniqueKeyStringBuilder.append(createStoichiometryKey(entity.getStoichiometry()));
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createModelledEntityKey(ModelledEntity modelledEntity) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "modelled entity::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            if (modelledEntity.getInteractor() != null) {
                uniqueKeyStringBuilder.append(createInteractorKey(modelledEntity.getInteractor()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (modelledEntity.getStoichiometry() != null) {
                uniqueKeyStringBuilder.append(createStoichiometryKey(modelledEntity.getStoichiometry()));
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createExperimentalEntity(ExperimentalEntity experimentalEntity) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "experimental entity::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(createFeatureListKey(experimentalEntity.getFeatures()));
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createVariableParameterKey(VariableParameter variableParameter) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "variable parameter::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(createExperimentKey(variableParameter.getExperiment()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(variableParameter.getDescription());
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(createCvTermKey(variableParameter.getUnit()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(createVariableParameterValueListKey(variableParameter.getVariableValues()));
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createVariableParameterValueKey(VariableParameterValue value) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "variable parameter value::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(createVariableParameterKey(value.getVariableParameter()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            if (value.getValue() != null) {
                uniqueKeyStringBuilder.append(value.getValue());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }
            if (value.getOrder() != null) {
                uniqueKeyStringBuilder.append(value.getOrder());
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        // delete any trailing underscore
        uniqueKeyStringBuilder = deleteTrailingUnderScore(uniqueKeyStringBuilder);
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createVariableParametersValueSetKey(Collection<VariableParameterValue> variableParameterValues) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "variable parameter value set::";
        uniqueKeyStringBuilder.append(prefix);

        uniqueKeyStringBuilder.append(createVariableParameterValueListKey(variableParameterValues));
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createExperimentKey(Experiment experiment) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "experiment::";
        uniqueKeyStringBuilder.append(prefix);

        try {

            uniqueKeyStringBuilder.append(createPublicationKey(experiment.getPublication()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(createCvTermKey(experiment.getInteractionDetectionMethod()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(createOrganismKey(experiment.getHostOrganism()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            if (experiment.getInteractionEvidences() != null && !experiment.getInteractionEvidences().isEmpty()) {
                uniqueKeyStringBuilder.append(createInteractionEvidenceListKey(experiment.getInteractionEvidences()));
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();


    }

    public static String createPublicationKey(Publication publication) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "publication::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(publication.getPubmedId());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createCurationDepthKey(CurationDepth curationDepth) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "curation depth::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            uniqueKeyStringBuilder.append(curationDepth.name());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createConfidenceKey(Confidence confidence) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "confidence::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(createCvTermKey(confidence.getType()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(confidence.getValue());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createModelledConfidenceKey(ModelledConfidence modelledConfidence) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "modelled confidence::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(modelledConfidence.getPublication().getPubmedId());
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(createCvTermKey(modelledConfidence.getType()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(modelledConfidence.getValue());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createChecksumKey(Checksum checksum) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "checksum::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(createCvTermKey(checksum.getMethod()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(checksum.getValue());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createCausalRelationshipKey(CausalRelationship causalRelationship) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "causal relationship::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(createCvTermKey(causalRelationship.getRelationType()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(createEntityKey(causalRelationship.getTarget()));
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createClusteredInteractionKey(GraphClusteredInteraction clusteredInteraction) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "clustered interaction::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            // we don't need to sort it because it is already sorted in graph (i.e one pair will always be counted once)
            uniqueKeyStringBuilder.append(createInteractorKey(clusteredInteraction.getInteractorPA()));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(createInteractorKey(clusteredInteraction.getinteractorPB()));
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }


    private static StringBuilder deleteTrailingUnderScore(StringBuilder stringBuilder) {
        // delete any trailing underscore
        int lastUnderscoreIndex = stringBuilder.lastIndexOf("_");
        if (lastUnderscoreIndex == (stringBuilder.length() - 1)) {
            return stringBuilder.deleteCharAt(lastUnderscoreIndex);
        }
        return stringBuilder;

    }

    public static String createXrefListKey(Collection<Xref> xrefs) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "xref list::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            UnambiguousXrefComparator unambiguousXrefComparator = new UnambiguousXrefComparator();
            if (!xrefs.isEmpty()) {
                List<Xref> list1 = new ArrayList<Xref>(xrefs);
                Collections.sort(list1, unambiguousXrefComparator);
                int counter = 1;
                for (Xref xref : list1) {
                    uniqueKeyStringBuilder.append(createXrefKey(xref));
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }

            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createCvTermListKey(Collection<CvTerm> cvTerms) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "cv list::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            UnambiguousCvTermComparator unambiguousCvTermComparator = new UnambiguousCvTermComparator();
            if (!cvTerms.isEmpty()) {
                List<CvTerm> list1 = new ArrayList<CvTerm>(cvTerms);
                Collections.sort(list1, unambiguousCvTermComparator);
                int counter = 1;
                for (CvTerm cvTerm : list1) {
                    uniqueKeyStringBuilder.append(createCvTermKey(cvTerm));
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }


    public static String createParameterListKey(Collection<Parameter> parameters) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "parameter list::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            UnambiguousParameterComparator unambiguousParameterComparator = new UnambiguousParameterComparator();
            if (!parameters.isEmpty()) {
                List<Parameter> list1 = new ArrayList<Parameter>(parameters);
                Collections.sort(list1, unambiguousParameterComparator);
                int counter = 1;
                for (Parameter parameter : list1) {
                    uniqueKeyStringBuilder.append(createParameterKey(parameter));
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createInteractionEvidenceListKey(Collection<InteractionEvidence> interactionEvidences) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "interaction evidence list::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            UnambiguousInteractionEvidenceComparator unambiguousParameterComparator = new UnambiguousInteractionEvidenceComparator();
            if (!interactionEvidences.isEmpty()) {
                List<InteractionEvidence> list1 = new ArrayList<InteractionEvidence>(interactionEvidences);
                Collections.sort(list1, unambiguousParameterComparator);
                int counter = 1;
                for (InteractionEvidence interactionEvidence : list1) {
                    uniqueKeyStringBuilder.append(createInteractionEvidenceKey(interactionEvidence));
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createFeatureListKey(Collection<FeatureEvidence> features) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "feature list::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            UnambiguousFeatureEvidenceComparator unambiguousFeatureEvidenceComparator = new UnambiguousFeatureEvidenceComparator();
            if (!features.isEmpty()) {
                List<FeatureEvidence> list1 = new ArrayList<FeatureEvidence>(features);
                Collections.sort(list1, unambiguousFeatureEvidenceComparator);
                int counter = 1;
                for (FeatureEvidence featureEvidence : list1) {
                    uniqueKeyStringBuilder.append(createFeatureKey(featureEvidence));
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createVariableParameterValueListKey(Collection<VariableParameterValue> variableParameterValues) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "variable parameter value list::";
        uniqueKeyStringBuilder.append(prefix);
        try {
            UnambiguousVariableParameterComparator unambiguousVariableParameterComparator = new UnambiguousVariableParameterComparator();
            if (!variableParameterValues.isEmpty()) {
                List<VariableParameterValue> list1 = new ArrayList<VariableParameterValue>(variableParameterValues);
                Collections.sort(list1, unambiguousVariableParameterComparator.getVariableParameterValueCollectionComparator().getObjectComparator());
                int counter = 1;
                for (VariableParameterValue variableParameterValue : list1) {
                    uniqueKeyStringBuilder.append(createVariableParameterValueKey(variableParameterValue));
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }


}
