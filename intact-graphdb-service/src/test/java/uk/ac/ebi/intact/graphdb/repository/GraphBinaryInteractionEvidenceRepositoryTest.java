package uk.ac.ebi.intact.graphdb.repository;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.internal.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphBinaryInteractionEvidence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(readOnly = true)
public class GraphBinaryInteractionEvidenceRepositoryTest {

    @Autowired
    private GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository;

    @Test
    public void testGraphBinaryInteractionEvidenceRepositoryPagination() {
        int pageNumber = 0;
        int totalElements = 0;
        int pageSize = 100;
        int depth = 0;
        Page<GraphBinaryInteractionEvidence> page;

        do {
            page = graphBinaryInteractionEvidenceRepository.findAll(PageRequest.of(pageNumber, pageSize), depth);
            Assert.assertNotNull("Page is Null", page);
            totalElements = totalElements + page.getNumberOfElements();
            pageNumber++;
        } while (page.hasNext());

        Assert.assertEquals(pageNumber, page.getTotalPages());
        Assert.assertEquals(totalElements, page.getTotalElements());
        Assert.assertEquals(1534, totalElements);
        Assert.assertTrue(pageNumber > 1);
    }

    @Test
    @Ignore
    public void testCytoscapeAppQuery() {
        // null parameters check
        // with parameters check
        // interactor count check
        // interaction count check
        List<String> identifiers = new ArrayList<>();
        identifiers.add("Q9BZD4");
        identifiers.add("O14777");

        List<Integer> species = new ArrayList<>();
        species.add(9606);

        Iterable<Map<String, Object>> nodesIterable1 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(identifiers, null);
        Assert.assertEquals(8, Iterables.count(nodesIterable1));//152

        boolean interactorsPresent1 = false;
        Iterator<Map<String, Object>> iterator1 = nodesIterable1.iterator();
        try {
            while (iterator1.hasNext()) {
                Map<String, Object> map = iterator1.next();
                for (Map identifierMap : (Map[]) map.get("xrefs")) {
                    if (identifierMap.get("xref_id").equals("Q9BZD4") || identifierMap.get("xref_id").equals("O14777")) {
                        interactorsPresent1 = true;
                    }
                }
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Assert.assertTrue("Queried Interactors should have been present", interactorsPresent1);

        Iterable<Map<String, Object>> nodesIterable2 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(identifiers, species);
        Assert.assertEquals(8, Iterables.count(nodesIterable2));//141

        Map<String, Object> mapToBeTested = null;
        boolean interactorsPresent = false;
        Iterator<Map<String, Object>> iterator2 = nodesIterable2.iterator();
        try {
            while (iterator2.hasNext()) {
                Map<String, Object> map = iterator2.next();
                if (map.get("id").equals("EBI-949451")) {
                    mapToBeTested = map;
                }

                if (!map.get("species").equals("Homo sapiens")) {
                    Assert.assertTrue("Only Human species records were expected", false);
                }

                for (Map identifierMap : (Map[]) map.get("xrefs")) {
                    if (identifierMap.get("xref_id").equals("Q9BZD4") || identifierMap.get("xref_id").equals("O14777")) {
                        interactorsPresent = true;
                    }
                }
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        if (mapToBeTested == null) {
            Assert.assertTrue("An interactor with id 'EBI-949451' was expected", false);
        }
        Assert.assertTrue("Queried Interactors should have been present", interactorsPresent);
        Assert.assertTrue(mapToBeTested.get("preferred_id").equals("P07199"));
        Assert.assertTrue(mapToBeTested.get("species").equals("Homo sapiens"));
        Assert.assertTrue(mapToBeTested.get("taxId").equals(9606));
        Assert.assertTrue(mapToBeTested.get("label").equals("CENPB(P07199)"));
        Assert.assertTrue(mapToBeTested.get("type").equals("protein"));
        Assert.assertTrue(mapToBeTested.get("type_mi_dentifier").equals("MI:0326"));
        Assert.assertTrue(mapToBeTested.get("type_mod_identifier") == null);
        Assert.assertTrue(mapToBeTested.get("type_par_identifier") == null);
        Assert.assertTrue(mapToBeTested.get("interactor_name").equals("CENPB"));
        Assert.assertEquals(3, ((Map[]) mapToBeTested.get("xrefs")).length);

        Iterable<Map<String, Object>> nodesIterable3 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(null, species);
        Assert.assertEquals(473, Iterables.count(nodesIterable3));// 30179

        Iterator<Map<String, Object>> iterator3 = nodesIterable3.iterator();
        try {
            while (iterator3.hasNext()) {
                Map<String, Object> map = iterator3.next();

                if (!map.get("species").equals("Homo sapiens")) {
                    Assert.assertTrue("Only Human species records were expected", false);
                }
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        /*Iterable<Map<String, Object>> nodesIterable2 = graphBinaryInteractionEvidenceRepository.findBinaryInteractionsForCyAppNodes(null);
        Assert.assertEquals(112316, Iterables.count(nodesIterable2));*/

        Iterable<Map<String, Object>> edgesIterable = graphBinaryInteractionEvidenceRepository.findBinaryInteractionsForCyAppEdges();
        Assert.assertEquals(432, Iterables.count(edgesIterable));// 432
    }
}