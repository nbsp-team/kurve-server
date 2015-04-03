package frontend;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import frontend.servlet.SignInServlet;
import interfaces.AccountService;
import main.MemoryAccountService;
import model.UserProfile;
import org.eclipse.jetty.client.HttpRequest;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * nickolay, 03.04.15.
 */
public class SignInServletTests {
    @Test
    public void testNotRegistered() throws ServletException, IOException {
        AccountService accountService = new MemoryAccountService();
        AbstractServlet signInServlet = new SignInServlet(accountService);

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        HttpServletResponse testResponse = mock(HttpServletResponse.class);
        PrintWriter responsePrintWriter = mock(PrintWriter.class);
        ArgumentCaptor<String> servletResponseCaptor = ArgumentCaptor.forClass(String.class);

        when(testRequest.getParameter("username")).thenReturn("abc");
        when(testRequest.getParameter("password")).thenReturn("def");
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String rightResponse = "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя\"}}";

        assertEqualsJSON(servletResponse, rightResponse);
    }

    @Test
    public void testOk() throws ServletException, IOException {
        AccountService accountService = new MemoryAccountService();
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        HttpServletResponse testResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);
        PrintWriter responsePrintWriter = mock(PrintWriter.class);
        ArgumentCaptor<String> servletResponseCaptor = ArgumentCaptor.forClass(String.class);

        when(testRequest.getParameter("username")).thenReturn("abc");
        when(testRequest.getParameter("password")).thenReturn("def");
        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String rightResponse = "{\"error\":null,\"response\":{\"user\":{\"username\":\"abc\",\"email\":\"def@gmail.com\",\"global_rating\":0}}}";

        assertEqualsJSON(servletResponse, rightResponse);
    }

    @Test
    public void testIncorrectPassword() throws ServletException, IOException {
        AccountService accountService = new MemoryAccountService();
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        HttpServletResponse testResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);
        PrintWriter responsePrintWriter = mock(PrintWriter.class);
        ArgumentCaptor<String> servletResponseCaptor = ArgumentCaptor.forClass(String.class);

        when(testRequest.getParameter("username")).thenReturn("abc");
        when(testRequest.getParameter("password")).thenReturn("123");

        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String rightResponse = "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя\"}}";

        assertEqualsJSON(servletResponse, rightResponse);
    }

    @Test
    public void testWithoutPasswordParam() throws ServletException, IOException {
        AccountService accountService = new MemoryAccountService();
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        HttpServletResponse testResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);
        PrintWriter responsePrintWriter = mock(PrintWriter.class);
        ArgumentCaptor<String> servletResponseCaptor = ArgumentCaptor.forClass(String.class);

        when(testRequest.getParameter("username")).thenReturn("abc");
        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String rightResponse = "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя\"}}";

        assertEqualsJSON(servletResponse, rightResponse);
    }

    @Test
    public void testWithoutUserParam() throws ServletException, IOException {
        AccountService accountService = new MemoryAccountService();
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);

        HttpServletRequest testRequest = mock(HttpServletRequest.class);
        HttpServletResponse testResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);
        PrintWriter responsePrintWriter = mock(PrintWriter.class);
        ArgumentCaptor<String> servletResponseCaptor = ArgumentCaptor.forClass(String.class);

        when(testRequest.getParameter("password")).thenReturn("def");

        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String rightResponse = "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя\"}}";

        assertEqualsJSON(servletResponse, rightResponse);
    }

    private static void assertEqualsJSON(String json1, String json2) {
        JsonParser parser = new JsonParser();
        JsonElement o1 = parser.parse(json1);
        JsonElement o2 = parser.parse(json2);

        assertTrue(o1.equals(o2));
    }
}