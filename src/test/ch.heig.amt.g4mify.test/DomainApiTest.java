package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.Domain;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static ch.heig.amt.g4mify.Utils.HttpTestRequest.is500;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class DomainApiTest {
    private Domain testDomain = null;

    @Before
    public void init(){
        System.out.println("- INITIALISATION");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        TestResponse response = request.test("/register", "{\"name\": \"TestDomain\"}", null, "POST");
        if(is500(response)) return;
        testDomain = gson.fromJson(response.getBody(), Domain.class);
        System.out.println("Name: " + testDomain.getName() + " // Id: " + testDomain.getId() + " // Key: " + testDomain.getKey());
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void getDomain() {
        System.out.println("- getDomain");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/domain", null, headers, "GET");
        if(is500(response)) return;
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
        if(is500(response)) return;
        Domain domain = gson.fromJson(response.getBody(), Domain.class);
        System.out.println("Name: " + domain.getName() + " // Id: " + domain.getId() + " // Key: " + domain.getKey());
        assertEquals(200, response.getStatusCode());
        assertNotEquals(testDomain.getName(), domain.getName());
        assertEquals(testDomain.getId(), domain.getId());
    }

    @After
    public void postExec(){
        System.out.println("- POST EXECUTION");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/domain", null, headers, "DELETE");
        if(is500(response)) return;
        String responseBody = response.getBody();
        if(response.getStatusCode() == 200){
            System.out.print("Correctly deleted domain " + testDomain.getId());
        }
        assertEquals(200, response.getStatusCode());
    }
}
