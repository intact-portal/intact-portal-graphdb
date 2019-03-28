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
    public static final String COMM_NEIGH_OF_INTOR=
             "MATCH (interactorA:GraphInteractor)<-[:"+RelationshipTypes.INTERACTORS+"]-(binaryIE:GraphInteractionEvidence)-[:"+RelationshipTypes.INTERACTORS+"]->(interactorB:GraphInteractor)" +
             " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB" +
             " UNWIND interactions as interaction" +
             " MATCH (interaction:GraphInteractionEvidence) -[experiment:"+RelationshipTypes.EXPERIMENT+"] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:"+RelationshipTypes.INTERACTION_DETECTION_METHOD+"]->(dm_cvterm:GraphCvTerm)," +
             " (interaction:GraphInteractionEvidence) - [interactionType:"+RelationshipTypes.INTERACTION_TYPE+"] -> (it_cvterm:GraphCvTerm)," +
             " (graphExperiment:GraphExperiment)<-[publication:"+RelationshipTypes.PUB_EXP+"]-(graphpublication:GraphPublication)" +
             " WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB))" +
             " RETURN interactorA,interactorB,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments," +
                     "COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms," +
                     "COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications ORDER BY interactorA.ac";

    /*
    * Equivalent Query String : MATCH (interactorA:GraphInteractor)<-[:interactorA]-(binaryIE:GraphInteractionEvidence)-[:interactorB]->(interactorB:GraphInteractor)
                                WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB
                                WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB)) RETURN COUNT(*)
    * */
    public static final String INTERACTOR_PAIR_COUNT=
            "MATCH (interactorA:GraphInteractor)<-[:"+RelationshipTypes.INTERACTORS+"]-(binaryIE:GraphInteractionEvidence)-[:"+RelationshipTypes.INTERACTORS+"]->(interactorB:GraphInteractor)" +
            " WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB" +
            " WHERE  (ID(interactorA)<ID(interactorB)) OR (ID(interactorA) = ID(interactorB))" +
            " RETURN COUNT(*)";

    public static final String GET_CLUSTERED_INTERACTION="MATCH (n:GraphClusteredInteraction)-->(m:GraphBinaryInteractionEvidence{ uniqueKey: {0}}) RETURN (n)";


    /*
    * Equivalent Query String :"MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(participantEvidenceN:GraphParticipantEvidence)-[interactorR:interactor]-(interactorN:GraphInteractor)
                                 OPTIONAL MATCH (participantEvidenceN)-[expRoleR:experimentalRole]-(expRoleN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[bioRoleR:biologicalRole]-(bioRoleN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[identificationMethodR:identificationMethods]-(identificationMethodN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[experimentaPreparationR:experimentaPreparations]-(experimentaPreparationN:GraphCvTerm)
                                 OPTIONAL MATCH (participantEvidenceN)-[parametersR:parameters]-(parametersN:GraphParameter)
                                 OPTIONAL MATCH (participantEvidenceN)-[confidencesR:confidences]-(confidencesN:GraphConfidence)
                                 OPTIONAL MATCH (participantEvidenceN)-[aliasesR:aliases]-(aliasesN:GraphAlias)
                                 OPTIONAL MATCH (participantEvidenceN)-[featuresR:features]-(featuresN:GraphFeature)
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
    public static final String GET_PARTICIPANTS_BY_INTERACTION_AC=
            "MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(participantEvidenceN:GraphParticipantEvidence)-[interactorR:"+RelationshipTypes.INTERACTOR+"]-(interactorN:GraphInteractor)" +
            "OPTIONAL MATCH (participantEvidenceN)-[expRoleR:"+RelationshipTypes.EXPERIMENTAL_ROLE+"]-(expRoleN:GraphCvTerm)" +
            "OPTIONAL MATCH (participantEvidenceN)-[bioRoleR:"+RelationshipTypes.BIOLOGICAL_ROLE+"]-(bioRoleN:GraphCvTerm)" +
            "OPTIONAL MATCH (participantEvidenceN)-[identificationMethodR:"+RelationshipTypes.IDENTIFICATION_METHOD+"]-(identificationMethodN:GraphCvTerm)" +
            "OPTIONAL MATCH (participantEvidenceN)-[experimentaPreparationR:"+RelationshipTypes.EXPERIMENTAL_PREPARATION+"]-(experimentaPreparationN:GraphCvTerm)" +
            "OPTIONAL MATCH (participantEvidenceN)-[parametersR:"+RelationshipTypes.PARAMETERS+"]-(parametersN:GraphParameter)" +
            "OPTIONAL MATCH (participantEvidenceN)-[confidencesR:"+RelationshipTypes.CONFIDENCE+"]-(confidencesN:GraphConfidence)" +
            "OPTIONAL MATCH (participantEvidenceN)-[aliasesR:"+RelationshipTypes.ALIASES+"]-(aliasesN:GraphAlias)" +
            "OPTIONAL MATCH (participantEvidenceN)-[featuresR:"+RelationshipTypes.FEATURES+"]-(featuresN:GraphFeature)" +
            "OPTIONAL MATCH (interactorN)-[itorAliasesR:"+RelationshipTypes.ALIASES+"]-(itorAliasesN:GraphAlias)" +
            "OPTIONAL MATCH (interactorN)-[organismR:"+RelationshipTypes.ORGANISM+"]-(organismN:GraphOrganism)" +
            "OPTIONAL MATCH (interactorN)-[interactorTypeR:"+RelationshipTypes.INTERACTOR_TYPE+"]-(interactorTypeN:GraphCvTerm)" +
            "OPTIONAL MATCH (interactorN)-[preferredIdentifierR:"+RelationshipTypes.PREFERRED_IDENTIFIER+"]-(preferredIdentifierN:GraphXref)" +
            "OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:"+RelationshipTypes.DATABASE+"]-(preferredIdentifierDatabaseN:GraphCvTerm)" +
            "RETURN  participantEvidenceN,expRoleR,expRoleN,bioRoleR,bioRoleN,interactorR,interactorN,organismR,organismN,interactorTypeR,interactorTypeN,COLLECT(identificationMethodR),COLLECT(identificationMethodN)," +
                    "COLLECT(experimentaPreparationR),COLLECT(experimentaPreparationN),COLLECT(parametersR),COLLECT(parametersN),COLLECT(confidencesR),COLLECT(confidencesN),COLLECT(aliasesR),COLLECT(aliasesN),COLLECT(featuresR)," +
                    "COLLECT(featuresN),COLLECT(itorAliasesR),COLLECT(itorAliasesN),COLLECT(preferredIdentifierR),COLLECT(preferredIdentifierN),COLLECT(preferredIdentifierDatabaseR),COLLECT(preferredIdentifierDatabaseN)";

    public static final String GET_PARTICIPANTS_BY_INTERACTION_AC_COUNT="MATCH (gie:GraphBinaryInteractionEvidence{ ac: {0} }) --(p:GraphParticipantEvidence)  RETURN COUNT(DISTINCT p)";

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
    public static final String GET_EXP_PUB_BY_INTERACTION_AC=
            "MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(experimentN:GraphExperiment)-[publicationR:"+RelationshipTypes.PUB_EXP+"]-(publicationN:GraphPublication) " +
            "OPTIONAL MATCH (experimentN)-[interactionDetectionMethodR:"+RelationshipTypes.INTERACTION_DETECTION_METHOD+"]-(interactionDetectionMethodN:GraphCvTerm)" +
            "OPTIONAL MATCH (experimentN)-[hostOrganismR:"+RelationshipTypes.HOST_ORGANISM+"]-(hostOrganismN:GraphOrganism)" +
            "OPTIONAL MATCH (experimentN)-[expXrefsR:"+RelationshipTypes.XREFS+"]-(expXrefsN:GraphXref)" +
            "OPTIONAL MATCH (expXrefsN)-[expXrefsDatabaseR:"+RelationshipTypes.DATABASE+"]-(expXrefsDatabaseN:GraphCvTerm)" +
            "OPTIONAL MATCH (experimentN)-[expAnnotationsR:"+RelationshipTypes.ANNOTATIONS+"]-(expAnnotationsN:GraphAnnotation)" +
            "OPTIONAL MATCH (expAnnotationsN)-[expAnnotationsNTopicR:"+RelationshipTypes.TOPIC+"]-(expAnnotationsNTopicN:GraphCvTerm)" +
            "OPTIONAL MATCH (publicationN)-[pubXrefsR:"+RelationshipTypes.XREFS+"]-(pubXrefsN:GraphXref)" +
            "OPTIONAL MATCH (pubXrefsN)-[pubXrefsDatabaseR:"+RelationshipTypes.DATABASE+"]-(pubXrefsDatabaseN:GraphCvTerm)" +
            "OPTIONAL MATCH (publicationN)-[publicationAnnotationsR:"+RelationshipTypes.ANNOTATIONS+"]-(publicationAnnotationsN:GraphAnnotation)" +
            "OPTIONAL MATCH (publicationAnnotationsN)-[publicationAnnotationsTopicR:"+RelationshipTypes.TOPIC+"]-(publicationAnnotationsTopicN:GraphCvTerm) " +
            "RETURN experimentN,binaryIEN,publicationR,publicationN,interactionDetectionMethodR,interactionDetectionMethodN,hostOrganismR,hostOrganismN," +
                    "COLLECT(expXrefsR),COLLECT(expXrefsN),COLLECT(expAnnotationsR),COLLECT(expAnnotationsN),COLLECT(expXrefsDatabaseR),COLLECT(expXrefsDatabaseN)," +
                    "COLLECT(expAnnotationsNTopicR),COLLECT(expAnnotationsNTopicN),COLLECT(pubXrefsR),COLLECT(pubXrefsN),COLLECT(pubXrefsDatabaseR),COLLECT(pubXrefsDatabaseN)," +
                    "COLLECT(publicationAnnotationsR),COLLECT(publicationAnnotationsN),COLLECT(publicationAnnotationsTopicR),COLLECT(publicationAnnotationsTopicN)";

    public static final String GET_FEATURES_BY_INTERACTION_AC_COUNT =
            "MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(graphParticipantEvidenceN:GraphParticipantEvidence)--(graphFeaturesN:GraphFeature)" +
            "RETURN COUNT(DISTINCT graphFeaturesN)";

    /*
      Equivalent Query String : MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} }) --(graphParticipantEvidenceN:GraphParticipantEvidence)-[graphFeaturesR:features]-(graphFeaturesN:GraphFeature)
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

    public static final String GET_FEATURES_BY_INTERACTION_AC=
            "MATCH (binaryIEN:GraphBinaryInteractionEvidence{ ac: {0} })--(graphParticipantEvidenceN:GraphParticipantEvidence)-[graphFeaturesR:"+RelationshipTypes.FEATURES+"]-(graphFeaturesN:GraphFeature)" +
            "MATCH (graphParticipantEvidenceN)-[interactorR:"+RelationshipTypes.INTERACTOR+"]-(interactorN:GraphInteractor)" +
            "OPTIONAL MATCH (interactorN)-[preferredIdentifierR:"+RelationshipTypes.PREFERRED_IDENTIFIER+"]-(preferredIdentifierN:GraphXref)" +
            "OPTIONAL MATCH (preferredIdentifierN)-[preferredIdentifierDatabaseR:"+RelationshipTypes.DATABASE+"]-(preferredIdentifierDatabaseN:GraphCvTerm)" +
            "OPTIONAL MATCH (graphFeaturesN)-[typeR:"+RelationshipTypes.TYPE+"]-(typeN:GraphCvTerm)" +
            "OPTIONAL MATCH (graphFeaturesN)-[rangesR:"+RelationshipTypes.RANGES+"]-(rangesN:GraphRange)" +
            "OPTIONAL MATCH (rangesN)-[startR:"+RelationshipTypes.START+"]-(startN:GraphPosition)" +
            "OPTIONAL MATCH (rangesN)-[endR:"+RelationshipTypes.END+"]-(endN:GraphPosition)" +
            "OPTIONAL MATCH (endN)-[statusR:"+RelationshipTypes.STATUS+"]-(statusN:GraphCvTerm)"+
            "RETURN graphParticipantEvidenceN,COLLECT(graphFeaturesN),COLLECT(graphFeaturesR),COLLECT(interactorR),COLLECT(interactorN),COLLECT(typeR),COLLECT(typeN),COLLECT(rangesR)," +
                    "COLLECT(rangesN),COLLECT(startR),COLLECT(startN),COLLECT(endR),COLLECT(endN),COLLECT(statusR),COLLECT(statusN),COLLECT(preferredIdentifierR),COLLECT(preferredIdentifierN)," +
                    "COLLECT(preferredIdentifierDatabaseR),COLLECT(preferredIdentifierDatabaseN)";

}
