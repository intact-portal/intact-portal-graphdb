package uk.ac.ebi.intact.graphdb.utils;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    /*Common BinaryInteractionEvidences of any two interactors*/
 //   public static final String COMM_NEIGH_OF_INTOR="MATCH (interactorA:GraphInteractor)--(binaryIE:GraphBinaryInteractionEvidence)--(interactorB:GraphInteractor) WHERE NOT (interactorA) = (interactorB) RETURN interactorA,COLLECT(binaryIE) as binaryInteractionEvidences,interactorB";
    public static final String COMM_NEIGH_OF_INTOR=  "MATCH (interactorA:GraphInteractor)--(binaryIE:GraphInteractionEvidence)--(interactorB:GraphInteractor) WITH  COLLECT(binaryIE) as interactions,interactorA,interactorB UNWIND interactions as interaction MATCH (interaction:GraphInteractionEvidence) -[experiment] -(graphExperiment:GraphExperiment)-[interactionDetectionMethod:INTERACTION_DETECTION_METHOD]-(dm_cvterm:GraphCvTerm) ,(interaction:GraphInteractionEvidence) - [interactionType:INTERACTION_TYPE] - (it_cvterm:GraphCvTerm) WHERE id(interactorA) < id(interactorB) AND  NOT (interactorA) = (interactorB) RETURN interactorA,interactorB,COLLECT(interaction) as interactions,collect(experiment) as experiments,collect(graphExperiment) as graphExperiments,collect(interactionDetectionMethod) as interactionDetectionMethods,collect(dm_cvterm) as dm_cvterms,collect(interactionType) as interactionTypes,collect(it_cvterm) as it_cvterms";
}
