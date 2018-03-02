package uk.ac.ebi.intact.graphdb.utils;

/**
 * Created by anjali on 01/03/18.
 */
public class CypherQueries {

    /*Common BinaryInteractionEvidences of any two interactors*/
    public static final String COMM_NEIGH_OF_INTOR="MATCH (interactorA:GraphInteractor)--(binaryIE:GraphBinaryInteractionEvidence)--(interactorB:GraphInteractor) WHERE NOT (interactorA) = (interactorB) RETURN interactorA,COLLECT(binaryIE) as binaryInteractionEvidences,interactorB";
}
