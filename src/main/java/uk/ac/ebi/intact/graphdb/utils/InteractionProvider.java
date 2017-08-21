package uk.ac.ebi.intact.graphdb.utils;


import uk.ac.ebi.intact.graphdb.error.GraphDbException;
import uk.ac.ebi.intact.graphdb.model.relationships.Interaction;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 11/11/2012
 * Time: 17:25
 */
public interface InteractionProvider {

    Iterator getInteractions() throws GraphDbException;



}
