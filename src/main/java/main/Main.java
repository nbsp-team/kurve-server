package main;

import configuration.ApplicationConfig;
import frontend.servlet.*;
import game.GameManager;
import interfaces.AccountService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import configuration.ConfigLoader;
import utils.SessionManager;
import websocket.GameWebSocketHandler;

import javax.servlet.Servlet;
import java.net.InetSocketAddress;

/**
 * @author v.chibrikov
 */
public class Main {
    public static final int API_VERSION = 1;
    public static final ApplicationConfig appConfig =
            ConfigLoader.getInstance().loadApplicationConfig();

    public static void main(String[] args) throws Exception {

        System.out.append("Starting at port: ").append(String.valueOf(appConfig.getPort())).append("\n");
        System.out.println(appConfig.getHost());
        Server server = new Server(new InetSocketAddress(appConfig.getHost(), appConfig.getPort()));
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
        GameManager gameManager = new GameManager(accountService);

        // Create WebSocketHandler
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(new GameWebSocketHandler.GameWebSocketCreator(
                        sessionManager,
                        accountService,
                        gameManager
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