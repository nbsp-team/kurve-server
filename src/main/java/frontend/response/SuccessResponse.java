package frontend.response;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * nickolay, 25.02.15.
 */
public class SuccessResponse extends Response {
    public SuccessResponse() {
        rootJson.put("error", null);
    }

    public SuccessResponse(Map<String, Object> data) {
        this();
        setData(data);
    }

    public void setData(Map<String, Object> data) {
        JSONObject responseJson = new JSONObject(data);
        rootJson.put("response", responseJson);
    }
}
