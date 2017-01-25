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
import static org.junit.Assert.assertNotNull;

/**
 * Created by yathanasiades on 24/01/17.
 */
public class EventApiTest {
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

        testUser = gson.fromJson(response.getBody(), User.class);

        System.out.println("sucessfully created user");
    }

    @Test
    public void WeShouldBeAbleToPublishAnEvent() {
        System.out.println("-- " + name.getMethodName() + " --");

        // create an event
        String body = "{\n" +
                "  \"data\": {},\n" +
                "  \"type\": \"test-event\",\n" +
                "  \"user\": \"" + testUser.getProfileId() + "\"\n" +
                "}";

        TestResponse response = tester.post("/api/events", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating event");
            return;
        }

        assertEquals(200, response.getStatusCode());

        // get events
        Gson gson = new Gson();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("user", testUser.getProfileId());

        response = tester.get("/api/events", hm);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating event");
            return;
        }


        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
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
