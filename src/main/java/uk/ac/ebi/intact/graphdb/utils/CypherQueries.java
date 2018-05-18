package uk.ac.ebi.intact.graphdb.utils;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    /*Common BinaryInteractionEvidences of any two interactors*/
 //   public static final String COMM_NEIGH_OF_INTOR="MATCH (interactorA:GraphInteractor)--(binaryIE:GraphBinaryInteractionEvidence)--(interactorB:GraphInteractor) WHERE NOT (interactorA) = (interactorB) RETURN interactorA,COLLECT(binaryIE) as binaryInteractionEvidences,interactorB";
    public static final String COMM_NEIGH_OF_INTOR=  "MATCH (interactorA:GraphInteractor)<-[:interactorA]-(binaryIE:GraphInteractionEvidence)-[:interactorB]->(interactorB:GraphInteractor) WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB UNWIND interactions as interaction MATCH (interaction:GraphInteractionEvidence) -[experiment:experiment] ->(graphExperiment:GraphExperiment)-[interactionDetectionMethod:interactionDetectionMethod]->(dm_cvterm:GraphCvTerm) ,(interaction:GraphInteractionEvidence) - [interactionType:interactionType] -> (it_cvterm:GraphCvTerm),(graphExperiment:GraphExperiment)<-[publication:PUB_EXP]-(graphpublication:GraphPublication) WHERE id(interactorA) < id(interactorB) AND  NOT (interactorA) = (interactorB) RETURN interactorA,interactorB,COLLECT(interaction) as interactions,COLLECT(experiment) as experiments,COLLECT(graphExperiment) as graphExperiments,COLLECT(interactionDetectionMethod) as interactionDetectionMethods,COLLECT(dm_cvterm) as dm_cvterms,COLLECT(interactionType) as interactionTypes,COLLECT(it_cvterm) as it_cvterms,COLLECT(publication) as dm_publications,COLLECT(graphpublication) as publications ORDER BY interactorA.ac";
    public static final String INTERACTOR_PAIR_COUNT="MATCH (interactorA:GraphInteractor)<-[:interactorA]-(binaryIE:GraphBinaryInteractionEvidence)-[:interactorB]->(interactorB:GraphInteractor)  WHERE id(interactorA) < id(interactorB) AND  NOT (interactorA) = (interactorB) RETURN COUNT(DISTINCT(interactorA.ac+interactorB.ac))";
}
