package frontend.response;

import org.json.simple.JSONObject;

/**
 * nickolay, 25.02.15.
 */
public class Response {
    protected JSONObject rootJson = new JSONObject();

    public String getJSON() {
        return rootJson.toJSONString();
    }
}
