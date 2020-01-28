package uk.ac.ebi.intact.graphdb.utils;

import psidev.psi.mi.jami.model.*;
import uk.ac.ebi.intact.graphdb.model.nodes.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by anjali on 22/11/17.
 */
public class CollectionAdaptor {

    public static Collection<GraphCvTerm> convertCvTermIntoGraphModel(Collection<CvTerm> cvTerms) {
        return cvTerms.stream().map(p -> new GraphCvTerm(p, false)).collect(Collectors.toList());
    }

    public static Collection<GraphXref> convertXrefIntoGraphModel(Collection<Xref> xrefs) {
        if (!xrefs.isEmpty()) {
            Xref xref = xrefs.iterator().next();
            if (xref instanceof GraphXref) {
                return xrefs.stream().map(p -> (GraphXref) p).collect(Collectors.toList());
            }
        }
        return xrefs.stream().map(GraphXref::new).collect(Collectors.toList());
    }

    public static Collection<GraphChecksum> convertChecksumIntoGraphModel(Collection<Checksum> checksums) {
        if (!checksums.isEmpty()) {
            Checksum checksum = checksums.iterator().next();
            if (checksum instanceof GraphChecksum) {
                return checksums.stream().map(p -> (GraphChecksum) p).collect(Collectors.toList());
            }
        }
        return checksums.stream().map(GraphChecksum::new).collect(Collectors.toList());
    }

    public static Collection<GraphAlias> convertAliasIntoGraphModel(Collection<Alias> aliases) {
        if (!aliases.isEmpty()) {
            Alias alias = aliases.iterator().next();
            if (alias instanceof GraphAlias) {
                return aliases.stream().map(p -> (GraphAlias) p).collect(Collectors.toList());
            }
        }
        return aliases.stream().map(GraphAlias::new).collect(Collectors.toList());
    }

    public static Collection<GraphAnnotation> convertAnnotationIntoGraphModel(Collection<Annotation> annotations) {
        if (!annotations.isEmpty()) {
            Annotation annotation = annotations.iterator().next();
            if (annotation instanceof GraphAnnotation) {
                return annotations.stream().map(p -> (GraphAnnotation) p).collect(Collectors.toList());
            }
        }
        return annotations.stream().map(GraphAnnotation::new).collect(Collectors.toList());
    }

    public static Collection<GraphParameter> convertParameterIntoGraphModel(Collection<Parameter> parameters) {
        if (!parameters.isEmpty()) {
            Parameter parameter = parameters.iterator().next();
            if (parameter instanceof GraphParameter) {
                return parameters.stream().map(p -> (GraphParameter) p).collect(Collectors.toList());
            }
        }
        return parameters.stream().map(p -> new GraphParameter(p, false)).collect(Collectors.toList());
    }

    public static Collection<GraphCooperativityEvidence> convertCooperativityEvidenceIntoGraphModel(Collection<CooperativityEvidence> cooperativityEvidences) {
        if (!cooperativityEvidences.isEmpty()) {
            CooperativityEvidence cooperativityEvidence = cooperativityEvidences.iterator().next();
            if (cooperativityEvidence instanceof GraphCooperativityEvidence) {
                return cooperativityEvidences.stream().map(p -> (GraphCooperativityEvidence) p).collect(Collectors.toList());
            }
        }
        return cooperativityEvidences.stream().map(GraphCooperativityEvidence::new).collect(Collectors.toList());
    }

    public static Collection<GraphModelledInteraction> convertModelledInteractionIntoGraphModel(Collection<ModelledInteraction> modelledInteractions) {
        if (!modelledInteractions.isEmpty()) {
            ModelledInteraction modelledInteraction = modelledInteractions.iterator().next();
            if (modelledInteraction instanceof GraphModelledInteraction) {
                return modelledInteractions.stream().map(p -> (GraphModelledInteraction) p).collect(Collectors.toList());
            }
        }
        return modelledInteractions.stream().map(GraphModelledInteraction::new).collect(Collectors.toList());
    }

    public static Collection<GraphInteractionEvidence> convertInteractionEvidenceIntoGraphModel(Collection<InteractionEvidence> interactionEvidences) {
        if (!interactionEvidences.isEmpty()) {
            InteractionEvidence interactionEvidence = interactionEvidences.iterator().next();
            if (interactionEvidence instanceof GraphInteractionEvidence) {
                return interactionEvidences.stream().map(p -> (GraphInteractionEvidence) p).collect(Collectors.toList());
            }
        }
        return interactionEvidences.stream().map(p -> new GraphInteractionEvidence(p, false)).collect(Collectors.toList());
    }

