package main;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import configuration.DatabaseConfig;
import configuration.GameMechanicsConfig;
import configuration.NetworkConfig;
import configuration.XmlLoader;
import frontend.SessionManager;
import frontend.servlet.*;
import game.GameManager;
import interfaces.AccountService;
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

        Server server = new Server(new InetSocketAddress(networkConfig.host, Integer.valueOf(networkConfig.port)));
        SessionManager sessionManager = new SessionManager();
        server.setSessionIdManager(sessionManager);
        AccountService accountService;
        if(Boolean.valueOf(dbConfig.inMemory)) {
            accountService = new AccountServiceInMemory();
        } else {
            accountService = new MongoAccountService(db);
        }
        Servlet signIn = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet user = new UserServlet(accountService);
        Servlet rating = new RatingServlet(accountService);
        Servlet serverStatus = new ServerStatusServlet(accountService, sessionManager);
        Servlet serverShutdown = new ShutdownServlet(accountService);

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

        GameManager gameManager = new GameManager();

        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(new GameWebSocketCreator(
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