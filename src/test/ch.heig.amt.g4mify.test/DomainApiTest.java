package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.Utils.UtilsApiTest;
import ch.heig.amt.g4mify.model.Domain;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.HttpTestRequest.isError;
import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class DomainApiTest {
    private Domain testDomain = null;
    private HttpTestRequest tester = null;

    @Rule
    public TestName name = new TestName();

    @Before
    public void before(){
        System.out.println("-- " + name.getMethodName() + " --");
        tester = new HttpTestRequest();
        testDomain = baseDomainInit(BEFORE, tester);

        System.out.println("Identity: " + testDomain.getId() + ":" + testDomain.getKey());
        tester.setDefaultHeader("Identity", testDomain.getId() + ":" + testDomain.getKey());
    }

    @Test
    public void getDomain() {
        System.out.println("\n- " + name.getMethodName() + " -");
        Gson gson = new Gson();
        TestResponse response = tester.get("/api/domain", null);
        if(isError(response)) return;
        Domain domain = gson.fromJson(response.getBody(), Domain.class);
        System.out.println("Body: " + response.getBody());
        System.out.println("Name: " + domain.getName() + " // Id: " + domain.getId() + " // Key: " + domain.getKey());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(testDomain.getName(), domain.getName());
        assertEquals(testDomain.getId(), domain.getId());
    }

    @Test
    public void putDomain(){
        /*System.out.println("\n- " + name.getMethodName() + " -");
        Gson gson = new Gson();
        TestResponse response = tester.put("/api/domain", "{\"name\": \"This domain name has changed\"}", null, headers, "PUT");
        if(isError(response)) return;
        Domain domain = gson.fromJson(response.getBody(), Domain.class);
        System.out.println("Name: " + domain.getName() + " // Id: " + domain.getId() + " // Key: " + domain.getKey());
        assertEquals(200, response.getStatusCode());
        assertNotEquals(testDomain.getName(), domain.getName());
        assertEquals(testDomain.getId(), domain.getId());*/
    }

    @After
    public void after(){
        baseDomainPostExec(testDomain, AFTER, tester);
    }
}
