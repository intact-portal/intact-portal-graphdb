package uk.ac.ebi.intact.graphdb.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.RangeUtils;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.range.UnambiguousRangeComparator;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;

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

    public static String createKeyForXref(Xref xref) {

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
            return Constants.NOT_GENERATED_UNIQUE_KEY;
        }

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createKeyForCvTerm(CvTerm cvTerm) {

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
                    uniqueKeyStringBuilder.append(createKeyForXref(xref));// ***calls xref unique key generation
                    if (counter != list1.size()) {
                        uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                    }
                    counter++;
                }
            } else {
                uniqueKeyStringBuilder.append(cvTerm.getShortName());
            }
        } catch (Exception e) {
            return Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createKeyForAlias(Alias alias) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "alias::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            CvTerm type = alias.getType();
            uniqueKeyStringBuilder.append(createKeyForCvTerm(type));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(alias.getName());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createKeyForAnnotation(Annotation annotation) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "annotation::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            CvTerm topic = annotation.getTopic();
            uniqueKeyStringBuilder.append(createKeyForCvTerm(topic));
            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            uniqueKeyStringBuilder.append(annotation.getValue());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createKeyForAuthor(String authorName) {

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

    public static String createKeyForOrganism(Organism organism) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "organism::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            uniqueKeyStringBuilder.append(organism.getTaxId());

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm cellType = organism.getCellType();
            uniqueKeyStringBuilder.append(createKeyForCvTerm(cellType));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm tissue = organism.getTissue();
            uniqueKeyStringBuilder.append(createKeyForCvTerm(tissue));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm compartment = organism.getCompartment();
            uniqueKeyStringBuilder.append(createKeyForCvTerm(compartment));

        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createKeyForParameter(Parameter parameter) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "parameter::";
        uniqueKeyStringBuilder.append(prefix);

        try {
            CvTerm type = parameter.getType();
            uniqueKeyStringBuilder.append(createKeyForCvTerm(type));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            CvTerm unit = parameter.getUnit();
            uniqueKeyStringBuilder.append(createKeyForCvTerm(unit));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            ParameterValue value = parameter.getValue();
            uniqueKeyStringBuilder.append(createKeyForParameterValue(value));

            uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            BigDecimal uncertainty = parameter.getUncertainty();
            uniqueKeyStringBuilder.append(uncertainty.intValue());
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createKeyForParameterValue(ParameterValue parameterValue) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "parameter value::";
        uniqueKeyStringBuilder.append(prefix);

        String parameterValueString = (parameterValue.getBase() != 0 && parameterValue.getFactor().doubleValue() != 0 ? parameterValue.getFactor().toString() + (parameterValue.getExponent() != 0 ? "x" + parameterValue.getBase() + "^(" + parameterValue.getExponent() + ")" : "") : "0");
        uniqueKeyStringBuilder.append(parameterValueString);

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createKeyForRange(String featureShortLabel, Range range) {

        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "range::";
        uniqueKeyStringBuilder.append(prefix);

        uniqueKeyStringBuilder.append(featureShortLabel);
        uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
        uniqueKeyStringBuilder.append(RangeUtils.convertRangeToString(range));

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    /*public void position(Position position){ // not needed now
        PositionUtils.convertPositionToString(position);
    }*/


    public static String createKeyForFeature(Feature featureEvidence) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "feature::";
        uniqueKeyStringBuilder.append(prefix);

        try {

            if (featureEvidence.getShortName() != null) {
                uniqueKeyStringBuilder.append(featureEvidence.getShortName());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getType() != null) {
                uniqueKeyStringBuilder.append(createKeyForCvTerm(featureEvidence.getType()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getRole() != null) {
                uniqueKeyStringBuilder.append(createKeyForCvTerm(featureEvidence.getRole()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getInterpro() != null) {
                uniqueKeyStringBuilder.append(featureEvidence.getInterpro());
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getIdentifiers() != null) {
                uniqueKeyStringBuilder.append(createXrefListUniqueKey(featureEvidence.getIdentifiers()));
                uniqueKeyStringBuilder.append(Constants.FIELD_SEPARATOR);
            }

            if (featureEvidence.getRanges() != null) {
                UnambiguousRangeComparator unambiguousXrefComparator = new UnambiguousRangeComparator();
                if (!featureEvidence.getRanges().isEmpty()) {
                    List<Range> list1 = new ArrayList<Range>(featureEvidence.getRanges());
                    Collections.sort(list1, unambiguousXrefComparator);
                    int counter = 1;
                    for (Range range : list1) {
                        uniqueKeyStringBuilder.append(createKeyForRange(featureEvidence.getShortName(), range));
                        if (counter != list1.size()) {
                            uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                        }
                        counter++;
                    }
                }
            }

            // delete any trailing underscore
            int lastUnderscoreIndex = uniqueKeyStringBuilder.lastIndexOf("_");
            if (lastUnderscoreIndex == (uniqueKeyStringBuilder.length() - 1)) {
                uniqueKeyStringBuilder.deleteCharAt(lastUnderscoreIndex);
            }
        } catch (Exception e) {
            return prefix + Constants.NOT_GENERATED_UNIQUE_KEY;
        }
        return uniqueKeyStringBuilder.toString().toLowerCase();
    }

    public static String createXrefListUniqueKey(Collection<Xref> xrefs) {
        StringBuilder uniqueKeyStringBuilder = new StringBuilder();
        String prefix = "xref list::";
        uniqueKeyStringBuilder.append(prefix);

        UnambiguousXrefComparator unambiguousXrefComparator = new UnambiguousXrefComparator();
        if (!xrefs.isEmpty()) {
            List<Xref> list1 = new ArrayList<Xref>(xrefs);
            Collections.sort(list1, unambiguousXrefComparator);
            int counter = 1;
            for (Xref xref : list1) {
                uniqueKeyStringBuilder.append(createKeyForXref(xref));
                if (counter != list1.size()) {
                    uniqueKeyStringBuilder.append(Constants.LIST_SEPARATOR);
                }
                counter++;
            }

        }

        return uniqueKeyStringBuilder.toString().toLowerCase();
    }


  /*  public static int polymerHashCode(Polymer polymer) {

        int hashcode = 31;

        String preferredIdentifierStr = polymer.getPreferredIdentifier().getId();
        if (polymer.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }
        if (polymer.getOrganism() != null) {
            hashcode = 31 * hashcode + UnambiguousOrganismComparator.hashCode(polymer.getOrganism());/
* call organism
        }
        if (polymer.getSequence() != null) {
            hashcode = 31 * hashcode + polymer.getSequence().hashCode();// use hashcode (as it is)
        }

        return hashcode;
    }

    public static int proteinHashCode(Protein protein) {

        int hashcode = 31;
        hashcode = 31 * hashcode + "Protein".hashCode();

        String preferredIdentifierStr = null;
        if(protein.getPreferredIdentifier()!=null) {
            preferredIdentifierStr=protein.getPreferredIdentifier().getId();
        }

        if (preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }else{
            log.info("Preferred Identifier is null"+protein.getShortName());
        }
        return hashcode;
    }

    public static int nucleicAcidHashCode(NucleicAcid nucleicAcid) {
        int hashcode = 31;
        hashcode = 31 * hashcode + nucleicAcid.getInteractorType();/
** call CvTerm

        String preferredIdentifierStr = null;
        if(nucleicAcid.getPreferredIdentifier()!=null) {
            preferredIdentifierStr=nucleicAcid.getPreferredIdentifier().getId();
        }

        if (preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }else{
            log.info("Preferred Identifier is null"+nucleicAcid.getShortName());
        }
        return hashcode;
    }

    public static int geneHashCode(Gene gene) {
        hashcode = 31 * hashcode + gene.getInteractorType();/
** call CvTerm

        String preferredIdentifierStr = null;
        if (gene.getPreferredIdentifier() != null) {
            preferredIdentifierStr = gene.getPreferredIdentifier().getId();
        }

        if (preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        } else {
            log.info("Preferred Identifier is null" + gene.getShortName());
        }
    }

    public static int moleculeHashCode(Molecule molecule) {

        hashcode = 31 * hashcode + molecule.getInteractorType();/
** call CvTerm

        String preferredIdentifierStr = null;
        if (molecule.getPreferredIdentifier() != null) {
            preferredIdentifierStr = molecule.getPreferredIdentifier().getId();
        }

        if (preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        } else {
            log.info("Preferred Identifier is null" + molecule.getShortName());
        }
    }

    public static int interactorHashCode(Interactor interactor) {
        int hashcode = 0;
        if (interactor instanceof Polymer) {
            hashcode = polymerHashCode((Polymer) interactor);
        }else
        if (interactor instanceof Protein) {
            hashcode = proteinHashCode((Protein) interactor);
        }else
        if (interactor instanceof NucleicAcid) {
            hashcode = nucleicAcidHashCode((NucleicAcid) interactor);
        }else
        if (interactor instanceof Gene) {
            hashcode = geneHashCode((Gene) interactor);
        }else
        if (interactor instanceof Molecule) {
            hashcode = moleculeHashCode((Molecule) interactor);
        } else if (!interactor.getIdentifiers().isEmpty()){
            UnambiguousExternalIdentifierComparator unambiguousInteractorComparator=new UnambiguousExternalIdentifierComparator();
            List<Xref> list1 = new ArrayList<Xref>(interactor.getIdentifiers());
            Collections.sort(list1, unambiguousInteractorComparator);
            for (Xref ref : list1){
                hashcode = 31*hashcode + UnambiguousExternalIdentifierComparator.hashCode(ref);// ***calls xref unique key generation
            }
        }

        return hashcode;
    }

    public static int participantHashCode(ParticipantEvidence participantEvidence) {
        // since there was not hashcode implemented in jami, we had to come up with this
        int hashcode = 31;
        if (participantEvidence.getInteractor() != null) {
            hashcode = 31 * hashcode + interactorHashCode(participantEvidence.getInteractor()); /
** call interactor
        }
        if (participantEvidence.getBiologicalRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(participantEvidence.getBiologicalRole());/
** call cvTerm
        }
        if (participantEvidence.getExperimentalRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(participantEvidence.getExperimentalRole());/
** call cvTerm
        }
        if (!participantEvidence.getIdentificationMethods().isEmpty()) {
            hashcode = 31 * hashcode + HashCode.cvTermsHashCode(participantEvidence.getIdentificationMethods());// *** call cvTerm List
        }
        if (participantEvidence.getExperimentalPreparations() != null) {
            hashcode = 31 * hashcode + HashCode.cvTermsHashCode(participantEvidence.getExperimentalPreparations());// *** call cvTerm list
        }
        if (participantEvidence.getExpressedInOrganism() != null) {
            hashcode = 31 * hashcode + UnambiguousOrganismComparator.hashCode(participantEvidence.getExpressedInOrganism());// call organism
        }
        if (participantEvidence.getParameters() != null) {
            hashcode = 31 * hashcode + HashCode.parametersHashCode(participantEvidence.getParameters());// call parameters list
        }
        if (!participantEvidence.getFeatures().isEmpty()) {
            hashcode = 31 * hashcode + HashCode.featuresHashCode(participantEvidence.getFeatures()); // call features list
        }

        return hashcode;
    }

    public String binaryInteractionEvidence(BinaryInteractionEvidence binaryInteractionEvidence) {
        // since there was not hashcode implemented in jami, we had to come up with this
        int hashcode = 31;

        int hashcodeParticpantA=0;
        int hashcodeParticpantB=0;
        if (binaryInteractionEvidence.getParticipantA() != null) {
            hashcodeParticpantA = HashCode.participantHashCode(binaryInteractionEvidence.getParticipantA()); // call Participant
            hashcode = 31 * hashcode + hashcodeParticpantA;
        }
        if (binaryInteractionEvidence.getParticipantB() != null) {
            hashcodeParticpantB=HashCode.participantHashCode(binaryInteractionEvidence.getParticipantB());// call Participant
            hashcode = 31 * hashcode + hashcodeParticpantB;
        }

        binaryInteractionEvidence.getInteractionType(); // call cvTerm

        String uniqueSumValue = "" + hashcodeParticpantA + "-" +hashcodeParticpantB;
        hashcode = 31 * hashcode + uniqueSumValue.hashCode();

        if (binaryInteractionEvidence.getComplexExpansion() != null) {
            hashcode = 31 * hashcode +
                    UnambiguousCvTermComparator.hashCode(binaryInteractionEvidence.getComplexExpansion());// call cvTerm
        }
        (!binaryInteractionEvidence.getIdentifiers().isEmpty()) {
            UnambiguousExternalIdentifierComparator unambiguousInteractorComparator = new UnambiguousExternalIdentifierComparator();
            List<Xref> list1 = new ArrayList<Xref>(binaryInteractionEvidence.getIdentifiers());
            Collections.sort(list1, unambiguousInteractorComparator);
            for (Xref ref : list1) {
                hashcode = 31 * hashcode + UnambiguousExternalIdentifierComparator.hashCode(ref);// ***calls xref unique key generation
            }
        }

        return hashcode + "";
    }

        public int interactionEvidence() {

            if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
                return Integer.parseInt(this.getUniqueKey());
            }

            if (!cv1.getIdentifiers().isEmpty()) {
                List<Xref> list1 = new ArrayList<Xref>(cv1.getIdentifiers());
                Collections.sort(list1, unambiguousCvTermComparator.getIdentifierComparator());
                for (Xref ref : list1) {
                    hashcode = 31 * hashcode + UnambiguousExternalIdentifierComparator.hashCode(ref);// ***calls xref unique key generation
                }
            }
            return hashcode;
        }

        public static int hashCode(Stoichiometry stc){
            if (stoichiometryComparator == null){
                stoichiometryComparator = new StoichiometryComparator();
            }

            if (stc == null){
                return 0;
            }

            int hashcode = 31;
            hashcode = 31 * hashcode + stc.getMinValue();
            hashcode = 31*hashcode + stc.getMaxValue();

            return hashcode;
        }

        public static int stoichiometry(Stoichiometry stc){
            if (stoichiometryComparator == null){
                stoichiometryComparator = new StoichiometryComparator();
            }

            if (stc == null){
                return 0;
            }

            int hashcode = 31;
            hashcode = 31 * hashcode + stc.getMinValue();
            hashcode = 31*hashcode + stc.getMaxValue();

            return hashcode;
        }

    public int source() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + "Source".hashCode();
        if (this.getShortName() != null) {
            hashcode = 31 * hashcode + this.getShortName().toLowerCase().hashCode();// either call cvTerm or use as it is
        }
        return hashcode;
    }

    public int entity() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + "Entity".hashCode();
        if (this.getInteractor() != null) {
            hashcode = 31 * hashcode + this.getInteractor().hashCode();// call interactor
        }
        if (this.getStoichiometry() != null) {
            hashcode = 31 * hashcode + this.getStoichiometry().hashCode(); // call stoichiometry
        }
        return hashcode;
    }

    @Override
    public int experimentalEntity() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + "ExperimentalEntity".hashCode();
        hashcode = 31 * hashcode + super.hashCode(); // call entity
        if (this.getFeatures() != null) {
            hashcode = 31 * hashcode + HashCode.featuresGraphHashCode(super.getFeatures()); // call features list
        }
        return hashcode;
    }

    public static int variableParameter(VariableParameter param){
        if (unambiguousVariableParameterComparator == null){
            unambiguousVariableParameterComparator = new UnambiguousVariableParameterComparator();
        }

        if (param == null){
            return 0;
        }

        int hashcode = 31;
        String description = param.getDescription();
        hashcode = 31*hashcode + (description != null ? description.toLowerCase().trim().hashCode() : 0);

        experiment // call experiment

        CvTerm unit = param.getUnit();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(unit);

        List<VariableParameterValue> list1 = new ArrayList<VariableParameterValue>(param.getVariableValues());
        Collections.sort(list1, unambiguousVariableParameterComparator.getVariableParameterValueCollectionComparator().getObjectComparator());
        for (VariableParameterValue value : list1){
            hashcode = 31*hashcode + VariableParameterValueComparator.hashCode(value);
        }

        return hashcode;
    }

    public static int variableParameterValue(VariableParameterValue value){
        if (variableParameterValueComparator == null){
            variableParameterValueComparator = new VariableParameterValueComparator();
        }

        if (value == null){
            return 0;
        }

        // call variableParameter

        int hashcode = 31;
        hashcode = 31*hashcode + (value.getValue() != null ? value.getValue().trim().toLowerCase().hashCode() : 0);
        hashcode = 31*hashcode + (value.getOrder() != null ? value.getOrder() : 0);

        return hashcode;
    }

    public static int variableParametersValueSet(Collection<VariableParameterValue> variableParameterValues) {
        int hashcode = 0;

        Collections.sort(variableParameterValues, unambiguousVariableParameterComparator.getVariableParameterValueCollectionComparator().getObjectComparator());
        for (VariableParameterValue value : list1){
            hashcode = 31*hashcode + VariableParameterValueComparator.hashCode(value); // call variableParameterValue
        }
        return hashcode;
    }

    public static int experiment(Experiment exp){

            UnambiguousInteractionEvidenceComparator unambiguousExperimentComparator = new UnambiguousInteractionEvidenceComparator();


        if (exp == null){
            return 0;
        }

        int hashcode = 31;
        Publication pub = exp.getPublication();
        hashcode = 31*hashcode + UnambiguousPublicationComparator.hashCode(pub);

        CvTerm detMethod = exp.getInteractionDetectionMethod();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(detMethod);

        Organism organism = exp.getHostOrganism();
        hashcode = 31*hashcode + UnambiguousOrganismComparator.hashCode(organism);

        List<InteractionEvidence> list1 = new ArrayList<InteractionEvidence>(exp.getInteractionEvidences());
        Collections.sort(list1, unambiguousExperimentComparator);
        for (VariableParameter param : list1){
            hashcode = 31*hashcode + UnambiguousVariableParameterComparator.hashCode(param);// call interaction Evidence
        }

        return hashcode;
    }

    public int publication() {

        if (!isForceHashCodeGeneration() && this.getUniqueKey() != null && !this.getUniqueKey().isEmpty()) {
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + "Publication".hashCode();
        if (this.getPubmedId() != null) {
            hashcode = 31 * hashcode + this.getPubmedId().hashCode();
        }
        return hashcode;
    }

    public int curationDepth() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        if (this.getCurationDepth() != null) {
            hashcode = 31 * hashcode + this.getCurationDepth().toLowerCase().hashCode();
        }

        return hashcode;
    }

    public static int confidence(Confidence conf){
        if (unambiguousConfidenceComparator == null){
            unambiguousConfidenceComparator = new UnambiguousConfidenceComparator();
        }

        if (conf == null){
            return 0;
        }

        int hashcode = 31;
        CvTerm type = conf.getType();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(type);// call cvTerm

        String value = conf.getValue();
        hashcode = 31*hashcode + value.hashCode();

        return hashcode;
    }

    public static int checksum(Checksum checksum){
        if (unambiguousChecksumComparator == null){
            unambiguousChecksumComparator = new UnambiguousChecksumComparator();
        }

        if (checksum == null){
            return 0;
        }

        int hashcode = 31;
        CvTerm method = checksum.getMethod();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(method);// call CvTerm

        String value = checksum.getValue();
        hashcode = 31*hashcode + value.hashCode();

        return hashcode;
    }

    public static int causalRelationship(CausalRelationship rel){
        if (unambiguousCausalRelationshipComparator == null){
            unambiguousCausalRelationshipComparator = new UnambiguousCausalRelationshipComparator();
        }

        if (rel == null){
            return 0;
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(rel.getRelationType());// call cvTerm
        hashcode = 31*hashcode + UnambiguousEntityBaseComparator.hashCode(rel.getTarget());// call entity

        return hashcode;
    }

    public int cluster() {

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31 * hashcode + "Cluster".hashCode();

        // we don't need to sort it because it is already sorted in graph
        if (this.getInteractorPA() != null) {
            hashcode = 31 * hashcode + this.getInteractorPA().hashCode();// call interactor
        }

        if (this.getinteractorPB() != null) {
            hashcode = 31 * hashcode + this.getinteractorPB().hashCode();// call interactor
        }


        return hashcode;
    }
*/

}
