package uk.ac.ebi.intact.graphdb.utils;


import uk.ac.ebi.intact.graphdb.error.GraphDbException;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 11/11/2012
 * Time: 17:25
 */
public interface InteractionProvider {

    Iterator getInteractions() throws GraphDbException;

}
