package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Event;
import ch.heig.amt.g4mify.model.User;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.rules.TestName;

import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by yathanasiades on 24/01/17.
 */
public class EventApiTest {
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
    public void testEvents() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create an event
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"data\": {},\n" +
                "  \"type\": \"test-event\",\n" +
                "  \"user\": \"" + testUser.getProfileId() + "\"\n" +
                "}";

        TestResponse response = request.test("/api/events/", body, null, headers, "POST");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating event");
            return;
        }

        assertEquals("201", response.getStatusCode());

        // get events
        request = new HttpTestRequest();
        gson = new Gson();
        headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        response = request.test("/api/events/", null, null, headers, "GET");

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating event");
            return;
        }

        Event[] events = gson.fromJson(response.getBody(), Event[].class);

        assertEquals("test-event", events[0].getType());
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