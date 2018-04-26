package uk.ac.ebi.intact.graphdb.utils;

import org.neo4j.graphdb.Label;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphAlias;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphAnnotation;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

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
}
}
