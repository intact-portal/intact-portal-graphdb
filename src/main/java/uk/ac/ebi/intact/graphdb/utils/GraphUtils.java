package uk.ac.ebi.intact.graphdb.utils;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

/**
 * Utility class for intact classes and properties
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>08/01/14</pre>
 */


// TO DO Review
public class GraphUtils {

    public static final String DATABASE_OBJCLASS="CvDatabase";
    public static final String QUALIFIER_OBJCLASS="CvXrefQualifier";
    public static final String TOPIC_OBJCLASS="CvTopic";
    public static final String ALIAS_TYPE_OBJCLASS="CvAliasType";
    public static final String UNIT_OBJCLASS ="CvParameterUnit";
    public static final String FEATURE_TYPE_OBJCLASS ="CvFeatureType";
    public static final String EXPERIMENTAL_ROLE_OBJCLASS ="CvExperimentalRole";
    public static final String BIOLOGICAL_ROLE_OBJCLASS ="CvBiologicalRole";
    public static final String INTERACTION_DETECTION_METHOD_OBJCLASS ="CvInteraction";
    public static final String INTERACTION_TYPE_OBJCLASS ="CvInteractionType";
    public static final String PARTICIPANT_DETECTION_METHOD_OBJCLASS ="CvIdentification";
    public static final String PARTICIPANT_EXPERIMENTAL_PREPARATION_OBJCLASS ="CvExperimentalPreparation";
    public static final String INTERACTOR_TYPE_OBJCLASS ="CvInteractorType";
    public static final String RANGE_STATUS_OBJCLASS ="CvFuzzyType";
    public static final String CONFIDENCE_TYPE_OBJCLASS ="CvConfidenceType";
    public static final String PARAMETER_TYPE_OBJCLASS ="CvParameterType";
    public static final String CELL_TYPE_OBJCLASS ="CvCellType";
    public static final String TISSUE_OBJCLASS ="CvTissue";
    public static final String FEATURE_METHOD_OBJCLASS ="CvFeatureIdentification";
    public static final String PUBLICATION_STATUS_OBJCLASS ="CvPublicationStatus";
    public static final String LIFECYCLE_EVENT_OBJCLASS ="CvLifecycleEvent";


    public static GraphCvTerm createMIUnit(String name, String MI) {
        return createGraphMITerm(name, MI, UNIT_OBJCLASS);
    }

    public static GraphCvTerm createMIExperimentalPreparation(String name, String MI) {
        return createGraphMITerm(name, MI, PARTICIPANT_EXPERIMENTAL_PREPARATION_OBJCLASS);
    }

    public static GraphCvTerm createLifecycleEvent(String name) {
        return new GraphCvTerm(name, (String) null, (String) null, LIFECYCLE_EVENT_OBJCLASS);
    }

