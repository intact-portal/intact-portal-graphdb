package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Collection;

/**
 * Created by anjali on 24/04/20.
 */
public class NetworkNodeDetails {

    private String id;
    private Collection<Xref> xrefs;
    private Collection<Alias> aliases;

    public NetworkNodeDetails(String id, Collection<Xref> xrefs, Collection<Alias> aliases) {
        this.xrefs = xrefs;
        this.aliases = aliases;
        this.id = id;
    }

    public Collection<Xref> getXrefs() {
        return xrefs;
    }

    public void setXrefs(Collection<Xref> xrefs) {
        this.xrefs = xrefs;
    }

    public Collection<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(Collection<Alias> aliases) {
        this.aliases = aliases;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
