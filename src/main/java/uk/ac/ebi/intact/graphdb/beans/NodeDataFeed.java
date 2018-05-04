package uk.ac.ebi.intact.graphdb.beans;

/**
 * Created by anjali on 04/05/18.
 */
public class NodeDataFeed {

    private long graphId;
    private boolean isAlreadyCreated;

    public NodeDataFeed(long graphId,boolean isAlreadyCreated){
        this.graphId=graphId;
        this.isAlreadyCreated=isAlreadyCreated;
    }


    public long getGraphId() {
        return graphId;
    }

    public void setGraphId(long graphId) {
        this.graphId = graphId;
    }

    public boolean isAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setAlreadyCreated(boolean alreadyCreated) {
        isAlreadyCreated = alreadyCreated;
    }
}
