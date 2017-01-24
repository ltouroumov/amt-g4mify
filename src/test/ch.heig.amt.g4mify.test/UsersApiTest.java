package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.Badge;
import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.*;
import org.junit.rules.TestName;

import java.util.ArrayList;

import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Yves Athanasiadès on 05.12.2016.
 */
public class UsersApiTest {
    static private HttpTestRequest tester = null;
    static private Domain testDomain = null;
    private User testUser = null;

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
    }

    @Test
    public void getUsers() {
        System.out.println("-- " + name.getMethodName() + " --");

        Gson gson = new Gson();

        TestResponse response = tester.get("/api/users", null);

        if (HttpTestRequest.isError(response))
            return;

        User[] users = gson.fromJson(response.getBody(), User[].class);

        assertEquals(testUser.getProfileId(), users[0].getProfileId());
        assertEquals(testUser.getProfileUrl(), users[0].getProfileUrl());
        assertEquals(testUser.getId(), users[0].getId());
    }

    @Test
    public void putUser() {
        System.out.println("-- " + name.getMethodName() + " --");

        String body = "{\n" +
                "  \"badges\": [],\n" +
                "  \"id\": " + testUser.getId() + ",\n" +
                "  \"profileId\": \"Donald Duck\",\n" +
                "  \"profileUrl\": \"pduck\"\n" +
                "}";

        TestResponse response = tester.put("/api/users/" + testUser.getProfileId(), null, body);

        if (HttpTestRequest.isError(response))
            return;

        assertEquals(200, response.getStatusCode());

        // get user to verify
        Gson gson = new Gson();

        response = tester.get("/api/users/" + testUser.getProfileId(), null);

        if (HttpTestRequest.isError(response))
            return;

        User user = gson.fromJson(response.getBody(), User.class);

        assertEquals("Donald Duck", user.getProfileId());
        assertEquals("pduck", user.getProfileUrl());
        assertEquals(testUser.getId(), user.getId());
    }

    @Test
    public void getUser() {
        System.out.println("-- " + name.getMethodName() + " --");

        Gson gson = new Gson();

        TestResponse response = tester.get("/api/users/" + testUser.getProfileId(), null);

        if (HttpTestRequest.isError(response))
            return;

        User user = gson.fromJson(response.getBody(), User.class);

        assertEquals(testUser.getProfileId(), user.getProfileId());
        assertEquals(testUser.getProfileUrl(), user.getProfileUrl());
        assertEquals(testUser.getId(), user.getId());
    }

    @Test
    public void getUserBadges() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create badge type (test-badge)
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

        // create eventRule that gives a badge when event is triggered
        body = "{" +
                "  \"script\": \"award 'test-badge'\"," +
                "  \"types\": [" +
                "\"test-badge\"" +
                "]" +
                "}";

        response = tester.post("/api/event-rules/", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        System.out.println("Created event-rule");

        // trigger events
        body = "{\n" +
                "  \"data\": {},\n" +
                "  \"type\": \"test-badge\",\n" +
                "  \"user\": \"" + testUser.getProfileId() + "\"\n" +
                "}";

        response = tester.post("/api/events/", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating event");
            return;
        }

        System.out.println("Created event");
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        // get badges
        Gson gson = new Gson();

        response = tester.get("/api/users/" + testUser.getProfileId() + "/badges", null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badge list");
            return;
        }

        System.out.println("got user's badges");

        ArrayList<Badge> badges = gson.fromJson(response.getBody(), new TypeToken<ArrayList<Badge>>() {
        }.getType());

        System.out.println(response.getBody());
        //assertEquals("test-badge", badges.get(0).getType().getName());
    }

    @Test
    public void getUserCounters() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create counter(test)
        String body = "{\n" +
                "  \"name\": \"test\"\n" +
                "}";

        TestResponse response = tester.post("/api/counters/", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating counter");
            return;
        }

        // get counters
        Gson gson = new Gson();

        response = tester.get("/api/users/" + testUser.getProfileId() + "/counter/test", null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting counter");
            return;
        }

        Counter counter = gson.fromJson(response.getBody(), Counter.class);


        assertEquals("test", counter.getName());
    }

    @After
    public void after() {
        System.out.println("-- AFTER --");

        TestResponse response = tester.delete("/api/users/" + testUser.getProfileId());

        if (HttpTestRequest.isError(response))
            return;

        System.out.println("sucessfully deleted user");
    }

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS, tester);
    }
}
