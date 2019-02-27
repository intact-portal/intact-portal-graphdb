package uk.ac.ebi.intact.graphdb.utils;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.experiment.VariableParameterValueComparator;
import psidev.psi.mi.jami.utils.comparator.organism.UnambiguousOrganismComparator;
import psidev.psi.mi.jami.utils.comparator.parameter.UnambiguousParameterComparator;
import psidev.psi.mi.jami.utils.comparator.range.UnambiguousRangeComparator;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousExternalIdentifierComparator;
import uk.ac.ebi.intact.graphdb.model.nodes.*;

import java.util.Collection;

/**
 * Created by anjali on 06/07/18.
 */
public class HashCode {

    public static int identifiersHashCode(Collection<Xref> xrefs) {
        int hashcode = 0;

        for (Xref ref : xrefs) {
            hashcode = 31 * hashcode + UnambiguousExternalIdentifierComparator.hashCode(ref);
        }

        return hashcode;
    }

    public static int identifiersGraphHashCode(Collection<GraphXref> xrefs) {
        int hashcode = 0;

        for (Xref ref : xrefs) {
            hashcode = 31 * hashcode + UnambiguousExternalIdentifierComparator.hashCode(ref);
        }

        return hashcode;
    }

    public static int rangesHashCode(Collection<Range> ranges) {
        int hashcode = 0;

        for (Range range : ranges) {
            hashcode = 31 * hashcode + UnambiguousRangeComparator.hashCode(range);
        }

        return hashcode;
    }

    public static int rangesGraphHashCode(Collection<GraphRange> ranges) {
        int hashcode = 0;

        for (GraphRange range : ranges) {
            hashcode = 31 * hashcode + UnambiguousRangeComparator.hashCode(range);
        }

        return hashcode;
    }

    public static int featuresGraphHashCode(Collection<GraphFeatureEvidence> featureEvidences) {
        int hashcode = 0;

        for (GraphFeatureEvidence featureEvidence : featureEvidences) {
            hashcode = 31 * hashcode + featureEvidence.hashCode();
        }

        return hashcode;
    }

    public static int featuresHashCode(Collection<FeatureEvidence> featureEvidences) {
        int hashcode = 0;

        for (FeatureEvidence featureEvidence : featureEvidences) {
            hashcode = 31 * hashcode + featureHashCode(featureEvidence);
        }

        return hashcode;
    }

    public static int cvTermsHashCode(Collection<CvTerm> cvTerms) {
        int hashcode = 0;

        for (CvTerm cvTerm : cvTerms) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(cvTerm);
        }

