package ch.heig.amt.g4mify.Utils;

import java.net.URL;

/**
 * Created by Le Poulet Suisse on 12.12.2016.
 */
public class TestResponse {
    private int statusCode;
    private String body;
    private String url;

    public TestResponse(){

    }

    public TestResponse(int statusCode, String body, String url) {
        this.statusCode = statusCode;
        this.body = body;
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}