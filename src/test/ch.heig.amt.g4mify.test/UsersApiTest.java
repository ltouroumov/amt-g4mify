package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.Badge;
import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.User;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.rules.TestName;

import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Yves Athanasiadès on 05.12.2016.
 */
public class UsersApiTest {
    static private Domain testDomain = null;
    private User testUser = null;

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    static public void beforeClass() {
        testDomain = baseDomainInit(BEFORE_CLASS);
    }

    @Before
    public void before() {
        System.out.println("-- BEFORE --");

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"profileId\": \"Donald Duck\",\n" +
                "  \"profileUrl\": \"dduck\"\n" +
                "}";
        TestResponse response = request.test("/api/users", body, null, headers, "POST");

        if (HttpTestRequest.isError(response))
            return;

        testUser = gson.fromJson(response.getBody(), User.class);

        System.out.println("sucessfully created user");
    }

    @Test
    public void getUsers() {
        System.out.println("-- " + name.getMethodName() + " --");

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/users", null, null, headers, "GET");

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

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"badges\": [],\n" +
                "  \"id\": " + testUser.getId() + ",\n" +
                "  \"profileId\": \"Picsou Duck\",\n" +
                "  \"profileUrl\": \"pduck\"\n" +
                "}";

        TestResponse response = request.test("/api/users/" + testUser.getProfileId(), body, null, headers, "PUT");

        if (HttpTestRequest.isError(response))
            return;

        assertEquals("201", response.getStatusCode());

        // get user to verify
        request = new HttpTestRequest();
        gson = new Gson();
        headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        response = request.test("/api/users/" + testUser.getProfileId(), null, null, headers, "GET");

        if (HttpTestRequest.isError(response))
            return;

        User user = gson.fromJson(response.getBody(), User.class);

        assertEquals("Picsou Duck", user.getProfileId());
        assertEquals("pduck", user.getProfileUrl());
        assertEquals(testUser.getId(), user.getId());
    }

    @Test
    public void getUser() {
        System.out.println("-- " + name.getMethodName() + " --");

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        TestResponse response = request.test("/api/users/" + testUser.getProfileId(), null, null, headers, "GET");

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
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"color\": \"none\",\n" +
                "  \"image\": \"none\",\n" +
                "  \"isSingleton\": false,\n" +
                "  \"key\": \"test\",\n" +
                "  \"name\": \"test-badge\",\n" +
                "  \"previous\": \"none\"\n" +
                "}";

        TestResponse response = request.test("/api/badge-types/", body, null, headers, "POST");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badge");
            return;
        }

        // create eventRule that gives a badge when event is triggered
        request = new HttpTestRequest();
        gson = new Gson();
        headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        body = "{" +
                "  \"script\": \"award 'test-badge'\"," +
                "  \"types\": [" +
                "\"test\"" +
                "]" +
                "}";

        response = request.test("/api/event-rules/", body, null, headers, "POST");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        // trigger events
        request = new HttpTestRequest();
        gson = new Gson();
        headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        body = "{\n" +
                "  \"data\": {},\n" +
                "  \"type\": \"test-badge\",\n" +
                "  \"user\": \"" + testUser.getProfileId() + "\"\n" +
                "}";

        response = request.test("/api/events/", body, null, headers, "POST");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating event");
            return;
        }

        // get badges
        request = new HttpTestRequest();
        gson = new Gson();
        headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        response = request.test("/api/users/" + testUser.getProfileId() + "/badges", null, null, headers, "GET");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badge list");
            return;
        }

        Badge[] badges = gson.fromJson(response.getBody(), Badge[].class);


        assertEquals("test-badge", badges[0].getType().getName());
    }

    @Test
    public void getUserCounters() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create counter(test)
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"name\": \"test\"\n" +
                "}";

        TestResponse response = request.test("/api/counters/", body, null, headers, "POST");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating counter");
            return;
        }

        // get counters
        request = new HttpTestRequest();
        gson = new Gson();
        headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        response = request.test("/api/users/" + testUser.getProfileId() + "/counter/test", null, null, headers, "GET");

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

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        TestResponse response = request.test("/api/users/" + testUser.getProfileId(), null, null, headers, "DELETE");

        if (HttpTestRequest.isError(response))
            return;

        System.out.println("sucessfully deleted user");
    }

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS);
    }
}
