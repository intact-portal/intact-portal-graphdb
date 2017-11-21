package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Interactor;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 05/05/2014
 * Time: 19:22
 */
@NodeEntity
public class Participant {

    @GraphId
    private Long id;

//    @Fetch
    private Interactor interactor;

    public Participant(Interactor interactor) {
        this.interactor = interactor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Interactor getInteractor() {
        return interactor;
    }

    public void setInteractor(Interactor interactor) {
        this.interactor = interactor;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", interactor=" + interactor +
                '}';
    }
}
