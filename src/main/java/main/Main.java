package main;

import auth.MongoAccountService;
import auth.SocialAccountService;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dao.ScoresDao;
import frontend.SessionManager;
import frontend.servlet.*;
import game.GameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import websocket.GameWebSocketCreator;
import websocket.SocketServlet;

import javax.servlet.Servlet;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * @author v.chibrikov
 */
public class Main {
    public static final Logger LOG = LogManager.getLogger(Main.class);

    public static final int API_VERSION = 1;

    public static final String NETWORK_CONFIG_FILE = "config/network.cfg";
    public static final String MECHANICS_CONFIG_FILE = "config/mechanics.cfg";
    public static final String DB_CONFIG_FILE = "config/db.cfg";
    public static final String SOCIAL_CONFIG_FILE = "config/social.cfg";

    public static final Config networkConfig = ConfigFactory.parseFile(new File(NETWORK_CONFIG_FILE));
    public static final Config dbConfig = ConfigFactory.parseFile(new File(DB_CONFIG_FILE));
    public static final Config mechanicsConfig = ConfigFactory.parseFile(new File(MECHANICS_CONFIG_FILE));
    public static final Config socialConfig = ConfigFactory.parseFile(new File(SOCIAL_CONFIG_FILE));

    public static void main(String[] args) throws Exception {
        LOG.info(String.format("Starting server at: %s:%s",
                networkConfig.getString("host"),
                String.valueOf(networkConfig.getInt("port"))
        ));

        SocialAccountService socialAccountService;
        ServerAddress mongoServer = new ServerAddress(
                dbConfig.getString("host"), dbConfig.getInt("port"));

        MongoCredential credential = MongoCredential.createCredential(
                dbConfig.getString("username"),
                dbConfig.getString("dbname"),
                dbConfig.getString("password").toCharArray()
        );

        MongoClient mongoClient = new MongoClient(mongoServer, new ArrayList<MongoCredential>() {{
            add(credential);
        }});
        DB db = mongoClient.getDB(dbConfig.getString("dbname"));
        socialAccountService = new MongoAccountService(db);

        Server server = new Server(new InetSocketAddress(
                networkConfig.getString("host"), networkConfig.getInt("port")
        ));
        SessionManager sessionManager = new SessionManager();
        server.setSessionIdManager(sessionManager);

        Servlet socialSignIn = new SocialSignInServlet(socialAccountService);
        Servlet signOut = new SignOutServlet(socialAccountService);

        GameService gameService = new GameService(new ScoresDao(db));
        SocketServlet socketServlet = new SocketServlet(new GameWebSocketCreator(
                sessionManager,
                socialAccountService,
                gameService
        ));


        Servlet user = new UserServlet(socialAccountService);
        Servlet rating = new RatingServlet(socialAccountService, new ScoresDao(db));
        Servlet serverStatus = new ServerStatusServlet(socialAccountService, sessionManager, gameService);
        Servlet serverShutdown = new ShutdownServlet(socialAccountService);

        MobileUrlServlet mobileUrlServlet = new MobileUrlServlet(socialAccountService);
        MobileAuthServlet mobileAuthServlet = new MobileAuthServlet(sessionManager);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socialSignIn), "/api/v" + API_VERSION + "/auth/social");
        context.addServlet(new ServletHolder(signOut), "/api/v" + API_VERSION + "/auth/signout");
        context.addServlet(new ServletHolder(user), "/api/v" + API_VERSION + "/user/");
        context.addServlet(new ServletHolder(rating), "/api/v" + API_VERSION + "/rating/");
        context.addServlet(new ServletHolder(serverStatus), "/api/v" + API_VERSION + "/admin/status");
        context.addServlet(new ServletHolder(serverShutdown), "/api/v" + API_VERSION + "/admin/shutdown");
        context.addServlet(new ServletHolder(mobileUrlServlet), "/api/v" + API_VERSION + "/mobile/get");
        context.addServlet(new ServletHolder(mobileAuthServlet), "/api/v" + API_VERSION + "/mobile/auth");
        context.addServlet(new ServletHolder(socketServlet), "/socket/");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        server.setHandler(handlers);

        server.start();
        server.join();
    }
}