    public static GraphCvTerm createCellType(String name) {
        return new GraphCvTerm(name, (String) null, (String) null, CELL_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createTissue(String name) {
        return new GraphCvTerm(name, (String) null, (String) null, TISSUE_OBJCLASS);
    }

    public static GraphCvTerm createLifecycleStatus(String name) {
        return new GraphCvTerm(name, (String) null, (String) null, PUBLICATION_STATUS_OBJCLASS);
    }

    public static GraphCvTerm createMIInteractionType(String name, String MI) {
        return createGraphMITerm(name, MI, INTERACTION_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createMIParticipantIdentificationMethod(String name, String MI) {
        return createGraphMITerm(name, MI, PARTICIPANT_DETECTION_METHOD_OBJCLASS);
    }

    public static GraphCvTerm createMIInteractorType(String name, String MI) {
        return createGraphMITerm(name, MI, INTERACTOR_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createMIFeatureDetectionMethod(String name, String MI) {
        return createGraphMITerm(name, MI, FEATURE_METHOD_OBJCLASS);
    }

    public static GraphCvTerm createMIParameterType(String name, String MI) {
        return createGraphMITerm(name, MI, PARAMETER_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createMIConfidenceType(String name, String MI) {
        return createGraphMITerm(name, MI, CONFIDENCE_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createMIRangeStatus(String name, String MI) {
        return createGraphMITerm(name, MI, RANGE_STATUS_OBJCLASS);
    }

    public static GraphCvTerm createMIDatabase(String name, String MI) {
        return createGraphMITerm(name, MI, DATABASE_OBJCLASS);
    }

    public static GraphCvTerm createMIQualifier(String name, String MI) {
        return createGraphMITerm(name, MI, QUALIFIER_OBJCLASS);
    }

    public static GraphCvTerm createMITopic(String name, String MI) {
        return createGraphMITerm(name, MI, TOPIC_OBJCLASS);
    }

    public static GraphCvTerm createMIFeatureType(String name, String MI) {
        return createGraphMITerm(name, MI, FEATURE_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createMODFeatureType(String name, String MOD) {
        return createIntactMODTerm(name, MOD, FEATURE_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createMIBiologicalRole(String name, String MI) {
        return createGraphMITerm(name, MI, BIOLOGICAL_ROLE_OBJCLASS);
    }

    public static GraphCvTerm createMIExperimentalRole(String name, String MI) {
        return createGraphMITerm(name, MI, EXPERIMENTAL_ROLE_OBJCLASS);
    }

    public static GraphCvTerm createMIInteractionDetectionMethod(String name, String MI) {
        return createGraphMITerm(name, MI, INTERACTION_DETECTION_METHOD_OBJCLASS);
    }

    public static GraphCvTerm createMIAliasType(String name, String MI) {
        return createGraphMITerm(name, MI, ALIAS_TYPE_OBJCLASS);
    }

    public static GraphCvTerm createGraphMITerm(String name, String MI, String typeLabel) {
        if (MI != null) {
            CvTerm psimi = createPsiMiDatabase();
            return new GraphCvTerm(name, new GraphXref(psimi, MI, createIdentityQualifier(psimi)), typeLabel);
        } else {
            return new GraphCvTerm(name, (String) null, (String) null);
        }
    }

    public static GraphCvTerm createIntactMODTerm(String name, String MOD, String typeLabel) {
        if (MOD != null) {
            return new GraphCvTerm(name, new GraphXref(new GraphCvTerm(CvTerm.PSI_MOD, null, CvTerm.PSI_MOD_MI, DATABASE_OBJCLASS), MOD, createIdentityQualifier()), typeLabel);
        } else {
            return new GraphCvTerm(name, (String) null, (String) null);
        }
    }

    public static CvTerm createPsiMiDatabase() {
        CvTerm psiMi = new GraphCvTerm(CvTerm.PSI_MI, null, (String) null, DATABASE_OBJCLASS);
        Xref psiMiXref = new GraphXref(psiMi, CvTerm.PSI_MI_MI, createIdentityQualifier(psiMi));
        psiMi.getIdentifiers().add(psiMiXref);
        return psiMi;
    }

    public static CvTerm createIdentityQualifier() {
        CvTerm identity = new GraphCvTerm(Xref.IDENTITY, null, (String) null, QUALIFIER_OBJCLASS);
        Xref psiMiXref = new GraphXref(createPsiMiDatabase(identity), Xref.IDENTITY_MI, identity);
        identity.getIdentifiers().add(psiMiXref);
        return identity;
    }

    public static CvTerm createPsiMiDatabase(CvTerm identity) {
        CvTerm psiMi = new GraphCvTerm(CvTerm.PSI_MI, null, (String) null, DATABASE_OBJCLASS);
        Xref psiMiXref = new GraphXref(psiMi, CvTerm.PSI_MI_MI, identity);
        psiMi.getIdentifiers().add(psiMiXref);
        return psiMi;
    }

    public static CvTerm createIdentityQualifier(CvTerm psiMi) {
        CvTerm identity = new GraphCvTerm(Xref.IDENTITY, null, (String) null, QUALIFIER_OBJCLASS);
        Xref psiMiXref = new GraphXref(psiMi, Xref.IDENTITY_MI, identity);
        identity.getIdentifiers().add(psiMiXref);
        return identity;
    }
}
