package frontend;

import frontend.servlet.SignInServlet;
import interfaces.AccountService;
import main.AccountServiceInMemory;
import model.UserProfile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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
public class SignInServletTests extends ServletTests {
    @Test
    public void testNotRegistered() throws ServletException, IOException {
        AbstractServlet signInServlet = new SignInServlet(accountService);

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
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);

        when(testRequest.getParameter("username")).thenReturn("abc");
        when(testRequest.getParameter("password")).thenReturn("def");
        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String expectedResponse = "{\"error\":null,\"response\":{\"user\":{\"username\":\"abc\",\"email\":\"def@gmail.com\",\"global_rating\":0}}}";

        verify(httpSession, times(1)).setAttribute(eq("username"), Mockito.anyObject());
        assertEqualsJSON(servletResponse, expectedResponse);
    }

    @Test
    public void testIncorrectPassword() throws ServletException, IOException {
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);
        when(testRequest.getParameter("username")).thenReturn("abc");
        when(testRequest.getParameter("password")).thenReturn("123");

        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String expectedResponse = "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя\"}}";

        assertEqualsJSON(servletResponse, expectedResponse);
    }

    @Test
    public void testWithoutPasswordParam() throws ServletException, IOException {
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);

        when(testRequest.getParameter("username")).thenReturn("abc");
        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String expectedResponse = "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя\"}}";

        assertEqualsJSON(servletResponse, expectedResponse);
    }

    @Test
    public void testWithoutUserParam() throws ServletException, IOException {
        accountService.addUser(new UserProfile("abc", "def", "def@gmail.com"));

        AbstractServlet signInServlet = new SignInServlet(accountService);

        when(testRequest.getParameter("password")).thenReturn("def");
        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signInServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String expectedResponse = "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя\"}}";

        assertEqualsJSON(servletResponse, expectedResponse);
    }
}