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

        Set<String> acs = new HashSet<>();
        acs.add("EBI-724102");
        acs.add("EBI-715849");

        boolean neighboursRequired = true;

        // With neighbours only

        Instant starts = Instant.now();
        Iterable<Map<String, Object>> edgesIterable1 = graphBinaryInteractionEvidenceRepository.findNetworkEdges(acs, neighboursRequired);
        Instant ends = Instant.now();
        Duration executionDuration = Duration.between(starts, ends);
        System.out.println("Total process with identifiers only took :" + executionDuration);
        Assert.assertTrue("Performance is low for querying with identifiers only", executionDuration.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable1);
        Assert.assertEquals(30, Iterables.count(edgesIterable1));// 432

        Iterable<Map<String, Object>> nodesIterable1 = graphInteractorRepository.findNetworkNodes(acs, neighboursRequired);
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
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.INTERACTION_DETECTION_METHOD), "anti tag coimmunoprecipitation");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.INTERACTION_DETECTION_METHOD_MI_IDENTIFIER), "MI:0007");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.PARTICIPANT_DETECTION_METHOD), "predetermined participant");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.PARTICIPANT_DETECTION_METHOD_MI_IDENTIFIER), "MI:0396");
            Assert.assertNull(mapToBeTested1.get(NetworkEdgeParamNames.EXPANSION_TYPE));
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.HOST_ORGANISM), "In vitro");
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.HOST_ORGANISM_TAX_ID), -1);
            Assert.assertEquals(mapToBeTested1.get(NetworkEdgeParamNames.MI_SCORE), 0.69);

            // source node

            Map<String, Object> sourceMap = (Map<String, Object>) mapToBeTested1.get(NetworkEdgeParamNames.SOURCE_NODE);
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.ID), "EBI-999909");
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE), "unspecified role");
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER), "MI:0499");
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_EXPERIMENTAL_ROLE), "prey");
            Assert.assertEquals(sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_EXPERIMENTAL_ROLE_MI_IDENTIFIER), "MI:0498");

            Iterable<Map<String, Object>> sourceFeatures = (Iterable<Map<String, Object>>) sourceMap.get(NetworkEdgeParamNames.PARTICIPANT_FEATURES);
            Assert.assertEquals(1, Iterables.count(sourceFeatures));
            Map<String, Object> sourceFeature = sourceFeatures.iterator().next();

            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_NAME), "region");
            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");
            Assert.assertNull(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_MOD_IDENTIFIER));
            Assert.assertNull(sourceFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_PAR_IDENTIFIER));
            Assert.assertEquals(sourceFeature.get(NetworkEdgeParamNames.FEATURE_AC), "EBI-1000300");

            // target node

            Map<String, Object> targetMap = (Map<String, Object>) mapToBeTested1.get(NetworkEdgeParamNames.TARGET_NODE);
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.ID), "EBI-715849");
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE), "unspecified role");
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.PARTICIPANT_BIOLOGICAL_ROLE_MI_IDENTIFIER), "MI:0499");
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.PARTICIPANT_EXPERIMENTAL_ROLE), "bait");
            Assert.assertEquals(targetMap.get(NetworkEdgeParamNames.PARTICIPANT_EXPERIMENTAL_ROLE_MI_IDENTIFIER), "MI:0496");

            Iterable<Map<String, Object>> targetFeatures = (Iterable<Map<String, Object>>) targetMap.get(NetworkEdgeParamNames.PARTICIPANT_FEATURES);
            Assert.assertEquals(2, Iterables.count(targetFeatures));
            Iterator<Map<String, Object>> targetFeaturesIterator = targetFeatures.iterator();
            boolean requiredFeaturePresent = false;

            while (targetFeaturesIterator.hasNext()) {
                Map<String, Object> targetFeature = targetFeaturesIterator.next();
                if (targetFeature.get(NetworkEdgeParamNames.FEATURE_NAME).equals("region")) {
                    Assert.assertEquals(targetFeature.get(NetworkEdgeParamNames.FEATURE_TYPE), "35s radiolabel");
                    Assert.assertEquals(targetFeature.get(NetworkEdgeParamNames.FEATURE_AC), "EBI-1000314");
                    Assert.assertEquals(targetFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_MI_IDENTIFIER), "MI:0371");
                    Assert.assertNull(targetFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_MOD_IDENTIFIER));
                    Assert.assertNull(targetFeature.get(NetworkEdgeParamNames.FEATURE_TYPE_PAR_IDENTIFIER));
                    requiredFeaturePresent = true;
                }
            }

            Assert.assertTrue(requiredFeaturePresent);

        } catch (Exception e) {
            Assert.assertTrue("Map with the key value was expected", false);
        }

        //Without Neighbours

        boolean neighboursRequired1 = false;

        Instant starts3 = Instant.now();
        Iterable<Map<String, Object>> edgesIterable4 = graphBinaryInteractionEvidenceRepository.findNetworkEdges(acs, neighboursRequired1);
        Instant ends3 = Instant.now();
        Duration executionDuration3 = Duration.between(starts3, ends3);
        System.out.println("Total process with species only took" + executionDuration3);
        Assert.assertTrue("Performance is low for querying with species only", executionDuration3.getSeconds() < 6);
        Assert.assertNotNull(edgesIterable4);
        Assert.assertEquals(3, Iterables.count(edgesIterable4));// 432

        Iterable<Map<String, Object>> nodesIterable4 = graphInteractorRepository.findNetworkNodes(acs, neighboursRequired1);
        Assert.assertEquals(2, Iterables.count(nodesIterable4));//152

        Set<String> interactorAcsFromEdgesQuery4 = new HashSet<>();
        Iterator<Map<String, Object>> edgeIterator4 = edgesIterable4.iterator();
        try {
            while (edgeIterator4.hasNext()) {
                Map<String, Object> map = edgeIterator4.next();

                String source = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.SOURCE_NODE)).get(NetworkEdgeParamNames.ID);
                String target = (String) ((Map<String, Object>) map.get(NetworkEdgeParamNames.TARGET_NODE)).get(NetworkEdgeParamNames.ID);

                if (source != null) {
                    interactorAcsFromEdgesQuery4.add(source);
                }
                if (target != null) {
                    interactorAcsFromEdgesQuery4.add(target);
                }
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Iterator<Map<String, Object>> nodeIterator4 = nodesIterable4.iterator();
        List<String> interactorAcsFromNodesQuery4 = new ArrayList<>();

        try {
            while (nodeIterator4.hasNext()) {
                Map<String, Object> map = nodeIterator4.next();
                interactorAcsFromNodesQuery4.add((String) map.get(NetworkEdgeParamNames.ID));
            }
        } catch (Exception e) {
            Assert.assertTrue("A map with the key value was expected", false);
        }

        Assert.assertEquals(interactorAcsFromEdgesQuery4.size(), interactorAcsFromNodesQuery4.size());

        for (String interactorAcFromEdgeQuery : interactorAcsFromEdgesQuery4) {
            if (!interactorAcsFromNodesQuery4.contains(interactorAcFromEdgeQuery)) {
                Assert.assertTrue("Node from edges query was expected to be in nodes from nodes query", false);
            }
        }


    }

    /*
     * For performance testing with only in neo4j server with whole database
     * */
    @Test
    @Ignore
    public void testSmallScalePerformanceOfCytoscapeAppNodesAndEdgesQuery() {
        Set<String> acs = new HashSet<>();
        acs.add("EBI-724102");
        acs.add("EBI-715849");
        acs.add("EBI-5323863");
        acs.add("EBI-366083");

        boolean neighboursRequired = true;

        Instant processStarted = Instant.now();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(2);

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> edgesIterable = graphBinaryInteractionEvidenceRepository.findNetworkEdges(acs, neighboursRequired);
                Instant ends = Instant.now();
                System.out.println("Cy App Edges retrieval took" + Duration.between(starts, ends));
            });

            //Thread.currentThread().sleep(3);

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> nodesIterable = graphInteractorRepository.findNetworkNodes(acs, neighboursRequired);
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

    /*
     * For performance testing with only in neo4j server with whole database
     * */
    @Test
    @Ignore
    public void testLargeScalePerformanceOfCytoscapeAppNodesAndEdgesQuery() {

        Set<String> acs = new HashSet<>();
        acs.add("EBI-724102");
        acs.add("EBI-715849");
        acs.add("EBI-5323863");
        acs.add("EBI-366083");
        //add more to have large graph

        boolean neighboursRequired = true;

        Instant processStarted = Instant.now();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(2);

            HashMap<String, Integer> dataRetrievalStatus = new HashMap<>();

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> edgesIterable = graphBinaryInteractionEvidenceRepository.findNetworkEdges(acs, neighboursRequired);
                Instant ends = Instant.now();
                System.out.println("Cy App Edges retrieval took" + Duration.between(starts, ends));
                if (edgesIterable != null) {
                    dataRetrievalStatus.put("edges", Iterables.count(edgesIterable));
                }
            });

            //Thread.currentThread().sleep(3);

            executor.execute(() -> {
                Instant starts = Instant.now();
                Iterable<Map<String, Object>> nodesIterable = graphInteractorRepository.findNetworkNodes(acs, neighboursRequired);
                Instant ends = Instant.now();
                System.out.println("Cy App Nodes retrieval took" + Duration.between(starts, ends));
                if (nodesIterable != null) {
                    dataRetrievalStatus.put("nodes", Iterables.count(nodesIterable));
                }
            });

            executor.shutdown();

            boolean finished = executor.awaitTermination(10, TimeUnit.MINUTES);
            executor.shutdownNow();
            Instant processEnds = Instant.now();
            Duration executionDuration = Duration.between(processStarted, processEnds);
            System.out.println("Total process took" + executionDuration);
            Assert.assertTrue(executionDuration.getSeconds() < 600);
            Assert.assertNotNull(dataRetrievalStatus.get("edges"));
            Assert.assertNotNull(dataRetrievalStatus.get("nodes"));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Exception", false);
        }
    }
}
