package uk.ac.ebi.intact.graphdb.utils.cache;

import uk.ac.ebi.intact.graphdb.model.nodes.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by anjali on 24/04/18.
 */
public class GraphEntityCache {

    public static Map<String,GraphCvTerm> cvTermCacheMap =new TreeMap<String,GraphCvTerm>(String.CASE_INSENSITIVE_ORDER);
    public static HashMap<String,GraphXref> xrefCacheMap = new HashMap<String,GraphXref>();
    public static HashMap<String,GraphFeatureEvidence> featureCacheMap = new HashMap<String,GraphFeatureEvidence>();
    public static HashMap<String,GraphExperiment> experimentCacheMap = new HashMap<String,GraphExperiment>();
    public static HashMap<String,GraphVariableParameter> parameterValueCacheMap = new HashMap<String,GraphVariableParameter>();


}
