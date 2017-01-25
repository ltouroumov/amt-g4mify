package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
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
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class CountersApiTest {
    private static Domain testDomain = null;
    private static ArrayList<CounterSummary> counters = new ArrayList<>();
    private static HttpTestRequest tester = null;

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void beforeClass(){
        tester = new HttpTestRequest();
        testDomain = baseDomainInit(BEFORE_CLASS, tester);
        System.out.println(testDomain);
        tester.setDefaultHeader("Identity", testDomain.getId() + ":" + testDomain.getKey());

    }

    @Before
    public void before(){
        System.out.println("\n-- " + name.getMethodName() + " --");
        System.out.println("- BEFORE -");
        Gson gson = new Gson();
        TestResponse response = tester.post("/api/counters", null, "{\"name\":\"myCounter\"}");
        if(isError(response)) return;
        CounterSummary counter = gson.fromJson(response.getBody(), CounterSummary.class);
        counters.add(counter);
        displayCounter(counter);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void WeShouldBeAbleToGetAllCounters(){
        System.out.println("\n- " + name.getMethodName() + " -");
        Gson gson = new Gson();
        TestResponse response = tester.get("/api/counters", null);
        if(isError(response)) return;

        CounterSummary[] countersList = gson.fromJson(response.getBody(), CounterSummary[].class);
        ArrayList<CounterSummary> counters = new ArrayList<>(Arrays.asList(countersList));
        counters.forEach(this::displayCounter);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void WeShouldBeAbleToGetACounter(){
        System.out.println("\n- " + name.getMethodName() + " -");
        Gson gson = new Gson();
        TestResponse response = tester.get("/api/counters/" + counters.get(0).name, null);
        if(isError(response)) return;
        assertEquals(200, response.getStatusCode());

        CounterSummary countersList = gson.fromJson(response.getBody(), CounterSummary.class);
        ArrayList<CounterSummary> counters = new ArrayList<>(Arrays.asList(countersList));
        counters.forEach(counterSummary -> {
            TestResponse responseCounter = tester.get("/api/counters/" + counterSummary.name, null);
            if(isError(responseCounter)) return;
            CounterSummary counter = gson.fromJson(responseCounter.getBody(), CounterSummary.class);
            assertEquals(counterSummary.id, counter.id);
            assertEquals(counterSummary.name, counter.name);
            assertEquals(200, responseCounter.getStatusCode());
        });
    }

    @Test
    public void WeShouldntBeABleToGetACounterWithABadName(){
        System.out.println("\n- " + name.getMethodName() + " -");
        Gson gson = new Gson();
        TestResponse response = tester.get("/api/counters/" + "ThisIsARandomName", null);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void WeShouldBeAbleToUpdateACounter(){
        System.out.println("\n- " + name.getMethodName() + " -");
        Gson gson = new Gson();
        CounterSummary oldCounter = counters.get(0);
        System.out.println("Old counter");
        displayCounter(oldCounter);
        TestResponse response = tester.put("/api/counters/" + counters.get(0).name, null, "{\"name\": \"updatedCounter\"}");
        if(isError(response)) return;
        CounterSummary counter = gson.fromJson(response.getBody(), CounterSummary.class);
        System.out.println("New counter");
        counters.set(0, counter);
        displayCounter(oldCounter);
        assertEquals(200, response.getStatusCode());
        assertNotEquals(oldCounter.name, counter.name);
        assertEquals(oldCounter.id, counter.id);

    }

    @After
    public void after(){
        System.out.println("\n- AFTER -");
        for(CounterSummary counter : counters){
            TestResponse response = tester.delete("/api/counters/" + counter.name);
            if(isError(response)) return;
            System.out.println("Successfully deleted counter with id " + counter.id);
            assertEquals(200, response.getStatusCode());
        }
        counters.clear();
    }

    @AfterClass
    public static void afterClass(){
        baseDomainPostExec(testDomain, AFTER_CLASS, tester);
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
    }
}
