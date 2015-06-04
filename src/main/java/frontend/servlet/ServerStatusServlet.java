package frontend.servlet;

import auth.SocialAccountService;
import frontend.AbstractServlet;
import frontend.annotation.AdminRightsRequired;
import frontend.response.Response;
import frontend.response.ServerStatusResponse;
import game.GameService;
import org.eclipse.jetty.server.session.HashSessionIdManager;

import javax.servlet.http.HttpServletRequest;

@AdminRightsRequired
public class ServerStatusServlet extends AbstractServlet {
    private final GameService gameService;
    private HashSessionIdManager sessionIdManager;

    public ServerStatusServlet(SocialAccountService socialAccountService, HashSessionIdManager sessionIdManager, GameService gameService) {
        super(socialAccountService);
        this.sessionIdManager = sessionIdManager;
        this.gameService = gameService;
    }

    public Response onGet(HttpServletRequest request) {
        long userCount = socialAccountService.getUserCount();
        long sessionCount = sessionIdManager.getSessions().size();
        return new ServerStatusResponse(userCount, sessionCount, gameService.getRoomCount());
    }
}
