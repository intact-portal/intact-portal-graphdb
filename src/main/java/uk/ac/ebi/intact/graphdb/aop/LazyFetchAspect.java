package uk.ac.ebi.intact.graphdb.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphCvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphDatabaseObject;
import uk.ac.ebi.intact.graphdb.services.AdvancedDatabaseObjectService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author Guilherme Viteri (gviteri@ebi.ac.uk)
 */
@Aspect
@Component
public class LazyFetchAspect {

    @Value("${aop.enabled}")
    private boolean enableAOP;

    @Autowired
    private AdvancedDatabaseObjectService advancedDatabaseObjectService;

    @Around("modelGetter()")
    public Object autoFetch(ProceedingJoinPoint pjp) throws Throwable {
        if (!enableAOP) return pjp.proceed();

         // Target is the whole object that originated this pointcut.
        GraphDatabaseObject databaseObject = (GraphDatabaseObject) pjp.getTarget();

         // Gathering information of the method we are invoking and it's being intercepted by AOP
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

         // Get the relationship that is annotated in the attribute
        Relationship relationship = getRelationship(method.getName(), databaseObject.getClass());
        if (relationship != null && !databaseObject.preventLazyLoading) { // && !databaseObject.isLoaded) {
             // Check whether the object has been loaded.
             // pjp.proceed() has the result of the invoked method.
            if (pjp.proceed() == null) {
                Long dbId = databaseObject.getGraphId();
                String setterMethod = method.getName().replaceFirst("get", "set");
                Class<?> methodReturnClazz = method.getReturnType();

                if (Collection.class.isAssignableFrom(methodReturnClazz)) {
                    ParameterizedType stringListType = (ParameterizedType)  method.getGenericReturnType();
                    Class<?> type = (Class<?>) stringListType.getActualTypeArguments()[0];
                    String clazz = type.getSimpleName();
                    // DatabaseObject.isLoaded only works for OUTGOING relationships
                    //noinspection EqualsBetweenInconvertibleTypes
                    boolean isLoaded = databaseObject.isLoaded && relationship.equals(Relationship.OUTGOING);
                    // querying the graph and fill the collection if it hasn't been fully loaded before
                    Collection<GraphDatabaseObject> lazyLoadedObjectAsCollection = isLoaded ? null : advancedDatabaseObjectService.findCollectionByRelationship(dbId, clazz, methodReturnClazz, relationship.direction(), relationship.type());
                    if (lazyLoadedObjectAsCollection == null) {
                        //If a set or list has been requested and is null, then we set empty collection to avoid requesting again
                        if (List.class.isAssignableFrom(methodReturnClazz)) lazyLoadedObjectAsCollection = new ArrayList<>();
                        if (Set.class.isAssignableFrom(methodReturnClazz)) lazyLoadedObjectAsCollection = new HashSet<>();
                    }
                    if (lazyLoadedObjectAsCollection != null) {
                        // invoke the setter in order to set the object in the target
                        databaseObject.getClass().getMethod(setterMethod, methodReturnClazz).invoke(databaseObject, lazyLoadedObjectAsCollection);
                        return lazyLoadedObjectAsCollection;
                    }
                }

                if (GraphDatabaseObject.class.isAssignableFrom(methodReturnClazz)) {
                    String clazz = methodReturnClazz.getSimpleName();
                    // querying the graph and fill the single object
                    GraphDatabaseObject lazyLoadedObject = advancedDatabaseObjectService.findByRelationship(dbId, clazz, relationship.direction(), relationship.type());
                    if (lazyLoadedObject != null) {
                        // invoke the setter in order to set the object in the target
                        databaseObject.getClass().getMethod(setterMethod, methodReturnClazz).invoke(databaseObject, lazyLoadedObject);
                        return lazyLoadedObject;
                    }
                }
            }
        }

        return pjp.proceed();
    }

    /**
     * AspectJ pointcut for all the getters that return a Collection of DatabaseObject
     * or instance of DatabaseObject.
     */
    /*@SuppressWarnings("SingleElementAnnotation")
    @Pointcut("execution(public java.util.Collection<java.lang.Object>+ uk.ac.ebi.intact.graphdb.model.nodes.*.get*(..))" +
            "|| execution(public java.lang.Object+ uk.ac.ebi.intact.graphdb.model.nodes.*.get*(..))")
    public void modelGetter() {
    }*/

    @SuppressWarnings("SingleElementAnnotation")
    @Pointcut("execution(* uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence.getExperiment())")
    public void modelGetter() {
    }

    /**
     * Method used to get the Relationship annotation on top of the
     * given attribute. This method looks for the annotation in the current class
     * and keep looking in the superclass.
     *
     * @return the Relationship annotation
     */
    private Relationship getRelationship(String methodName, Class<?> _clazz) {
        methodName = methodName.substring(3); // crop, remove 'get'
        char c[] = methodName.toCharArray();
        c[0] = Character.toLowerCase(c[0]); // lower the first char

        String attribute = new String(c);

         // Look up for the given attribute in the class and after superclasses.
        //noinspection ClassGetClass
        while (_clazz != null && !_clazz.getClass().equals(Object.class)) {
            for (Field field : _clazz.getDeclaredFields()) {
                if (field.getAnnotation(Relationship.class) != null) {
                    if (field.getName().equals(attribute)) {
                        return field.getAnnotation(Relationship.class);
                    }
                }
            }

            // Didn't find the field in the given class. Check the Superclass.
            _clazz = _clazz.getSuperclass();
        }

        return null;
    }

    @SuppressWarnings("unused")
    public Boolean getEnableAOP() {
        return enableAOP;
    }

    public void setEnableAOP(boolean enableAOP) {
        this.enableAOP = enableAOP;
    }

}