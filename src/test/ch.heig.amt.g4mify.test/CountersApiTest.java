package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import com.google.gson.Gson;
import org.junit.*;
import org.junit.rules.TestName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static ch.heig.amt.g4mify.Utils.HttpTestRequest.isError;
import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class CountersApiTest {
    /*private static Domain testDomain = null;
    private static ArrayList<CounterSummary> counters = new ArrayList<>();

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void beforeClass(){
        testDomain = baseDomainInit(BEFORE_CLASS);
    }

    @Before
    public void before(){
        System.out.println("\n-- " + name.getMethodName() + " --");
        System.out.println("- BEFORE -");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());

        TestResponse response = request.test("/api/counters", "{\"name\":\"myCounter\"}", null, headers, "POST");
        if(isError(response)) return;
        CounterSummary counter = gson.fromJson(response.getBody(), CounterSummary.class);
        counters.add(counter);
        displayCounter(counter);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void getCounters(){
        System.out.println("\n- " + name.getMethodName() + " -");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/counters", null, null, headers, "GET");
        if(isError(response)) return;

        CounterSummary[] countersList = gson.fromJson(response.getBody(), CounterSummary[].class);
        ArrayList<CounterSummary> counters = new ArrayList<>(Arrays.asList(countersList));
        counters.forEach(this::displayCounter);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void getCounter(){
        System.out.println("\n- " + name.getMethodName() + " -");
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/counters", null, null, headers, "GET");
        if(isError(response)) return;
        assertEquals(200, response.getStatusCode());

        CounterSummary[] countersList = gson.fromJson(response.getBody(), CounterSummary[].class);
        ArrayList<CounterSummary> counters = new ArrayList<>(Arrays.asList(countersList));
        counters.forEach(counterSummary -> {
            TestResponse responseCounter = request.test("/api/counters/" + counterSummary.name, null, null, headers, "GET");
            if(isError(responseCounter)) return;
            CounterSummary counter = gson.fromJson(responseCounter.getBody(), CounterSummary.class);
            assertEquals(counterSummary.id, counter.id);
            assertEquals(counterSummary.name, counter.name);
            assertEquals(200, responseCounter.getStatusCode());
        });
    }

    @After
    public void after(){
        System.out.println("\n- AFTER -");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        for(CounterSummary counter : counters){
            HttpTestRequest request = new HttpTestRequest();
            TestResponse response = request.test("/api/counters/" + counter.id, null, null, headers, "DELETE");
            if(isError(response)) return;
            System.out.println("Successfully deleted counter with id " + counter.id);
            assertEquals(200, response.getStatusCode());
        }
        counters.clear();
    }

    @AfterClass
    public static void afterClass(){
        baseDomainPostExec(testDomain, AFTER_CLASS);
    }

    private void displayCounter(CounterSummary counter){
        System.out.println("\nNew counter!");
        System.out.println("Name: " + counter.name);
        System.out.println("Id: " + counter.id);
        System.out.println("Metrics: ");
        for(MetricSummary metric : counter.metrics){
            System.out.println("\tId: " + metric.id);
            System.out.println("\tName: " + metric.name);
            System.out.println("\tDuration: " + metric.duration);
        }
    }*/
}
