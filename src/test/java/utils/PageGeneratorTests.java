package utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * nickolay, 22.05.15.
 */
public class PageGeneratorTests {
    @Test
    public void testParams() {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("authSuccess", "true");
        String result = PageGenerator.getPage("social_signin_popup.html", pageVariables);
        String expectedResult = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <script>\n" +
                "        window.opener.onSocialAuth(true);\n" +
                "        window.close();\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body></body>\n" +
                "</html>";

        assertEquals(result, expectedResult);
    }
}
