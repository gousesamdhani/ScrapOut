package cashin.scrapout;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by Sammy on 27/02/16.
 */
public class RequestParamsBuilder {

    public HashMap<String, String> reqParams;
    public HashMap<String, String> headerParams;

    public RequestParamsBuilder() {
        reqParams = new HashMap<String, String>();
        headerParams = new HashMap<String, String>();
    }

    public void addHeader(String key, String value) {
        headerParams.put(key, value);
    }

    public void addHeader(String key, JSONArray value) {
        headerParams.put(key, value.toString());
    }

    public void add(String key, String value) {
        reqParams.put(key, value);
    }

    public void add(String key, JSONArray value) {
        reqParams.put(key, value.toString());
    }

    public HashMap getReqParams() {
        return this.reqParams;
    }

    public HashMap getHeaderParams() {
        return this.headerParams;
    }
}
