package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.Interactor;
import uk.ac.ebi.intact.graphdb.model.relationships.Interaction;
import uk.ac.ebi.intact.graphdb.repositories.InteractorRepository;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 04/09/2014
 * Time: 17:43
 */
@Service
public class InteractorService {

    @Autowired
    InteractorRepository interactorRepository;

    private Map<String, Object> toD3Format(Collection<Interactor> interactors) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();
        int i = 0;
        Iterator<Interactor> result = interactors.iterator();
        while (result.hasNext()) {
            Interactor interactorB = result.next();
            nodes.add(map("accession", interactorB.getAccession(), "label", "interactorB"));
            int target = i;
            i++;
            for (Interaction interaction : interactorB.getInteractions()) {
                Map<String, Object> interactorA = map("accession", interaction.getInteractorA(), "label", "interactorA");
                int source = nodes.indexOf(interactorA);
                if (source == -1) {
                    nodes.add(interactorA);
                    source = i++;
                }
                rels.add(map("interactorA", source, "interactorB", target));
            }
        }
        return map("nodes", nodes, "links", rels);
    }

    private Map<String, Object> map(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object>  graph(int limit) {
        Collection<Interactor> result = interactorRepository.graph(limit);
        return toD3Format(result);
    }

}
