package uk.ac.ebi.intact.graphdb.utils;

import uk.ac.ebi.intact.graphdb.model.relationships.RelationshipTypes;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    public static final String ALL_BINARY_INTERACTIONS =
            "MATCH (interaction:GraphBinaryInteractionEvidence)" +
                    "RETURN distinct interaction";

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

    public static final String GET_NETWORK_NODES =
            " MATCH (interactor:GraphInteractor) WHERE (interactor.ac IN {acs}) OR {acs} is null" +
                    " MATCH (interactor)-[organismR:" + RelationshipTypes.ORGANISM + "]->(organismN:GraphOrganism) WHERE ((organismN.taxId IN {species}) OR {species} is null)" +
                    " MATCH (interactor)<-[interactorsFR:" + RelationshipTypes.INTERACTORS + "]-(interaction:GraphBinaryInteractionEvidence)" +
                    " WITH COLLECT(distinct interaction) as interactionCollection" +
                    " UNWIND interactionCollection as interactionN " +

                    " MATCH (interactionN)-[interactorsR:" + RelationshipTypes.INTERACTORS + "]->(interactorsN:GraphInteractor)" +
                    " WITH COLLECT(distinct interactorsN) as interactorsCollection" +
                    " UNWIND interactorsCollection as interactorN " +
                    " MATCH (interactorN)-[organismR:" + RelationshipTypes.ORGANISM + "]->(organismN:GraphOrganism) WHERE ((organismN.taxId IN {species}) OR {species} is null)" +
                    " MATCH (interactorN)-[identifierR:" + RelationshipTypes.PREFERRED_IDENTIFIER + "]->(identifierN:GraphXref) " +
                    " MATCH (identifierN)-[identifierDBR:" + RelationshipTypes.DATABASE + "]->(identifierDBN:GraphCvTerm)" +
                    " WITH interactorN,identifierN,identifierDBN,organismN" +
                    " OPTIONAL MATCH (interactorN)-[interactorTypeR:" + RelationshipTypes.INTERACTOR_TYPE + "]->(interactorTypeN:GraphCvTerm)" +
                    " WITH interactorN,identifierN,identifierDBN,organismN,interactorTypeN" +
                    " OPTIONAL MATCH (interactorN)-[interactorXrefsR:" + RelationshipTypes.IDENTIFIERS + "]->(interactorXrefsN:GraphXref)" +
                    " WITH" +
                    "       interactorN.ac as ac ," +
                    "       interactorN.fullName as fullName ," +
                    "       identifierN.identifier as identifier," +
                    "       identifierDBN.shortName as identifier_db_name," +
                    "       identifierDBN.mIIdentifier as identifier_db_mi," +
                    "       organismN.scientificName as scientificName," +
                    "       organismN.taxId as taxId ," +
                    "       (interactorN.preferredName +'('+identifierN.identifier+')') as label," +
                    "       interactorTypeN.shortName as shortName," +
                    "       interactorTypeN.mIIdentifier as mIIdentifier," +
                    "       interactorTypeN.mODIdentifier as mODIdentifier," +
                    "       interactorTypeN.pARIdentifier as pARIdentifier," +
                    "       interactorN.preferredName as preferredName," +
                    "       COLLECT(interactorXrefsN) as interactorXrefsNCollection" +
                    " UNWIND interactorXrefsNCollection as interactorXref" +
                    " MATCH (interactorXref)-[interactorXrefDatabaseR:" + RelationshipTypes.DATABASE + "]->(interactorXrefDatabaseN:GraphCvTerm)" +
                    " MATCH (interactorXref)-[interactorXrefQualifierR:" + RelationshipTypes.QUALIFIER + "]->(interactorXrefQualifierN:GraphCvTerm)" +
                    " WITH" +
                    "       ac," +
                    "       fullName," +
                    "       identifier," +
                    "       identifier_db_name," +
                    "       identifier_db_mi," +
                    "       scientificName," +
                    "       taxId," +
                    "       label," +
                    "       shortName," +
                    "       mIIdentifier," +
                    "       mODIdentifier," +
                    "       pARIdentifier," +
                    "       preferredName," +
                    " COLLECT({" + NetworkNodeParamNames.XREF_DB_NAME + ":interactorXrefDatabaseN.shortName," +
                    "          " + NetworkNodeParamNames.XREF_DB_MI + ":interactorXrefDatabaseN.mIIdentifier," +
                    "          " + NetworkNodeParamNames.XREF_ID + ":interactorXref.identifier," +
                    "          " + NetworkNodeParamNames.XREF_AC + ":interactorXref.ac," +
                    "          " + NetworkNodeParamNames.XREF_QUALIFIER_NAME + ":interactorXrefQualifierN.shortName," +
                    "          " + NetworkNodeParamNames.XREF_QUALIFIER_MI + ":interactorXrefQualifierN.mIIdentifier" +
                    "        }) as xrefs" +

                    " RETURN " +
                    "       DISTINCT" +
                    "       ac as " + NetworkNodeParamNames.ID + "," +
                    "       fullName as " + NetworkNodeParamNames.FULL_NAME + "," +
                    "       identifier as " + NetworkNodeParamNames.PREFERRED_ID + "," +
                    "       identifier_db_name as " + NetworkNodeParamNames.PREFERRED_ID_DB_NAME + "," +
                    "       identifier_db_mi as " + NetworkNodeParamNames.PREFERRED_ID_DB_MI + "," +
                    "       scientificName as " + NetworkNodeParamNames.SPECIES + "," +
                    "       taxId as " + NetworkNodeParamNames.TAXID + "," +
                    "       label as " + NetworkNodeParamNames.LABEL + "," +
                    "       shortName as " + NetworkNodeParamNames.TYPE + "," +
                    "       mIIdentifier as " + NetworkNodeParamNames.TYPE_MI_IDENTIFIER + "," +
                    "       preferredName as " + NetworkNodeParamNames.INTERACTOR_NAME + "," +
                    "       xrefs as " + NetworkNodeParamNames.IDENTIFIERS;

    public static final String GET_NETWORK_EDGES =
            " MATCH (interactor:GraphInteractor) WHERE (interactor.ac IN {acs}) OR {acs} is null" +
                    " MATCH (interactor)-[organismR:" + RelationshipTypes.ORGANISM + "]->(organismN:GraphOrganism) WHERE ((organismN.taxId IN {species}) OR {species} is null)" +
                    " MATCH (interactor)<-[interactorsFR:" + RelationshipTypes.INTERACTORS + "]-(interaction:GraphBinaryInteractionEvidence)" +
                    " WITH COLLECT(distinct interaction) as interactionCollection" +
                    " UNWIND interactionCollection as interactionN" +
                    " MATCH (interactionN)-[:" + RelationshipTypes.INTERACTION_TYPE + "]-(interactionTypeN:GraphCvTerm)" +
                    " MATCH (interactionN)-[:" + RelationshipTypes.INTERACTIONS + "]-(clusteredInteractionN:GraphClusteredInteraction)" +
                    " MATCH (interactionN)-[:" + RelationshipTypes.EXPERIMENT + "]-(experimentN:GraphExperiment)" +

                    " MATCH (experimentN)-[:" + RelationshipTypes.INTERACTION_DETECTION_METHOD + "]-(interactionDetectionMethodN:GraphCvTerm)" +
                    " MATCH (experimentN)-[:" + RelationshipTypes.PUB_EXP + "]-(publicationN:GraphPublication)" +
                    " MATCH (publicationN)-[:" + RelationshipTypes.PMID + "]-(pmIdN:GraphXref)" +
                    " OPTIONAL MATCH (experimentN)-[:" + RelationshipTypes.HOST_ORGANISM + "]-(hostOrganismN:GraphOrganism)" +

                    " OPTIONAL MATCH (interactionN)-[:" + RelationshipTypes.COMPLEX_EXPANSION + "]-(complexExpansionN:GraphCvTerm) " +

                    " OPTIONAL MATCH (interactionN)-[:" + RelationshipTypes.INTERACTOR_A + "]-(interactorAN:GraphInteractor)" +
                    "                -[:" + RelationshipTypes.ORGANISM + "]->(organismAN:GraphOrganism) WHERE ((organismAN.taxId IN {species}) OR {species} is null)" +
                    " OPTIONAL MATCH (interactionN)-[:" + RelationshipTypes.INTERACTOR_B + "]-(interactorBN:GraphInteractor) " +
                    "                -[:" + RelationshipTypes.ORGANISM + "]->(organismBN:GraphOrganism) WHERE ((organismBN.taxId IN {species}) OR {species} is null)" +

                    " OPTIONAL MATCH (interactionN)-[:" + RelationshipTypes.BIE_PARTICIPANT_A + "]-(entityAN:GraphEntity) " +
                    " OPTIONAL MATCH (entityAN)-[:" + RelationshipTypes.BIOLOGICAL_ROLE + "]-(biologicalRoleAN:GraphCvTerm) " +
                    " OPTIONAL MATCH (entityAN)-[:" + RelationshipTypes.PARTICIPANT_FEATURE + "]-(featuresAN:GraphFeature) " +
                    " OPTIONAL MATCH (featuresAN)-[:" + RelationshipTypes.TYPE + "]-(featureTypeAN:GraphCvTerm) " +

                    " WITH interactionN," +
                    "      interactorAN.ac as interactor_A_ac," +
                    "      interactorBN.ac as interactor_B_ac," +
                    "      interactionTypeN.shortName as type_short_name," +
                    "      interactionTypeN.mIIdentifier as type_mi," +
                    "      interactionDetectionMethodN.fullName as detection_method_long_name," +
                    "      interactionDetectionMethodN.mIIdentifier as detection_method_mi," +
                    "      clusteredInteractionN.miscore as mi_score," +
                    "      hostOrganismN.taxId as host_organism_tax_id," +
                    "      hostOrganismN.scientificName as host_organism_scientific_name," +
                    "      pmIdN.identifier as pmid,complexExpansionN.shortName as expansion_type," +
                    "     biologicalRoleAN.shortName as biological_role_A_short_name," +
                    "      biologicalRoleAN.mIIdentifier as biological_role_A_mi," +
                    " COLLECT({" + NetworkEdgeParamNames.FEATURE_NAME + ":featuresAN.shortName," +
                    "          " + NetworkEdgeParamNames.FEATURE_AC + ":featuresAN.ac," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE + ":featureTypeAN.shortName," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE_MOD_IDENTIFIER + ":featureTypeAN.mODIdentifier," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE_PAR_IDENTIFIER + ":featureTypeAN.pARIdentifier," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER + ":featureTypeAN.mIIdentifier}" +
                    "        ) as source_features" +

                    " OPTIONAL MATCH (interactionN)-[:" + RelationshipTypes.BIE_PARTICIPANT_B + "]-(entityBN:GraphEntity) " +
                    " OPTIONAL MATCH (entityBN)-[:" + RelationshipTypes.BIOLOGICAL_ROLE + "]-(biologicalRoleBN:GraphCvTerm) " +
                    " OPTIONAL MATCH (entityBN)-[:" + RelationshipTypes.PARTICIPANT_FEATURE + "]-(featuresBN:GraphFeature) " +
                    " OPTIONAL MATCH (featuresBN)-[:" + RelationshipTypes.TYPE + "]-(featureTypeBN:GraphCvTerm) " +
                    " WITH ID(interactionN) as id," +
                    "      interactionN.ac as interaction_ac," +
                    "      interactor_A_ac," +
                    "      interactor_B_ac," +
                    "      type_short_name," +
                    "      type_mi," +
                    "      detection_method_long_name," +
                    "      detection_method_mi," +
                    "      mi_score," +
                    "      host_organism_tax_id," +
                    "      host_organism_scientific_name," +
                    "      pmid," +
                    "      expansion_type," +
                    "      biological_role_A_short_name," +
                    "      biological_role_A_mi," +
                    "      biologicalRoleBN.shortName as biological_role_B_short_name," +
                    "      biologicalRoleBN.mIIdentifier as biological_role_B_mi," +
                    "      source_features," +
                    " COLLECT({" + NetworkEdgeParamNames.FEATURE_NAME + ":featuresBN.shortName," +
                    "          " + NetworkEdgeParamNames.FEATURE_AC + ":featuresBN.ac," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE + ":featureTypeBN.shortName," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE_MOD_IDENTIFIER + ":featureTypeBN.mODIdentifier," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE_PAR_IDENTIFIER + ":featureTypeBN.pARIdentifier," +
                    "          " + NetworkEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER + ":featureTypeBN.mIIdentifier}" +
                    "         ) as target_features" +

                    "" +

                    " RETURN " +
                    "       id as " + NetworkEdgeParamNames.ID + ", " +
                    "       interaction_ac as " + NetworkEdgeParamNames.AC + ", " +
                    "       type_short_name as " + NetworkEdgeParamNames.INTERACTION_TYPE + "," +
                    "       type_mi as " + NetworkEdgeParamNames.INTERACTION_TYPE_MI_IDENTIFIER + "," +
                    "       detection_method_long_name as " + NetworkEdgeParamNames.INTERACTION_DETECTION_METHOD + "," +
                    "       detection_method_mi as " + NetworkEdgeParamNames.INTERACTION_DETECTION_METHOD_MI_IDENTIFIER + "," +
                    "       mi_score as " + NetworkEdgeParamNames.MI_SCORE + "," +
                    "       host_organism_scientific_name as " + NetworkEdgeParamNames.HOST_ORGANISM + "," +
                    "       host_organism_tax_id as " + NetworkEdgeParamNames.HOST_ORGANISM_TAX_ID + "," +
                    "       pmid as " + NetworkEdgeParamNames.PUBMED_ID + "," +
                    "       expansion_type as " + NetworkEdgeParamNames.EXPANSION_TYPE + "," +
                    "       {" +
                    "       " + NetworkEdgeParamNames.ID + ": interactor_A_ac," +
                    "       " + NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE + ": biological_role_A_short_name," +
                    "       " + NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER + ": biological_role_A_mi," +
                    "       " + NetworkEdgeParamNames.PARTICIPANT_FEATURES + ": source_features" +
                    "       } as  " + NetworkEdgeParamNames.SOURCE_NODE + "," +
                    "       {" +
                    "       " + NetworkEdgeParamNames.ID + ": interactor_B_ac," +
                    "       " + NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE + ": biological_role_B_short_name," +
                    "       " + NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER + ": biological_role_B_mi," +
                    "       " + NetworkEdgeParamNames.PARTICIPANT_FEATURES + ": target_features" +
                    "       } as  " + NetworkEdgeParamNames.TARGET_NODE + "" +

                    "";

    public static final String INTERACTION_BY_BINARY_ID =
            "MATCH (interaction:GraphBinaryInteractionEvidence)" +
                    "WHERE ID(interaction)={binary_id}" +
                    "RETURN interaction";

}
