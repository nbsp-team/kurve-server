package websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * nickolay, 30.05.15.
 */
@WebServlet(name = "WebSocketServlet", urlPatterns = {"/socket"})
public class SocketServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private WebSocketCreator webSocketCreator;

    public SocketServlet(WebSocketCreator creator) {
        this.webSocketCreator = creator;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(webSocketCreator);
    }

}