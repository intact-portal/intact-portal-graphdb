package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.ModelledConfidence;
import psidev.psi.mi.jami.model.Publication;

/**
 * Created by anjali on 30/04/19.
 */
public class GraphModelledConfidence extends GraphConfidence implements ModelledConfidence {

    private GraphPublication publication;

    public Publication getPublication() {
        return this.publication;
    }

    public void setPublication(Publication publication) {
        if (publication != null) {
            if (publication instanceof GraphPublication) {
                this.publication = (GraphPublication) publication;
            } else {
                this.publication = new GraphPublication(publication);
            }
        } else {
            this.publication = null;
        }
    }
}
