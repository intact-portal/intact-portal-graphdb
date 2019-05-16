package uk.ac.ebi.intact.graphdb.utils;

import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    /*Common BinaryInteractionEvidences of any two interactors*/

    /*
    * Equivalent Query String : MATCH (interactorA:GraphInteractor)<-[:interactors]-(binaryIE:GraphInteractionEvidence)-[:interactors]->(interactorB:GraphInteractor)
                                WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB
                                UNWIND interactions as interaction
                                MATCH (interaction:GraphInteractionEvidence) -[experiment:experiment] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:interactionDetectionMethod]->(dm_cvterm:GraphCvTerm),
                                (interaction:GraphInteractionEvidence) - [interactionType:interactionType] -> (it_cvterm:GraphCvTerm),
                                (graphExperiment:GraphExperiment)<-[publication:PUB_EXP]-(graphpublication:GraphPublication)
                                WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB))
                                RETURN interactorA,interactorB,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments,
                                         COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms,
                                         COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications ORDER BY interactorA.ac;
    * */
    public static final String COMM_NEIGH_OF_INTOR =
            "MATCH (interactorA:GraphInteractor)<-[:" + RelationshipTypes.INTERACTORS + "]-(binaryIE:GraphInteractionEvidence)-[:" + RelationshipTypes.INTERACTORS + "]->(interactorB:GraphInteractor)" +
                    " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB" +
                    " UNWIND interactions as interaction" +
                    " MATCH (interaction:GraphInteractionEvidence) -[experiment:" + RelationshipTypes.EXPERIMENT + "] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:" + RelationshipTypes.INTERACTION_DETECTION_METHOD + "]->(dm_cvterm:GraphCvTerm)," +
                    " (interaction:GraphInteractionEvidence) - [interactionType:" + RelationshipTypes.INTERACTION_TYPE + "] -> (it_cvterm:GraphCvTerm)," +
                    " (graphExperiment:GraphExperiment)<-[publication:" + RelationshipTypes.PUB_EXP + "]-(graphpublication:GraphPublication)" +
                    " WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB))" +
                    " RETURN interactorA,interactorB,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments," +
                    "COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms," +
                    "COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications ORDER BY interactorA.ac";

    /*
    * Equivalent Query String : MATCH (interactorA:GraphInteractor)<-[:interactorA]-(binaryIE:GraphInteractionEvidence)-[:interactorB]->(interactorB:GraphInteractor)
                                WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB
                                WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB)) RETURN COUNT(*)
    * */
    public static final String INTERACTOR_PAIR_COUNT =
            "MATCH (interactorA:GraphInteractor)<-[:" + RelationshipTypes.INTERACTORS + "]-(binaryIE:GraphInteractionEvidence)-[:" + RelationshipTypes.INTERACTORS + "]->(interactorB:GraphInteractor)" +
                    " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB" +
                    " WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB))" +
                    " RETURN COUNT(*)";

    public static final String GET_CLUSTERED_INTERACTION = "MATCH (n:GraphClusteredInteraction)-->(m:GraphBinaryInteractionEvidence{ uniqueKey: {0}}) RETURN (n)";


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
            "MATCH (binaryIEN:GraphInteractionEvidence{ ac: {0} }) --(participantEvidenceN:GraphParticipantEvidence)-[interactorR:" + RelationshipTypes.INTERACTOR + "]-(interactorN:GraphInteractor)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[expRoleR:" + RelationshipTypes.EXPERIMENTAL_ROLE + "]-(expRoleN:GraphCvTerm)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[bioRoleR:" + RelationshipTypes.BIOLOGICAL_ROLE + "]-(bioRoleN:GraphCvTerm)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[identificationMethodR:" + RelationshipTypes.IDENTIFICATION_METHOD + "]-(identificationMethodN:GraphCvTerm)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[experimentaPreparationR:" + RelationshipTypes.EXPERIMENTAL_PREPARATION + "]-(experimentaPreparationN:GraphCvTerm)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[parametersR:" + RelationshipTypes.PARAMETERS + "]-(parametersN:GraphParameter)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[confidencesR:" + RelationshipTypes.CONFIDENCE + "]-(confidencesN:GraphConfidence)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[aliasesR:" + RelationshipTypes.ALIASES + "]-(aliasesN:GraphAlias)" +
                    "OPTIONAL MATCH (participantEvidenceN)-[featuresR:" + RelationshipTypes.PARTICIPANT_FEATURE + "]-(featuresN:GraphFeatureEvidence)" +
                    "OPTIONAL MATCH (interactorN)-[itorAliasesR:" + RelationshipTypes.ALIASES + "]-(itorAliasesN:GraphAlias)" +
                    "OPTIONAL MATCH (interactorN)-[organismR:" + RelationshipTypes.ORGANISM + "]-(organismN:GraphOrganism)" +
                    "OPTIONAL MATCH (interactorN)-[interactorTypeR:" + RelationshipTypes.INTERACTOR_TYPE + "]-(interactorTypeN:GraphCvTerm)" +
                    "OPTIONAL MATCH (interactorN)-[preferredIdentifierR:" + RelationshipTypes.PREFERRED_IDENTIFIER + "]-(preferredIdentifierN:GraphXref)" +
                    "OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:" + RelationshipTypes.DATABASE + "]-(preferredIdentifierDatabaseN:GraphCvTerm)" +
                    "RETURN  participantEvidenceN,expRoleR,expRoleN,bioRoleR,bioRoleN,interactorR,interactorN,organismR,organismN,interactorTypeR,interactorTypeN,COLLECT(identificationMethodR),COLLECT(identificationMethodN)," +
                    "COLLECT(experimentaPreparationR),COLLECT(experimentaPreparationN),COLLECT(parametersR),COLLECT(parametersN),COLLECT(confidencesR),COLLECT(confidencesN),COLLECT(aliasesR),COLLECT(aliasesN),COLLECT(featuresR)," +
                    "COLLECT(featuresN),COLLECT(itorAliasesR),COLLECT(itorAliasesN),COLLECT(preferredIdentifierR),COLLECT(preferredIdentifierN),COLLECT(preferredIdentifierDatabaseR),COLLECT(preferredIdentifierDatabaseN)";

    public static final String GET_PARTICIPANTS_BY_INTERACTION_AC_COUNT = "MATCH (gie:GraphInteractionEvidence{ ac: {0} }) --(p:GraphParticipantEvidence)  RETURN COUNT(DISTINCT p)";

    /*
    * Equivalent Query String : MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(experimentN:GraphExperiment)-[publicationR:PUB_EXP]-(publicationN:GraphPublication)
                                OPTIONAL MATCH (experimentN)-[interactionDetectionMethodR:interactionDetectionMethod]-(interactionDetectionMethodN:GraphCvTerm)
                                OPTIONAL MATCH (experimentN)-[hostOrganismR:hostOrganism]-(hostOrganismN:GraphOrganism)
                                OPTIONAL MATCH (experimentN)-[expXrefsR:xrefs]-(expXrefsN:GraphXref)
                                OPTIONAL MATCH (expXrefsN)-[expXrefsDatabaseR:database]-(expXrefsDatabaseN:GraphCvTerm)
                                OPTIONAL MATCH (experimentN)-[expAnnotationsR:annotations]-(expAnnotationsN:GraphAnnotation)
                                OPTIONAL MATCH (expAnnotationsN)-[expAnnotationsNTopicR:topic]-(expAnnotationsNTopicN:GraphCvTerm)
                                OPTIONAL MATCH (publicationN)-[pubXrefsR:xrefs]-(pubXrefsN:GraphXref)
                                OPTIONAL MATCH (pubXrefsN)-[pubXrefsDatabaseR:database]-(pubXrefsDatabaseN:GraphCvTerm)
                                OPTIONAL MATCH (publicationN)-[publicationAnnotationsR:annotations]-(publicationAnnotationsN:GraphAnnotation)
                                OPTIONAL MATCH (publicationAnnotationsN)-[publicationAnnotationsTopicR:topic]-(publicationAnnotationsTopicN:GraphCvTerm)
                                RETURN experimentN,binaryIEN,publicationR,publicationN,interactionDetectionMethodR,interactionDetectionMethodN,hostOrganismR,hostOrganismN,
                                       COLLECT(expXrefsR),COLLECT(expXrefsN),COLLECT(expAnnotationsR),COLLECT(expAnnotationsN),COLLECT(expXrefsDatabaseR),COLLECT(expXrefsDatabaseN),
                                       COLLECT(expAnnotationsNTopicR),COLLECT(expAnnotationsNTopicN),COLLECT(pubXrefsR),COLLECT(pubXrefsN),COLLECT(pubXrefsDatabaseR),COLLECT(pubXrefsDatabaseN),
                                       COLLECT(publicationAnnotationsR),COLLECT(publicationAnnotationsN),COLLECT(publicationAnnotationsTopicR),COLLECT(publicationAnnotationsTopicN)
    */
    public static final String GET_EXP_PUB_BY_INTERACTION_AC =
            "MATCH (binaryIEN:GraphInteractionEvidence{ ac: {0} }) --(experimentN:GraphExperiment)-[publicationR:" + RelationshipTypes.PUB_EXP + "]-(publicationN:GraphPublication) " +
                    "OPTIONAL MATCH (experimentN)-[interactionDetectionMethodR:" + RelationshipTypes.INTERACTION_DETECTION_METHOD + "]-(interactionDetectionMethodN:GraphCvTerm)" +
                    "OPTIONAL MATCH (experimentN)-[hostOrganismR:" + RelationshipTypes.HOST_ORGANISM + "]-(hostOrganismN:GraphOrganism)" +
                    "OPTIONAL MATCH (experimentN)-[expXrefsR:" + RelationshipTypes.XREFS + "]-(expXrefsN:GraphXref)" +
                    "OPTIONAL MATCH (expXrefsN)-[expXrefsDatabaseR:" + RelationshipTypes.DATABASE + "]-(expXrefsDatabaseN:GraphCvTerm)" +
                    "OPTIONAL MATCH (experimentN)-[expAnnotationsR:" + RelationshipTypes.ANNOTATIONS + "]-(expAnnotationsN:GraphAnnotation)" +
                    "OPTIONAL MATCH (expAnnotationsN)-[expAnnotationsNTopicR:" + RelationshipTypes.TOPIC + "]-(expAnnotationsNTopicN:GraphCvTerm)" +
                    "OPTIONAL MATCH (publicationN)-[pubXrefsR:" + RelationshipTypes.XREFS + "]-(pubXrefsN:GraphXref)" +
                    "OPTIONAL MATCH (pubXrefsN)-[pubXrefsDatabaseR:" + RelationshipTypes.DATABASE + "]-(pubXrefsDatabaseN:GraphCvTerm)" +
                    "OPTIONAL MATCH (publicationN)-[publicationAnnotationsR:" + RelationshipTypes.ANNOTATIONS + "]-(publicationAnnotationsN:GraphAnnotation)" +
                    "OPTIONAL MATCH (publicationAnnotationsN)-[publicationAnnotationsTopicR:" + RelationshipTypes.TOPIC + "]-(publicationAnnotationsTopicN:GraphCvTerm) " +
                    "RETURN experimentN,binaryIEN,publicationR,publicationN,interactionDetectionMethodR,interactionDetectionMethodN,hostOrganismR,hostOrganismN," +
                    "COLLECT(expXrefsR),COLLECT(expXrefsN),COLLECT(expAnnotationsR),COLLECT(expAnnotationsN),COLLECT(expXrefsDatabaseR),COLLECT(expXrefsDatabaseN)," +
                    "COLLECT(expAnnotationsNTopicR),COLLECT(expAnnotationsNTopicN),COLLECT(pubXrefsR),COLLECT(pubXrefsN),COLLECT(pubXrefsDatabaseR),COLLECT(pubXrefsDatabaseN)," +
                    "COLLECT(publicationAnnotationsR),COLLECT(publicationAnnotationsN),COLLECT(publicationAnnotationsTopicR),COLLECT(publicationAnnotationsTopicN)";

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
                    "MATCH (graphParticipantEvidenceN)-[interactorR:" + RelationshipTypes.INTERACTOR + "]-(interactorN:GraphInteractor)" +
                    "OPTIONAL MATCH (interactorN)-[preferredIdentifierR:" + RelationshipTypes.PREFERRED_IDENTIFIER + "]-(preferredIdentifierN:GraphXref)" +
                    "OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:" + RelationshipTypes.DATABASE + "]-(preferredIdentifierDatabaseN:GraphCvTerm)" +
                    "OPTIONAL MATCH (graphFeaturesN)-[typeR:" + RelationshipTypes.TYPE + "]-(typeN:GraphCvTerm)" +
                    "OPTIONAL MATCH (graphFeaturesN)-[rangesR:" + RelationshipTypes.RANGES + "]-(rangesN:GraphRange)" +
                    "OPTIONAL MATCH (rangesN)-[startR:" + RelationshipTypes.START + "]-(startN:GraphPosition)" +
                    "OPTIONAL MATCH (rangesN)-[endR:" + RelationshipTypes.END + "]-(endN:GraphPosition)" +
                    "OPTIONAL MATCH (endN)-[statusR:" + RelationshipTypes.STATUS + "]-(statusN:GraphCvTerm)" +
                    "RETURN graphParticipantEvidenceN,COLLECT(graphFeaturesN),COLLECT(graphFeaturesR),COLLECT(interactorR),COLLECT(interactorN),COLLECT(typeR),COLLECT(typeN),COLLECT(rangesR)," +
                    "COLLECT(rangesN),COLLECT(startR),COLLECT(startN),COLLECT(endR),COLLECT(endN),COLLECT(statusR),COLLECT(statusN),COLLECT(preferredIdentifierR),COLLECT(preferredIdentifierN)," +
                    "COLLECT(preferredIdentifierDatabaseR),COLLECT(preferredIdentifierDatabaseN)";

    /*
    * Equivalent Query String :
MATCH (publicationN)-[publicationR:PUB_EXP]-(experimentN:GraphExperiment)-[experimentR:experiment]-(binaryIEN:GraphBinaryInteractionEvidence{ ac: 'EBI-10048599' })
         OPTIONAL MATCH (binaryIEN)-[interactionIdentifiersR:identifiers]-(interactionIdentifiersN:GraphXref)
         OPTIONAL MATCH (interactionIdentifiersN)-[interactionIdentifiersDatabaseR:database]-(interactionIdentifiersDatabaseN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR ,COLLECT(interactionIdentifiersN) as interactionIdentifiersNCollection,
              COLLECT(interactionIdentifiersR) as interactionIdentifiersRCollection,
              COLLECT(interactionIdentifiersDatabaseR) as interactionIdentifiersDatabaseRCollection,
              COLLECT(interactionIdentifiersDatabaseN) as interactionIdentifiersDatabaseNCollection

         OPTIONAL MATCH (binaryIEN)-[interactionXrefsR:xrefs]-(interactionXrefsN:GraphXref)
         OPTIONAL MATCH (interactionXrefsN)-[interactionXrefsDatabaseR:database]-(interactionXrefsDatabaseN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection,
              interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,
              COLLECT(interactionXrefsR) as interactionXrefsRCollection,COLLECT(interactionXrefsN) as interactionXrefsNCollection,
              COLLECT(interactionXrefsDatabaseR) as interactionXrefsDatabaseRCollection,COLLECT(interactionXrefsDatabaseN)
              as interactionXrefsDatabaseNCollection

         OPTIONAL MATCH (binaryIEN)-[interactionAnnotationR:annotations]-(interactionAnnotationN:GraphAnnotation)
         OPTIONAL MATCH (interactionAnnotationN)-[interactionAnnotationTopicR:topic]-(interactionAnnotationTopicN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection,
              interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              COLLECT(interactionAnnotationR) as interactionAnnotationRCollection,COLLECT(interactionAnnotationN) as
              interactionAnnotationNCollection,COLLECT(interactionAnnotationTopicR) as interactionAnnotationTopicRCollection
              ,COLLECT(interactionAnnotationTopicN) as interactionAnnotationTopicNCollection

         OPTIONAL MATCH (binaryIEN)-[interactionConfidencesR:confidences]-(interactionConfidencesN:GraphConfidence)
         OPTIONAL MATCH (interactionConfidencesN)-[interactionConfidencesTypeR:type]-(interactionConfidencesTypeN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,COLLECT(interactionConfidencesR) as interactionConfidencesRCollection,
              COLLECT(interactionConfidencesN) as interactionConfidencesNCollection,COLLECT(interactionConfidencesTypeR) as
              interactionConfidencesTypeRCollection,COLLECT(interactionConfidencesTypeN) as interactionConfidencesTypeNCollection

         OPTIONAL MATCH (binaryIEN)-[interactionParametersR:parameters]-(interactionParametersN:GraphParameter)
         OPTIONAL MATCH (interactionParametersN)-[interactionParametersTypeR:type]-(interactionParametersTypeN:GraphCvTerm)
         OPTIONAL MATCH (interactionParametersN)-[interactionParametersUnitR:unit]-(interactionParametersUnitN:GraphCvTerm)
         OPTIONAL MATCH (interactionParametersN)-[interactionParametersValueR:value]-(interactionParametersValueN:GraphParameterValue)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
              interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,COLLECT(interactionParametersR) as
              interactionParametersRCollection ,COLLECT(interactionParametersN) as interactionParametersNCollection,
              COLLECT(interactionParametersTypeR) as interactionParametersTypeRCollection,COLLECT(interactionParametersTypeN)
              as interactionParametersTypeNCollection,COLLECT(interactionParametersUnitR) as interactionParametersUnitRCollection,
              COLLECT(interactionParametersUnitN) as interactionParametersUnitNCollection,COLLECT(interactionParametersValueR)
              as interactionParametersValueRCollection,COLLECT(interactionParametersValueN) as interactionParametersValueNCollection

         OPTIONAL MATCH (binaryIEN)-[interactionTypeR:interactionType]-(interactionTypeN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
              interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ,
              interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
              interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
              interactionParametersValueNCollection,interactionTypeR,interactionTypeN

         OPTIONAL MATCH (experimentN)-[interactionDetectionMethodR:interactionDetectionMethod]-(interactionDetectionMethodN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
              interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ,
              interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
              interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
              interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR

         OPTIONAL MATCH (experimentN)-[hostOrganismR:hostOrganism]-(hostOrganismN:GraphOrganism)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,
              interactionXrefsRCollection,interactionXrefsNCollection,interactionXrefsDatabaseRCollection,
              interactionXrefsDatabaseNCollection,interactionAnnotationRCollection,interactionAnnotationNCollection,
              interactionAnnotationTopicRCollection,interactionAnnotationTopicNCollection,interactionConfidencesRCollection,
              interactionConfidencesNCollection,interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,
              interactionParametersRCollection ,interactionParametersNCollection,interactionParametersTypeRCollection,
              interactionParametersTypeNCollection, interactionParametersUnitRCollection,interactionParametersUnitNCollection,
              interactionParametersValueRCollection,interactionParametersValueNCollection,interactionTypeR,interactionTypeN,
              interactionDetectionMethodN,interactionDetectionMethodR,hostOrganismR,hostOrganismN

         OPTIONAL MATCH (experimentN)-[expXrefsR:xrefs]-(expXrefsN:GraphXref)
         OPTIONAL MATCH (expXrefsN)-[expXrefsDatabaseR:database]-(expXrefsDatabaseN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
              interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ,
              interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
              interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
              interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,
              interactionDetectionMethodR,hostOrganismR,hostOrganismN,COLLECT(expXrefsR) as expXrefsRCollection,
              COLLECT(expXrefsN) as expXrefsNCollection,COLLECT(expXrefsDatabaseR) as expXrefsDatabaseRCollection,
              COLLECT(expXrefsDatabaseN) as expXrefsDatabaseNCollection

         OPTIONAL MATCH (experimentN)-[experimentAnnotationR:annotations]-(experimentAnnotationN:GraphAnnotation)
         OPTIONAL MATCH (experimentAnnotationN)-[experimentAnnotationTopicR:topic]-(experimentAnnotationTopicN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
              interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ,
              interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
              interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
              interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,
              interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,
              expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,
              COLLECT(experimentAnnotationR) as experimentAnnotationRCollection,COLLECT(experimentAnnotationN) as
              experimentAnnotationNCollection,COLLECT(experimentAnnotationTopicR) as experimentAnnotationTopicRCollection,
              COLLECT(experimentAnnotationTopicN) as experimentAnnotationTopicNCollection


         OPTIONAL MATCH (publicationN)-[pubIdentifiersR:identifiers]-(pubIdentifiersN:GraphXref)
         OPTIONAL MATCH (pubIdentifiersN)-[pubIdentifiersDatabaseR:database]-(pubIdentifiersDatabaseN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,
              interactionXrefsRCollection,interactionXrefsNCollection,interactionXrefsDatabaseRCollection,
              interactionXrefsDatabaseNCollection,interactionAnnotationRCollection,interactionAnnotationNCollection,
              interactionAnnotationTopicRCollection,interactionAnnotationTopicNCollection,interactionConfidencesRCollection,
              interactionConfidencesNCollection,interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,
              interactionParametersRCollection ,interactionParametersNCollection,interactionParametersTypeRCollection,
              interactionParametersTypeNCollection, interactionParametersUnitRCollection,interactionParametersUnitNCollection,
              interactionParametersValueRCollection,interactionParametersValueNCollection,interactionTypeR,interactionTypeN,
              interactionDetectionMethodN,interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection,
              expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,experimentAnnotationRCollection,
              experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,
              COLLECT(pubIdentifiersR) as pubIdentifiersRCollection,COLLECT(pubIdentifiersN) as pubIdentifiersNCollection,
              COLLECT(pubIdentifiersDatabaseR) as pubIdentifiersDatabaseRCollection,COLLECT(pubIdentifiersDatabaseN)
              as pubIdentifiersDatabaseNCollection

         OPTIONAL MATCH (publicationN)-[pubXrefsR:xrefs]-(pubXrefsN:GraphXref)
         OPTIONAL MATCH (pubXrefsN)-[pubXrefsDatabaseR:database]-(pubXrefsDatabaseN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection
              ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
              interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection
              ,interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
              interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
              interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR,
              hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,
              pubIdentifiersRCollection,pubIdentifiersNCollection,experimentAnnotationRCollection,
              experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,
              pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection,COLLECT(pubXrefsR) as pubXrefsRCollection,
              COLLECT(pubXrefsN) as pubXrefsNCollection,COLLECT(pubXrefsDatabaseR) as pubXrefsDatabaseRCollection,
              COLLECT(pubXrefsDatabaseN) as pubXrefsDatabaseNCollection

         OPTIONAL MATCH (publicationN)-[pubImexIdR:imexId]-(pubImexIdN:GraphXref)
         OPTIONAL MATCH (pubImexIdN)-[pubImexIdDatabaseR:database]-(pubImexIdDatabaseN:GraphCvTerm)

         WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN,interactionIdentifiersNCollection,interactionIdentifiersRCollection,
              interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
              interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
              interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
              interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
              interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection,
              interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
              interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
              interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR,
              hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,
              pubIdentifiersRCollection,pubIdentifiersNCollection,experimentAnnotationRCollection,
              experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,
              pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection,pubXrefsRCollection,pubXrefsNCollection,
              pubXrefsDatabaseRCollection,pubXrefsDatabaseNCollection,pubImexIdDatabaseR,pubImexIdDatabaseN

          OPTIONAL MATCH (publicationN)-[pubSourceR:source]-(pubSourceN:GraphSource)

          WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN,
               interactionIdentifiersNCollection,interactionIdentifiersRCollection
               ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
               interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
               interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
               interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
               interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection,
               interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
               interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
               interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,
               interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,
               expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,experimentAnnotationRCollection,
               experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,
               pubIdentifiersRCollection,pubIdentifiersNCollection,
               pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection,pubXrefsRCollection,pubXrefsNCollection,
               pubXrefsDatabaseRCollection,pubXrefsDatabaseNCollection,pubImexIdDatabaseR,pubImexIdDatabaseN,pubSourceR,pubSourceN

         OPTIONAL MATCH (binaryIEN)-[participantEvidenceR:IE_PARTICIPANT]-(participantEvidenceN:GraphParticipantEvidence)
         OPTIONAL MATCH (participantEvidenceN)-[expRoleR:experimentalRole]-(expRoleN:GraphCvTerm)
         OPTIONAL MATCH (participantEvidenceN)-[bioRoleR:biologicalRole]-(bioRoleN:GraphCvTerm)
         OPTIONAL MATCH (participantEvidenceN)-[identificationMethodR:identificationMethods]-(identificationMethodN:GraphCvTerm)
         OPTIONAL MATCH (participantEvidenceN)-[featuresR:PARTICIPANT_FEATURE]-(featuresN:GraphFeatureEvidence)
         OPTIONAL MATCH (featuresN)-[featuresTypeR:type]-(featuresTypeN:GraphCvTerm)
		 OPTIONAL MATCH (featuresN)-[featuresRoleR:role]-(featuresRoleN:GraphCvTerm)
		 OPTIONAL MATCH (featuresN)-[featureLinkedFeaturesR:linkedFeatures]-(featureLinkedFeaturesN:GraphFeatureEvidence)
		 OPTIONAL MATCH (featureLinkedFeaturesN)-[featureLinkedFeaturesLFR:linkedFeatures]-(featureLinkedFeaturesLFN:GraphFeatureEvidence)
         OPTIONAL MATCH (featuresN)-[featuresRangeR:ranges]-(featuresRangeN:GraphRange)
	     OPTIONAL MATCH (featuresN)-[featuresParticipantR:PARTICIPANT_FEATURE]-(featuresParticipantN:GraphExperimentalEntity)
	     OPTIONAL MATCH (featuresParticipantN)-[featuresParticipantInteractorR:interactor]-(featuresParticipantInteractorN:GraphInteractor)
         OPTIONAL MATCH (featuresRangeN)-[featuresRangeStartPositionR:start]-(featuresRangeStartPositionN:GraphPosition)
         OPTIONAL MATCH (featuresRangeN)-[featuresRangeEndPositionR:end]-(featuresRangeEndPositionN:GraphPosition)
         OPTIONAL MATCH (featuresRangeN)-[featuresRangeParticipantR:participant]-(featuresRangeParticipantN:GraphEntity)
	     OPTIONAL MATCH (featuresRangeParticipantN)-[featuresRangeParticipantInteractorR:interactor]-(featuresRangeParticipantInteractorN:GraphInteractor)
         OPTIONAL MATCH (featuresRangeStartPositionN)-[featuresRangeStartPositionStatusR:status]-(featuresRangeStartPositionStatusN:GraphCvTerm)
         OPTIONAL MATCH (featuresRangeEndPositionN)-[featuresRangeEndPositionStatusR:status]-(featuresRangeEndPositionStatusN:GraphCvTerm)
         OPTIONAL MATCH (participantEvidenceN)-[stoichiometryR:stoichiometry]-(stoichiometryN:GraphStoichiometry)
         OPTIONAL MATCH (participantEvidenceN)-[participantExpressedInR:expressedIn]-(participantExpressedInN:GraphOrganism)
         OPTIONAL MATCH (participantExpressedInN)-[participantExpressedInCellTypeR:cellType]-(participantExpressedInCellTypeN:GraphCvTerm)
         OPTIONAL MATCH (participantExpressedInCellTypeN)-[participantExpressedInCellTypeIdentifiersR:identifiers]-(participantExpressedInCellTypeIdentifiersN:GraphXref)
         OPTIONAL MATCH (participantExpressedInCellTypeIdentifiersN)-[participantExpressedInCellTypeIdentifiersDatabaseR:database]
                        -(participantExpressedInCellTypeIdentifiersDatabaseN:GraphCvTerm)
		 OPTIONAL MATCH (participantEvidenceN)-[participantParametersR:parameters]-(participantParametersN:GraphParameter)
		 OPTIONAL MATCH (participantParametersN)-[participantParametersTypeR:type]-(participantParametersTypeN:GraphCvTerm)
		 OPTIONAL MATCH (participantParametersN)-[participantParametersUnitR:unit]-(participantParametersUnitN:GraphCvTerm)
		 OPTIONAL MATCH (participantParametersN)-[participantParametersValueR:value]-(participantParametersValueN:GraphParameterValue)
	     OPTIONAL MATCH (participantEvidenceN)-[participantConfidencesR:confidences]-(participantConfidencesN:GraphConfidence)
		 OPTIONAL MATCH (participantConfidencesN)-[participantConfidencesTypeR:type]-(participantConfidencesTypeN:GraphCvTerm)
         OPTIONAL MATCH (participantEvidenceN)-[interactorR:interactor]-(interactorN:GraphInteractor)
		 OPTIONAL MATCH (interactorN)-[organismR:organism]-(organismN:GraphOrganism)
         OPTIONAL MATCH (interactorN)-[interactorTypeR:interactorType]-(interactorTypeN:GraphCvTerm)
         OPTIONAL MATCH (interactorN)-[preferredIdentifierR:preferredIdentifier]-(preferredIdentifierN:GraphXref)
         OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:database]-(preferredIdentifierDatabaseN:GraphCvTerm)


          WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN,
               interactionIdentifiersNCollection,interactionIdentifiersRCollection
               ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,
               interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection,
               interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection,
               interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection,
               interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection,
               interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection,
               interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection,
               interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR,
               hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,
               experimentAnnotationRCollection,experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,
               pubIdentifiersRCollection,pubIdentifiersNCollection,pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection,
               pubXrefsRCollection,pubXrefsNCollection,pubXrefsDatabaseRCollection,pubXrefsDatabaseNCollection,pubImexIdDatabaseR,
               pubImexIdDatabaseN,pubSourceR,pubSourceN,COLLECT(expRoleR) as expRoleRCollection,COLLECT(expRoleN) as expRoleNCollection,
               COLLECT(bioRoleR) as bioRoleRCollection,COLLECT(bioRoleN) as bioRoleNCollection,
               COLLECT(identificationMethodR) as identificationMethodRCollection,COLLECT(identificationMethodN) as identificationMethodNCollection,
               COLLECT(featuresR) as featuresRCollection,COLLECT(featuresN) as featuresNCollection,
               COLLECT(featuresParticipantR) as featuresParticipantRCollection,
               COLLECT(featuresParticipantN) as featuresParticipantNCollection,COLLECT(featuresParticipantInteractorR) as
               featuresParticipantInteractorRCollection,COLLECT(featuresParticipantInteractorN) as featuresParticipantInteractorNCollection,
               COLLECT(featuresTypeR) as featuresTypeRCollection,COLLECT(featuresTypeN) as featuresTypeNCollection,
               COLLECT(featuresRangeR) as featuresRangeRCollection,COLLECT(featuresRangeN) as featuresRangeNCollection,
               COLLECT(featuresRangeParticipantR) as featuresRangeParticipantRCollection,COLLECT(featuresRangeParticipantN) as
               featuresRangeParticipantNCollection,COLLECT(featuresRangeParticipantInteractorR) as featuresRangeParticipantInteractorRCollection,
               COLLECT(featuresRangeParticipantInteractorN) as featuresRangeParticipantInteractorNCollection,COLLECT(featuresRangeStartPositionR)
               as featuresRangeStartPositionRCollection,COLLECT(featuresRangeStartPositionN) as featuresRangeStartPositionNCollection,
               COLLECT(featuresRangeEndPositionR) as featuresRangeEndPositionRCollection,COLLECT(featuresRangeEndPositionN) as
               featuresRangeEndPositionNCollection,COLLECT(featuresRangeStartPositionStatusR) as featuresRangeStartPositionStatusRCollection,
               COLLECT(featuresRangeStartPositionStatusN) as featuresRangeStartPositionStatusNCollection,COLLECT(featuresRangeEndPositionStatusR) as
               featuresRangeEndPositionStatusRCollection,COLLECT(featuresRangeEndPositionStatusN) as featuresRangeEndPositionStatusNCollection,
               COLLECT(stoichiometryR) as stoichiometryRCollection,COLLECT(stoichiometryN) as stoichiometryNCollection,
               COLLECT(organismR) as organismRCollection,COLLECT(organismN) as organismNCollection,COLLECT(interactorTypeR)
               as interactorTypeRCollection,COLLECT(interactorTypeN) as interactorTypeNCollection,COLLECT(preferredIdentifierR) as
               preferredIdentifierRCollection,COLLECT(preferredIdentifierN) as preferredIdentifierNCollection,
               COLLECT(preferredIdentifierDatabaseR) as preferredIdentifierDatabaseRCollection,
               COLLECT(preferredIdentifierDatabaseN) as preferredIdentifierDatabaseNCollection,
               COLLECT(participantEvidenceR) as participantEvidenceRCollection,
               COLLECT(participantEvidenceN) as participantEvidenceNCollection,
               COLLECT(interactorR) as interactorRCollection ,COLLECT(interactorN) as interactorNCollection,
               COLLECT(featuresRoleR) as featuresRoleRCollection,COLLECT(featuresRoleN) as featuresRoleNCollection,
               COLLECT(featureLinkedFeaturesR) as featureLinkedFeaturesRCollection,COLLECT(featureLinkedFeaturesN) as
               featureLinkedFeaturesNCollection,COLLECT(participantExpressedInR) as participantExpressedInRCollection,
               COLLECT(participantExpressedInN) as participantExpressedInNCollection,COLLECT(participantParametersR) as
               participantParametersRCollection,COLLECT(participantParametersN) as participantParametersNCollection,
               COLLECT(participantParametersTypeR) as participantParametersTypeRCollection,COLLECT(participantParametersTypeN)
               as participantParametersTypeNCollection,COLLECT(participantParametersUnitR) as participantParametersUnitRCollection,
               COLLECT(participantParametersUnitN) as participantParametersUnitNCollection,COLLECT(participantParametersValueR)
               as participantParametersValueRCollection,COLLECT(participantParametersValueN) as participantParametersValueNCollection,
               COLLECT(participantConfidencesR) as participantConfidencesRCollection,COLLECT(participantConfidencesN) as
               participantConfidencesNCollection,COLLECT(participantConfidencesTypeR) as participantConfidencesTypeRCollection,
               COLLECT(participantConfidencesTypeN) as participantConfidencesTypeNCollection,COLLECT(participantExpressedInCellTypeR)
               as participantExpressedInCellTypeRCollection,COLLECT(participantExpressedInCellTypeN) as participantExpressedInCellTypeNCollection,
               COLLECT(participantExpressedInCellTypeIdentifiersR) as participantExpressedInCellTypeIdentifiersRCollection,
               COLLECT(participantExpressedInCellTypeIdentifiersN) as participantExpressedInCellTypeIdentifiersNCollection,
               COLLECT(participantExpressedInCellTypeIdentifiersDatabaseR) as participantExpressedInCellTypeIdentifiersDatabaseRCollection,
               COLLECT(participantExpressedInCellTypeIdentifiersDatabaseN) as participantExpressedInCellTypeIdentifiersDatabaseNCollection,
               COLLECT(featureLinkedFeaturesLFR) as featureLinkedFeaturesLFR,COLLECT(featureLinkedFeaturesLFN) as featureLinkedFeaturesLFN




          RETURN publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN,interactionIdentifiersNCollection,interactionIdentifiersRCollection,interactionIdentifiersDatabaseRCollection,
                 interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,interactionXrefsNCollection,interactionXrefsDatabaseRCollection,
                 interactionXrefsDatabaseNCollection,interactionAnnotationRCollection,interactionAnnotationNCollection,
                 interactionAnnotationTopicRCollection,interactionAnnotationTopicNCollection,interactionConfidencesRCollection,
                 interactionConfidencesNCollection,interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,
                 interactionParametersRCollection ,interactionParametersNCollection,interactionParametersTypeRCollection,
                 interactionParametersTypeNCollection, interactionParametersUnitRCollection,interactionParametersUnitNCollection,
                 interactionParametersValueRCollection,interactionParametersValueNCollection,interactionTypeR,interactionTypeN,
                 interactionDetectionMethodN,interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,
                 expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,experimentAnnotationRCollection,experimentAnnotationNCollection,
                 experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,pubIdentifiersRCollection,
                 pubIdentifiersNCollection,pubIdentifiersDatabaseRCollection,
                 pubIdentifiersDatabaseNCollection,pubXrefsRCollection,pubXrefsNCollection,pubXrefsDatabaseRCollection,
                 pubXrefsDatabaseNCollection,pubImexIdDatabaseR,pubImexIdDatabaseN,pubSourceR,pubSourceN,expRoleRCollection,
                 expRoleNCollection,bioRoleRCollection,bioRoleNCollection,identificationMethodRCollection,identificationMethodNCollection,
                 featuresRCollection ,featuresNCollection,featuresParticipantRCollection,featuresParticipantNCollection,
                 featuresParticipantInteractorRCollection,featuresParticipantInteractorNCollection,featuresTypeRCollection,featuresTypeNCollection,featuresRangeRCollection,
                 featuresRangeNCollection,featuresRangeParticipantRCollection,featuresRangeParticipantNCollection,
                 featuresRangeParticipantInteractorRCollection,featuresRangeParticipantInteractorNCollection,
                 featuresRangeStartPositionRCollection,featuresRangeStartPositionNCollection,featuresRangeEndPositionRCollection,
                 featuresRangeEndPositionNCollection,featuresRangeStartPositionStatusRCollection,featuresRangeStartPositionStatusNCollection,
                 featuresRangeEndPositionStatusRCollection,featuresRangeEndPositionStatusNCollection, stoichiometryRCollection,
                 stoichiometryNCollection,organismRCollection,organismNCollection,interactorTypeRCollection,interactorTypeNCollection,
                 preferredIdentifierRCollection,preferredIdentifierNCollection, preferredIdentifierDatabaseRCollection,
                 preferredIdentifierDatabaseNCollection,participantEvidenceRCollection,participantEvidenceNCollection,
                 interactorRCollection,interactorNCollection,featuresRoleRCollection,featuresRoleNCollection,featureLinkedFeaturesRCollection,
                 featureLinkedFeaturesNCollection,participantExpressedInRCollection,participantExpressedInNCollection,
                 participantParametersRCollection,participantParametersNCollection,participantParametersTypeRCollection,
                 participantParametersTypeNCollection,participantParametersUnitRCollection,participantParametersUnitNCollection,
                 participantParametersValueRCollection,participantParametersValueNCollection,participantConfidencesRCollection,
                 participantConfidencesNCollection,participantConfidencesTypeRCollection,participantConfidencesTypeNCollection,
                 participantExpressedInCellTypeRCollection,participantExpressedInCellTypeNCollection,participantExpressedInCellTypeIdentifiersRCollection,
                 participantExpressedInCellTypeIdentifiersNCollection,participantExpressedInCellTypeIdentifiersDatabaseRCollection,
                 participantExpressedInCellTypeIdentifiersDatabaseNCollection,featureLinkedFeaturesLFR,featureLinkedFeaturesLFN,binaryIEN limit 1



    **/
    public static final String GET_INTERACTION_DETAILS_FOR_MIJSON =
            "MATCH (publicationN)-[publicationR:" + RelationshipTypes.PUB_EXP + "]-(experimentN:GraphExperiment)-[experimentR:" + RelationshipTypes.EXPERIMENT + "]-" +
                    " (binaryIEN:GraphInteractionEvidence{ ac: {0} })" +
                    " OPTIONAL MATCH (binaryIEN)-[interactionIdentifiersR:" + RelationshipTypes.IDENTIFIERS + "]-(interactionIdentifiersN:GraphXref)" +
                    " OPTIONAL MATCH (interactionIdentifiersN)-[interactionIdentifiersDatabaseR:" + RelationshipTypes.DATABASE + "]-(interactionIdentifiersDatabaseN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " COLLECT(interactionIdentifiersN) as interactionIdentifiersNCollection," +
                    " COLLECT(interactionIdentifiersR) as interactionIdentifiersRCollection," +
                    " COLLECT(interactionIdentifiersDatabaseR) as interactionIdentifiersDatabaseRCollection," +
                    " COLLECT(interactionIdentifiersDatabaseN) as interactionIdentifiersDatabaseNCollection" +

                    " OPTIONAL MATCH (binaryIEN)-[interactionXrefsR:" + RelationshipTypes.XREFS + "]-(interactionXrefsN:GraphXref)" +
                    " OPTIONAL MATCH (interactionXrefsN)-[interactionXrefsDatabaseR:" + RelationshipTypes.DATABASE + "]-(interactionXrefsDatabaseN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection," +
                    " interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection," +
                    " COLLECT(interactionXrefsR) as interactionXrefsRCollection,COLLECT(interactionXrefsN) as interactionXrefsNCollection," +
                    " COLLECT(interactionXrefsDatabaseR) as interactionXrefsDatabaseRCollection,COLLECT(interactionXrefsDatabaseN)" +
                    " as interactionXrefsDatabaseNCollection" +

                    " OPTIONAL MATCH (binaryIEN)-[interactionAnnotationR:" + RelationshipTypes.ANNOTATIONS + "]-(interactionAnnotationN:GraphAnnotation)" +
                    " OPTIONAL MATCH (interactionAnnotationN)-[interactionAnnotationTopicR:" + RelationshipTypes.TOPIC + "]-(interactionAnnotationTopicN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection," +
                    " interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " COLLECT(interactionAnnotationR) as interactionAnnotationRCollection,COLLECT(interactionAnnotationN) as" +
                    " interactionAnnotationNCollection,COLLECT(interactionAnnotationTopicR) as interactionAnnotationTopicRCollection" +
                    " ,COLLECT(interactionAnnotationTopicN) as interactionAnnotationTopicNCollection" +

                    " OPTIONAL MATCH (binaryIEN)-[interactionConfidencesR:" + RelationshipTypes.CONFIDENCE + "]-(interactionConfidencesN:GraphConfidence)" +
                    " OPTIONAL MATCH (interactionConfidencesN)-[interactionConfidencesTypeR:" + RelationshipTypes.TYPE + "]-(interactionConfidencesTypeN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,COLLECT(interactionConfidencesR) as interactionConfidencesRCollection," +
                    " COLLECT(interactionConfidencesN) as interactionConfidencesNCollection,COLLECT(interactionConfidencesTypeR) as" +
                    " interactionConfidencesTypeRCollection,COLLECT(interactionConfidencesTypeN) as interactionConfidencesTypeNCollection" +

                    " OPTIONAL MATCH (binaryIEN)-[interactionParametersR:" + RelationshipTypes.PARAMETERS + "]-(interactionParametersN:GraphParameter)" +
                    " OPTIONAL MATCH (interactionParametersN)-[interactionParametersTypeR:" + RelationshipTypes.TYPE + "]-(interactionParametersTypeN:GraphCvTerm)" +
                    " OPTIONAL MATCH (interactionParametersN)-[interactionParametersUnitR:" + RelationshipTypes.UNIT + "]-(interactionParametersUnitN:GraphCvTerm)" +
                    " OPTIONAL MATCH (interactionParametersN)-[interactionParametersValueR:" + RelationshipTypes.VALUE + "]-(interactionParametersValueN:GraphParameterValue)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,COLLECT(interactionParametersR) as" +
                    " interactionParametersRCollection ,COLLECT(interactionParametersN) as interactionParametersNCollection," +
                    " COLLECT(interactionParametersTypeR) as interactionParametersTypeRCollection,COLLECT(interactionParametersTypeN)" +
                    " as interactionParametersTypeNCollection,COLLECT(interactionParametersUnitR) as interactionParametersUnitRCollection," +
                    " COLLECT(interactionParametersUnitN) as interactionParametersUnitNCollection,COLLECT(interactionParametersValueR)" +
                    " as interactionParametersValueRCollection,COLLECT(interactionParametersValueN) as interactionParametersValueNCollection" +

                    " OPTIONAL MATCH (binaryIEN)-[interactionTypeR:" + RelationshipTypes.INTERACTION_TYPE + "]-(interactionTypeN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ," +
                    " interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN" +

                    " OPTIONAL MATCH (experimentN)-[interactionDetectionMethodR:" + RelationshipTypes.INTERACTION_DETECTION_METHOD + "]-(interactionDetectionMethodN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ," +
                    " interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR" +

                    " OPTIONAL MATCH (experimentN)-[hostOrganismR:" + RelationshipTypes.HOST_ORGANISM + "]-(hostOrganismN:GraphOrganism)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection," +
                    " interactionXrefsRCollection,interactionXrefsNCollection,interactionXrefsDatabaseRCollection," +
                    " interactionXrefsDatabaseNCollection,interactionAnnotationRCollection,interactionAnnotationNCollection," +
                    " interactionAnnotationTopicRCollection,interactionAnnotationTopicNCollection,interactionConfidencesRCollection," +
                    " interactionConfidencesNCollection,interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection," +
                    " interactionParametersRCollection ,interactionParametersNCollection,interactionParametersTypeRCollection," +
                    " interactionParametersTypeNCollection, interactionParametersUnitRCollection,interactionParametersUnitNCollection," +
                    " interactionParametersValueRCollection,interactionParametersValueNCollection,interactionTypeR,interactionTypeN," +
                    " interactionDetectionMethodN,interactionDetectionMethodR,hostOrganismR,hostOrganismN" +

                    " OPTIONAL MATCH (experimentN)-[expXrefsR:" + RelationshipTypes.XREFS + "]-(expXrefsN:GraphXref)" +
                    " OPTIONAL MATCH (expXrefsN)-[expXrefsDatabaseR:" + RelationshipTypes.DATABASE + "]-(expXrefsDatabaseN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ," +
                    " interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN," +
                    " interactionDetectionMethodR,hostOrganismR,hostOrganismN,COLLECT(expXrefsR) as expXrefsRCollection," +
                    " COLLECT(expXrefsN) as expXrefsNCollection,COLLECT(expXrefsDatabaseR) as expXrefsDatabaseRCollection," +
                    " COLLECT(expXrefsDatabaseN) as expXrefsDatabaseNCollection" +

                    " OPTIONAL MATCH (experimentN)-[experimentAnnotationR:annotations]-(experimentAnnotationN:GraphAnnotation)" +
                    " OPTIONAL MATCH (experimentAnnotationN)-[experimentAnnotationTopicR:topic]-(experimentAnnotationTopicN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,interactionIdentifiersNCollection,interactionIdentifiersRCollection," +
                    " interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection ," +
                    " interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN," +
                    " interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection," +
                    " expXrefsDatabaseRCollection,expXrefsDatabaseNCollection," +
                    " COLLECT(experimentAnnotationR) as experimentAnnotationRCollection,COLLECT(experimentAnnotationN) as" +
                    " experimentAnnotationNCollection,COLLECT(experimentAnnotationTopicR) as experimentAnnotationTopicRCollection," +
                    " COLLECT(experimentAnnotationTopicN) as experimentAnnotationTopicNCollection" +

                    " OPTIONAL MATCH (publicationN)-[pubIdentifiersR:" + RelationshipTypes.IDENTIFIERS + "]-(pubIdentifiersN:GraphXref)" +
                    " OPTIONAL MATCH (pubIdentifiersN)-[pubIdentifiersDatabaseR:" + RelationshipTypes.DATABASE + "]-(pubIdentifiersDatabaseN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection," +
                    " interactionXrefsRCollection,interactionXrefsNCollection,interactionXrefsDatabaseRCollection," +
                    " interactionXrefsDatabaseNCollection,interactionAnnotationRCollection,interactionAnnotationNCollection," +
                    " interactionAnnotationTopicRCollection,interactionAnnotationTopicNCollection,interactionConfidencesRCollection," +
                    " interactionConfidencesNCollection,interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection," +
                    " interactionParametersRCollection ,interactionParametersNCollection,interactionParametersTypeRCollection," +
                    " interactionParametersTypeNCollection, interactionParametersUnitRCollection,interactionParametersUnitNCollection," +
                    " interactionParametersValueRCollection,interactionParametersValueNCollection,interactionTypeR,interactionTypeN," +
                    " interactionDetectionMethodN,interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection," +
                    " expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,experimentAnnotationRCollection," +
                    " experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection," +
                    " COLLECT(pubIdentifiersR) as pubIdentifiersRCollection,COLLECT(pubIdentifiersN) as pubIdentifiersNCollection," +
                    " COLLECT(pubIdentifiersDatabaseR) as pubIdentifiersDatabaseRCollection,COLLECT(pubIdentifiersDatabaseN)" +
                    " as pubIdentifiersDatabaseNCollection" +

                    " OPTIONAL MATCH (publicationN)-[pubXrefsR:" + RelationshipTypes.XREFS + "]-(pubXrefsN:GraphXref)" +
                    " OPTIONAL MATCH (pubXrefsN)-[pubXrefsDatabaseR:" + RelationshipTypes.DATABASE + "]-(pubXrefsDatabaseN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection" +
                    " ,interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR," +
                    " hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection," +
                    " experimentAnnotationRCollection,experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection," +
                    " pubIdentifiersRCollection,pubIdentifiersNCollection,pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection," +
                    " COLLECT(pubXrefsR) as pubXrefsRCollection,COLLECT(pubXrefsN) as pubXrefsNCollection,COLLECT(pubXrefsDatabaseR) as" +
                    " pubXrefsDatabaseRCollection,COLLECT(pubXrefsDatabaseN) as pubXrefsDatabaseNCollection" +

                    " OPTIONAL MATCH (publicationN)-[pubImexIdR:" + RelationshipTypes.IMEX_ID + "]-(pubImexIdN:GraphXref)" +
                    " OPTIONAL MATCH (pubImexIdN)-[pubImexIdDatabaseR:" + RelationshipTypes.DATABASE + "]-(pubImexIdDatabaseN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection," +
                    " interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection," +
                    " interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR," +
                    " hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection," +
                    " experimentAnnotationRCollection,experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection," +
                    " pubIdentifiersRCollection,pubIdentifiersNCollection,pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection," +
                    " pubXrefsRCollection,pubXrefsNCollection,pubXrefsDatabaseRCollection,pubXrefsDatabaseNCollection,pubImexIdDatabaseR,pubImexIdDatabaseN" +

                    " OPTIONAL MATCH (publicationN)-[pubSourceR:" + RelationshipTypes.SOURCE + "]-(pubSourceN:GraphSource)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection" +
                    " ,interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection," +
                    " interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN," +
                    " interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection," +
                    " expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,experimentAnnotationRCollection,experimentAnnotationNCollection," +
                    " experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,pubIdentifiersRCollection,pubIdentifiersNCollection," +
                    " pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection,pubXrefsRCollection,pubXrefsNCollection," +
                    " pubXrefsDatabaseRCollection,pubXrefsDatabaseNCollection,pubImexIdDatabaseR,pubImexIdDatabaseN,pubSourceR,pubSourceN " +

                    " OPTIONAL MATCH (binaryIEN)-[participantEvidenceR:" + RelationshipTypes.IE_PARTICIPANT + "]-(participantEvidenceN:GraphParticipantEvidence)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[expRoleR:" + RelationshipTypes.EXPERIMENTAL_ROLE + "]-(expRoleN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[bioRoleR:" + RelationshipTypes.BIOLOGICAL_ROLE + "]-(bioRoleN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[identificationMethodR:" + RelationshipTypes.IDENTIFICATION_METHOD + "]-(identificationMethodN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[featuresR:" + RelationshipTypes.PARTICIPANT_FEATURE + "]-(featuresN:GraphFeatureEvidence)" +
                    " OPTIONAL MATCH (featuresN)-[featuresTypeR:" + RelationshipTypes.TYPE + "]-(featuresTypeN:GraphCvTerm)" +
                    " OPTIONAL MATCH (featuresN)-[featuresRoleR:" + RelationshipTypes.ROLE + "]-(featuresRoleN:GraphCvTerm)" +
                    " OPTIONAL MATCH (featuresN)-[featureLinkedFeaturesR:" + RelationshipTypes.LINKED_FEATURES + "]-(featureLinkedFeaturesN:GraphFeatureEvidence)" +
                    " OPTIONAL MATCH (featureLinkedFeaturesN)-[featureLinkedFeaturesLFR:" + RelationshipTypes.LINKED_FEATURES + "]-(featureLinkedFeaturesLFN:GraphFeatureEvidence)" +
                    " OPTIONAL MATCH (featuresN)-[featuresRangeR:" + RelationshipTypes.RANGES + "]-(featuresRangeN:GraphRange)" +
                    " OPTIONAL MATCH (featuresN)-[featuresParticipantR:" + RelationshipTypes.PARTICIPANT_FEATURE + "]-(featuresParticipantN:GraphExperimentalEntity)" +
                    " OPTIONAL MATCH (featuresParticipantN)-[featuresParticipantInteractorR:" + RelationshipTypes.INTERACTOR + "]-(featuresParticipantInteractorN:GraphInteractor)" +
                    " OPTIONAL MATCH (featuresRangeN)-[featuresRangeStartPositionR:" + RelationshipTypes.START + "]-(featuresRangeStartPositionN:GraphPosition)" +
                    " OPTIONAL MATCH (featuresRangeN)-[featuresRangeEndPositionR:" + RelationshipTypes.END + "]-(featuresRangeEndPositionN:GraphPosition)" +
                    " OPTIONAL MATCH (featuresRangeN)-[featuresRangeParticipantR:" + RelationshipTypes.PARTICIPANT + "]-(featuresRangeParticipantN:GraphEntity)" +
                    " OPTIONAL MATCH (featuresRangeParticipantN)-[featuresRangeParticipantInteractorR:" + RelationshipTypes.INTERACTOR + "]-(featuresRangeParticipantInteractorN:GraphInteractor)" +
                    " OPTIONAL MATCH (featuresRangeStartPositionN)-[featuresRangeStartPositionStatusR:" + RelationshipTypes.STATUS + "]-(featuresRangeStartPositionStatusN:GraphCvTerm)" +
                    " OPTIONAL MATCH (featuresRangeEndPositionN)-[featuresRangeEndPositionStatusR:" + RelationshipTypes.STATUS + "]-(featuresRangeEndPositionStatusN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[stoichiometryR:" + RelationshipTypes.STOICHIOMETRY + "]-(stoichiometryN:GraphStoichiometry)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[participantExpressedInR:" + RelationshipTypes.EXPRESSED_IN + "]-(participantExpressedInN:GraphOrganism)" +
                    " OPTIONAL MATCH (participantExpressedInN)-[participantExpressedInCellTypeR:" + RelationshipTypes.CELL_TYPE + "]-(participantExpressedInCellTypeN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantExpressedInCellTypeN)-[participantExpressedInCellTypeIdentifiersR:" + RelationshipTypes.IDENTIFIERS + "]" +
                    "-(participantExpressedInCellTypeIdentifiersN:GraphXref)" +
                    " OPTIONAL MATCH (participantExpressedInCellTypeIdentifiersN)-[participantExpressedInCellTypeIdentifiersDatabaseR:" + RelationshipTypes.DATABASE + "]" +
                    "-(participantExpressedInCellTypeIdentifiersDatabaseN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[participantParametersR:" + RelationshipTypes.PARAMETERS + "]-(participantParametersN:GraphParameter)" +
                    " OPTIONAL MATCH (participantParametersN)-[participantParametersTypeR:" + RelationshipTypes.TYPE + "]-(participantParametersTypeN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantParametersN)-[participantParametersUnitR:" + RelationshipTypes.UNIT + "]-(participantParametersUnitN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantParametersN)-[participantParametersValueR:" + RelationshipTypes.VALUE + "]-(participantParametersValueN:GraphParameterValue)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[participantConfidencesR:" + RelationshipTypes.CONFIDENCE + "]-(participantConfidencesN:GraphConfidence)" +
                    " OPTIONAL MATCH (participantConfidencesN)-[participantConfidencesTypeR:" + RelationshipTypes.TYPE + "]-(participantConfidencesTypeN:GraphCvTerm)" +
                    " OPTIONAL MATCH (participantEvidenceN)-[interactorR:" + RelationshipTypes.INTERACTOR + "]-(interactorN:GraphInteractor)" +
                    " OPTIONAL MATCH (interactorN)-[organismR:" + RelationshipTypes.ORGANISM + "]-(organismN:GraphOrganism)" +
                    " OPTIONAL MATCH (interactorN)-[interactorTypeR:" + RelationshipTypes.INTERACTOR_TYPE + "]-(interactorTypeN:GraphCvTerm)" +
                    " OPTIONAL MATCH (interactorN)-[preferredIdentifierR:" + RelationshipTypes.PREFERRED_IDENTIFIER + "]-(preferredIdentifierN:GraphXref)" +
                    " OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:" + RelationshipTypes.DATABASE + "]-(preferredIdentifierDatabaseN:GraphCvTerm)" +
                    " OPTIONAL MATCH (featuresRangeN)-[featuresRangeResultingSequenceR:" + RelationshipTypes.RESULTING_SEQUENCE + "]-(featuresRangeResultingSequenceN:GraphResultingSequence)" +
                    " OPTIONAL MATCH (featuresN)-[featureDetectionMethodR:" + RelationshipTypes.DETECTION_METHOD + "]-(featureDetectionMethodN:GraphCvTerm)" +
                    " OPTIONAL MATCH (featureDetectionMethodN)-[featureDetectionMethodIdentifiersR:" + RelationshipTypes.IDENTIFIERS + "]" +
                    "-(featureDetectionMethodIdentifiersN:GraphXref)" +
                    " OPTIONAL MATCH (featureDetectionMethodIdentifiersN)-[featureDetectionMethodIdentifiersDatabaseR:" + RelationshipTypes.DATABASE + "]" +
                    "-(featureDetectionMethodIdentifiersDatabaseN:GraphCvTerm)" +

                    " WITH binaryIEN,publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN," +
                    " interactionIdentifiersNCollection,interactionIdentifiersRCollection," +
                    " interactionIdentifiersDatabaseRCollection,interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection," +
                    " interactionXrefsNCollection,interactionXrefsDatabaseRCollection,interactionXrefsDatabaseNCollection," +
                    " interactionAnnotationRCollection,interactionAnnotationNCollection,interactionAnnotationTopicRCollection," +
                    " interactionAnnotationTopicNCollection,interactionConfidencesRCollection,interactionConfidencesNCollection," +
                    " interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection,interactionParametersRCollection," +
                    " interactionParametersNCollection,interactionParametersTypeRCollection,interactionParametersTypeNCollection," +
                    " interactionParametersUnitRCollection,interactionParametersUnitNCollection,interactionParametersValueRCollection," +
                    " interactionParametersValueNCollection,interactionTypeR,interactionTypeN,interactionDetectionMethodN,interactionDetectionMethodR," +
                    " hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection,expXrefsDatabaseRCollection,expXrefsDatabaseNCollection," +
                    " experimentAnnotationRCollection,experimentAnnotationNCollection,experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection," +
                    " pubIdentifiersRCollection,pubIdentifiersNCollection,pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection," +
                    " pubXrefsRCollection,pubXrefsNCollection,pubXrefsDatabaseRCollection,pubXrefsDatabaseNCollection,pubImexIdDatabaseR," +
                    " pubImexIdDatabaseN,pubSourceR,pubSourceN,COLLECT(expRoleR) as expRoleRCollection,COLLECT(expRoleN) as expRoleNCollection," +
                    " COLLECT(bioRoleR) as bioRoleRCollection,COLLECT(bioRoleN) as bioRoleNCollection," +
                    " COLLECT(identificationMethodR) as identificationMethodRCollection,COLLECT(identificationMethodN) as identificationMethodNCollection," +
                    " COLLECT(featuresR) as featuresRCollection,COLLECT(featuresN) as featuresNCollection," +
                    " COLLECT(featuresParticipantR) as featuresParticipantRCollection," +
                    " COLLECT(featuresParticipantN) as featuresParticipantNCollection,COLLECT(featuresParticipantInteractorR) as" +
                    " featuresParticipantInteractorRCollection,COLLECT(featuresParticipantInteractorN) as featuresParticipantInteractorNCollection," +
                    " COLLECT(featuresTypeR) as featuresTypeRCollection,COLLECT(featuresTypeN) as featuresTypeNCollection," +
                    " COLLECT(featuresRangeR) as featuresRangeRCollection,COLLECT(featuresRangeN) as featuresRangeNCollection," +
                    " COLLECT(featuresRangeParticipantR) as featuresRangeParticipantRCollection,COLLECT(featuresRangeParticipantN) as" +
                    " featuresRangeParticipantNCollection,COLLECT(featuresRangeParticipantInteractorR) as featuresRangeParticipantInteractorRCollection," +
                    " COLLECT(featuresRangeParticipantInteractorN) as featuresRangeParticipantInteractorNCollection,COLLECT(featuresRangeStartPositionR)" +
                    " as featuresRangeStartPositionRCollection,COLLECT(featuresRangeStartPositionN) as featuresRangeStartPositionNCollection," +
                    " COLLECT(featuresRangeEndPositionR) as featuresRangeEndPositionRCollection,COLLECT(featuresRangeEndPositionN) as" +
                    " featuresRangeEndPositionNCollection,COLLECT(featuresRangeStartPositionStatusR) as featuresRangeStartPositionStatusRCollection," +
                    " COLLECT(featuresRangeStartPositionStatusN) as featuresRangeStartPositionStatusNCollection,COLLECT(featuresRangeEndPositionStatusR) as" +
                    " featuresRangeEndPositionStatusRCollection,COLLECT(featuresRangeEndPositionStatusN) as featuresRangeEndPositionStatusNCollection," +
                    " COLLECT(stoichiometryR) as stoichiometryRCollection,COLLECT(stoichiometryN) as stoichiometryNCollection," +
                    " COLLECT(organismR) as organismRCollection," +
                    " COLLECT(organismN) as organismNCollection," +
                    " COLLECT(interactorTypeR) as interactorTypeRCollection,COLLECT(interactorTypeN) as interactorTypeNCollection," +
                    " COLLECT(preferredIdentifierR) as preferredIdentifierRCollection,COLLECT(preferredIdentifierN) as" +
                    " preferredIdentifierNCollection,COLLECT(preferredIdentifierDatabaseR) as preferredIdentifierDatabaseRCollection," +
                    " COLLECT(preferredIdentifierDatabaseN) as preferredIdentifierDatabaseNCollection," +
                    " COLLECT(participantEvidenceR) as participantEvidenceRCollection," +
                    " COLLECT(participantEvidenceN) as participantEvidenceNCollection," +
                    " COLLECT(interactorR) as interactorRCollection ,COLLECT(interactorN) as interactorNCollection," +
                    " COLLECT(featuresRoleR) as featuresRoleRCollection,COLLECT(featuresRoleN) as featuresRoleNCollection," +
                    " COLLECT(featureLinkedFeaturesR) as featureLinkedFeaturesRCollection,COLLECT(featureLinkedFeaturesN) as" +
                    " featureLinkedFeaturesNCollection,COLLECT(participantExpressedInR) as participantExpressedInRCollection," +
                    " COLLECT(participantExpressedInN) as participantExpressedInNCollection,COLLECT(participantParametersR) as" +
                    " participantParametersRCollection,COLLECT(participantParametersN) as participantParametersNCollection," +
                    " COLLECT(participantParametersTypeR) as participantParametersTypeRCollection,COLLECT(participantParametersTypeN)" +
                    " as participantParametersTypeNCollection,COLLECT(participantParametersUnitR) as participantParametersUnitRCollection," +
                    " COLLECT(participantParametersUnitN) as participantParametersUnitNCollection,COLLECT(participantParametersValueR)" +
                    " as participantParametersValueRCollection,COLLECT(participantParametersValueN) as participantParametersValueNCollection," +
                    " COLLECT(participantConfidencesR) as participantConfidencesRCollection,COLLECT(participantConfidencesN) as" +
                    " participantConfidencesNCollection,COLLECT(participantConfidencesTypeR) as participantConfidencesTypeRCollection," +
                    " COLLECT(participantConfidencesTypeN) as participantConfidencesTypeNCollection,COLLECT(participantExpressedInCellTypeR)" +
                    " as participantExpressedInCellTypeRCollection,COLLECT(participantExpressedInCellTypeN) as participantExpressedInCellTypeNCollection," +
                    " COLLECT(participantExpressedInCellTypeIdentifiersR) as participantExpressedInCellTypeIdentifiersRCollection," +
                    " COLLECT(participantExpressedInCellTypeIdentifiersN) as participantExpressedInCellTypeIdentifiersNCollection," +
                    " COLLECT(participantExpressedInCellTypeIdentifiersDatabaseR) as participantExpressedInCellTypeIdentifiersDatabaseRCollection," +
                    " COLLECT(participantExpressedInCellTypeIdentifiersDatabaseN) as participantExpressedInCellTypeIdentifiersDatabaseNCollection," +
                    " COLLECT(featureLinkedFeaturesLFR) as featureLinkedFeaturesLFR,COLLECT(featureLinkedFeaturesLFN) as featureLinkedFeaturesLFN," +
                    " COLLECT(featuresRangeResultingSequenceR) as featuresRangeResultingSequenceRCollection,COLLECT(featuresRangeResultingSequenceN) " +
                    " as featuresRangeResultingSequenceNCollection,COLLECT(featureDetectionMethodR) as featureDetectionMethodRCollection," +
                    " COLLECT(featureDetectionMethodN) as featureDetectionMethodNCollection," +
                    " COLLECT(featureDetectionMethodIdentifiersR) as featureDetectionMethodIdentifiersRCollection," +
                    " COLLECT(featureDetectionMethodIdentifiersN) as featureDetectionMethodIdentifiersNCollection," +
                    " COLLECT(featureDetectionMethodIdentifiersDatabaseR) as featureDetectionMethodIdentifiersDatabaseRCollection," +
                    " COLLECT(featureDetectionMethodIdentifiersDatabaseN) as featureDetectionMethodIdentifiersDatabaseNCollection" +

                    " RETURN publicationN,publicationR,experimentN,experimentR,pubImexIdR,pubImexIdN,interactionIdentifiersNCollection,interactionIdentifiersRCollection,interactionIdentifiersDatabaseRCollection," +
                    " interactionIdentifiersDatabaseNCollection,interactionXrefsRCollection,interactionXrefsNCollection,interactionXrefsDatabaseRCollection," +
                    " interactionXrefsDatabaseNCollection,interactionAnnotationRCollection,interactionAnnotationNCollection," +
                    " interactionAnnotationTopicRCollection,interactionAnnotationTopicNCollection,interactionConfidencesRCollection," +
                    " interactionConfidencesNCollection,interactionConfidencesTypeRCollection,interactionConfidencesTypeNCollection," +
                    " interactionParametersRCollection ,interactionParametersNCollection,interactionParametersTypeRCollection," +
                    " interactionParametersTypeNCollection, interactionParametersUnitRCollection,interactionParametersUnitNCollection," +
                    " interactionParametersValueRCollection,interactionParametersValueNCollection,interactionTypeR,interactionTypeN," +
                    " interactionDetectionMethodN,interactionDetectionMethodR,hostOrganismR,hostOrganismN,expXrefsRCollection,expXrefsNCollection," +
                    " expXrefsDatabaseRCollection,expXrefsDatabaseNCollection,experimentAnnotationRCollection,experimentAnnotationNCollection," +
                    " experimentAnnotationTopicRCollection,experimentAnnotationTopicNCollection,pubIdentifiersRCollection,pubIdentifiersNCollection," +
                    " pubIdentifiersDatabaseRCollection,pubIdentifiersDatabaseNCollection,pubXrefsRCollection,pubXrefsNCollection,pubXrefsDatabaseRCollection," +
                    " pubXrefsDatabaseNCollection,pubImexIdDatabaseR,pubImexIdDatabaseN,pubSourceR,pubSourceN,expRoleRCollection," +
                    " expRoleNCollection,bioRoleRCollection,bioRoleNCollection,identificationMethodRCollection,identificationMethodNCollection," +
                    " featuresRCollection ,featuresNCollection,featuresParticipantRCollection,featuresParticipantNCollection," +
                    " featuresParticipantInteractorRCollection,featuresParticipantInteractorNCollection,featuresTypeRCollection,featuresTypeNCollection,featuresRangeRCollection," +
                    " featuresRangeNCollection,featuresRangeParticipantRCollection,featuresRangeParticipantNCollection," +
                    " featuresRangeParticipantInteractorRCollection,featuresRangeParticipantInteractorNCollection," +
                    " featuresRangeStartPositionRCollection,featuresRangeStartPositionNCollection,featuresRangeEndPositionRCollection," +
                    " featuresRangeEndPositionNCollection,featuresRangeStartPositionStatusRCollection,featuresRangeStartPositionStatusNCollection," +
                    " featuresRangeEndPositionStatusRCollection,featuresRangeEndPositionStatusNCollection, stoichiometryRCollection," +
                    " stoichiometryNCollection,organismRCollection,organismNCollection,interactorTypeRCollection,interactorTypeNCollection," +
                    " preferredIdentifierRCollection,preferredIdentifierNCollection, preferredIdentifierDatabaseRCollection," +
                    " preferredIdentifierDatabaseNCollection,participantEvidenceRCollection,participantEvidenceNCollection," +
                    " interactorRCollection,interactorNCollection,featuresRoleRCollection,featuresRoleNCollection,featureLinkedFeaturesRCollection," +
                    " featureLinkedFeaturesNCollection,participantExpressedInRCollection,participantExpressedInNCollection," +
                    " participantParametersRCollection,participantParametersNCollection,participantParametersTypeRCollection," +
                    " participantParametersTypeNCollection,participantParametersUnitRCollection,participantParametersUnitNCollection," +
                    " participantParametersValueRCollection,participantParametersValueNCollection,participantConfidencesRCollection," +
                    " participantConfidencesNCollection,participantConfidencesTypeRCollection,participantConfidencesTypeNCollection," +
                    " participantExpressedInCellTypeRCollection,participantExpressedInCellTypeNCollection,participantExpressedInCellTypeIdentifiersRCollection," +
                    " participantExpressedInCellTypeIdentifiersNCollection,participantExpressedInCellTypeIdentifiersDatabaseRCollection," +
                    " participantExpressedInCellTypeIdentifiersDatabaseNCollection,featureLinkedFeaturesLFR,featureLinkedFeaturesLFN," +
                    " featuresRangeResultingSequenceRCollection,featuresRangeResultingSequenceNCollection,featureDetectionMethodRCollection," +
                    " featureDetectionMethodNCollection,featureDetectionMethodIdentifiersRCollection,featureDetectionMethodIdentifiersNCollection," +
                    " featureDetectionMethodIdentifiersDatabaseRCollection,featureDetectionMethodIdentifiersDatabaseNCollection,binaryIEN limit 1";


}
