package uk.ac.ebi.intact.graphdb.model.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Transient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 * Created by anjali on 24/07/19.
 */
@NodeEntity
public class GraphDatabaseObject {

    @GraphId
    private Long graphId;

    @Transient
    public transient boolean preventLazyLoading = false;

    @Transient
    public transient boolean isLoaded = false;

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }


    @JsonIgnore
    public <T extends GraphDatabaseObject> T preventLazyLoading() {
        return preventLazyLoading(true);
    }

    @SuppressWarnings({"unchecked", "WeakerAccess", "UnusedReturnValue"})
    @JsonIgnore
    public <T extends GraphDatabaseObject> T preventLazyLoading(boolean preventLazyLoading) {
        if(this.preventLazyLoading == preventLazyLoading) return (T) this;

        this.preventLazyLoading = preventLazyLoading;

        //Here we go through all the getters and prevent LazyLoading for all the objects
        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("get")) continue;
            try {
                Class<?> methodReturnClazz = method.getReturnType();

                if (GraphDatabaseObject.class.isAssignableFrom(methodReturnClazz)) {
                    GraphDatabaseObject object = (GraphDatabaseObject) method.invoke(this);
                    if (object != null && object.preventLazyLoading != preventLazyLoading) {
                        object.preventLazyLoading(preventLazyLoading);
                    }
                }

                if (Collection.class.isAssignableFrom(methodReturnClazz)) {
                    ParameterizedType stringListType = (ParameterizedType) method.getGenericReturnType();
                    Class<?> type = (Class<?>) stringListType.getActualTypeArguments()[0];
                    String clazz = type.getSimpleName();
                    if (GraphDatabaseObject.class.isAssignableFrom(type)) {
                        Collection collection = (Collection) method.invoke(this);
                        if (collection != null) {
                            for (Object obj : collection) {
                                GraphDatabaseObject object = (GraphDatabaseObject) obj;
                                if (object != null && object.preventLazyLoading != preventLazyLoading) {
                                    object.preventLazyLoading(preventLazyLoading);
                                }
                            }
                        }
                    }
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return (T) this;
    }


}
