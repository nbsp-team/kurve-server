package frontend;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import frontend.servlet.SignOutServlet;
import interfaces.AccountService;
import main.MemoryAccountService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * nickolay, 03.04.15.
 */
public class SignOutServletTests {
    @Test
    public void testNotAuth() throws ServletException, IOException {
        AccountService accountService = new MemoryAccountService();
        AbstractServlet signOutServlet = new SignOutServlet(accountService);

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        HttpServletResponse testResponse = mock(HttpServletResponse.class);
        HttpSession testSession = mock(HttpSession.class);
        PrintWriter responsePrintWriter = mock(PrintWriter.class);
        ArgumentCaptor<String> servletResponseCaptor = ArgumentCaptor.forClass(String.class);

        when(testRequest.getSession()).thenReturn(testSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signOutServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String rightResponse = "{\"error\":{\"code\":4,\"description\":\"Ошибка доступа\"}}";

        assertEqualsJSON(servletResponse, rightResponse);
    }

    @Test
    public void testOk() throws ServletException, IOException {
        AccountService accountService = new MemoryAccountService();
        AbstractServlet signOutServlet = new SignOutServlet(accountService);

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        HttpServletResponse testResponse = mock(HttpServletResponse.class);
        HttpSession testSession = mock(HttpSession.class);
        PrintWriter responsePrintWriter = mock(PrintWriter.class);
        ArgumentCaptor<String> servletResponseCaptor = ArgumentCaptor.forClass(String.class);

        when(testSession.getAttribute("username")).thenReturn("abc");
        when(testRequest.getSession()).thenReturn(testSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signOutServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String rightResponse = "{\"error\":null,\"response\":{}}";

        verify(testSession, times(1)).removeAttribute("username");
        assertEqualsJSON(servletResponse, rightResponse);
    }

    private static void assertEqualsJSON(String json1, String json2) {
        JsonParser parser = new JsonParser();
        JsonElement o1 = parser.parse(json1);
        JsonElement o2 = parser.parse(json2);

        assertTrue(o1.equals(o2));
    }
}