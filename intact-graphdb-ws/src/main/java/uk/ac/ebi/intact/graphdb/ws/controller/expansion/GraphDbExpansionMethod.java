package uk.ac.ebi.intact.graphdb.ws.controller.expansion;

import psidev.psi.mi.jami.binary.expansion.ComplexExpansionMethod;
import psidev.psi.mi.jami.exception.ComplexExpansionException;
import psidev.psi.mi.jami.factory.BinaryInteractionFactory;
import psidev.psi.mi.jami.model.CvTerm;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractionEvidence;

import java.util.Collection;
import java.util.Collections;

public class GraphDbExpansionMethod implements ComplexExpansionMethod<GraphInteractionEvidence, GraphBinaryInteractionEvidence> {

    @Override
    public CvTerm getMethod() {
        return null;
    }

    @Override
    public boolean isInteractionExpandable(GraphInteractionEvidence interaction) {
        if (interaction == null || interaction.getParticipants().isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public Collection<GraphBinaryInteractionEvidence> expand(GraphInteractionEvidence interaction) throws ComplexExpansionException {
        if (interaction instanceof GraphBinaryInteractionEvidence) {
            return Collections.singletonList((GraphBinaryInteractionEvidence) interaction);
        }
        return interaction.getBinaryInteractionEvidences();
    }

    @Override
    public BinaryInteractionFactory getBinaryInteractionFactory() {
        return null;
    }

    @Override
    public void setBinaryInteractionFactory(BinaryInteractionFactory factory) {

    }
}
