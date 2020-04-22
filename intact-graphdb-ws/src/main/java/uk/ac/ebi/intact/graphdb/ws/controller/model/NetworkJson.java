package uk.ac.ebi.intact.graphdb.ws.controller.model;

import java.util.Map;

/**
 * Created by anjali on 22/04/20.
 */
public class NetworkJson {

    private Iterable<Map<String, Object>> nodes;
    private Iterable<Map<String, Object>> edges;

    public Iterable<Map<String, Object>> getNodes() {
        return nodes;
    }

    public void setNodes(Iterable<Map<String, Object>> nodes) {
        this.nodes = nodes;
    }

    public Iterable<Map<String, Object>> getEdges() {
        return edges;
    }

    public void setEdges(Iterable<Map<String, Object>> edges) {
        this.edges = edges;
    }
}
