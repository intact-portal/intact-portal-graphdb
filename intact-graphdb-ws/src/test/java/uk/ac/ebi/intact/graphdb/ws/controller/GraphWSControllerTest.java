package uk.ac.ebi.intact.graphdb.ws.controller;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.ac.ebi.intact.graphdb.ws.controller.model.*;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static uk.ac.ebi.intact.graphdb.ws.controller.model.InteractionExportFormat.*;

/**
 * Created by anjali on 26/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphWSControllerTest {

    // Noe: For the time being I couldn't find a way to separate the tests of different controllers in different classes
    // because the embedded neo4j driver doesn't stop between classed of the test suite and the lock of the database exits
    // because it is actually running. Investigate further a way to stop the diver as pointing here:
    // https://github.com/spring-projects/spring-data-neo4j/issues/1879
    //
    @LocalServerPort
    private int port;

    @Autowired
    private InteractionController interactionController;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private InteractionSearchService interactionSearchService;

    @Before
    public void setUp() {

        String query = "EBI-10043615"; //publication
        String[] acs = {"EBI-10043643", "EBI-10043649", "EBI-10043655", "EBI-10043691", "EBI-10043699"};
        long[] binaryIds = {39572, 39606, 39617, 39669, 39686};

        boolean batchSearch = false;
        Set<String> interactorSpeciesFilter = null;
        Set<String> interactorTypeFilter = null;
        Set<String> interactionDetectionMethodFilter = null;
        Set<String> interactionTypeFilter = null;
        Set<String> interactionHostOrganismFilter = null;
        boolean negativeFilter = false;
        boolean mutationFilter = false;
        boolean expansionFilter = false;
        double minMiScore = 0.0;
        double maxMiScore = 1.0;
        boolean interSpecies = false;
        Set<Long> binaryInteractionIds = null;
        Set<String> interactorAcs = null;
        Pageable page = PageRequest.of(0,100);

        //Mock the searchInteractionService
        ArrayList<SearchInteraction> interactionIdentifiers = new ArrayList<>();
        for (int i = 0; i < acs.length; i++) {
            SearchInteraction interaction = new SearchInteraction();
            interaction.setAc(acs[i]);
            interaction.setBinaryInteractionId(binaryIds[i]);
            interactionIdentifiers.add(interaction);
        }


        Page<SearchInteraction> result = new PageImpl<>(interactionIdentifiers);

        doReturn(5L).when(interactionSearchService).countInteractionResult(query, batchSearch, interactorSpeciesFilter,
                interactorTypeFilter, interactionDetectionMethodFilter, interactionTypeFilter,
                interactionHostOrganismFilter, negativeFilter, mutationFilter, expansionFilter, minMiScore, maxMiScore, interSpecies,
                binaryInteractionIds, interactorAcs);

        doReturn(result).when(interactionSearchService).findInteractionIdentifiers(query, batchSearch, interactorSpeciesFilter,
                interactorTypeFilter, interactionDetectionMethodFilter, interactionTypeFilter,
                interactionHostOrganismFilter, negativeFilter, mutationFilter, expansionFilter, minMiScore, maxMiScore, interSpecies,
                binaryInteractionIds, interactorAcs, page);

    }

    @Test
    public void contexLoads() {
        assertNotNull(interactionController);
    }

    @Test
    public void exportInteractionAsMITAB25() throws IOException {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miTab25);

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("text", response.getHeaders().getContentType().getType());
            assertEquals("plain", response.getHeaders().getContentType().getSubtype());

            ContentDisposition disposition = response.getHeaders().getContentDisposition();
            assertNotNull(disposition);

            // System.out.println(response.getBody());
            String file = GraphWSControllerTest.class.getResource("/mitab/"+ interactionAc + "-miTab25.txt").getFile();
            assertEquals( new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8), response.getBody());

        }
    }

    @Test
    public void exportInteractionAsMITAB26() throws IOException {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miTab26);

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("text", response.getHeaders().getContentType().getType());
            assertEquals("plain", response.getHeaders().getContentType().getSubtype());

            // System.out.println(response.getBody());
            String file = GraphWSControllerTest.class.getResource("/mitab/"+ interactionAc + "-miTab26.txt").getFile();
            assertEquals( new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8), response.getBody());

        }
    }

    @Test
    public void exportInteractionAsMITAB27() throws IOException {

        List<String> interactionAcsToTest = new ArrayList<>();
        interactionAcsToTest.add("EBI-10052707");// generic checking
        interactionAcsToTest.add("EBI-10048599");// for checking interaction parameters
        interactionAcsToTest.add("EBI-10049314");// for checking expressedIn
        interactionAcsToTest.add("EBI-10054743");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-10042058");// for checking linkedFeatures
        interactionAcsToTest.add("EBI-1005174"); // for checking interpro
        interactionAcsToTest.add("EBI-1004945"); // for checking experiment modifications

        for (String interactionAc : interactionAcsToTest) {

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miTab27);

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("text", response.getHeaders().getContentType().getType());
            assertEquals("plain", response.getHeaders().getContentType().getSubtype());

            // System.out.println(response.getBody());
            String file = GraphWSControllerTest.class.getResource("/mitab/"+ interactionAc + "-miTab27.txt").getFile();
            assertEquals( new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8), response.getBody());

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
                        "/export/interaction/" + interactionAc + "?format={format}", String.class, miJSON);

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
                String actualJsonString = response.getBody();
                assertNotNull(actualJsonString);

                JSONAssert.assertEquals(expectedJsonString, actualJsonString, JSONCompareMode.LENIENT);

                // use below if you need to ignore fields
                /*
                JSONAssert.assertEquals(expectedJsonString, actualJsonString,
                        new CustomComparator(JSONCompareMode.LENIENT,
                                new Customization("*.participants[*].id", (o1, o2) -> true)
                        ));

                ArrayValueMatcher<Object> arrValMatch = new ArrayValueMatcher<>(
                        new CustomComparator(JSONCompareMode.LENIENT,
                                new Customization("data[*].participants[*].id", (o1, o2) -> true)));

                Customization arrayValueMatchCustomization = new Customization("data[*].participants", arrValMatch);
                CustomComparator customArrayValueComparator =
                        new CustomComparator(JSONCompareMode.LENIENT,
                        arrayValueMatchCustomization);
                JSONAssert.assertEquals(expectedJsonString, actualJsonString, customArrayValueComparator);
                */
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

                System.out.println("Test interaction with ac: " + interactionAc);

                ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                        "/export/interaction/" + interactionAc + "?format={format}", String.class, miXML25);

                assertNotNull("Response is null", response);
                assertNotNull("Response status line is null", response.getStatusCode());
                assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

                assertNotNull(response.getHeaders().getContentType());
                assertEquals("application", response.getHeaders().getContentType().getType());
                assertEquals("xml", response.getHeaders().getContentType().getSubtype());

                //TODO Add XML comparison
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

            System.out.println("Test interaction with ac: " + interactionAc);

            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port +
                    "/export/interaction/" + interactionAc + "?format={format}", String.class, miXML30);

            assertNotNull("Response is null", response);
            assertNotNull("Response status line is null", response.getStatusCode());
            assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

            assertNotNull(response.getHeaders().getContentType());
            assertEquals("application", response.getHeaders().getContentType().getType());
            assertEquals("xml", response.getHeaders().getContentType().getSubtype());

            //TODO Add XML comparison
        }
    }

    @Test
    public void exportInteractionsAsMITAB25QueryByPublication() throws IOException {

        String query = "EBI-10043615"; //publication

        System.out.println("Test interaction with query: " + query);

        // Request parameters and other properties.
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//        parameters.add("query", query);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<String>  response = restTemplate.postForEntity("http://localhost:" + port +
                "/export/interaction/list?query=" + query + "&format={format}", request, String.class, miTab25);

        assertNotNull("Response is null", response);
        assertNotNull("Response status line is null", response.getStatusCode());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("Response Code is wrong", 200, response.getStatusCodeValue());

        assertNotNull(response.getHeaders().getContentType());
        assertEquals("text", response.getHeaders().getContentType().getType());
        assertEquals("plain", response.getHeaders().getContentType().getSubtype());

        ContentDisposition disposition = response.getHeaders().getContentDisposition();
        assertNotNull(disposition);
        String fileName = disposition.getFilename();
        assertNotNull(fileName);

        String file = GraphWSControllerTest.class.getResource("/mitab/query-EBI-10043615-miTab25.txt").getFile();
        assertEquals( new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8), response.getBody());

    }

    @Test
    @Ignore
    public void export100InteractionsAsMITAB25() {
        //TODO
    }

    @Test
    @Ignore
    public void export1000InteractionsAsMITAB25() {
        //TODO
    }

    @Test
    @Ignore
    public void export10InteractionsAsMIXML25() {
        //TODO
    }

    @Test
    @Ignore
    public void export100InteractionsAsMIXML25() {
        //TODO
    }

    @Test
    @Ignore
    public void export1000InteractionsAsMIXML25() {
        //TODO
    }

    /*** Interaction Controller Testing ***/

    @Test
    public void shuffleDataBetweenModels() {

        //initializing xrefs
        Set<Xref> publicationXrefs = new HashSet<>();
        publicationXrefs.add(new Xref(new CvTerm("db_shortname1", "db_identifier1"),
                "identifier1",
                new CvTerm("qualifier_shortname1", "qualifier_identifier1"),
                "ac1"
        ));
        publicationXrefs.add(new Xref(new CvTerm("db_shortname2", "db_identifier2"),
                "identifier2",
                new CvTerm("qualifier_shortname2", "qualifier_identifier2"),
                "ac2"
        ));

        Set<Xref> experimentXrefs = new HashSet<>();
        experimentXrefs.add(new Xref(new CvTerm("db_shortname3", "db_identifier3"),
                "identifier3",
                new CvTerm("qualifier_shortname3", "qualifier_identifier3"),
                "ac1"
        ));
        experimentXrefs.add(new Xref(new CvTerm("db_shortname2", "db_identifier2"),
                "identifier2",
                new CvTerm("qualifier_shortname2", "qualifier_identifier2"),
                "ac2"
        ));

        //should be removed from everywhere
        experimentXrefs.add(new Xref(new CvTerm("db_shortname5", "MI:0446"),// pubmed id
                "identifier5",
                new CvTerm("qualifier_shortname5", "qualifier_identifier5"),
                "ac5"
        ));

        Set<Xref> interactionXrefs = new HashSet<>();
        interactionXrefs.add(new Xref(new CvTerm("db_shortname4", "db_identifier4"),
                "identifier4",
                new CvTerm("qualifier_shortname4", "qualifier_identifier4"),
                "ac4"
        ));

        //initializing annotations
        Set<Annotation> publicationAnnotations = new HashSet<>();

        //should be removed from everywhere
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname7", "MI:0957"),//full-coverage
                "description4"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname8", "MI:0959"),//imex curation
                "description5"));

        //should retain in publication
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname10", "MI:0634"),//contact email
                "description10"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname11", "MI:0878"),//author-submitted
                "description11"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname12", "MI:0612"),//comments
                "description12"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname13", "MI:0618"),//cautions
                "description13"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname14", "MI:0614"),//urls
                "description14"));

        //should retain in interaction
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname9", "db_identifier9"),
                "description9"));
        publicationAnnotations.add(new Annotation(new CvTerm("db_shortname15", "db_identifier15"),
                "description15"));


        Set<Annotation> experimentAnnotations = new HashSet<>();

        //should be removed from everywhere
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname6", "MI:0886"),//publication year
                "description1"));
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname7", "MI:0885"),//journal
                "description2"));
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname8", "MI:0636"),//authors
                "description3"));

        //should retain in interactions
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname9", "db_identifier9"),
                "description9"));
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname16", "db_identifier16"),
                "description16"));

        Set<Annotation> interactionAnnotations = new HashSet<>();
        experimentAnnotations.add(new Annotation(new CvTerm("db_shortname17", "db_identifier17"),
                "description17"));

        //intializing experiment details to be transferred to interaction details

        CvTerm interactionDetMethod = new CvTerm("interaction detection method", "MI:Identifier");
        Organism hostOrganism = new Organism("Homo Sapiens", 9606);

        PublicationDetails publicationDetails = new PublicationDetails(null, null, null,
                null, null, publicationXrefs, publicationAnnotations);
        ExperimentDetails experimentDetails = new ExperimentDetails(null, interactionDetMethod,
                null, hostOrganism, experimentXrefs, experimentAnnotations);
        InteractionDetails interactionDetails = new InteractionDetails(null, null,
                null, interactionXrefs, interactionAnnotations, null, null,
                null, null, null);

        interactionController.shuffleDataBetweenModels(experimentDetails,
                publicationDetails, interactionDetails);

        //shuffled data test
        List<Xref> modifiedInteractionXrefs = interactionXrefs.stream().filter(xref ->
                xref.getDatabase().getIdentifier().equals("db_identifier3") ||
                        xref.getDatabase().getIdentifier().equals("db_identifier4")
        ).collect(Collectors.toList());
        Assert.assertEquals(2, modifiedInteractionXrefs.size());
        Assert.assertEquals(2, interactionXrefs.size());


        List<Xref> modifiedPublicationXrefs = publicationXrefs.stream().filter(xref ->
                xref.getDatabase().getIdentifier().equals("db_identifier1") ||
                        xref.getDatabase().getIdentifier().equals("db_identifier2")
        ).collect(Collectors.toList());
        Assert.assertEquals(2, modifiedPublicationXrefs.size());
        Assert.assertEquals(2, publicationXrefs.size());

        List<Annotation> modifiedPublicationAnnotations = publicationAnnotations.stream().filter(annotation ->
                annotation.getTopic().getIdentifier().equals("MI:0634") || //contact-email
                        annotation.getTopic().getIdentifier().equals("MI:0878") || //author-submitted
                        annotation.getTopic().getIdentifier().equals("MI:0612") || //comments
                        annotation.getTopic().getIdentifier().equals("MI:0618") || //cautions
                        annotation.getTopic().getIdentifier().equals("MI:0614")   //urls
        ).collect(Collectors.toList());

        Assert.assertEquals(5, modifiedPublicationAnnotations.size());
        Assert.assertEquals(5, publicationAnnotations.size());

        List<Annotation> modifiedInteractionAnnotations = interactionAnnotations.stream().filter(annotation ->
                annotation.getTopic().getIdentifier().equals("db_identifier9") ||
                        annotation.getTopic().getIdentifier().equals("db_identifier15") ||
                        annotation.getTopic().getIdentifier().equals("db_identifier16") ||
                        annotation.getTopic().getIdentifier().equals("db_identifier17")
        ).collect(Collectors.toList());

        Assert.assertEquals(4, modifiedInteractionAnnotations.size());
        Assert.assertEquals(4, interactionAnnotations.size());

        Assert.assertEquals("interaction detection method", interactionDetails.getDetectionMethod().getShortName());
        Assert.assertEquals("MI:Identifier", interactionDetails.getDetectionMethod().getIdentifier());
        Assert.assertEquals("Homo Sapiens", interactionDetails.getHostOrganism().getScientificName());
        Assert.assertEquals(9606, interactionDetails.getHostOrganism().getTaxId());


    }

    /*Only for manual testing*/
    @Test
    @Ignore
    public void test() {

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://intact-neo4j-001.ebi.ac.uk:8083/intact/ws/graph/network/data");

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("species", "9606"));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            Instant starts = Instant.now();
            HttpResponse response = httpclient.execute(httppost);
            org.apache.http.HttpEntity entity = response.getEntity();
            Instant ends = Instant.now();
            Duration executionDuration = Duration.between(starts, ends);
            System.out.println("Total process took" + executionDuration);
            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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