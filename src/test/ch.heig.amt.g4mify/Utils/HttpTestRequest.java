package ch.heig.amt.g4mify.Utils;

import com.google.gson.Gson;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.fail;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class HttpTestRequest {

    private static final int minGoodStatusCode = 100;
    private static final int maxGoodStatusCode = 399;
    private static final String baseURL = "http://localhost:8080";

    public TestResponse test(String endpoint, String payload, HashMap<String, String> parameters, HashMap<String, String> headers, String method){
        HttpURLConnection conn = null;
        Gson gson = new Gson();
        try {
            StringBuilder parametersURL = new StringBuilder();
            if(parameters != null){
                parametersURL.append("?");
                for(Map.Entry<String, String> parameter : parameters.entrySet()){
                    parametersURL.append(parameter.getKey() + "=" + parameter.getValue() + "&");
                }
                parametersURL.deleteCharAt(parametersURL.length() - 1);
            }
            //create connection
            URL url = new URL(baseURL + endpoint + parametersURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            //Send request
            if (payload != null) {
                DataOutputStream wr = new DataOutputStream(
                        conn.getOutputStream());
                wr.writeBytes(payload);
                wr.close();
            }

            //Get Response
            HashMap<String, Object> error = null;
            String bodyMessage = null;
            InputStream is;

            if(conn.getResponseCode() < minGoodStatusCode || conn.getResponseCode() > maxGoodStatusCode){
                is = conn.getErrorStream();
                System.out.println("ErrorStream: " + is);
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder resultIs = new StringBuilder(); // or StringBuffer if Java version 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    resultIs.append(line);
                    resultIs.append('\n');
                }
                rd.close();
                HashMap<String,Object> map = new HashMap<>();
                map = (HashMap<String,Object>) gson.fromJson(resultIs.toString(), map.getClass());
                error = map;
            }else{
                is = conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder resultIs = new StringBuilder(); // or StringBuffer if Java version 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    resultIs.append(line);
                    resultIs.append('\n');
                }
                rd.close();
                bodyMessage = resultIs.toString();
            }
            TestResponse response = new TestResponse(
                    conn.getResponseCode(),
                    bodyMessage,
                    conn.getContentLength(),
                    conn.getContentType(),
                    conn.getDate(),
                    conn.getURL());
            if(error != null){
                response.setError(error);
            }
            return response;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static boolean isError(TestResponse response){
        if(response.getStatusCode() < minGoodStatusCode || response.getStatusCode() > maxGoodStatusCode){
            System.err.println("Error: " + response.getStatusCode());
            for(Map.Entry<String, Object> entry : response.getError().entrySet()){
                System.err.println("\t" + entry.getKey() + ": " + entry.getValue());
            }
            fail("Got " + response.getStatusCode() + ", see the error message in the console!");
            return true;
        }
        return false;
    }
}
