package uk.ac.ebi.intact.graphdb.utils;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    /*Common BinaryInteractionEvidences of any two interactors*/
 //   public static final String COMM_NEIGH_OF_INTOR="MATCH (interactorA:GraphInteractor)--(binaryIE:GraphBinaryInteractionEvidence)--(interactorB:GraphInteractor) WHERE NOT (interactorA) = (interactorB) RETURN interactorA,COLLECT(binaryIE) as binaryInteractionEvidences,interactorB";
    public static final String COMM_NEIGH_OF_INTOR=  "MATCH (interactorA:GraphInteractor)--(binaryIE:GraphInteractionEvidence)--(interactorB:GraphInteractor) \n" +
            "MATCH (binaryIE:GraphInteractionEvidence) -[experiment] -(graphExperiment:GraphExperiment)-[interactionDetectionMethod:INTERACTION_DETECTION_METHOD]-(dm_cvterm:GraphCvTerm)\n" +
            "MATCH (binaryIE:GraphInteractionEvidence) - [interactionType:INTERACTION_TYPE] - (it_cvterm:GraphCvTerm)\n" +
            "WHERE NOT (interactorA) = (interactorB) RETURN interactorA,COLLECT(binaryIE) as interactions,interactorB,experiment,graphExperiment,interactionDetectionMethod,dm_cvterm,interactionType,it_cvterm";
}
