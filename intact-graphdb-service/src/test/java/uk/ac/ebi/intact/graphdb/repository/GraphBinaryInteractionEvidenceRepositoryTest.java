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
import uk.ac.ebi.intact.graphdb.utils.CyAppJsonEdgeParamNames;
import uk.ac.ebi.intact.graphdb.utils.CyAppJsonNodeParamNames;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    public void testCytoscapeAppNodesQuery() {
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
                for (Map identifierMap : (Map[]) map.get(CyAppJsonNodeParamNames.XREFS)) {
                    if (identifierMap.get(CyAppJsonNodeParamNames.XREF_ID).equals("Q9BZD4") || identifierMap.get(CyAppJsonNodeParamNames.XREF_ID).equals("O14777")) {
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
                if (map.get(CyAppJsonNodeParamNames.ID).equals("EBI-949451")) {
                    mapToBeTested = map;
                }

                if (!map.get(CyAppJsonNodeParamNames.SPECIES).equals("Homo sapiens")) {
                    Assert.assertTrue("Only Human species records were expected", false);
                }

                for (Map identifierMap : (Map[]) map.get(CyAppJsonNodeParamNames.XREFS)) {
                    if (identifierMap.get(CyAppJsonNodeParamNames.XREF_ID).equals("Q9BZD4") || identifierMap.get(CyAppJsonNodeParamNames.XREF_ID).equals("O14777")) {
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
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.PREFERRED_ID).equals("P07199"));
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.SPECIES).equals("Homo sapiens"));
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.TAXID).equals(9606));
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.LABEL).equals("CENPB(P07199)"));
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.TYPE).equals("protein"));
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.TYPE_MI_IDENTIFIER).equals("MI:0326"));
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.TYPE_MOD_IDENTIFIER) == null);
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.TYPE_PAR_IDENTIFIER) == null);
        Assert.assertTrue(mapToBeTested.get(CyAppJsonNodeParamNames.INTERACTOR_NAME).equals("CENPB"));
        Assert.assertEquals(3, ((Map[]) mapToBeTested.get(CyAppJsonNodeParamNames.XREFS)).length);

        Iterable<Map<String, Object>> nodesIterable3 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(null, species);
        Assert.assertEquals(473, Iterables.count(nodesIterable3));// 30179

        Iterator<Map<String, Object>> iterator3 = nodesIterable3.iterator();
        try {
            while (iterator3.hasNext()) {
                Map<String, Object> map = iterator3.next();

                if (!map.get(CyAppJsonNodeParamNames.SPECIES).equals("Homo sapiens")) {
                    Assert.assertTrue("Only Human species records were expected", false);
                }
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }
    }

    @Test
    @Ignore
    public void testCytoscapeAppEdgesQuery() {

        List<String> identifiers = new ArrayList<>();
        identifiers.add("Q9BZD4");
        identifiers.add("O14777");

        List<Integer> species = new ArrayList<>();
        species.add(9606);
        Instant starts = Instant.now();
        Iterable<Map<String, Object>> edgesIterable1 = graphBinaryInteractionEvidenceRepository.findCyAppEdges(identifiers, null);
        Instant ends = Instant.now();
        Duration executionDuration = Duration.between(starts, ends);
        System.out.println("Total process took" + executionDuration);
        Assert.assertTrue(executionDuration.getSeconds() < 5);
        Assert.assertNotNull(edgesIterable1);
        Assert.assertEquals(30, Iterables.count(edgesIterable1));// 432

        Iterable<Map<String, Object>> nodesIterable1 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(identifiers, null);
        Assert.assertEquals(8, Iterables.count(nodesIterable1));//152

        Map<String, Object> mapToBeTested1 = null;
        Set<String> interactorAcsFromEdgesQuery1 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator2 = edgesIterable1.iterator();
        try {
            while (edgeIterator2.hasNext()) {
                Map<String, Object> map = edgeIterator2.next();
                if (map.get(CyAppJsonEdgeParamNames.AC).equals("EBI-949451")) {
                    mapToBeTested1 = map;
                }


                interactorAcsFromEdgesQuery1.add((String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.SOURCE)).get(CyAppJsonEdgeParamNames.SOURCE));
                interactorAcsFromEdgesQuery1.add((String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.TARGET)).get(CyAppJsonEdgeParamNames.TARGET));

            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Iterator<Map<String, Object>> nodeIterator1 = nodesIterable1.iterator();
        List<String> interactorAcsFromNodesQuery1 = new ArrayList<>();

        try {
            while (nodeIterator1.hasNext()) {
                Map<String, Object> map = nodeIterator1.next();
                interactorAcsFromNodesQuery1.add((String) map.get(CyAppJsonEdgeParamNames.ID));
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Assert.assertEquals(interactorAcsFromEdgesQuery1.size(), interactorAcsFromNodesQuery1.size());

        for (String interactorAcFromEdgeQuery : interactorAcsFromEdgesQuery1) {
            if (!interactorAcsFromNodesQuery1.contains(interactorAcFromEdgeQuery)) {
                Assert.assertTrue("Node from edges query was expected to be in nodes from nodes query", false);
            }
        }


    }

    /*
    * For performance testing with only in neo4j server with whole database
    * */
    @Test
    @Ignore
    public void testCytoscapeAppNodesAndEdgesQyery() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add("Q9BZD4");
        identifiers.add("O14777");
        identifiers.add("Q5S007");
        identifiers.add("P04637");

        List<Integer> species = new ArrayList<>();
        species.add(9606);

        Instant processStarted = Instant.now();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(2);

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> edgesIterable = graphBinaryInteractionEvidenceRepository.findCyAppEdges(identifiers, species);
                Instant ends = Instant.now();
                System.out.println("Cy App Edges retrieval took" + Duration.between(starts, ends));
            });

            //Thread.currentThread().sleep(3);

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> nodesIterable = graphBinaryInteractionEvidenceRepository.findCyAppNodes(identifiers, species);
                Instant ends = Instant.now();
                System.out.println("Cy App Nodes retrieval took" + Duration.between(starts, ends));
            });

            executor.shutdown();

            boolean finished = executor.awaitTermination(7, TimeUnit.MINUTES);
            Instant processEnds = Instant.now();
            Duration executionDuration = Duration.between(processStarted, processEnds);
            System.out.println("Total process took" + executionDuration);
            Assert.assertTrue(executionDuration.getSeconds() < 10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}