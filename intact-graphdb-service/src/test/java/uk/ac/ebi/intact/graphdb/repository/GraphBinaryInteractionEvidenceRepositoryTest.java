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
    public void testCytoscapeAppEdgesQuery() {

        List<String> identifiers = new ArrayList<>();
        identifiers.add("Q9BZD4");
        identifiers.add("O14777");

        List<Integer> species = new ArrayList<>();
        species.add(9606);

        // With identifiers only

        Instant starts = Instant.now();
        Iterable<Map<String, Object>> edgesIterable1 = graphBinaryInteractionEvidenceRepository.findCyAppEdges(identifiers, null);
        Instant ends = Instant.now();
        Duration executionDuration = Duration.between(starts, ends);
        System.out.println("Total process with identifiers only took :" + executionDuration);
        Assert.assertTrue("Performance is low for querying with identifiers only", executionDuration.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable1);
        Assert.assertEquals(30, Iterables.count(edgesIterable1));// 432

        Iterable<Map<String, Object>> nodesIterable1 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(identifiers, null);
        Assert.assertEquals(8, Iterables.count(nodesIterable1));//152

        Map<String, Object> mapToBeTested1 = null;
        Set<String> interactorAcsFromEdgesQuery1 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator1 = edgesIterable1.iterator();
        try {
            while (edgeIterator1.hasNext()) {
                Map<String, Object> map = edgeIterator1.next();
                if (map.get(CyAppJsonEdgeParamNames.AC).equals("EBI-1000294")) {
                    mapToBeTested1 = map;
                }

                String source = (String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.SOURCE_NODE)).get(CyAppJsonEdgeParamNames.ID);
                String target = (String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.TARGET_NODE)).get(CyAppJsonEdgeParamNames.ID);

                if (source != null) {
                    interactorAcsFromEdgesQuery1.add(source);
                }
                if (target != null) {
                    interactorAcsFromEdgesQuery1.add(target);
                }

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

        try {
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.INTERACTION_TYPE), "physical association");
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.INTERACTION_TYPE_MI_IDENTIFIER), "MI:0915");
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.PUBMED_ID), "14699129");
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.INTERACTION_DETECTION_METHOD), "anti tag coip");
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.INTERACTION_DETECTION_METHOD_MI_IDENTIFIER), "MI:0007");
            Assert.assertNull(mapToBeTested1.get(CyAppJsonEdgeParamNames.EXPANSION_TYPE));
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.HOST_ORGANISM), "In vitro");
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.HOST_ORGANISM_TAX_ID), -1);
            Assert.assertEquals(mapToBeTested1.get(CyAppJsonEdgeParamNames.MI_SCORE), 0.69);

            // source node

            Map<String, Object> sourceMap = (Map<String, Object>) mapToBeTested1.get(CyAppJsonEdgeParamNames.SOURCE_NODE);
            Assert.assertEquals(sourceMap.get(CyAppJsonEdgeParamNames.ID), "EBI-999909");
            Assert.assertEquals(sourceMap.get(CyAppJsonEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE), "unspecified role");
            Assert.assertEquals(sourceMap.get(CyAppJsonEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER), "MI:0499");

            Iterable<Map<String, Object>> sourceFeatures = (Iterable<Map<String, Object>>) sourceMap.get(CyAppJsonEdgeParamNames.PARTICIPANT_FEATURES);
            Assert.assertEquals(Iterables.count(sourceFeatures), 1);
            Map<String, Object> sourceFeature = sourceFeatures.iterator().next();

            Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_NAME), "region");
            Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
            Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");

            // target node

            Map<String, Object> targetMap = (Map<String, Object>) mapToBeTested1.get(CyAppJsonEdgeParamNames.TARGET_NODE);
            Assert.assertEquals(targetMap.get(CyAppJsonEdgeParamNames.ID), "EBI-715849");
            Assert.assertEquals(targetMap.get(CyAppJsonEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE), "unspecified role");
            Assert.assertEquals(targetMap.get(CyAppJsonEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER), "MI:0499");

            Iterable<Map<String, Object>> targetFeatures = (Iterable<Map<String, Object>>) targetMap.get(CyAppJsonEdgeParamNames.PARTICIPANT_FEATURES);
            Assert.assertEquals(Iterables.count(targetFeatures), 2);
            Iterator<Map<String, Object>> targetFeaturesIterator = sourceFeatures.iterator();
            boolean requiredFeaturePresent = false;

            while (targetFeaturesIterator.hasNext()) {
                Map<String, Object> targetFeature = targetFeaturesIterator.next();
                if (targetFeature.get(CyAppJsonEdgeParamNames.FEATURE_NAME).equals("region")) {
                    Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
                    Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");
                    requiredFeaturePresent = true;
                }
            }

            Assert.assertTrue(requiredFeaturePresent);

            Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_NAME), "region");
            Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
            Assert.assertEquals(sourceFeature.get(CyAppJsonEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");

        } catch (Exception e) {
            Assert.assertTrue("Map with the key value was expected", false);
        }

        // With identifiers and species

        Instant starts1 = Instant.now();
        Iterable<Map<String, Object>> edgesIterable2 = graphBinaryInteractionEvidenceRepository.findCyAppEdges(identifiers, species);
        Instant ends1 = Instant.now();
        Duration executionDuration1 = Duration.between(starts1, ends1);
        System.out.println("Total process with identifiers and species took" + executionDuration);
        Assert.assertTrue("Performance is low for querying with identifiers and species", executionDuration1.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable2);
        Assert.assertEquals(30, Iterables.count(edgesIterable2));// 432

        Iterable<Map<String, Object>> nodesIterable2 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(identifiers, species);
        Assert.assertEquals(8, Iterables.count(nodesIterable2));//152

        Set<String> interactorAcsFromEdgesQuery2 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator2 = edgesIterable2.iterator();
        try {
            while (edgeIterator2.hasNext()) {
                Map<String, Object> map = edgeIterator2.next();

                String source = (String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.SOURCE_NODE)).get(CyAppJsonEdgeParamNames.ID);
                String target = (String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.TARGET_NODE)).get(CyAppJsonEdgeParamNames.ID);

                if (source != null) {
                    interactorAcsFromEdgesQuery2.add(source);
                }
                if (target != null) {
                    interactorAcsFromEdgesQuery2.add(target);
                }

            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Iterator<Map<String, Object>> nodeIterator2 = nodesIterable2.iterator();
        List<String> interactorAcsFromNodesQuery2 = new ArrayList<>();

        try {
            while (nodeIterator2.hasNext()) {
                Map<String, Object> map = nodeIterator2.next();
                interactorAcsFromNodesQuery2.add((String) map.get(CyAppJsonEdgeParamNames.ID));
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Assert.assertEquals(interactorAcsFromEdgesQuery2.size(), interactorAcsFromNodesQuery2.size());

        for (String interactorAcFromEdgeQuery : interactorAcsFromEdgesQuery2) {
            if (!interactorAcsFromNodesQuery2.contains(interactorAcFromEdgeQuery)) {
                Assert.assertTrue("Node from edges query was expected to be in nodes from nodes query", false);
            }
        }

        //With species only

        Instant starts2 = Instant.now();
        Iterable<Map<String, Object>> edgesIterable3 = graphBinaryInteractionEvidenceRepository.findCyAppEdges(null, species);
        Instant ends2 = Instant.now();
        Duration executionDuration2 = Duration.between(starts2, ends2);
        System.out.println("Total process with species only took" + executionDuration);
        Assert.assertTrue("Performance is low for querying with species only", executionDuration2.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable3);
        Assert.assertEquals(1220, Iterables.count(edgesIterable3));// 432

        Iterable<Map<String, Object>> nodesIterable3 = graphBinaryInteractionEvidenceRepository.findCyAppNodes(null, species);
        Assert.assertEquals(473, Iterables.count(nodesIterable3));//152

        Set<String> interactorAcsFromEdgesQuery3 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator3 = edgesIterable3.iterator();
        try {
            while (edgeIterator3.hasNext()) {
                Map<String, Object> map = edgeIterator3.next();

                String source = (String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.SOURCE_NODE)).get(CyAppJsonEdgeParamNames.ID);
                String target = (String) ((Map<String, Object>) map.get(CyAppJsonEdgeParamNames.TARGET_NODE)).get(CyAppJsonEdgeParamNames.ID);

                if (source != null) {
                    interactorAcsFromEdgesQuery3.add(source);
                }
                if (target != null) {
                    interactorAcsFromEdgesQuery3.add(target);
                }
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Iterator<Map<String, Object>> nodeIterator3 = nodesIterable3.iterator();
        List<String> interactorAcsFromNodesQuery3 = new ArrayList<>();

        try {
            while (nodeIterator3.hasNext()) {
                Map<String, Object> map = nodeIterator3.next();
                interactorAcsFromNodesQuery3.add((String) map.get(CyAppJsonEdgeParamNames.ID));
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Assert.assertEquals(interactorAcsFromEdgesQuery3.size(), interactorAcsFromNodesQuery3.size());

        for (String interactorAcFromEdgeQuery : interactorAcsFromEdgesQuery3) {
            if (!interactorAcsFromNodesQuery3.contains(interactorAcFromEdgeQuery)) {
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