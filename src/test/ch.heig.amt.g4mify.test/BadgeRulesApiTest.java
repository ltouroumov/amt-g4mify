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
public class BadgeRulesApiTest {
    static private Domain testDomain = null;
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

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS);
    }
}
