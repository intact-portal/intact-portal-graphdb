package uk.ac.ebi.intact.graphdb.utils;

import org.neo4j.graphdb.Label;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import uk.ac.ebi.intact.graphdb.model.nodes.*;

/**
 * Created by anjali on 25/04/18.
 */
public class CreationConfig {

    public static BatchInserter batchInserter;
    public static boolean createNatively;



public  static void createSchemaConstraint(){
    CommonUtility.createSchemaConstraint(GraphCvTerm.class,"shortName");
    CommonUtility.createSchemaConstraint(GraphAlias.class, "shortName");
    CommonUtility.createSchemaConstraint(GraphAnnotation.class,"shortName");
    CommonUtility.createSchemaConstraint(GraphXref.class,"identifier");
    CommonUtility.createSchemaConstraint(GraphBinaryInteractionEvidence.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphCausalRelationship.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphChecksum.class, "uniqueKey");
   // CommonUtility.createSchemaConstraint(GraphClusteredInteraction.class,"shortName");
    CommonUtility.createSchemaConstraint(GraphConfidence.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphExperiment.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphFeatureEvidence.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphInteractionEvidence.class, "ac");
    CommonUtility.createSchemaConstraint(GraphInteractor.class,"ac");
    CommonUtility.createSchemaConstraint(GraphNucleicAcid.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphOrganism.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphParticipantEvidence.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphPosition.class, "uniqueKey");
    CommonUtility.createSchemaConstraint(GraphProtein.class,"uniprotName");
    CommonUtility.createSchemaConstraint(GraphPublication.class,"pubmedIdStr");
    CommonUtility.createSchemaConstraint(GraphRange.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphResultingSequence.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphSource.class, "uniqueKey");
    CommonUtility.createSchemaConstraint(GraphStoichiometry.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphVariableParameter.class,"uniqueKey");
    CommonUtility.createSchemaConstraint(GraphVariableParameterValue.class,"uniqueKey");
    //CommonUtility.createSchemaConstraint(GraphVariableParameterValueSet.class,"shortName");
}
}
