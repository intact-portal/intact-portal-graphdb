package uk.ac.ebi.intact.graphdb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.repositories.GraphInteractorRepository;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: noedelta
 * Date: 04/09/2014
 * Time: 17:43
 */
@Service
public class GraphInteractorService {

    final private GraphInteractorRepository graphInteractorRepository;

    @Autowired
    public GraphInteractorService(GraphInteractorRepository graphInteractorRepository) {
        this.graphInteractorRepository = graphInteractorRepository;
    }

    private Map<String, Object> toD3Format(Collection<GraphInteractor> interactors) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();
        int i = 0;
        Iterator<GraphInteractor> result = interactors.iterator();
        while (result.hasNext()) {
            GraphInteractor interactorB = result.next();
            nodes.add(map("identifier", interactorB.getPreferredIdentifier().getDatabase(), "label", "interactorB"));
            int target = i;
            i++;
            for (GraphBinaryInteractionEvidence interaction : interactorB.getInteractions()) {
                Map<String, Object> interactorA = map("identifier", interaction.getInteractorA().getPreferredIdentifier().getId(), "label", "interactorA");
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
        Collection<GraphInteractor> result = graphInteractorRepository.graph(limit);
        return toD3Format(result);
    }

    public Page<GraphInteractor> findAll(Pageable page, int depth) {
        return graphInteractorRepository.findAll(page, depth);
    }

    public Optional<GraphInteractor> find(String id) {
        return graphInteractorRepository.findById(id);
    }

    public Optional<GraphInteractor> findWithDepth(String id,int depth) {
        return graphInteractorRepository.findById(id,depth);
    }

    public GraphInteractor findByAc(String ac) {
        return graphInteractorRepository.findByAc(ac);
    }

//    public List<Interactor> retrieveInteractors (String name) {
//        return this.interactorRepository.findByName(name);
//    }
}
