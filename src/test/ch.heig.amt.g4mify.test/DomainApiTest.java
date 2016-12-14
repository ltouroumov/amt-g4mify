package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.Domain;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.HttpTestRequest.isError;
import static ch.heig.amt.g4mify.Utils.UtilsApiTest.baseInit;
import static ch.heig.amt.g4mify.Utils.UtilsApiTest.basePostExec;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class DomainApiTest {
    private Domain testDomain = null;

    @Rule
    public TestName name = new TestName();

    @Before
    public void init(){
        System.out.println("-- " + name.getMethodName() + " --");
        testDomain = baseInit();
    }

    @Test
    public void getDomain() {
        System.out.println("- getDomain");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/domain", null, headers, "GET");
        if(isError(response)) return;
        Domain domain = gson.fromJson(response.getBody(), Domain.class);
        System.out.println("Name: " + domain.getName() + " // Id: " + domain.getId() + " // Key: " + domain.getKey());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(testDomain.getName(), domain.getName());
        assertEquals(testDomain.getId(), domain.getId());
    }

    @Test
    public void putDomain(){
        System.out.println("- putDomain");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/domain", "{\"name\": \"This domain name has changed\"}", headers, "PUT");
        if(isError(response)) return;
        Domain domain = gson.fromJson(response.getBody(), Domain.class);
        System.out.println("Name: " + domain.getName() + " // Id: " + domain.getId() + " // Key: " + domain.getKey());
        assertEquals(200, response.getStatusCode());
        assertNotEquals(testDomain.getName(), domain.getName());
        assertEquals(testDomain.getId(), domain.getId());
    }

    @After
    public void postExec(){
        basePostExec(testDomain);
    }
}
