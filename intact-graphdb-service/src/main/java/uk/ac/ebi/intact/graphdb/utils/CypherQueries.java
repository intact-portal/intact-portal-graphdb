package uk.ac.ebi.intact.graphdb.utils;

import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    /*Common BinaryInteractionEvidences of any two interactors*/

    public static final String COMM_NEIGH_OF_INTOR =
            "MATCH (interactorA:GraphInteractor)<-[:" + RelationshipTypes.INTERACTORS + "]-(binaryIE:GraphBinaryInteractionEvidence)-[:" + RelationshipTypes.INTERACTORS + "]->(interactorB:GraphInteractor)" +
                    " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB" +
                    " UNWIND interactions as interaction" +
                    " MATCH (interaction:GraphBinaryInteractionEvidence) -[experiment:" + RelationshipTypes.EXPERIMENT + "] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:" + RelationshipTypes.INTERACTION_DETECTION_METHOD + "]->(dm_cvterm:GraphCvTerm)," +
                    " (interaction:GraphBinaryInteractionEvidence) - [interactionType:" + RelationshipTypes.INTERACTION_TYPE + "] -> (it_cvterm:GraphCvTerm)," +
                    " (graphExperiment:GraphExperiment)<-[publication:" + RelationshipTypes.PUB_EXP + "]-(graphpublication:GraphPublication)," +
                    " (graphpublication)-[pubmedXrefR:" + RelationshipTypes.PMID + "]-(pubmedXrefN:GraphXref)" +
                    " WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB))" +
                    " RETURN interactorA,interactorB,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments," +
                    " COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms," +
                    " COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications,COLLECT(pubmedXrefR) as pubmedXrefRs,COLLECT(pubmedXrefN) as pubmedXrefNs ORDER BY interactorA.ac";


    public static final String COMM_NEIGH_OF_INTOR_SELF_INTERACTION_CASE =
            "MATCH (interactorA:GraphInteractor)<-[:" + RelationshipTypes.INTERACTOR_A + "]-(binaryIE:GraphBinaryInteractionEvidence) -[:" + RelationshipTypes.INTERACTOR_B + "]->(interactorB:GraphInteractor)" +
                    " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB" +
                    " UNWIND interactions as interaction" +
                    " MATCH (interaction:GraphBinaryInteractionEvidence) -[experiment:" + RelationshipTypes.EXPERIMENT + "] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:" + RelationshipTypes.INTERACTION_DETECTION_METHOD + "]->(dm_cvterm:GraphCvTerm)," +
                    " (interaction:GraphBinaryInteractionEvidence) - [interactionType:" + RelationshipTypes.INTERACTION_TYPE + "] -> (it_cvterm:GraphCvTerm)," +
                    " (graphExperiment:GraphExperiment)<-[publication:" + RelationshipTypes.PUB_EXP + "]-(graphpublication:GraphPublication)," +
                    " (graphpublication)-[pubmedXrefR:" + RelationshipTypes.PMID + "]-(pubmedXrefN:GraphXref)" +
                    " WHERE  (ID(interactorA)=ID(interactorB))" +
                    " RETURN interactorA,interactorB,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments," +
                    " COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms," +
                    " COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications,COLLECT(pubmedXrefR) as pubmedXrefRs,COLLECT(pubmedXrefN) as pubmedXrefNs ORDER BY interactorA.ac";

    public static final String COMM_NEIGH_OF_INTOR_SELF_PUTATIVE_CASE =
            "MATCH (interactorA:GraphInteractor)-[:" + RelationshipTypes.INTERACTOR_A + "]-(binaryIE:GraphBinaryInteractionEvidence)" +
                    " WHERE  (NOT (binaryIE)-[:" + RelationshipTypes.INTERACTOR_B + "]-(:GraphInteractor))" +
                    " WITH  COLLECT(binaryIE) as interactions,interactorA" +
                    " UNWIND interactions as interaction" +
                    " MATCH (interaction:GraphBinaryInteractionEvidence) -[experiment:" + RelationshipTypes.EXPERIMENT + "] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:" + RelationshipTypes.INTERACTION_DETECTION_METHOD + "]->(dm_cvterm:GraphCvTerm)," +
                    " (interaction:GraphBinaryInteractionEvidence) - [interactionType:" + RelationshipTypes.INTERACTION_TYPE + "] -> (it_cvterm:GraphCvTerm)," +
                    " (graphExperiment:GraphExperiment)<-[publication:" + RelationshipTypes.PUB_EXP + "]-(graphpublication:GraphPublication)," +
                    " (graphpublication)-[pubmedXrefR:" + RelationshipTypes.PMID + "]-(pubmedXrefN:GraphXref)" +
                    " RETURN interactorA,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments," +
                    " COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms," +
                    " COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications,COLLECT(pubmedXrefR) as pubmedXrefRs,COLLECT(pubmedXrefN) as pubmedXrefNs ORDER BY interactorA.ac";
    /*
    * Equivalent Query String : MATCH (interactorA:GraphInteractor)<-[:interactorA]-(binaryIE:GraphInteractionEvidence)-[:interactorB]->(interactorB:GraphInteractor)
                                WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB
                                WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB)) RETURN COUNT(*)
    * */
    public static final String INTERACTOR_PAIR_COUNT =
            "MATCH (interactorA:GraphInteractor)<-[:" + RelationshipTypes.INTERACTORS + "]-(binaryIE:GraphBinaryInteractionEvidence) -[:" + RelationshipTypes.INTERACTORS + "]->(interactorB:GraphInteractor) " +
                    " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB" +
                    " WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB)) " +
                    " WITH COUNT(*) as binaryCount   MATCH (interactorA:GraphInteractor)<-[:" + RelationshipTypes.INTERACTOR_A + "]-(binaryIE:GraphBinaryInteractionEvidence)" +
                    " OPTIONAL MATCH (binaryIE)-[:" + RelationshipTypes.INTERACTOR_B + "]->(interactorB:GraphInteractor) " +
                    " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB,binaryCount" +
                    " WHERE  (ID(interactorA) = ID(interactorB)) OR interactorB is null  " +
                    " RETURN COUNT(*)+binaryCount as totalCount";

    public static final String GET_CLUSTERED_INTERACTION =
            "MATCH (:GraphInteractor { ac:{0}})<--(n:GraphClusteredInteraction)-->(:GraphInteractor{ ac:{1}}) return (n)";

    public static final String GET_FEATURES_BY_INTERACTION_AC_COUNT =
            "MATCH (binaryIEN:GraphInteractionEvidence{ ac: {0} }) --(graphParticipantEvidenceN:GraphParticipantEvidence)--(graphFeaturesN:GraphFeatureEvidence)" +
                    "RETURN COUNT(DISTINCT graphFeaturesN)";


    public static final String GET_FEATURES_BY_INTERACTION_AC =
            "MATCH (binaryIEN:GraphInteractionEvidence{ ac: {0} })--(graphParticipantEvidenceN:GraphParticipantEvidence)-[graphFeaturesR:" + RelationshipTypes.PARTICIPANT_FEATURE + "]-(graphFeaturesN:GraphFeatureEvidence)" +
                    " RETURN graphFeaturesN";

    public static final String GET_HOST_ORGANISM_BY_EXPERIMENT_AC =
            "MATCH (e:GraphExperiment{ ac: {0} })-[r:hostOrganism]->(n:GraphOrganism) RETURN n;";

    public static final String GET_INTERACTION_DETMETHOD_BY_EXPERIMENT_AC =
            "MATCH (e:GraphExperiment{ac:{0} })-[r:interactionDetectionMethod]->(n:GraphCvTerm) RETURN n;";

    public static final String CYTOSCAPE_APP_QUERY_FOR_NODES =
            " MATCH (interactor:GraphInteractor)-[identifiersFR:" + RelationshipTypes.IDENTIFIERS + "]->(identifiersFN:GraphXref) WHERE ((identifiersFN.identifier IN {identifiers}) OR {identifiers} is null)" +
                    " MATCH (interactor)-[organismR:" + RelationshipTypes.ORGANISM + "]->(organismN:GraphOrganism) WHERE ((organismN.taxId IN {species}) OR {species} is null)" +
                    " MATCH (interactor)<-[interactorsFR:" + RelationshipTypes.INTERACTORS + "]-(interaction:GraphBinaryInteractionEvidence)" +
                    " WITH COLLECT(distinct interaction) as interactionCollection" +
                    " UNWIND interactionCollection as interactionN " +

                    " MATCH (interactionN)-[interactorsR:" + RelationshipTypes.INTERACTORS + "]->(interactorsN:GraphInteractor)" +
                    " WITH COLLECT(distinct interactorsN) as interactorsCollection" +
                    " UNWIND interactorsCollection as interactorN " +
                    " MATCH (interactorN)-[organismR:" + RelationshipTypes.ORGANISM + "]->(organismN:GraphOrganism) WHERE ((organismN.taxId IN {species}) OR {species} is null)" +
                    " MATCH (interactorN)-[identifierR:" + RelationshipTypes.PREFERRED_IDENTIFIER + "]->(identifierN:GraphXref) " +
                    " WITH interactorN,identifierN,organismN" +
                    " OPTIONAL MATCH (interactorN)-[interactorTypeR:" + RelationshipTypes.INTERACTOR_TYPE + "]->(interactorTypeN:GraphCvTerm)" +
                    " WITH interactorN,identifierN,organismN,interactorTypeN" +
                    " OPTIONAL MATCH (interactorN)-[interactorXrefsR:" + RelationshipTypes.IDENTIFIERS + "]->(interactorXrefsN:GraphXref)" +
                    " WITH" +
                    "       interactorN.ac as " + CyAppJsonNodeParamNames.ID + "," +
                    "       identifierN.identifier as " + CyAppJsonNodeParamNames.PREFERRED_ID + "," +
                    "       organismN.scientificName as " + CyAppJsonNodeParamNames.SPECIES + "," +
                    "       organismN.taxId as " + CyAppJsonNodeParamNames.TAXID + "," +
                    "       (interactorN.preferredName +'('+identifierN.identifier+')') as " + CyAppJsonNodeParamNames.LABEL + "," +
                    "       interactorTypeN.shortName as " + CyAppJsonNodeParamNames.TYPE + "," +
                    "       interactorTypeN.mIIdentifier as " + CyAppJsonNodeParamNames.TYPE_MI_IDENTIFIER + "," +
                    "       interactorTypeN.mODIdentifier as " + CyAppJsonNodeParamNames.TYPE_MOD_IDENTIFIER + "," +
                    "       interactorTypeN.pARIdentifier as " + CyAppJsonNodeParamNames.TYPE_PAR_IDENTIFIER + "," +
                    "       interactorN.preferredName as " + CyAppJsonNodeParamNames.INTERACTOR_NAME + "," +
                    "       COLLECT(interactorXrefsN) as interactorXrefsNCollection" +
                    " UNWIND interactorXrefsNCollection as interactorXref" +
                    " MATCH (interactorXref)-[interactorXrefDatabaseR:database]->(interactorXrefDatabaseN:GraphCvTerm)" +
                    " WITH" +
                    "       " + CyAppJsonNodeParamNames.ID + "," +
                    "       " + CyAppJsonNodeParamNames.PREFERRED_ID + "," +
                    "       " + CyAppJsonNodeParamNames.SPECIES + "," +
                    "       " + CyAppJsonNodeParamNames.TAXID + "," +
                    "       " + CyAppJsonNodeParamNames.LABEL + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE_MI_IDENTIFIER + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE_MOD_IDENTIFIER + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE_PAR_IDENTIFIER + "," +
                    "       " + CyAppJsonNodeParamNames.INTERACTOR_NAME + "," +
                    " COLLECT({" + CyAppJsonNodeParamNames.XREF_DB_NAME + ":interactorXrefDatabaseN.shortName," + CyAppJsonNodeParamNames.XREF_MI + ":interactorXrefDatabaseN.mIIdentifier," + CyAppJsonNodeParamNames.XREF_ID + ":interactorXref.identifier}) as " + CyAppJsonNodeParamNames.XREFS + "" +

                    " RETURN " +
                    "       DISTINCT" +
                    "       " + CyAppJsonNodeParamNames.ID + "," +
                    "       " + CyAppJsonNodeParamNames.PREFERRED_ID + "," +
                    "       " + CyAppJsonNodeParamNames.SPECIES + "," +
                    "       " + CyAppJsonNodeParamNames.TAXID + "," +
                    "       " + CyAppJsonNodeParamNames.LABEL + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE_MI_IDENTIFIER + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE_MOD_IDENTIFIER + "," +
                    "       " + CyAppJsonNodeParamNames.TYPE_PAR_IDENTIFIER + "," +
                    "       " + CyAppJsonNodeParamNames.INTERACTOR_NAME + "," +
                    "       " + CyAppJsonNodeParamNames.XREFS;

    public static final String CYTOSCAPE_APP_QUERY_FOR_EDGES = "MATCH (interaction:GraphBinaryInteractionEvidence)" +
            " MATCH (interaction)-[interactorsR:" + RelationshipTypes.INTERACTORS + "]-(interactorsN:GraphInteractor)-[identifiersR:" + RelationshipTypes.IDENTIFIERS + "]-(identifiersN:GraphXref) WHERE identifiersN.identifier IN ['Q9BZD4','O14777']" +
            " OPTIONAL MATCH (interaction)-[interactorBR:" + RelationshipTypes.INTERACTOR_B + "]-(interactorBN:GraphInteractor)-[identifierBR:" + RelationshipTypes.PREFERRED_IDENTIFIER + "]-(identifierBN:GraphXref) " +
            " OPTIONAL MATCH (interaction)-[interactorAR:" + RelationshipTypes.INTERACTOR_A + "]-(interactorAN:GraphInteractor)-[identifierAR:" + RelationshipTypes.PREFERRED_IDENTIFIER + "]-(identifierAN:GraphXref) " +
            /*" OPTIONAL MATCH (interaction)-[identifiersR:identifiers]-(identifiersN:GraphXref)-[sourceR:database]-(sourceN:GraphCvTerm) WHERE sourceN.shortName IN ['reactome','signor','intact']\n" +
            " OPTIONAL MATCH (interaction)-[interactiontypeR:interactionType]-(interactiontypeN:GraphCvTerm)\n" +
            "OPTIONAL MATCH (interaction)-[experimentR:experiment]-(experimentN:GraphExperiment)-[interactionDetectionMethodR:interactionDetectionMethod]-(interactionDetectionMethodN:GraphCvTerm)\n" +
            "OPTIONAL MATCH (experimentN)-[hostOrganismR:hostOrganism]-(hostOrganismN:GraphOrganism)\n" +
            "OPTIONAL MATCH (experimentN)-[participantIdentificationMethodR:participantIdentificationMethod]-(participantIdentificationMethodN:GraphCvTerm)\n" +
            "OPTIONAL MATCH (experimentN)-[publicationR:PUB_EXP]-(publicationN:GraphPublication)-[pubmedIdXrefR:pubmedId]-(pubmedIdXrefN:GraphXref)\n" +
            "OPTIONAL MATCH (interaction)-[clusteredInteractionR:interactions]-(clusteredInteractionN:GraphClusteredInteraction)\n" +
            "OPTIONAL MATCH (interaction)-[complexExpansionR:complexExpansion]-(complexExpansionN:GraphCvTerm) \n" +*/

            " RETURN " +
            "       ID(interaction) as id, " +
            "       interactorAN.ac as source," +
            "       interactorBN.ac as target" +
            "       ";


}
