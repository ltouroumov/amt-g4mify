package ch.heig.amt.g4mify.Utils;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.junit.Test;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.fail;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class HttpTestRequest {

    private static final int minGoodStatusCode = 100;
    private static final int maxGoodStatusCode = 399;
    private static final String baseURL = "http://localhost:8080";

    private Unirest unirest = null;

    public HttpTestRequest(){
        unirest = new Unirest();
        setDefaultHeader("Accept", "application/json");
        setDefaultHeader("Content-Type", "application/json");
    }

    public void setDefaultHeaders(HashMap<String, String> headers){
        for(Map.Entry<String, String> header : headers.entrySet()){
            Unirest.setDefaultHeader(header.getKey(), header.getValue());
        }
    }

    public void setDefaultHeader(String name, String value){
        Unirest.setDefaultHeader(name, value);
    }

    public TestResponse get(String endpoint, HashMap<String, Object> parameters){
        TestResponse response = null;
        if(endpoint == null) throw new RuntimeException("GET: No URL!");
        try{
            String url = new URL(baseURL + endpoint).toString();
            HttpRequest request = unirest.get(url);
            if(parameters != null){
                request.queryString(parameters);
            }
            HttpResponse<JsonNode> responseUnirest = request.asJson();
            response = new TestResponse(
                    responseUnirest.getStatus(),
                    responseUnirest.getBody().toString(),
                    url);

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return response;
    }

    public TestResponse post(String endpoint, HashMap<String, Object> parameters, String body){
        TestResponse response = null;
        if(endpoint == null) throw new RuntimeException("POST: No URL!");
        try{
            String url = new URL(baseURL + endpoint).toString();
            HttpRequestWithBody request = unirest.post(url);
            if(parameters != null){
                request.queryString(parameters);
            }
            if(body != null){
                request.body(body);
            }

            HttpResponse<JsonNode> responseUnirest = request.asJson();
            response = new TestResponse(
                    responseUnirest.getStatus(),
                    responseUnirest.getBody().toString(),
                    url);

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return response;
    }

    public TestResponse delete(String endpoint){
        TestResponse response = null;
        if(endpoint == null) throw new RuntimeException("DELETE: No URL!");
        try{
            String url = new URL(baseURL + endpoint).toString();
            HttpRequest request = unirest.get(url);
            HttpResponse<JsonNode> responseUnirest = request.asJson();
            response = new TestResponse(
                    responseUnirest.getStatus(),
                    responseUnirest.getBody().toString(),
                    url);

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return response;
    }

    public static boolean isError(TestResponse response){
        if(response.getStatusCode() < minGoodStatusCode || response.getStatusCode() > maxGoodStatusCode){
            System.err.println("Error: " + response.getStatusCode());
            System.err.println("\t" + response.getBody());
            fail("Got " + response.getStatusCode() + ", see the error message in the console!");
            return true;
        }
        return false;
    }
}
