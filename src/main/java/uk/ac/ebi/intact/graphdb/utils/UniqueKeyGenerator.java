package uk.ac.ebi.intact.graphdb.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.PositionUtils;
import psidev.psi.mi.jami.utils.RangeUtils;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.interactor.UnambiguousInteractorComparator;
import psidev.psi.mi.jami.utils.comparator.organism.UnambiguousOrganismComparator;
import psidev.psi.mi.jami.utils.comparator.parameter.ParameterValueComparator;
import psidev.psi.mi.jami.utils.comparator.participant.StoichiometryComparator;
import psidev.psi.mi.jami.utils.comparator.range.UnambiguousPositionComparator;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousExternalIdentifierComparator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by anjali on 27/03/19.
 */
public class UniqueKeyGenerator {

    private static final Log log = LogFactory.getLog(UniqueKeyGenerator.class);

    public void xref(){
        if (unambiguousXrefComparator == null){
            unambiguousXrefComparator = new UnambiguousXrefComparator();
        }
        if (xref == null){
            return 0;
        }

        int hashcode = 31;
        CvTerm database1 = xref.getDatabase();
        String mi = database1.getMIIdentifier();
        String mod = database1.getMODIdentifier();
        String par = database1.getPARIdentifier();

        if (mi != null){
            hashcode = 31*hashcode + mi.hashCode();
        }
        else if (mod != null){
            hashcode = 31*hashcode + mod.hashCode();
        }
        else if (par != null){
            hashcode = 31*hashcode + par.hashCode();
        }
        else {
            hashcode = 31*hashcode + database1.getShortName().toLowerCase().trim().hashCode();
        }

        hashcode = 31 * hashcode + xref.getId().hashCode();

        CvTerm qualifier = xref.getQualifier();
        if (qualifier != null){
            String qualifierMi = qualifier.getMIIdentifier();

            if (qualifierMi != null){
                hashcode = 31*hashcode + qualifierMi.hashCode();
            }
            else {
                hashcode = 31*hashcode + qualifier.getShortName().toLowerCase().trim().hashCode();
            }
        }

        return hashcode;
    }

    public static int cvTerm(CvTerm cv1){
        if (unambiguousCvTermComparator == null){
            unambiguousCvTermComparator = new UnambiguousCvTermComparator();
        }

        if (cv1 == null){
            return 0;
        }

        int hashcode = 31;

        if (cv1.getMIIdentifier() != null){
            hashcode = 31*hashcode + cv1.getMIIdentifier().hashCode();
        }
        else if (cv1.getMODIdentifier() != null){
            hashcode = 31*hashcode + cv1.getMODIdentifier().hashCode();
        }
        else if (cv1.getPARIdentifier() != null){
            hashcode = 31*hashcode + cv1.getPARIdentifier().hashCode();
        }
        else if (!cv1.getIdentifiers().isEmpty()){
            List<Xref> list1 = new ArrayList<Xref>(cv1.getIdentifiers());
            Collections.sort(list1, unambiguousCvTermComparator.getIdentifierComparator());
            for (Xref ref : list1){
                hashcode = 31*hashcode + UnambiguousExternalIdentifierComparator.hashCode(ref);// ***calls xref unique key generation
            }
        }
        else {
            hashcode = 31*hashcode + cv1.getShortName().toLowerCase().trim().hashCode();
        }

        return hashcode;
    }

    public static int alias(Alias alias){
        if (unambiguousAliasComparator == null){
            unambiguousAliasComparator = new UnambiguousAliasComparator();
        }
        if (alias == null){
            return 0;
        }

        int hashcode = 31;
        CvTerm type = alias.getType();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(type);//*** call cvTerm

        String name = alias.getName();
        hashcode = 31*hashcode + name.hashCode();

        return hashcode;
    }

    public static int annotation(Annotation annot){
        if (unambiguousAnnotationComparator == null){
            unambiguousAnnotationComparator = new UnambiguousAnnotationComparator();
        }

        if (annot == null){
            return 0;
        }

        int hashcode = 31;
        CvTerm topic = annot.getTopic();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(topic);//*** call cvTerm

        String text = annot.getValue();
        hashcode = 31*hashcode + (text != null ? text.toLowerCase().trim().hashCode() : 0);

        return hashcode;
    }

    public int author() {
        //*** take orchid in future
        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        if (this.getAuthorName() != null) {
            hashcode = 31 * hashcode + this.getAuthorName().toLowerCase().hashCode();
        }

        return hashcode;
    }

    public static int organism(Organism organism){
        if (unambiguousOrganismComparator == null){
            unambiguousOrganismComparator = new UnambiguousOrganismComparator();
        }

        if (organism == null){
            return 0;
        }

        int hashcode = 31;
        hashcode = 31*hashcode + organism.getTaxId();

        CvTerm cellType = organism.getCellType();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(cellType);//*** call cvTerm

        CvTerm tissue = organism.getTissue();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(tissue);//*** call cvTerm

        CvTerm compartment = organism.getCompartment();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(compartment);//*** call cvTerm

        return hashcode;
    }

