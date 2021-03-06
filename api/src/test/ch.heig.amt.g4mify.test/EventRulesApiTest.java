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
    static private HttpTestRequest tester = null;
    static private Domain testDomain = null;
    private long eventRuleId;

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    static public void beforeClass() {
        tester = new HttpTestRequest();
        testDomain = baseDomainInit(BEFORE_CLASS, tester);
        tester.setDefaultHeader("Identity", testDomain.getId() + ":" + testDomain.getKey());
    }

    @Before
    public void before() {
        System.out.println("-- BEFORE --");

        // create eventRule that gives a badge when event is triggered
        Gson gson = new Gson();

        String body = "{" +
                "  \"script\": \"award 'test-badge'\"," +
                "  \"types\": [" +
                "\"test\"" +
                "]" +
                "}";

        TestResponse response = tester.post("/api/event-rules", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        EventRule eventRule = gson.fromJson(response.getBody(), EventRule.class);
        eventRuleId = eventRule.getId();

        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void WeShouldBeAbleToGetAllEventRules() {
        System.out.println("-- " + name.getMethodName() + " --");

        Gson gson = new Gson();

        TestResponse response = tester.get("/api/event-rules", null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting eventRules");
            return;
        }

        EventRule[] eventRule = gson.fromJson(response.getBody(), EventRule[].class);

        // not super cool depends on when the put test is run
        assertEquals("award 'test-badge'", eventRule[0].getScript());
    }

    @Test
    public void WeShouldBeAbleToGetAnEventRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        // in case eventRuleId changes
        long id = eventRuleId;

        Gson gson = new Gson();

        TestResponse response = tester.get("/api/event-rules/" + id, null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting eventRule");
            return;
        }

        EventRule eventRule = gson.fromJson(response.getBody(), EventRule.class);
        assertEquals(id, eventRule.getId());
    }

    @Test
    public void WeShouldBeAbleToUpdateAnEventRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        // in case eventRuleId changes
        long id = eventRuleId;

        // update eventRule
        String body = "{\n" +
                "  \"script\": \"update 'test-counter' set 10\",\n" +
                "  \"types\": [\n" +
                "    \"test\"\n" +
                "  ]\n" +
                "}";

        TestResponse response = tester.put("/api/event-rules/" + id, null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        assertEquals(200, response.getStatusCode());

        // get eventRule to verify
        Gson gson = new Gson();

        response = tester.get("/api/event-rules/" + id, null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        EventRule eventRule = gson.fromJson(response.getBody(), EventRule.class);
        assertEquals(id, eventRule.getId());
        assertEquals("update 'test-counter' set 10", eventRule.getScript());
    }

    @Test
    public void WeShouldBeAbleToDeleteAnEventRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        // in case eventRuleId changes
        long id = eventRuleId;

        // update eventRule
        TestResponse response = tester.delete("/api/event-rules/" + id);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        assertEquals(200, response.getStatusCode());
    }

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS, tester);
    }
}
