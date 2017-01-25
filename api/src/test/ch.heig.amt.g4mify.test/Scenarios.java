package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.Utils.UtilsApiTest;
import ch.heig.amt.g4mify.model.BadgeRule;
import ch.heig.amt.g4mify.model.BadgeType;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.EventRule;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static ch.heig.amt.g4mify.Utils.HttpTestRequest.isError;
import static ch.heig.amt.g4mify.Utils.UtilsApiTest.AFTER;
import static ch.heig.amt.g4mify.Utils.UtilsApiTest.baseDomainPostExec;
import static org.junit.Assert.assertEquals;

/**
 * Created by Le Poulet Suisse on 25.01.2017.
 */
@SuppressWarnings("ALL")
public class Scenarios {

    private HttpTestRequest tester = null;
    private Domain domain = null;
    private ArrayList<CounterSummary> counters = new ArrayList<>();
    private ArrayList<EventRule> eventRules = new ArrayList<>();
    private ArrayList<BadgeType> badgeTypes = new ArrayList<>();
    private ArrayList<BadgeRule> badgeRules = new ArrayList<>();

    @Before
    public void before(){
        /*
        * Scenario Initialisation
        * */
        //Post Domain -> Get Domain -> Post Counters -> Post event-rules -> Post Badge-Types -> Post Badge-Rules

        Gson gson = new Gson();
        tester = new HttpTestRequest();

        //Post Domain / Get Domain
        domain = UtilsApiTest.baseDomainInit(UtilsApiTest.BEFORE, tester);
        tester.setDefaultHeader("Identity", domain.getId() + ":" + domain.getKey());

        //Post Counters
        for (int i = 0 ; i < 2 ; i++){
            TestResponse response = tester.post("/api/counters", null, "{\"name\":\"myCounter" + i + "\"}");
            if(isError(response)) return;
            CounterSummary counter = gson.fromJson(response.getBody(), CounterSummary.class);
            counters.add(counter);
            displayCounter(counter);
            assertEquals(201, response.getStatusCode());
        }

        //Post event-rules


        //Post Badge-types


        //Post Badge-rules



    }

    @Test
    public void Scenario1(){
        //Get Domain -> Post User -> Post event (Multiple times) -> Get users/PID/Badges (After 10 events)
    }

    @After
    public void after(){
        baseDomainPostExec(domain, AFTER, tester);
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
