package ch.heig.amt.g4mify.Utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Le Poulet Suisse on 05.12.2016.
 */
public class HttpTestRequest {

    public String test(String endpoint, String payload, HashMap<String, String> headers, String method){
        HttpURLConnection conn = null;

        try{
            //create connection
            URL url = new URL("http://localhost:8080" + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            if(headers != null){
                for(Map.Entry<String, String> header : headers.entrySet()){
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            //Send request
            if(payload != null){
                DataOutputStream wr = new DataOutputStream (
                        conn.getOutputStream());
                wr.writeBytes(payload);
                wr.close();
            }

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
