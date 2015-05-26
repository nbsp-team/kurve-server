package frontend;

import frontend.servlet.SignOutServlet;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * nickolay, 03.04.15.
 */
public class SignOutServletTests extends ServletTests {
    @Test
    public void testNotAuth() throws ServletException, IOException {
        AbstractServlet signOutServlet = new SignOutServlet(socialAccountService);

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
        AbstractServlet signOutServlet = new SignOutServlet(socialAccountService);

        when(httpSession.getAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE)).thenReturn("abc");
        when(testRequest.getSession()).thenReturn(httpSession);
        when(testResponse.getWriter()).thenReturn(responsePrintWriter);

        signOutServlet.doPost(testRequest, testResponse);

        verify(testResponse.getWriter()).println(servletResponseCaptor.capture());

        String servletResponse = servletResponseCaptor.getValue();
        String expectedResponse = "{\"error\":null,\"response\":{}}";

        verify(httpSession, times(1)).removeAttribute(AbstractServlet.USER_ID_SESSION_ATTRIBUTE);
        assertEqualsJSON(servletResponse, expectedResponse);
    }
}