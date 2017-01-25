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
    public static final int BEFORE = 0;
    public static final int BEFORE_CLASS = 1;
    public static final int AFTER = 0;
    public static final int AFTER_CLASS = 1;

    private static String domainName = "TestDomain";

    public static Domain baseDomainInit(int type, HttpTestRequest testerFrom){
        HttpTestRequest tester = testerFrom;
        System.out.print("\n");
        if(type == BEFORE){
            System.out.println("- BEFORE -");
        }else if(type == BEFORE_CLASS){
            System.out.println("-- BEFORE CLASS --");
        }else{
            System.err.println("Error in the type specified in before");
        }
        Gson gson = new Gson();
        TestResponse response = tester.post("/register", null, "{\"name\": \"" + domainName + "\"}");
        if(isError(response)) return null;
        Domain testDomain = gson.fromJson(response.getBody(), Domain.class);
        if(response.getStatusCode() == 201){
            System.out.println("Correctly create a new domain with name " + domainName + " and ID " + testDomain.getId());
        }else{
            throw new RuntimeException("Couldn't create a new domain!");
        }
        return testDomain;
    }

    public static void baseDomainPostExec(Domain testDomain, int type, HttpTestRequest testerFrom){
        HttpTestRequest tester = testerFrom;
        System.out.print("\n");
        if(type == AFTER){
            System.out.println("- AFTER -");
        }else if(type == AFTER_CLASS){
            System.out.println("-- AFTER CLASS --");
        }else{
            System.err.println("Error in the type specified in after!");
        }
        //tester.setDefaultHeader("Identity", testDomain.getId() + ":" + testDomain.getKey());
        TestResponse response = tester.delete("/api/domain");
        if(isError(response)) return;
        if(response.getStatusCode() == 200){
            System.out.println("Correctly deleted domain " + testDomain.getId());
        }else{
            throw new RuntimeException("Couldn't delete the domain " + testDomain.getId() + "! Please run the DomainApiTest test class!");
        }
    }
}
