package ch.heig.amt.g4mify.Utils;

import ch.heig.amt.g4mify.model.Domain;
import com.google.gson.Gson;

import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.HttpTestRequest.isError;
import static org.junit.Assert.assertEquals;

/**
 * Created by Le Poulet Suisse on 14.12.2016.
 */
public class UtilsApiTest {
    public static Domain baseInit(){
        System.out.println("- INITIALISATION");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        TestResponse response = request.test("/register", "{\"name\": \"TestDomain\"}", null, "POST");
        if(isError(response)) return null;
        Domain testDomain = gson.fromJson(response.getBody(), Domain.class);
        System.out.println("Name: " + testDomain.getName() + " // Id: " + testDomain.getId() + " // Key: " + testDomain.getKey());
        assertEquals(201, response.getStatusCode());
        return testDomain;
    }

    public static void basePostExec(Domain testDomain){
        System.out.println("- POST EXECUTION");
        HttpTestRequest request = new HttpTestRequest();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/domain", null, headers, "DELETE");
        if(isError(response)) return;
        if(response.getStatusCode() == 200){
            System.out.println("Correctly deleted domain " + testDomain.getId());
        }
        System.out.println("\n");
        assertEquals(200, response.getStatusCode());
    }
}
