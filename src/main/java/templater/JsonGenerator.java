package templater;

import com.oracle.javafx.jmx.json.JSONDocument;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.json.simple.JSONObject;
import sun.org.mozilla.javascript.internal.json.JsonParser;
import utils.ResponseCodes;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class JsonGenerator {

    public static String toJson(Map<String, Object> data) {

        Map<String, Object> returnObject = new HashMap<>();

        if(data.get("error") == ResponseCodes.OK) {
            returnObject.put("error", null);

            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("user", data.get("data"));
            returnObject.put("response", responseObject);
        } else {
            returnObject.putAll(data);
        }

        return new JSONObject(returnObject).toJSONString();
    }



}
