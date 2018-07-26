package uk.ac.ebi.intact.graphdb.utils;

import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.jami.model.CvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphAlias;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by anjali on 26/07/18.
 */
public class GraphAliasUtils {
    /**
     * Remove all Alias having this method name/method id from the collection of aliases
     * @param aliases : the collection of Checksum
     * @param typeId : the method id to look for
     * @param typeName : the method name to look for
     */
    public static void removeAllAliasesWithType(Collection<GraphAlias> aliases, String typeId, String typeName){

        if (aliases != null){
            Iterator<GraphAlias> aliasIterator = aliases.iterator();

            while (aliasIterator.hasNext()){
                if (doesAliasHaveType(aliasIterator.next(), typeId, typeName)){
                    aliasIterator.remove();
                }
            }
        }
    }

    /**
     * To know if an alias have a specific type.
     * @param alias : the alias
     * @param typeId : alias type MI identifier
     * @param typeName : alias type name
     * @return true if the alias has the type with given name/identifier
     */
    public static boolean doesAliasHaveType(Alias alias, String typeId, String typeName){

        if (alias == null || (typeName == null && typeId == null)){
            return false;
        }

        CvTerm type = alias.getType();
        if (type == null){
            return false;
        }

        // we can compare identifiers
        if (typeId != null && type.getMIIdentifier() != null){
            // we have the same type id
            return type.getMIIdentifier().equals(typeId);
        }
        // we need to compare type names
        else if (typeName != null) {
            return typeName.equalsIgnoreCase(type.getShortName());
        }

        return false;
    }
}
