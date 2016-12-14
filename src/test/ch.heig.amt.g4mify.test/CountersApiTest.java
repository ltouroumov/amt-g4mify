package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.*;
import org.junit.rules.TestName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static ch.heig.amt.g4mify.Utils.HttpTestRequest.isError;
import static ch.heig.amt.g4mify.Utils.UtilsApiTest.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class CountersApiTest {
    private static Domain testDomain = null;
    private static ArrayList<CounterSummary> counters = new ArrayList<>();

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void initClass(){
        testDomain = baseDomainInit(BEFORE_CLASS);
    }

    @Before
    public void init(){
        System.out.println("- METHOD INITIALISATION " + name.getMethodName());
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/counters", "{\"name\":\"myCounter\"}", headers, "POST");
        if(isError(response)) return;
        CounterSummary counter = gson.fromJson(response.getBody(), CounterSummary.class);
        counters.add(counter);
        displayCounter(counter);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void getCounters(){
        System.out.println("- " + name.getMethodName());
        HttpTestRequest request = new HttpTestRequest();
        Gson gson = new Gson();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = request.test("/api/counters", null, headers, "GET");
        if(isError(response)) return;

        CounterSummary[] countersList = gson.fromJson(response.getBody(), CounterSummary[].class);
        ArrayList<CounterSummary> counters = new ArrayList<>(Arrays.asList(countersList));
        counters.forEach(this::displayCounter);
        assertEquals(200, response.getStatusCode());
    }

    @AfterClass
    public static void postExecClass(){
        baseDomainPostExec(testDomain, AFTER_CLASS);
    }

    private void displayCounter(CounterSummary counter){
        System.out.println("New counter!");
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
