package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.model.Domain;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class AbstractDomainApiTest {
    private Domain testDomain = null;

    @Before
    public void init(){
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        testDomain = gson.fromJson(request.test("domains", "{\"name\": \"asdasd\"}", null, "POST"), Domain.class);
        System.out.println("Response-------------");
        System.out.println("Name: " + testDomain.getName() + " // Id: " + testDomain.getId() + " // Key: " + testDomain.getKey());
    }

    @Test
    public void getDomain() {
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        Domain domain = gson.fromJson(request.test("domains/" + testDomain.getId(), null, null, "GET"), Domain.class);
        System.out.println("Response-------------");
        System.out.println("Name: " + domain.getName() + " // Id: " + domain.getId() + " // Key: " + domain.getKey());
        assertEquals(testDomain.getName(), domain.getName());
        assertEquals(testDomain.getId(), domain.getId());
    }
}
