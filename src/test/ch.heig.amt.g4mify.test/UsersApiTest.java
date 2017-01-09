package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
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

        if(HttpTestRequest.isError(response))
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

        if(HttpTestRequest.isError(response))
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
                "  \"id\": "+ testUser.getId() +",\n" +
                "  \"profileId\": \"Picsou Duck\",\n" +
                "  \"profileUrl\": \"pduck\"\n" +
                "}";

        TestResponse response = request.test("/api/users/"+testUser.getProfileId(), body, null, headers, "PUT");

        if(HttpTestRequest.isError(response))
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

        TestResponse response = request.test("/api/users/"+testUser.getProfileId(), null, null, headers, "GET");

        if (HttpTestRequest.isError(response))
            return;

        User user = gson.fromJson(response.getBody(), User.class);

        assertEquals(testUser.getProfileId(), user.getProfileId());
        assertEquals(testUser.getProfileUrl(), user.getProfileUrl());
        assertEquals(testUser.getId(), user.getId());
    }

    // getUserBadges(id)

    @After
    public void after() {
        System.out.println("-- AFTER --");

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        TestResponse response = request.test("/api/users/"+testUser.getProfileId(), null, null, headers, "DELETE");

        if(HttpTestRequest.isError(response))
            return;

        System.out.println("sucessfully deleted user");
    }

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS);
    }
}
