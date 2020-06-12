package uk.ac.ebi.intact.graphdb.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.internal.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.graphdb.model.nodes.GraphInteractor;
import uk.ac.ebi.intact.graphdb.utils.NetworkNodeParamNames;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(readOnly = true)
public class GraphInteractorRepositoryTest {

    @Autowired
    private GraphInteractorRepository graphInteractorRepository;

    @Test
    public void testGraphInteractorRepositoryPagination() {
        int pageNumber = 0;
        int totalElements = 0;
        int pageSize = 10;
        int depth = 0;
        Page<GraphInteractor> page;

        do {
            page = graphInteractorRepository.findAll(PageRequest.of(pageNumber, pageSize), depth);
            Assert.assertNotNull("Page is Null", page);
            totalElements = totalElements + page.getNumberOfElements();
            pageNumber++;
        } while (page.hasNext());

        Assert.assertEquals(pageNumber, page.getTotalPages());
        Assert.assertEquals(totalElements, page.getTotalElements());
        Assert.assertEquals(655, totalElements);
        Assert.assertTrue(pageNumber > 1);

    }

    @Test
    public void testCytoscapeAppNodesQuery() {

        Set<String> acs = new HashSet<>();
        acs.add("EBI-724102");
        acs.add("EBI-715849");

        boolean neighboursRequired = true;

        Iterable<Map<String, Object>> nodesIterable2 = graphInteractorRepository.findNetworkNodes(acs, neighboursRequired);
        Assert.assertEquals(8, Iterables.count(nodesIterable2));//141

        Map<String, Object> mapToBeTested = null;
        boolean interactorsPresent = false;
        Iterator<Map<String, Object>> iterator2 = nodesIterable2.iterator();
        try {
            while (iterator2.hasNext()) {
                Map<String, Object> map = iterator2.next();
                if (map.get(NetworkNodeParamNames.ID).equals("EBI-949451")) {
                    mapToBeTested = map;
                }

                if (!map.get(NetworkNodeParamNames.SPECIES).equals("Homo sapiens")) {
                    Assert.assertTrue("Only Human species records were expected", false);
                }

                for (Map identifierMap : (Map[]) map.get(NetworkNodeParamNames.IDENTIFIERS)) {
                    if (identifierMap.get(NetworkNodeParamNames.XREF_ID).equals("Q9BZD4") || identifierMap.get(NetworkNodeParamNames.XREF_ID).equals("O14777")) {
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
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.PREFERRED_ID), ("P07199"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.FULL_NAME), ("Major centromere autoantigen B"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.PREFERRED_ID_DB_NAME), ("uniprotkb"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.PREFERRED_ID_DB_MI), ("MI:0486"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.SPECIES), ("Homo sapiens"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.TAXID), (9606));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.LABEL), ("CENPB(P07199)"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.TYPE), ("protein"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.TYPE_MI_IDENTIFIER), ("MI:0326"));
        Assert.assertEquals(mapToBeTested.get(NetworkNodeParamNames.INTERACTOR_NAME), ("CENPB"));
        Assert.assertEquals(3, ((Map[]) mapToBeTested.get(NetworkNodeParamNames.IDENTIFIERS)).length);

        boolean identifierCheck = false;
        for (Map identifierMap : (Map[]) mapToBeTested.get(NetworkNodeParamNames.IDENTIFIERS)) {
            if (identifierMap.get(NetworkNodeParamNames.XREF_ID).equals("Q96EI4")) {
                identifierCheck = true;
                Assert.assertEquals("MI:0360", identifierMap.get(NetworkNodeParamNames.XREF_QUALIFIER_MI));
                Assert.assertEquals("secondary-ac", identifierMap.get(NetworkNodeParamNames.XREF_QUALIFIER_NAME));
                Assert.assertEquals("uniprotkb", identifierMap.get(NetworkNodeParamNames.XREF_DB_NAME));
                Assert.assertEquals("MI:0486", identifierMap.get(NetworkNodeParamNames.XREF_DB_MI));
                Assert.assertEquals("EBI-9222998", identifierMap.get(NetworkNodeParamNames.XREF_AC));
            }
        }

        Assert.assertTrue(identifierCheck);

        // Without Neighbours

        boolean neighboursRequired1 = false;

        Iterable<Map<String, Object>> nodesIterable4 = graphInteractorRepository.findNetworkNodes(acs, neighboursRequired1);
        Assert.assertEquals(2, Iterables.count(nodesIterable4));
    }
}