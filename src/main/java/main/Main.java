package main;

import frontend.servlet.*;
import game.Game;
import interfaces.AccountService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import utils.SessionManager;
import websocket.GameWebSocketHandler;

import javax.servlet.Servlet;
import java.net.InetSocketAddress;

/**
 * @author v.chibrikov
 */
public class Main {
    public static final int API_VERSION = 1;

    public static void main(String[] args) throws Exception {

        int port = 8080;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        Server server = new Server(new InetSocketAddress("0.0.0.0", port));
        SessionManager sessionManager = new SessionManager();
        server.setSessionIdManager(sessionManager);

        AccountService accountService = new MemoryAccountService();

        // Create servlets
        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet user = new UserServlet(accountService);
        Servlet rating = new RatingServlet(accountService);
        Servlet serverStatus = new ServerStatusServlet(accountService, sessionManager);
        Servlet serverShutdown = new ShutdownServlet(accountService);

        // Build web app context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signIn), "/api/v" + API_VERSION + "/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v" + API_VERSION + "/auth/signup");
        context.addServlet(new ServletHolder(signOut), "/api/v" + API_VERSION + "/auth/signout");
        context.addServlet(new ServletHolder(user), "/api/v" + API_VERSION + "/user/");
        context.addServlet(new ServletHolder(rating), "/api/v" + API_VERSION + "/rating/");
        context.addServlet(new ServletHolder(serverStatus), "/api/v" + API_VERSION + "/admin/status");
        context.addServlet(new ServletHolder(serverShutdown), "/api/v" + API_VERSION + "/admin/shutdown");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");


        // Init game
        Game game = new Game(accountService);

        // Create WebSocketHandler
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(new GameWebSocketHandler.GameWebSocketCreator(
                        sessionManager,
                        accountService,
                        game
                ));
            }
        };

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{wsHandler, resourceHandler, context});

        server.setHandler(handlers);

        server.start();
        server.join();
    }
}