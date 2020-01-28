package uk.ac.ebi.intact.graphdb.utils.objectpool;


import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import uk.ac.ebi.intact.graphdb.utils.CommonUtility;

/**
 * Created by anjali on 06/12/17.
 */
public class CommonUtilityFactory  extends BasePooledObjectFactory<CommonUtility> {


    @Override
    public CommonUtility create() {
        return new CommonUtility();
    }

    /**
     * Use the default PooledObject implementation.
     */
    @Override
    public PooledObject<CommonUtility> wrap(CommonUtility buffer) {
        return new DefaultPooledObject<CommonUtility>(buffer);
    }

    /**
     * When an object is returned to the pool, clear the buffer.
     */
    @Override
    public void passivateObject(PooledObject<CommonUtility> pooledObject) {

    }

    // for all other methods, the no-op implementation
    // in BasePooledObjectFactory will suffice
}