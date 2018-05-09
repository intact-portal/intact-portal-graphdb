package uk.ac.ebi.intact.graphdb.utils.cache;

import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphFeatureEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

import java.util.HashMap;

/**
 * Created by anjali on 24/04/18.
 */
public class GraphEntityCache {

    public static HashMap<String,GraphCvTerm> cvTermCacheMap = new HashMap<String,GraphCvTerm>();
    public static HashMap<String,GraphXref> xrefCacheMap = new HashMap<String,GraphXref>();
    public static HashMap<String,GraphFeatureEvidence> featureCacheMap = new HashMap<String,GraphFeatureEvidence>();

}