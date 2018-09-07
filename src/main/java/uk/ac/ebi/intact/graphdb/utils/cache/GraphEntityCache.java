package uk.ac.ebi.intact.graphdb.utils.cache;

import uk.ac.ebi.intact.graphdb.model.nodes.*;

import java.util.HashMap;

/**
 * Created by anjali on 24/04/18.
 */
public class GraphEntityCache {

    public static HashMap<String,GraphCvTerm> cvTermCacheMap = new HashMap<String,GraphCvTerm>();
    public static HashMap<String,GraphXref> xrefCacheMap = new HashMap<String,GraphXref>();
    public static HashMap<String,GraphFeature> featureCacheMap = new HashMap<String,GraphFeature>();
    public static HashMap<String,GraphExperiment> experimentCacheMap = new HashMap<String,GraphExperiment>();
    public static HashMap<String,GraphVariableParameter> parameterValueCacheMap = new HashMap<String,GraphVariableParameter>();

}
