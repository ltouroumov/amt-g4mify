package ch.heig.amt.g4mify.test;

import ch.heig.amt.g4mify.Utils.HttpTestRequest;
import ch.heig.amt.g4mify.Utils.TestResponse;
import ch.heig.amt.g4mify.Utils.UtilsApiTest;
import ch.heig.amt.g4mify.model.*;
import ch.heig.amt.g4mify.model.view.counter.CounterSummary;
import ch.heig.amt.g4mify.model.view.metric.MetricSummary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
    //private ArrayList<BadgeRule> badgeRules = new ArrayList<>();

    @Before
    public void before() {
        /*
        * Scenario Initialisation
        * */
        //Post Domain -> Get Domain -> Post Counters -> Post event-rules -> Post Badge-Types -> Post Badge-Rules

        System.out.println("-- Scenario INITIALISATION --");

        Gson gson = new Gson();
        tester = new HttpTestRequest();

        //Post Domain / Get Domain
        domain = UtilsApiTest.baseDomainInit(UtilsApiTest.BEFORE, tester);
        tester.setDefaultHeader("Identity", domain.getId() + ":" + domain.getKey());

        //Post Counters
        String body = "{" +
                "\"name\":\"beeps\"" +
                "}";

        TestResponse response = tester.post("/api/counters", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badge");
            return;
        }
        /*
            CounterSummary counter = gson.fromJson(response.getBody(), CounterSummary.class);
            counters.add(counter);
            displayCounter(counter);
        */

        System.out.println("Created counter");


        //Post Badge-types
        body = "{" +
                " \"key\": \"bronze-beep\"," +
                " \"name\": \"Bronze Beeps\"," +
                " \"color\": \"bronze\"," +
                " \"isSingleton\": \"True\"" +
                "}";

        response = tester.post("/api/badge-types", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badge");
            return;
        }

        body = "{" +
                "\"key\": \"silver-beep\"," +
                "\"name\": \"Silver Beeps\"," +
                "\"color\": \"silver\"," +
                "\"isSingleton\": \"True\"" +
                "}";

        response = tester.post("/api/badge-types", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badge");
            return;
        }

        System.out.println("Created badgeType");


        //Post event-rules
        body = "{" +
                "\"types\": [\"beep\"]," +
                "\"script\": \"update 'beeps' add 1\"" +
                "}";

        response = tester.post("/api/event-rules/", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating eventRule");
            return;
        }

        System.out.println("Created eventRule");


        //Post Badge-rules
        body = "{ " +
                "\"condition\": \"when 'beeps' matches { it >= 10 }\"," +
                "\"grants\": \"bronze-beep\"" +
                "}";

        response = tester.post("/api/badge-rules", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badgeRule");
            return;
        }

        body = "{" +
                "\"condition\": \"when 'beeps' matches { it >= 20 }\"," +
                "\"grants\": \"silver-beep\"" +
                "}";

        response = tester.post("/api/badge-rules", null, body);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error creating badgeRule");
            return;
        }

        System.out.println("Created badgeRule");

    }

    @Test
    public void Scenario1() {
        //Get Domain -> Post User -> Post event (Multiple times) -> Get users/PID/Badges (After 10 events)

        System.out.println("-- Scenario 1 --");

        Gson gson = new Gson();

        String body = "{\n" +
                "  \"profileId\": \"Donald Duck\",\n" +
                "  \"profileUrl\": \"dduck\"\n" +
                "}";
        TestResponse response = tester.post("/api/users", null, body);

        if (HttpTestRequest.isError(response))
            return;

        User testUser = gson.fromJson(response.getBody(), User.class);

        System.out.println("sucessfully created user");


        // Post events
        for (int i = 0; i < 10; i++) {
            body = "{\n" +
                    "  \"data\": {},\n" +
                    "  \"type\": \"beep\",\n" +
                    "  \"user\": \"" + testUser.getProfileId() + "\"\n" +
                    "}";

            response = tester.post("/api/events/", null, body);

            if (HttpTestRequest.isError(response)) {
                System.out.println("Error creating event");
                return;
            }

            System.out.println("Created event");
        }

        // leave time to process the events (asynchronous)
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Get user badges
        gson = new Gson();

        response = tester.get("/api/users/" + testUser.getProfileId() + "/badges", null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badge list");
            return;
        }

        System.out.println("got user's badges");

        ArrayList<Badge> badges = gson.fromJson(response.getBody(), new TypeToken<ArrayList<Badge>>() {
        }.getType());

        System.out.println(response.getBody());
        assertEquals(1, badges.size());


        // Post more events
        for (int i = 0; i < 10; i++) {
            body = "{\n" +
                    "  \"data\": {},\n" +
                    "  \"type\": \"beep\",\n" +
                    "  \"user\": \"" + testUser.getProfileId() + "\"\n" +
                    "}";

            response = tester.post("/api/events/", null, body);

            if (HttpTestRequest.isError(response)) {
                System.out.println("Error creating event");
                return;
            }

            System.out.println("Created event");
        }

        // leave time to process the events (asynchronous)
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get more user badges
        gson = new Gson();

        response = tester.get("/api/users/" + testUser.getProfileId() + "/badges", null);

        if (HttpTestRequest.isError(response)) {
            System.out.println("Error getting badge list");
            return;
        }

        System.out.println("got user's badges");

        badges = gson.fromJson(response.getBody(), new TypeToken<ArrayList<Badge>>() {
        }.getType());

        System.out.println(response.getBody());
        assertEquals(2, badges.size());
    }

    //@After
    public void after() {
        baseDomainPostExec(domain, AFTER, tester);
    }

    private void displayCounter(CounterSummary counter) {
        System.out.println("\nNew counter!");
        System.out.println("Name: " + counter.name);
        System.out.println("Id: " + counter.id);
        System.out.println("Metrics: ");
        for (MetricSummary metric : counter.metrics) {
            System.out.println("\tId: " + metric.id);
            System.out.println("\tName: " + metric.name);
            System.out.println("\tDuration: " + metric.duration);
        }
    }
}
