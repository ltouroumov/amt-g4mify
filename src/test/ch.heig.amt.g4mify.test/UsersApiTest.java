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

        //System.out.println("Profile Id: " + testUser.getProfileId() + " // Profile Url: " + testUser.getProfileUrl() + " // Id: " + testUser.getId());

        assertEquals(201, response.getStatusCode());
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

        //System.out.println("Profile Id: " + users[0].getProfileId() + " // Profile Url: " + users[0].getProfileUrl()+ " // Id: " + users[0].getId());

        assertEquals(testUser.getProfileId(), users[0].getProfileId());
        assertEquals(testUser.getProfileUrl(), users[0].getProfileUrl());
        assertEquals(testUser.getId(), users[0].getId());
    }

    public void putUser(int id) {
        System.out.println("-- " + name.getMethodName() + " --");

        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        String body = "{\n" +
                "  \"badges\": [],\n" +
                "  \"id\": 0,\n" +
                "  \"profileId\": \"Picsou Duck\",\n" +
                "  \"profileUrl\": \"pduck\"\n" +
                "}";

        TestResponse response = request.test("/api/users", body, null, headers, "PUT");


    }

    // getUser(id)

    // getUserBadges(id)

    @AfterClass
    public static void afterClass() {
        baseDomainPostExec(testDomain, AFTER_CLASS);
    }
}
