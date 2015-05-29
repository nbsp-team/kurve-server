package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AdminRightsRequired;
import frontend.response.Response;
import frontend.response.ServerStatusResponse;
import auth.SocialAccountService;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import service.ServiceManager;

import javax.servlet.http.HttpServletRequest;

@AdminRightsRequired
public class ServerStatusServlet extends AbstractServlet {
    private HashSessionIdManager sessionIdManager;

    public ServerStatusServlet(ServiceManager serviceManager, SocialAccountService socialAccountService, HashSessionIdManager sessionIdManager) {
        super(serviceManager, socialAccountService);
        this.sessionIdManager = sessionIdManager;
    }

    public Response onGet(HttpServletRequest request) {
        long userCount = socialAccountService.getUserCount();
        long sessionCount = sessionIdManager.getSessions().size();
        return new ServerStatusResponse(userCount, sessionCount);
    }
}
