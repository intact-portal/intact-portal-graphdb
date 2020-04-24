package uk.ac.ebi.intact.graphdb.repository;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.internal.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.utils.NetworkEdgeParamNames;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by anjali on 24/04/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(readOnly = true)
public class GraphNetworkCompositeRepositoryTest {

    @Autowired
    private GraphBinaryInteractionEvidenceRepository graphBinaryInteractionEvidenceRepository;

    @Autowired
    private GraphInteractorRepository graphInteractorRepository;

    @Test
    public void testNetworkQuery() {

        Set<String> identifiers = new HashSet<>();
        identifiers.add("Q9BZD4");
        identifiers.add("O14777");

        Set<Integer> species = new HashSet<>();
        species.add(9606);

        // With identifiers only

        Instant starts = Instant.now();
        Iterable<Map<String, Object>> edgesIterable1 = graphBinaryInteractionEvidenceRepository.findNetworkEdges(identifiers, null);
        Instant ends = Instant.now();
        Duration executionDuration = Duration.between(starts, ends);
        System.out.println("Total process with identifiers only took :" + executionDuration);
        Assert.assertTrue("Performance is low for querying with identifiers only", executionDuration.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable1);
        Assert.assertEquals(30, Iterables.count(edgesIterable1));// 432

        Iterable<Map<String, Object>> nodesIterable1 = graphInteractorRepository.findNetworkNodes(identifiers, null);
        Assert.assertEquals(8, Iterables.count(nodesIterable1));//152

        Map<String, Object> mapToBeTested1 = null;
        Set<String> interactorAcsFromEdgesQuery1 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator1 = edgesIterable1.iterator();
        try {
            while (edgeIterator1.hasNext()) {
                Map<String, Object> map = edgeIterator1.next();
                if (map.get(NetworkEdgeParamNames.AC).equals("EBI-1000294")) {
                    mapToBeTested1 = map;
                }

                String source = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.SOURCE_NODE)).get(NetworkEdgeParamNames.ID);
                String target = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.TARGET_NODE)).get(NetworkEdgeParamNames.ID);

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
                interactorAcsFromNodesQuery1.add((String) map.get(NetworkEdgeParamNames.ID));
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
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.INTERACTION_TYPE), "physical association");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.INTERACTION_TYPE_MI_IDENTIFIER), "MI:0915");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.PUBMED_ID), "14699129");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.INTERACTION_DETECTION_METHOD), "anti tag coip");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.INTERACTION_DETECTION_METHOD_MI_IDENTIFIER), "MI:0007");
            Assert.assertNull(mapToBeTested1.get(NetworkEdgeParamNames.EXPANSION_TYPE));
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.HOST_ORGANISM), "In vitro");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.HOST_ORGANISM_TAX_ID), -1);
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.MI_SCORE), 0.69);

            // source node

            Map<String, Object> sourceMap = (Map<String, Object>) mapToBeTested1.get(NetworkEdgeParamNames.SOURCE_NODE);
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.ID), "EBI-999909");
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE), "unspecified role");
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER), "MI:0499");

            Iterable<Map<String, Object>> sourceFeatures = (Iterable<Map<String, Object>>) sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_FEATURES);
            Assert.assertEquals(Iterables.count(sourceFeatures), 1);
            Map<String, Object> sourceFeature = sourceFeatures.iterator().next();

            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_NAME), "region");
            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");

            // target node

            Map<String, Object> targetMap = (Map<String, Object>) mapToBeTested1.get(NetworkEdgeParamNames.TARGET_NODE);
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.ID), "EBI-715849");
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE), "unspecified role");
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER), "MI:0499");

            Iterable<Map<String, Object>> targetFeatures = (Iterable<Map<String, Object>>) targetMap.get(NetworkEdgeParamNames.PARTICIPANT_FEATURES);
            Assert.assertEquals(Iterables.count(targetFeatures), 2);
            Iterator<Map<String, Object>> targetFeaturesIterator = sourceFeatures.iterator();
            boolean requiredFeaturePresent = false;

            while (targetFeaturesIterator.hasNext()) {
                Map<String, Object> targetFeature = targetFeaturesIterator.next();
                if (targetFeature.get(NetworkEdgeParamNames.FEATURE_NAME).equals("region")) {
                    Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
                    Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");
                    requiredFeaturePresent = true;
                }
            }

            Assert.assertTrue(requiredFeaturePresent);

            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_NAME), "region");
            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");

        } catch (Exception e) {
            Assert.assertTrue("Map with the key value was expected", false);
        }

        // With identifiers and species

        Instant starts1 = Instant.now();
        Iterable<Map<String, Object>> edgesIterable2 = graphBinaryInteractionEvidenceRepository.findNetworkEdges(identifiers, species);
        Instant ends1 = Instant.now();
        Duration executionDuration1 = Duration.between(starts1, ends1);
        System.out.println("Total process with identifiers and species took" + executionDuration);
        Assert.assertTrue("Performance is low for querying with identifiers and species", executionDuration1.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable2);
        Assert.assertEquals(30, Iterables.count(edgesIterable2));// 432

        Iterable<Map<String, Object>> nodesIterable2 = graphInteractorRepository.findNetworkNodes(identifiers, species);
        Assert.assertEquals(8, Iterables.count(nodesIterable2));//152

        Set<String> interactorAcsFromEdgesQuery2 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator2 = edgesIterable2.iterator();
        try {
            while (edgeIterator2.hasNext()) {
                Map<String, Object> map = edgeIterator2.next();

                String source = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.SOURCE_NODE)).get(NetworkEdgeParamNames.ID);
                String target = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.TARGET_NODE)).get(NetworkEdgeParamNames.ID);

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
                interactorAcsFromNodesQuery2.add((String) map.get(NetworkEdgeParamNames.ID));
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
        Iterable<Map<String, Object>> edgesIterable3 = graphBinaryInteractionEvidenceRepository.findNetworkEdges(null, species);
        Instant ends2 = Instant.now();
        Duration executionDuration2 = Duration.between(starts2, ends2);
        System.out.println("Total process with species only took" + executionDuration);
        Assert.assertTrue("Performance is low for querying with species only", executionDuration2.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable3);
        Assert.assertEquals(1220, Iterables.count(edgesIterable3));// 432

        Iterable<Map<String, Object>> nodesIterable3 = graphInteractorRepository.findNetworkNodes(null, species);
        Assert.assertEquals(473, Iterables.count(nodesIterable3));//152

        Set<String> interactorAcsFromEdgesQuery3 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator3 = edgesIterable3.iterator();
        try {
            while (edgeIterator3.hasNext()) {
                Map<String, Object> map = edgeIterator3.next();

                String source = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.SOURCE_NODE)).get(NetworkEdgeParamNames.ID);
                String target = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.TARGET_NODE)).get(NetworkEdgeParamNames.ID);

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
                interactorAcsFromNodesQuery3.add((String) map.get(NetworkEdgeParamNames.ID));
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
    public void testPerformanceOfCytoscapeAppNodesAndEdgesQuery() {
        Set<String> identifiers = new HashSet<>();
        identifiers.add("Q9BZD4");
        identifiers.add("O14777");
        identifiers.add("Q5S007");
        identifiers.add("P04637");

        Set<Integer> species = new HashSet<>();
        species.add(9606);

        Instant processStarted = Instant.now();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(2);

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> edgesIterable = graphBinaryInteractionEvidenceRepository.findNetworkEdges(identifiers, species);
                Instant ends = Instant.now();
                System.out.println("Cy App Edges retrieval took" + Duration.between(starts, ends));
            });

            //Thread.currentThread().sleep(3);

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> nodesIterable = graphInteractorRepository.findNetworkNodes(identifiers, species);
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
            Assert.assertTrue("Exception", false);
        }
    }
}
