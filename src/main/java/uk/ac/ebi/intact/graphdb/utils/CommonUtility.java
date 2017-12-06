package uk.ac.ebi.intact.graphdb.utils;

import psidev.psi.mi.jami.model.Xref;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphXref;

import java.util.Collection;

/**
 * Created by anjali on 06/12/17.
 */
public class CommonUtility {

    public String extractAc(Collection<GraphXref> xrefs){
        String ac=null;

        for(Xref xref:xrefs){
            if(xref.getDatabase()!=null&&xref.getDatabase().getShortName()!=null&xref.getDatabase().getShortName().equals(Constants.INTACT_DB)){
                ac=xref.getId();
                break;
            }
        }

        return ac;
    }
}
