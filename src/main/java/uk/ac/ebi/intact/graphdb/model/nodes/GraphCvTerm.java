package uk.ac.ebi.intact.graphdb.model.nodes;


import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 05/09/2014
 * Time: 08:40
 */
@NodeEntity
public class GraphCvTerm extends DefaultCvTerm {

    @GraphId
    private Long id;


    public GraphCvTerm(String shortName) {
        super(shortName);
    }

    public GraphCvTerm(String shortName, String miIdentifier) {
        super(shortName, miIdentifier);
    }

    public GraphCvTerm(String shortName, String fullName, String miIdentifier) {
        super(shortName, fullName, miIdentifier);
    }

    public GraphCvTerm(String shortName, Xref ontologyId) {
        super(shortName, ontologyId);
    }

    public GraphCvTerm(String shortName, String fullName, Xref ontologyId) {
        super(shortName, fullName, ontologyId);
    }
}