    public static Collection<GraphModelledConfidence> convertModelledConfidenceIntoGraphModel(Collection<ModelledConfidence> confidences) {
        if (!confidences.isEmpty()) {
            ModelledConfidence modelledConfidence = confidences.iterator().next();
            if (modelledConfidence instanceof GraphModelledConfidence) {
                return confidences.stream().map(p -> (GraphModelledConfidence) p).collect(Collectors.toList());
            }
        }
        return confidences.stream().map(GraphModelledConfidence::new).collect(Collectors.toList());
    }

    public static Collection<GraphModelledParameter> convertModelledParameterIntoGraphModel(Collection<ModelledParameter> parameters) {
        if (!parameters.isEmpty()) {
            ModelledParameter modelledParameter = parameters.iterator().next();
            if (modelledParameter instanceof GraphModelledParameter) {
                return parameters.stream().map(p -> (GraphModelledParameter) p).collect(Collectors.toList());
            }
        }
        return parameters.stream().map(GraphModelledParameter::new).collect(Collectors.toList());
    }

    public static Collection<GraphCooperativeEffect> convertCooperativeEffectIntoGraphModel(Collection<CooperativeEffect> cooperativeEffects) {
        if (!cooperativeEffects.isEmpty()) {
            CooperativeEffect cooperativeEffect = cooperativeEffects.iterator().next();
            if (cooperativeEffect instanceof GraphCooperativeEffect) {
                return cooperativeEffects.stream().map(p -> (GraphCooperativeEffect) p).collect(Collectors.toList());
            }
        }
        return cooperativeEffects.stream().map(GraphCooperativeEffect::new).collect(Collectors.toList());
    }

    public static Collection<GraphConfidence> convertConfidenceIntoGraphModel(Collection<Confidence> confidences) {
        if (!confidences.isEmpty()) {
            Confidence confidence = confidences.iterator().next();
            if (confidence instanceof GraphConfidence) {
                return confidences.stream().map(p -> (GraphConfidence) p).collect(Collectors.toList());
            }
        }
        return confidences.stream().map(p -> new GraphConfidence(p, false)).collect(Collectors.toList());
    }

    public static Collection<GraphVariableParameterValueSet> convertVariableParameterValueSetIntoGraphModel(Collection<VariableParameterValueSet> variableParameterValueSets) {
        if (!variableParameterValueSets.isEmpty()) {
            VariableParameterValueSet variableParameterValueSet = variableParameterValueSets.iterator().next();
            if (variableParameterValueSet instanceof GraphVariableParameterValueSet) {
                return variableParameterValueSets.stream().map(p -> (GraphVariableParameterValueSet) p).collect(Collectors.toList());
            }
        }
        return variableParameterValueSets.stream().map(GraphVariableParameterValueSet::new).collect(Collectors.toList());
    }

    public static Collection<GraphVariableParameterValue> convertVariableParameterValueIntoGraphModel(Collection<VariableParameterValue> variableParameterValue) {
        if (!variableParameterValue.isEmpty()) {
            VariableParameterValue variableParameterValue1 = variableParameterValue.iterator().next();
            if (variableParameterValue1 instanceof GraphVariableParameterValue) {
                return variableParameterValue.stream().map(p -> (GraphVariableParameterValue) p).collect(Collectors.toList());
            }
        }
        return variableParameterValue.stream().map(GraphVariableParameterValue::new).collect(Collectors.toList());
    }

    public static Collection<GraphVariableParameter> convertVariableParameterIntoGraphModel(Collection<VariableParameter> variableParameters) {
        if (!variableParameters.isEmpty()) {
            VariableParameter variableParameter = variableParameters.iterator().next();
            if (variableParameter instanceof GraphVariableParameter) {
                return variableParameters.stream().map(p -> (GraphVariableParameter) p).collect(Collectors.toList());
            }
        }
        return variableParameters.stream().map(GraphVariableParameter::new).collect(Collectors.toList());
    }

    public static Collection<GraphParticipantEvidence> convertParticipantEvidenceIntoGraphModel(Collection<ParticipantEvidence> participantEvidences) {
        if (!participantEvidences.isEmpty()) {
            ParticipantEvidence participantEvidence = participantEvidences.iterator().next();
            if (participantEvidence instanceof GraphParticipantEvidence) {
                return participantEvidences.stream().map(p -> (GraphParticipantEvidence) p).collect(Collectors.toList());
            }
        }
        return participantEvidences.stream().map(GraphParticipantEvidence::new).collect(Collectors.toList());
    }

