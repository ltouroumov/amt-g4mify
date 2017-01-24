package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.BadgeRule;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.rules.TestName;

import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yathanasiades on 24/01/17.
 */
public class BadgeRulesApiTest {
    static private HttpTestRequest tester = null;
    static private Domain testDomain = null;
    private long badgeRuleId;

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    static public void beforeClass() {
        tester = new HttpTestRequest();
        testDomain = baseDomainInit(BEFORE_CLASS, tester);
        tester.setDefaultHeader("Identity", testDomain.getId() + ":" + testDomain.getKey());

        // create badge-type
        String body = "{\n" +
                "  \"color\": \"none\",\n" +
                "  \"image\": \"none\",\n" +
                "  \"isSingleton\": false,\n" +
                "  \"key\": \"test-badge\",\n" +
                "  \"name\": \"test-badge\",\n" +
                "  \"previous\": \"none\"\n" +
                "}";

        TestResponse response = tester.post("/api/badge-types", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badge");
            return;
        }

        System.out.println("Created badge-type");
    }

    @Before
    public void before() {
        System.out.println("-- BEFORE --");

        // create badgeRule
        Gson gson = new Gson();

        String body = "{\n" +
                "  \"condition\": \"\",\n" +
                "  \"grants\": \"test-badge\",\n" +
                "  \"id\": 0\n" +
                "}";

        TestResponse response = tester.post("/api/badge-rules", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badgeRule");
            return;
        }

        BadgeRule br = gson.fromJson(response.getBody(), BadgeRule.class);
        badgeRuleId = br.getId();

        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void getBadgeRules() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create badgeRule
        Gson gson = new Gson();

        TestResponse response = tester.get("/api/badge-rules", null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badgeRule");
            return;
        }

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getBadgeRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        Gson gson = new Gson();

        TestResponse response = tester.get("/api/badge-rules/" + badgeRuleId, null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badgeRule");
            return;
        }

        BadgeRule br = gson.fromJson(response.getBody(), BadgeRule.class);
        assertEquals("test-badge", br.getGrants().getName());
    }

    @Test
    public void deleteBadgeRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        Gson gson = new Gson();

        TestResponse response = tester.delete("/api/badge-rules/" + badgeRuleId);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badgeRule");
            return;
        }

        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void putBadgeRule() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create badge-type
        String body = "{\n" +
                "  \"color\": \"none\",\n" +
                "  \"image\": \"none\",\n" +
                "  \"isSingleton\": false,\n" +
                "  \"key\": \"test-badge2\",\n" +
                "  \"name\": \"test-badge2\",\n" +
                "  \"previous\": \"none\"\n" +
                "}";

        TestResponse response = tester.post("/api/badge-types", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badge");
            return;
        }

        System.out.println("Created badge-type");

        body = "{\n" +
                "  \"condition\": \"\",\n" +
                "  \"grants\": \"test-badge2\",\n" +
                "  \"id\": 0\n" +
                "}";

        response = tester.put("/api/badge-rules/" + badgeRuleId, null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badgeRule");
            return;
        }

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void getEvalutation() {
        System.out.println("-- " + name.getMethodName() + " --");

        User testUser = null;
        Gson gson = new Gson();

        String body = "{\n" +
                "  \"profileId\": \"Donald Duck\",\n" +
                "  \"profileUrl\": \"dduck\"\n" +
                "}";
        TestResponse response = tester.post("/api/users", null, body);

        if (HttpTestRequest.isError(response))
            return;
        System.out.println(response.getBody());
        testUser = gson.fromJson(response.getBody(), User.class);

        System.out.println("sucessfully created user");


        gson = new Gson();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("user", testUser.getProfileId());

        response = tester.get("/api/badge-rules/" + badgeRuleId + "/evaluate", hm);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badgeRule");
            return;
        }

        assertEquals("{\"result\":true}", response.getBody());
        assertEquals(200, response.getStatusCode());
    }

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS, tester);
    }
}
