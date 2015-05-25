package frontend;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import interfaces.SocialAccountService;
import org.junit.Before;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * nickolay, 18.04.15.
 */
public class ServletTests {
    protected SocialAccountService socialAccountService;
    protected HttpServletRequest testRequest;
    protected HttpServletResponse testResponse;
    protected PrintWriter responsePrintWriter;
    protected ArgumentCaptor<String> servletResponseCaptor;
    protected HttpSession httpSession;

    @Before
    public void setUp() {
        socialAccountService = mock(SocialAccountService.class);
        testRequest = mock(HttpServletRequest.class);
        testResponse = mock(HttpServletResponse.class);
        responsePrintWriter = mock(PrintWriter.class);
        servletResponseCaptor = ArgumentCaptor.forClass(String.class);
        httpSession = mock(HttpSession.class);
    }

    protected static void assertEqualsJSON(String json1, String json2) {
        JsonParser parser = new JsonParser();
        JsonElement o1 = parser.parse(json1);
        JsonElement o2 = parser.parse(json2);

        assertTrue(o1.equals(o2));
    }
}
