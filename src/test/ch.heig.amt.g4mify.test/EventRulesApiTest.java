package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.EventRule;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.rules.TestName;

import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by yathanasiades on 24/01/17.
 */
public class EventRulesApiTest {
    /*static private Domain testDomain = null;
    private long eventRuleId;

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    static public void beforeClass() {
        testDomain = baseDomainInit(BEFORE_CLASS);
    }

    @Before
    public void before() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create eventRule that gives a badge when event is triggered
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{" +
                "  \"script\": \"award 'test-badge'\"," +
                "  \"types\": [" +
                "\"test\"" +
                "]" +
                "}";

        TestResponse response = request.test("/api/event-rules", body, null, headers, "POST");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        EventRule eventRule = gson.fromJson(response.getBody(), EventRule.class);
        eventRuleId = eventRule.getId();

        assertEquals("201", response.getStatusCode());
    }

    @Test
    public void getEventRules() {
        System.out.println("-- " + name.getMethodName() + " --");

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        TestResponse response = request.test("/api/event-rules", null, null, headers, "GET");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting eventRules");
            return;
        }

        EventRule[] eventRule = gson.fromJson(response.getBody(), EventRule[].class);
        assertEquals("award 'test-badge'", eventRule[0].getScript());
    }

    @Test
    public void getEventRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        // in case eventRuleId changes
        long id = eventRuleId;

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        TestResponse response = request.test("/api/event-rules/" + id, null, null, headers, "GET");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting eventRule");
            return;
        }

        EventRule eventRule = gson.fromJson(response.getBody(), EventRule.class);
        assertEquals(id, eventRule.getId());
    }

    @Test
    public void putEventRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        // in case eventRuleId changes
        long id = eventRuleId;

        // update eventRule
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"script\": \"update 'test-counter' set 10\",\n" +
                "  \"types\": [\n" +
                "    \"test\"\n" +
                "  ]\n" +
                "}";

        TestResponse response = request.test("/api/event-rules/" + id, body, null, headers, "PUT");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        assertEquals("200", response.getStatusCode());

        // get eventRule to verify
        request = new HttpTestRequest();
        gson = new Gson();
        headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        response = request.test("/api/event-rules/" + id, null, null, headers, "GET");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        EventRule eventRule = gson.fromJson(response.getBody(), EventRule.class);
        assertEquals(id, eventRule.getId());
        assertEquals("update 'test-counter' set 10", eventRule.getScript());
    }

    @Test
    public void deleteEventRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        // in case eventRuleId changes
        long id = eventRuleId;

        // update eventRule
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"script\": \"update 'test-counter' set 10\",\n" +
                "  \"types\": [\n" +
                "    \"test\"\n" +
                "  ]\n" +
                "}";

        TestResponse response = request.test("/api/event-rules/" + id, body, null, headers, "DELETE");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        assertEquals("200", response.getStatusCode());
    }

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS);
    }*/
}
