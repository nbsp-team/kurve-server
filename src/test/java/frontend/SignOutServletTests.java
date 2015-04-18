package frontend;

import frontend.servlet.SignOutServlet;
import interfaces.AccountService;
import main.AccountServiceInMemory;
import org.junit.Before;
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
public class SignOutServletTests extends ServletTests {
    @Test
    public void testNotAuth() throws ServletException, IOException {
        AbstractServlet signOutServlet = new SignOutServlet(accountService);

        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signOutServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String expectedResponse = "{\"error\":{\"code\":4,\"description\":\"Ошибка доступа\"}}";

        assertEqualsJSON(servletResponse, expectedResponse);
    }

    @Test
    public void testOk() throws ServletException, IOException {
        AbstractServlet signOutServlet = new SignOutServlet(accountService);

        when(httpSession.getAttribute("username")).thenReturn("abc");
        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signOutServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String expectedResponse = "{\"error\":null,\"response\":{}}";

        verify(httpSession, times(1)).removeAttribute("username");
        assertEqualsJSON(servletResponse, expectedResponse);
    }
}