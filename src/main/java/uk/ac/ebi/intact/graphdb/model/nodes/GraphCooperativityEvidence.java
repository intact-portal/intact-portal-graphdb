package uk.ac.ebi.intact.graphdb.model.nodes;

import psidev.psi.mi.jami.model.CooperativityEvidence;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Publication;
import uk.ac.ebi.intact.graphdb.utils.CollectionAdaptor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anjali on 30/04/19.
 */
public class GraphCooperativityEvidence implements CooperativityEvidence {

    private GraphPublication publication;
    private Collection<GraphCvTerm> evidenceMethods;

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

    public Collection<GraphCvTerm> getEvidenceMethods() {
        if (this.evidenceMethods == null) {
            this.evidenceMethods = new ArrayList<GraphCvTerm>();
        }
        return this.evidenceMethods;
    }

    public void setEvidenceMethods(Collection<CvTerm> detectionMethods) {
        if (detectionMethods != null) {
            this.evidenceMethods = CollectionAdaptor.convertCvTermIntoGraphModel(detectionMethods);
        } else {
            this.evidenceMethods = new ArrayList<GraphCvTerm>();
        }
    }

}
