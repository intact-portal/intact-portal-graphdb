package uk.ac.ebi.intact.graphdb.ws.controller;


import org.apache.http.HttpEntity;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.graphdb.ws.Neo4jTestConfiguration;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by anjali on 26/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Neo4jTestConfiguration.class)
public class InteractionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private InteractionController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contexLoads() {
        assertNotNull(controller);
    }

    @Test
    public void exportInteraction() {

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
                        "/interaction/export/" + interactionAc, String.class);

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
            HttpEntity entity = response.getEntity();
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

}
