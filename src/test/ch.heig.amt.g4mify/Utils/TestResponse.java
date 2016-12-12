package ch.heig.amt.g4mify.Utils;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by Le Poulet Suisse on 12.12.2016.
 */
public class TestResponse {
    private int statusCode;
    private HashMap<String, Object> error;
    private String body;
    private int contentLength;
    private String contentType;
    private long date;
    private URL url;

    public TestResponse(){

    }

    public TestResponse(int statusCode, String body, int contentLength, String contentType, long date, URL url) {
        this.statusCode = statusCode;
        this.body = body;
        this.contentLength = contentLength;
        this.contentType = contentType;
        this.date = date;
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

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public HashMap<String, Object> getError() {
        return error;
    }

    public void setError(HashMap<String, Object> error) {
        this.error = error;
    }
}
