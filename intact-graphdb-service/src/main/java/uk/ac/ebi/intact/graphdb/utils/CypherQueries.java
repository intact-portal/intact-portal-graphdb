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

    /*
    * Equivalent Query String :"MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(participantEvidenceN:GraphParticipantEvidence)-[interactorR:interactor]-(interactorN:GraphInteractor)
                                 OPTIONAL MATCH (participantEvidenceN)-[expRoleR:experimentalRole]-(expRoleN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[bioRoleR:biologicalRole]-(bioRoleN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[identificationMethodR:identificationMethods]-(identificationMethodN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[experimentaPreparationR:experimentaPreparations]-(experimentaPreparationN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[parametersR:parameters]-(parametersN:GraphParameter)
                                 OPTIONAL MATCH (participantEvidenceN)-[confidencesR:confidences]-(confidencesN:GraphConfidence)
                                 OPTIONAL MATCH (participantEvidenceN)-[aliasesR:aliases]-(aliasesN:GraphAlias)
                                 OPTIONAL MATCH (participantEvidenceN)-[featuresR:PARTICIPANT_FEATURE]-(featuresN:GraphFeatureEvidence)
                                 OPTIONAL MATCH (interactorN)-[itorAliasesR:aliases]-(itorAliasesN:GraphAlias)
                                 OPTIONAL MATCH (interactorN)-[organismR:organism]-(organismN:GraphOrganism)
                                 OPTIONAL MATCH (interactorN)-[interactorTypeR:interactorType]-(interactorTypeN:GraphCvTerm)
                                 OPTIONAL MATCH (interactorN)-[preferredIdentifierR:preferredIdentifier]-(preferredIdentifierN:GraphXref)
                                 OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:database]-(preferredIdentifierDatabaseN:GraphCvTerm)
                                 RETURN  participantEvidenceN,expRoleR,expRoleN,bioRoleR,bioRoleN,interactorR,interactorN,organismR,organismN,interactorTypeR,interactorTypeN,COLLECT(identificationMethodR),
                                 COLLECT(identificationMethodN),COLLECT(experimentaPreparationR),COLLECT(experimentaPreparationN),COLLECT(parametersR),COLLECT(parametersN),COLLECT(confidencesR),COLLECT(confidencesN),
                                 COLLECT(aliasesR),COLLECT(aliasesN),COLLECT(featuresR),COLLECT(featuresN),COLLECT(itorAliasesR),COLLECT(itorAliasesN),COLLECT(preferredIdentifierR),COLLECT(preferredIdentifierN),
                                 COLLECT(preferredIdentifierDatabaseR),COLLECT(preferredIdentifierDatabaseN)";
    * */

    public static final String GET_PARTICIPANTS_BY_INTERACTION_AC =
            "MATCH (binaryIEN:GraphInteractionEvidence{ ac: {0} }) --(participantEvidenceN:GraphParticipantEvidence)" +
                    " RETURN  participantEvidenceN";

    public static final String GET_PARTICIPANTS_BY_INTERACTION_AC_COUNT = "MATCH (gie:GraphInteractionEvidence{ ac: {0} }) --(p:GraphParticipantEvidence)  RETURN COUNT(DISTINCT p)";

    public static final String GET_FEATURES_BY_INTERACTION_AC_COUNT =
            "MATCH (binaryIEN:GraphInteractionEvidence{ ac: {0} }) --(graphParticipantEvidenceN:GraphParticipantEvidence)--(graphFeaturesN:GraphFeatureEvidence)" +
                    "RETURN COUNT(DISTINCT graphFeaturesN)";


    /*
      Equivalent Query String : MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(graphParticipantEvidenceN:GraphParticipantEvidence)-[graphFeaturesR:PARTICIPANT_FEATURE]-(graphFeaturesN:GraphFeatureEvidence)
                                MATCH (graphParticipantEvidenceN)-[interactorR:interactor]-(interactorN:GraphInteractor)
                                OPTIONAL MATCH (interactorN)-[preferredIdentifierR:preferredIdentifier]-(preferredIdentifierN:GraphXref)
                                OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:database]-(preferredIdentifierDatabaseN:GraphCvTerm)
                                OPTIONAL MATCH (graphFeaturesN)-[typeR:type]-(typeN:GraphCvTerm)
                                OPTIONAL MATCH (graphFeaturesN)-[rangesR:ranges]-(rangesN:GraphRange)
                                OPTIONAL MATCH (rangesN)-[startR:start]-(startN:GraphPosition)
                                OPTIONAL MATCH (rangesN)-[endR:end]-(endN:GraphPosition)
                                OPTIONAL MATCH (endN)-[statusR:status]-(statusN:GraphCvTerm)
                                RETURN graphParticipantEvidenceN,COLLECT(graphFeaturesN),COLLECT(graphFeaturesR),COLLECT(interactorR),COLLECT(interactorN),COLLECT(typeR),COLLECT(typeN),COLLECT(rangesR),
                                       COLLECT(rangesN),COLLECT(startR),COLLECT(startN),COLLECT(endR),COLLECT(endN),COLLECT(statusR),COLLECT(statusN),COLLECT(preferredIdentifierR),COLLECT(preferredIdentifierN),
                                       COLLECT(preferredIdentifierDatabaseR),COLLECT(preferredIdentifierDatabaseN)
    * */


    public static final String GET_FEATURES_BY_INTERACTION_AC =
            "MATCH (binaryIEN:GraphInteractionEvidence{ ac: {0} })--(graphParticipantEvidenceN:GraphParticipantEvidence)-[graphFeaturesR:" + RelationshipTypes.PARTICIPANT_FEATURE + "]-(graphFeaturesN:GraphFeatureEvidence)" +
                    " RETURN graphFeaturesN";

    public static final String GET_HOST_ORGANISM_BY_EXPERIMENT_AC =
            "MATCH (e:GraphExperiment{ ac: {0} })-[r:hostOrganism]->(n:GraphOrganism) RETURN n;";

    public static final String GET_INTERACTION_DETMETHOD_BY_EXPERIMENT_AC =
            "MATCH (e:GraphExperiment{ac:{0} })-[r:interactionDetectionMethod]->(n:GraphCvTerm) RETURN n;";

}