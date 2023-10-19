package uk.ac.ebi.intact.graphdb.utils;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import uk.ac.ebi.intact.graphdb.utils.objectpool.CommonUtilityFactory;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by anjali on 29/11/17.
 */
public class Constants {

    public static final String IDENTITY = "identity";
    public static final String INTACT_DB = "intact";
    public static final String PUBMED_DB = "pubmed";
    public static final String PRIMARY_REFERENCE_QUALIFIER = "primary-reference";
    public static final String USED_IN_CLASS_TOPIC = "used-in-class";
    public static final ObjectPool<CommonUtility> COMMON_UTILITY_OBJECT_POOL = new GenericObjectPool<CommonUtility>(new CommonUtilityFactory(), new GenericObjectPoolConfig());

    public static final HashMap<String, Long> createdNodeIdMap = new HashMap<String, Long>();
    public static final HashSet<String> createdRelationShipList = new HashSet<String>();

    //for unique key generation
    public static final String FIELD_SEPARATOR = "_";
    public static final String LIST_SEPARATOR = ",";
    public static final String NOT_GENERATED_UNIQUE_KEY = "KNC";
    public static final String UNIQUE_KEY_NA = "UNIQUE_KEY_NA";
}