        return hashcode;
    }

    public static int cvTermsGraphHashCode(Collection<GraphCvTerm> cvTerms) {
        int hashcode = 0;

        for (GraphCvTerm cvTerm : cvTerms) {
            hashcode = 31 * hashcode + cvTerm.hashCode();
        }

        return hashcode;
    }

    public static int parametersGraphHashCode(Collection<GraphParameter> parameters) {
        int hashcode = 0;

        for (GraphParameter parameter : parameters) {
            hashcode = 31 * hashcode + parameter.hashCode();
        }

        return hashcode;
    }

    public static int parametersHashCode(Collection<Parameter> parameters) {
        int hashcode = 0;

        for (Parameter parameter : parameters) {
            hashcode = 31 * hashcode + UnambiguousParameterComparator.hashCode(parameter);
        }

        return hashcode;
    }

    public static int variableParametersValuesHashCode(Collection<VariableParameterValue> variableParameterValues) {
        int hashcode = 0;

        for (VariableParameterValue parameterValue : variableParameterValues) {
            hashcode = 31 * hashcode + VariableParameterValueComparator.hashCode(parameterValue);
        }

        return hashcode;
    }

    public static int variableParametersValuesGraphHashCode(Collection<GraphVariableParameterValue> variableParameterValues) {
        int hashcode = 0;

        for (GraphVariableParameterValue parameter : variableParameterValues) {
            hashcode = 31 * hashcode + parameter.hashCode();
        }

        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int featureHashCode(FeatureEvidence featureEvidence) {
        int hashcode = 31;
        hashcode = 31 * hashcode + "Feature:".hashCode();

        if (featureEvidence.getShortName() != null) {
            hashcode = 31 * hashcode + featureEvidence.getShortName().hashCode();
        }

        if (featureEvidence.getType() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(featureEvidence.getType());
        }

        if (featureEvidence.getRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(featureEvidence.getRole());
        }

        if (featureEvidence.getInterpro() != null) {
            hashcode = 31 * hashcode + featureEvidence.getInterpro().hashCode();
        }

        if (featureEvidence.getIdentifiers() != null) {
            hashcode = 31 * hashcode + HashCode.identifiersHashCode(featureEvidence.getIdentifiers());
        }

        if (featureEvidence.getRanges() != null) {
            hashcode = 31 * hashcode + HashCode.rangesHashCode(featureEvidence.getRanges());
        }

        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int participantHashCode(ParticipantEvidence participantEvidence) {
        // since there was not hashcode implemented in jami, we had to come up with this
        int hashcode = 31;
        if (participantEvidence.getInteractor() != null) {
            hashcode = 31 * hashcode + interactorHashCode(participantEvidence.getInteractor());
        }
        if (participantEvidence.getBiologicalRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(participantEvidence.getBiologicalRole());
        }
        if (participantEvidence.getExperimentalRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(participantEvidence.getExperimentalRole());
        }
        if (!participantEvidence.getIdentificationMethods().isEmpty()) {
            hashcode = 31 * hashcode + HashCode.cvTermsHashCode(participantEvidence.getIdentificationMethods());
        }
        if (participantEvidence.getExperimentalPreparations() != null) {
            hashcode = 31 * hashcode + HashCode.cvTermsHashCode(participantEvidence.getExperimentalPreparations());
        }
        if (participantEvidence.getExpressedInOrganism() != null) {
            hashcode = 31 * hashcode + UnambiguousOrganismComparator.hashCode(participantEvidence.getExpressedInOrganism());
        }
        if (participantEvidence.getParameters() != null) {
            hashcode = 31 * hashcode + HashCode.parametersHashCode(participantEvidence.getParameters());
        }
        if (!participantEvidence.getFeatures().isEmpty()) {
            hashcode = 31 * hashcode + HashCode.featuresHashCode(participantEvidence.getFeatures());
        }

        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int moleculeHashCode(Molecule molecule) {

        int hashcode = 31;
        hashcode = 31 * hashcode + "Molecule".hashCode();
        String preferredIdentifierStr = molecule.getPreferredIdentifier().getId();
        if (molecule.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }
        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int polymerHashCode(Polymer polymer) {

        int hashcode = 31;

        String preferredIdentifierStr = polymer.getPreferredIdentifier().getId();
        if (polymer.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }
        if (polymer.getOrganism() != null) {
            hashcode = 31 * hashcode + UnambiguousOrganismComparator.hashCode(polymer.getOrganism());
        }
        if (polymer.getSequence() != null) {
            hashcode = 31 * hashcode + polymer.getSequence().hashCode();
        }

        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int proteinHashCode(Protein protein) {

        int hashcode = 31;
        hashcode = 31 * hashcode + "Protein".hashCode();

        String preferredIdentifierStr = protein.getPreferredIdentifier().getId();
        if (protein.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }
        if (protein.getUniprotkb() != null) {
            hashcode = 31 * hashcode + protein.getUniprotkb().hashCode();
        }
        if (protein.getRefseq() != null) {
            hashcode = 31 * hashcode + protein.getRefseq().hashCode();
        }
        if (protein.getRogid() != null) {
            hashcode = 31 * hashcode + protein.getRogid().hashCode();
        }
        if (protein.getGeneName() != null) {
            hashcode = 31 * hashcode + protein.getGeneName().hashCode();
        }
        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int nucleicAcidHashCode(NucleicAcid nucleicAcid) {
        int hashcode = 31;
        hashcode = 31 * hashcode + "Nucleic acid".hashCode();

        String preferredIdentifierStr = nucleicAcid.getPreferredIdentifier().getId();
        if (nucleicAcid.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }
        if (nucleicAcid.getDdbjEmblGenbank() != null) {
            hashcode = 31 * hashcode + nucleicAcid.getDdbjEmblGenbank().hashCode();
        }
        if (nucleicAcid.getRefseq() != null) {
            hashcode = 31 * hashcode + nucleicAcid.getRefseq().hashCode();
        }
        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int geneHashCode(Gene gene) {
        int hashcode = 31;
        hashcode = 31 * hashcode + "Gene".hashCode();

        String preferredIdentifierStr = gene.getPreferredIdentifier().getId();
        if (gene.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }
        if (gene.getEnsembl() != null) {
            hashcode = 31 * hashcode + gene.getEnsembl().hashCode();
        }
        if (gene.getEnsemblGenome() != null) {
            hashcode = 31 * hashcode + gene.getEnsemblGenome().hashCode();
        }
        if (gene.getEntrezGeneId() != null) {
            hashcode = 31 * hashcode + gene.getEntrezGeneId().hashCode();
        }
        if (gene.getRefseq() != null) {
            hashcode = 31 * hashcode + gene.getRefseq().hashCode();
        }
        return hashcode;
    }

    /*This had to be included as jami does not have this method yet*/
    public static int interactorHashCode(Interactor interactor) {
        int hashcode = 0;
        if (interactor instanceof Polymer) {
            hashcode = polymerHashCode((Polymer) interactor);
        }
        if (interactor instanceof Protein) {
            hashcode = proteinHashCode((Protein) interactor);
        }
        if (interactor instanceof NucleicAcid) {
            hashcode = nucleicAcidHashCode((NucleicAcid) interactor);
        }
        if (interactor instanceof Gene) {
            hashcode = geneHashCode((Gene) interactor);
        }
        if (interactor instanceof Molecule) {
            hashcode = moleculeHashCode((Molecule) interactor);
        } else {
            hashcode = 31;
            hashcode = 31 * hashcode + "Interactor".hashCode();
            String preferredIdentifierStr = interactor.getPreferredIdentifier().getId();
            if (interactor.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
                hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
            }
        }

        return hashcode;
    }
}