    public static Collection<GraphModelledParticipant> convertModelledParticipantIntoGraphModel(Collection<ModelledParticipant> participantEvidences) {
        if (!participantEvidences.isEmpty()) {
            ModelledParticipant modelledParticipant = participantEvidences.iterator().next();
            if (modelledParticipant instanceof GraphModelledParticipant) {
                return participantEvidences.stream().map(p -> (GraphModelledParticipant) p).collect(Collectors.toList());
            }
        }
        return participantEvidences.stream().map(GraphModelledParticipant::new).collect(Collectors.toList());
    }

    public static Collection<GraphCausalRelationship> convertCausalRelationshipIntoGraphModel(Collection<CausalRelationship> causalRelationships) {
        if (!causalRelationships.isEmpty()) {
            CausalRelationship causalRelationship = causalRelationships.iterator().next();
            if (causalRelationship instanceof GraphCausalRelationship) {
                return causalRelationships.stream().map(p -> (GraphCausalRelationship) p).collect(Collectors.toList());
            }
        }
        return causalRelationships.stream().map(GraphCausalRelationship::new).collect(Collectors.toList());
    }

    public static Collection<GraphRange> convertRangeIntoGraphModel(Collection<Range> ranges, String featureUniqueKey) {
        if (!ranges.isEmpty()) {
            Range range = ranges.iterator().next();
            if (range instanceof GraphRange) {
                return ranges.stream().map(p -> (GraphRange) p).collect(Collectors.toList());
            }
        }
        return ranges.stream().map(p -> new GraphRange(p, featureUniqueKey)).collect(Collectors.toList());
    }

    public static Collection<GraphModelledRange> convertModelledRangeIntoGraphModel(Collection<Range> ranges, String featureUniqueKey) {
        if (!ranges.isEmpty()) {
            Range range = ranges.iterator().next();
            if (range instanceof GraphModelledRange) {
                return ranges.stream().map(p -> (GraphModelledRange) p).collect(Collectors.toList());
            }
        }
        return ranges.stream().map(p -> new GraphModelledRange(p, featureUniqueKey)).collect(Collectors.toList());
    }

    public static Collection<GraphFeatureEvidence> convertFeatureEvidenceIntoGraphModel(Collection<FeatureEvidence> featureEvidences) {
        if (!featureEvidences.isEmpty()) {
            FeatureEvidence featureEvidence = featureEvidences.iterator().next();
            if (featureEvidence instanceof GraphFeatureEvidence) {
                return featureEvidences.stream().map(p -> (GraphFeatureEvidence) p).collect(Collectors.toList());
            }
        }
        return featureEvidences.stream().map(GraphFeatureEvidence::new).collect(Collectors.toList());
    }

    public static Collection<GraphModelledFeature> convertModelledFeatureIntoGraphModel(Collection<ModelledFeature> featureEvidences) {
        if (!featureEvidences.isEmpty()) {
            ModelledFeature modelledFeature = featureEvidences.iterator().next();
            if (modelledFeature instanceof GraphModelledFeature) {
                return featureEvidences.stream().map(p -> (GraphModelledFeature) p).collect(Collectors.toList());
            }
        }
        return featureEvidences.stream().map(GraphModelledFeature::new).collect(Collectors.toList());
    }

    public static Collection<GraphFeature> convertFeatureIntoGraphModel(Collection<Feature> featureEvidences) {
        if (!featureEvidences.isEmpty()) {
            Feature feature = featureEvidences.iterator().next();
            if (feature instanceof GraphFeature) {
                return featureEvidences.stream().map(p -> (GraphFeature) p).collect(Collectors.toList());
            }
        }
        return featureEvidences.stream().map(p -> new GraphFeature(p, false)).collect(Collectors.toList());
    }


    public static Collection<GraphExperiment> convertExperimentIntoGraphModel(Collection<Experiment> experiments) {
        if (!experiments.isEmpty()) {
            Experiment experiment = experiments.iterator().next();
            if (experiment instanceof GraphExperiment) {
                return experiments.stream().map(p -> (GraphExperiment) p).collect(Collectors.toList());
            }
        }
        return experiments.stream().map(GraphExperiment::new).collect(Collectors.toList());
    }

    public static Collection<GraphAuthor> convertAuthorStringIntoGraphModel(Collection<String> authors) {
        return authors.stream().map(GraphAuthor::new).collect(Collectors.toList());
    }
}