    public static int parameter(Parameter param){
        if (unambiguousParameterComparator == null){
            unambiguousParameterComparator = new UnambiguousParameterComparator();
        }

        if (param == null){
            return 0;
        }

        int hashcode = 31;
        CvTerm type = param.getType();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(type);

        CvTerm unit = param.getUnit();
        hashcode = 31*hashcode + UnambiguousCvTermComparator.hashCode(unit);

        ParameterValue value = param.getValue();
        hashcode = 31*hashcode + ParameterValueComparator.hashCode(value);//*** call parameterValue

        BigDecimal uncertainty = param.getUncertainty();
        hashcode = 31*hashcode + (uncertainty != null ? uncertainty.hashCode() : 0);

        return hashcode;
    }

    public int parameterValue(){

        if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
            return Integer.parseInt(this.getUniqueKey());
        }

        int hashcode = 31;
        hashcode = 31*hashcode + (getBase() != 0 && getFactor().doubleValue() != 0 ? getFactor().toString()+(getExponent() != 0 ? "x"+getBase()+"^("+getExponent()+")" : "") : "0").hashCode();

        return hashcode;
    }

    public void range(String featureShortLabel){
        featureShortLabel;
        RangeUtils.convertRangeToString(range);
    }

   /* public void position(Position position){
        PositionUtils.convertPositionToString(position);
    }*/

    public static int featureHashCode(FeatureEvidence featureEvidence) {
        int hashcode = 31;
        hashcode = 31 * hashcode + "Feature:".hashCode();

        if (featureEvidence.getShortName() != null) {
            hashcode = 31 * hashcode + featureEvidence.getShortName().hashCode();
        }

        if (featureEvidence.getType() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(featureEvidence.getType()); //*** call cvTerm
        }

        if (featureEvidence.getRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(featureEvidence.getRole());//*** call cvTerm
        }

        if (featureEvidence.getInterpro() != null) {
            hashcode = 31 * hashcode + featureEvidence.getInterpro().hashCode();
        }

        if (featureEvidence.getIdentifiers() != null) {
            hashcode = 31 * hashcode + HashCode.identifiersHashCode(featureEvidence.getIdentifiers());//*** call xrefList
        }

        if (featureEvidence.getRanges() != null) {
            hashcode = 31 * hashcode + HashCode.rangesHashCode(featureEvidence.getRanges());//*** call rangesList
        }

        return hashcode;
    }

    public static int polymerHashCode(Polymer polymer) {

        int hashcode = 31;

        String preferredIdentifierStr = polymer.getPreferredIdentifier().getId();
        if (polymer.getPreferredIdentifier() != null && preferredIdentifierStr != null) {
            hashcode = 31 * hashcode + preferredIdentifierStr.hashCode();
        }
        if (polymer.getOrganism() != null) {
            hashcode = 31 * hashcode + UnambiguousOrganismComparator.hashCode(polymer.getOrganism());//** call organism
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
        hashcode = 31 * hashcode + nucleicAcid.getInteractorType();//*** call CvTerm

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
        hashcode = 31 * hashcode + gene.getInteractorType();//*** call CvTerm

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

        hashcode = 31 * hashcode + molecule.getInteractorType();//*** call CvTerm

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
            hashcode = 31 * hashcode + interactorHashCode(participantEvidence.getInteractor()); //*** call interactor
        }
        if (participantEvidence.getBiologicalRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(participantEvidence.getBiologicalRole());//*** call cvTerm
        }
        if (participantEvidence.getExperimentalRole() != null) {
            hashcode = 31 * hashcode + UnambiguousCvTermComparator.hashCode(participantEvidence.getExperimentalRole());//*** call cvTerm
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
        (!binaryInteractionEvidence.getIdentifiers().isEmpty()){
            UnambiguousExternalIdentifierComparator unambiguousInteractorComparator=new UnambiguousExternalIdentifierComparator();
            List<Xref> list1 = new ArrayList<Xref>(binaryInteractionEvidence.getIdentifiers());
            Collections.sort(list1, unambiguousInteractorComparator);
            for (Xref ref : list1){
                hashcode = 31*hashcode + UnambiguousExternalIdentifierComparator.hashCode(ref);// ***calls xref unique key generation
            }

        return hashcode + "";
    }

        public int interactionEvidence() {

            if(!isForceHashCodeGeneration() &&this.getUniqueKey()!=null&&!this.getUniqueKey().isEmpty()){
                return Integer.parseInt(this.getUniqueKey());
            }

            int hashcode = 31;
            if (this.getAc() != null) {
                hashcode = 31 * hashcode + this.getAc().hashCode();
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

}
