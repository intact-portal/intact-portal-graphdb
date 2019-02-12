package uk.ac.ebi.intact.graphdb.utils;

import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    /*Common BinaryInteractionEvidences of any two interactors*/
 //   public static final String COMM_NEIGH_OF_INTOR="MATCH (interactorA:GraphInteractor)--(binaryIE:GraphBinaryInteractionEvidence)--(interactorB:GraphInteractor) WHERE NOT (interactorA) = (interactorB) RETURN interactorA,COLLECT(binaryIE) as binaryInteractionEvidences,interactorB";
    public static final String COMM_NEIGH_OF_INTOR=  "MATCH (interactorA:GraphInteractor)<-[:interactorA]-(binaryIE:GraphInteractionEvidence)-[:interactorB]->(interactorB:GraphInteractor) WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB UNWIND interactions as interaction MATCH (interaction:GraphInteractionEvidence) -[experiment:experiment] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:interactionDetectionMethod]->(dm_cvterm:GraphCvTerm) ,(interaction:GraphInteractionEvidence) - [interactionType:interactionType] -> (it_cvterm:GraphCvTerm),(graphExperiment:GraphExperiment)<-[publication:PUB_EXP]-(graphpublication:GraphPublication) WHERE  NOT (interactorA) = (interactorB) RETURN interactorA,interactorB,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments,COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms,COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications ORDER BY interactorA.ac";

    public static final String INTERACTOR_PAIR_COUNT="MATCH (interactorA:GraphInteractor)<-[:interactorA]-(binaryIE:GraphInteractionEvidence)-[:interactorB]->(interactorB:GraphInteractor) WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB  WHERE  NOT (interactorA) = (interactorB) RETURN COUNT(*)";

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
                     RETURN  participantEvidenceN,expRoleR,expRoleN,bioRoleR,bioRoleN,interactorR,interactorN,organismR,organismN,interactorTypeR,interactorTypeN,COLLECT(identificationMethodR),
                     COLLECT(identificationMethodN),COLLECT(experimentaPreparationR),COLLECT(experimentaPreparationN),COLLECT(parametersR),COLLECT(parametersN),COLLECT(confidencesR),COLLECT(confidencesN),
                     COLLECT(aliasesR),COLLECT(aliasesN),COLLECT(featuresR),COLLECT(featuresN),COLLECT(itorAliasesR),COLLECT(itorAliasesN)";
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
            "RETURN  participantEvidenceN,expRoleR,expRoleN,bioRoleR,bioRoleN,interactorR,interactorN,organismR,organismN,interactorTypeR,interactorTypeN,COLLECT(identificationMethodR),COLLECT(identificationMethodN)," +
                    "COLLECT(experimentaPreparationR),COLLECT(experimentaPreparationN),COLLECT(parametersR),COLLECT(parametersN),COLLECT(confidencesR),COLLECT(confidencesN),COLLECT(aliasesR),COLLECT(aliasesN),COLLECT(featuresR)," +
                    "COLLECT(featuresN),COLLECT(itorAliasesR),COLLECT(itorAliasesN)";

    public static final String GET_PARTICIPANTS_BY_INTERACTION_AC_COUNT="MATCH (gie:GraphBinaryInteractionEvidence{ ac: {0} }) --(p:GraphParticipantEvidence)  RETURN COUNT(DISTINCT p)";

}
