package uk.ac.ebi.intact.graphdb.ws.controller;


import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static uk.ac.ebi.intact.graphdb.ws.controller.model.InteractionExportFormat.*;

/**
 * Created by anjali on 26/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ExportController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private InteractionSearchService interactionSearchService;

    @Before
    public void setUp() throws Exception {

        String query = "EBI-10043615"; //publication
        String[] acs = {"EBI-10043643","EBI-10043649","EBI-10043655","EBI-10043691","EBI-10043699"};
        boolean batchSearch = false;
        Set<String> interactorSpeciesFilter = null;
        Set<String> interactorTypesFilter = null;
        Set<String> interactionDetectionMethodsFilter = null;
        Set<String> interactionTypesFilter = null;
        Set<String> interactionHostOrganismsFilter = null;
        boolean isNegativeFilter = false;
        boolean mutationFilter = false;
        double minMiScore = 0.0;
        double maxMiScore = 1.0;
        boolean intraSpeciesFilter = false;
        Set<Integer> binaryInteractionIdFilter = null;
        Set<String> interactorAcFilter = null;
        Pageable page = PageRequest.of(0,100);

        //Mock the searchInteractionService
        ArrayList<SearchInteraction> interactionIdentifiers = new ArrayList<>();
        for (String ac : acs) {
            SearchInteraction interaction = new SearchInteraction();
            interaction.setAc(ac);
            interaction.setBinaryInteractionId(0);
            interactionIdentifiers.add(interaction);
        }
        Page<SearchInteraction> result = new PageImpl<>(interactionIdentifiers);

        doReturn(5L).when(interactionSearchService).countInteractionResult(query, batchSearch, interactorSpeciesFilter,
                interactorTypesFilter, interactionDetectionMethodsFilter, interactionTypesFilter,
                interactionHostOrganismsFilter, isNegativeFilter, mutationFilter, minMiScore, maxMiScore, intraSpeciesFilter,
                binaryInteractionIdFilter, interactorAcFilter);


//        given(interactionSearchService.countInteractionResult(query, batchSearch, interactorSpeciesFilter,
//                interactorTypeFilter, interactionDetectionMethodFilter, interactionTypeFilter,
//                interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, intraSpeciesFilter,
//                binaryInteractionIdFilter, interactorAcFilter)).willReturn(5L);

        doReturn(result).when(interactionSearchService).findInteractionIdentifiers(query, batchSearch, interactorSpeciesFilter,
                interactorTypesFilter, interactionDetectionMethodsFilter, interactionTypesFilter,
                interactionHostOrganismsFilter, isNegativeFilter, mutationFilter, minMiScore, maxMiScore, intraSpeciesFilter,
                binaryInteractionIdFilter, interactorAcFilter, page);

//        given(interactionSearchService.findInteractionIdentifiers(query, batchSearch, interactorSpeciesFilter,
//                interactorTypeFilter, interactionDetectionMethodFilter, interactionTypeFilter,
//                interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, intraSpeciesFilter,
//                binaryInteractionIdFilter, interactorAcFilter, page)).willReturn(result);
    }

    @Test
    public void contexLoads() {
        assertNotNull(controller);
    }

    @Test
    public void exportInteractionAsMITAB25() {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {
//            try {

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miTab25.getFormat());

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("text", response.getHeaders().getContentType().getType());
            assertEquals("plain", response.getHeaders().getContentType().getSubtype());

            System.out.println(response.getBody());
            //TODO Add XML comparison


//            } catch (IOException e) {
//                System.err.println("Exception while connecting to Graph DB Web Service");
//                e.printStackTrace();
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//                System.err.println("Exception while comparing json");
//            } catch (ParseException parseException) {
//                parseException.printStackTrace();
//                System.err.println("Exception while parsing expectedJsonFile");
//            }
        }
    }

    @Test
    public void exportInteractionAsMITAB26() {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {
//            try {

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miTab26.getFormat());

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("text", response.getHeaders().getContentType().getType());
            assertEquals("plain", response.getHeaders().getContentType().getSubtype());

            System.out.println(response.getBody());

            //TODO Add XML comparison


//            } catch (IOException e) {
//                System.err.println("Exception while connecting to Graph DB Web Service");
//                e.printStackTrace();
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//                System.err.println("Exception while comparing json");
//            } catch (ParseException parseException) {
//                parseException.printStackTrace();
//                System.err.println("Exception while parsing expectedJsonFile");
//            }
        }
    }

    @Test
    public void exportInteractionAsMITAB27() {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {
//            try {

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miTab27.getFormat());

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("text", response.getHeaders().getContentType().getType());
            assertEquals("plain", response.getHeaders().getContentType().getSubtype());

            System.out.println(response.getBody());

            //TODO Add XML comparison


//            } catch (IOException e) {
//                System.err.println("Exception while connecting to Graph DB Web Service");
//                e.printStackTrace();
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//                System.err.println("Exception while comparing json");
//            } catch (ParseException parseException) {
//                parseException.printStackTrace();
//                System.err.println("Exception while parsing expectedJsonFile");
//            }
        }
    }

    @Test
    public void exportInteractionAsMIJson() {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {
            try {

                System.out.println("Test interaction with ac: " + interactionAc);

                ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                        "/export/interaction/" + interactionAc, String.class);

                assertNotNull("Response is null", response);
                assertNotNull("Response status line is null", response.getStatusCode());
                assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

                assertNotNull(response.getHeaders().getContentType());
                assertEquals("application", response.getHeaders().getContentType().getType());
                assertEquals("json", response.getHeaders().getContentType().getSubtype());

                JSONParser parser = new JSONParser();
                //Use JSONObject for simple JSON and JSONArray for array of JSON.
                JSONObject data = (JSONObject) parser.parse(
                        new FileReader("./src/test/resources/json/" + interactionAc + "_mi-json.json"));//path to the JSON file.
                String expectedJsonString = data.toJSONString();


                JSONAssert.assertEquals(
                        expectedJsonString, response.getBody(), JSONCompareMode.LENIENT);

                // use below if you need to ignore fields
            /*JSONAssert.assertEquals(expectedJsonString, actualJsonString,
                    new CustomComparator(JSONCompareMode.LENIENT,

                            new Customization("*.participants[*].id", (o1, o2) -> true)

                    ));*/
                /*ArrayValueMatcher<Object> arrValMatch = new ArrayValueMatcher<>(new CustomComparator(
                        JSONCompareMode.LENIENT,
                        new Customization("data[*].participants[*].id", (o1, o2) -> true)));

                Customization arrayValueMatchCustomization = new Customization("data[*].participants", arrValMatch);
                CustomComparator customArrayValueComparator = new CustomComparator(
                        JSONCompareMode.LENIENT,
                        arrayValueMatchCustomization);
                JSONAssert.assertEquals(expectedJsonString, actualJsonString, customArrayValueComparator);*/

            } catch (IOException e) {
                System.err.println("Exception while connecting to Graph DB Web Service");
                e.printStackTrace();
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
                System.err.println("Exception while comparing json");
            } catch (ParseException parseException) {
                parseException.printStackTrace();
                System.err.println("Exception while parsing expectedJsonFile");
            }
        }
    }

    @Test
    public void exportInteractionAsMIXML25() {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {
//            try {

                System.out.println("Test interaction with ac: " + interactionAc);

                ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                        "/export/interaction/" + interactionAc + "?format={format}", String.class, miXML25.getFormat());

                assertNotNull("Response is null", response);
                assertNotNull("Response status line is null", response.getStatusCode());
                assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

                assertNotNull(response.getHeaders().getContentType());
                assertEquals("application", response.getHeaders().getContentType().getType());
                assertEquals("xml", response.getHeaders().getContentType().getSubtype());

                //TODO Add XML comparison


//            } catch (IOException e) {
//                System.err.println("Exception while connecting to Graph DB Web Service");
//                e.printStackTrace();
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//                System.err.println("Exception while comparing json");
//            } catch (ParseException parseException) {
//                parseException.printStackTrace();
//                System.err.println("Exception while parsing expectedJsonFile");
//            }
        }
    }

    @Test
    public void exportInteractionAsMIXML30() {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {
//            try {

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miXML30.getFormat());

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("application", response.getHeaders().getContentType().getType());
            assertEquals("xml", response.getHeaders().getContentType().getSubtype());

            //TODO Add XML comparison


//            } catch (IOException e) {
//                System.err.println("Exception while connecting to Graph DB Web Service");
//                e.printStackTrace();
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//                System.err.println("Exception while comparing json");
//            } catch (ParseException parseException) {
//                parseException.printStackTrace();
//                System.err.println("Exception while parsing expectedJsonFile");
//            }
        }
    }

    @Test
    public void exportInteractionsAsMITAB25() throws IOException {

        String query = "EBI-10043615"; //publication

        System.out.println("Test interaction with query: " + query);

        ResponseEntity<String>  response = restTemplate.getForEntity("http://localhost:" + port +
                "/export/interaction/list?query=" + query + "&format={format}", String.class, miTab25.getFormat());

        assertNotNull("Response is null", response);
        assertNotNull("Response status line is null", response.getStatusCode());
        assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

        assertNotNull(response.getHeaders().getContentType());
        assertEquals("text", response.getHeaders().getContentType().getType());
        assertEquals("plain", response.getHeaders().getContentType().getSubtype());

        ContentDisposition disposition = response.getHeaders().getContentDisposition();
        assertNotNull(disposition);
        String fileName = disposition.getFilename();
        assertNotNull(fileName);

        String file = ExportControllerTest.class.getResource("/mitab/EBI-10043615-mitab25.txt").getFile();
        assertEquals( new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8), response.getBody());
    }

    @Test
    public void export100InteractionsAsMITAB25() {

    }

    @Test
    public void export1000InteractionsAsMITAB25() {

    }

    @Test
    public void export10InteractionsAsMIXML25() {

    }

    @Test
    public void export100InteractionsAsMIXML25() {

    }

    @Test
    public void export1000InteractionsAsMIXML25() {

    }

    public boolean areFilesEqual (File file1, File file2) throws IOException {

        BufferedReader reader1 = new BufferedReader(new FileReader(file1));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));

        String line1 = reader1.readLine();
        String line2 = reader2.readLine();

        boolean isEqual = true;

        while (line1 != null && line2 != null && isEqual){
            isEqual = line1.equals(line2);
            line1 = reader1.readLine();
            line2 = reader2.readLine();
        }

        return isEqual;
    }
}
