package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Collection;

/**
 * Created by anjali on 24/04/20.
 */
public class NetworkNodeDetails {

    private Collection<Xref> xrefs;

    public NetworkNodeDetails(Collection<Xref> xrefs) {
        this.xrefs = xrefs;
    }

    public Collection<Xref> getXrefs() {
        return xrefs;
    }

    public void setXrefs(Collection<Xref> xrefs) {
        this.xrefs = xrefs;
    }
}
