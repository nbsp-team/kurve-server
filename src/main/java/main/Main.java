package main;

import auth.AccountServiceInMemory;
import auth.MongoAccountService;
import auth.SocialAccountService;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import configuration.DatabaseConfig;
import configuration.GameMechanicsConfig;
import configuration.NetworkConfig;
import configuration.XmlLoader;
import frontend.FrontendService;
import frontend.SessionManager;
import frontend.servlet.*;
import game.GameService;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import websocket.GameWebSocketCreator;

import javax.servlet.Servlet;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * @author v.chibrikov
 */
public class Main {
    public static final Logger LOG = LogManager.getLogger(Main.class);

    public static final int API_VERSION = 1;

    public static final String NETWORK_CONFIG_FILE = "config/network.xml";
    public static final String MECHANICS_CONFIG_FILE = "config/mechanics.xml";
    public static final String DB_CONFIG_FILE = "config/db.xml";

    public static final NetworkConfig networkConfig =
            (NetworkConfig) XmlLoader.getInstance()
                    .load(NetworkConfig.class, NETWORK_CONFIG_FILE);

    public static final DatabaseConfig dbConfig =
            (DatabaseConfig) XmlLoader.getInstance()
                    .load(DatabaseConfig.class, DB_CONFIG_FILE);

    public static final GameMechanicsConfig mechanicsConfig =
            (GameMechanicsConfig) XmlLoader.getInstance()
                    .load(GameMechanicsConfig.class, MECHANICS_CONFIG_FILE);

    public static void main(String[] args) throws Exception {
        LOG.info(String.format("Starting server at: %s:%s", networkConfig.port, String.valueOf(networkConfig.port)));
        final MessageSystem messageSystem = new MessageSystem();

        final Thread accountServiceThread;
        SocialAccountService socialAccountService;
        if (Boolean.valueOf(dbConfig.offline)) {
            AccountServiceInMemory memoryAccountService = new AccountServiceInMemory(messageSystem);
            accountServiceThread = new Thread(memoryAccountService);
            socialAccountService = memoryAccountService;
            accountServiceThread.setDaemon(true);
            accountServiceThread.setName("Memory account service");
        } else {
            ServerAddress mongoServer = new ServerAddress(dbConfig.host, Integer.valueOf(dbConfig.port));
            MongoCredential credential = MongoCredential.createCredential(
                    dbConfig.username,
                    dbConfig.name,
                    dbConfig.password.toCharArray()
            );

            MongoClient mongoClient = new MongoClient(mongoServer, new ArrayList<MongoCredential>() {{
                add(credential);
            }});
            DB db = mongoClient.getDB(dbConfig.name);

            socialAccountService = new MongoAccountService(messageSystem, db);
            accountServiceThread = new Thread(socialAccountService);
            accountServiceThread.setDaemon(true);
            accountServiceThread.setName("Social account service");
        }

        FrontendService frontendService = new FrontendService(messageSystem);
        Thread frontendServiceThread = new Thread(frontendService);
        frontendServiceThread.setDaemon(true);
        frontendServiceThread.setName("Frontend service");
        frontendServiceThread.start();


        Server server = new Server(new InetSocketAddress(networkConfig.host, Integer.valueOf(networkConfig.port)));
        SessionManager sessionManager = new SessionManager();
        server.setSessionIdManager(sessionManager);

        Servlet socialSignIn = new SocialSignInServlet(frontendService, socialAccountService);
        Servlet signOut = new SignOutServlet(socialAccountService);

        Servlet user = new UserServlet(socialAccountService);
        Servlet rating = new RatingServlet(socialAccountService);
        Servlet serverStatus = new ServerStatusServlet(socialAccountService, sessionManager);
        Servlet serverShutdown = new ShutdownServlet(socialAccountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socialSignIn), "/api/v" + API_VERSION + "/auth/social");
        context.addServlet(new ServletHolder(signOut), "/api/v" + API_VERSION + "/auth/signout");
        context.addServlet(new ServletHolder(user), "/api/v" + API_VERSION + "/user/");
        context.addServlet(new ServletHolder(rating), "/api/v" + API_VERSION + "/rating/");
        context.addServlet(new ServletHolder(serverStatus), "/api/v" + API_VERSION + "/admin/status");
        context.addServlet(new ServletHolder(serverShutdown), "/api/v" + API_VERSION + "/admin/shutdown");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        GameService gameService = new GameService();
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(new GameWebSocketCreator(
                        sessionManager,
                        socialAccountService,
                        gameService
                ));
            }
        };

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{wsHandler, resourceHandler, context});

        server.setHandler(handlers);

        accountServiceThread.start();
        server.start();
        server.join();
    }
}