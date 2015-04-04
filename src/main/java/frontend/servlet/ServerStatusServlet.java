package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AdminRightsRequired;
import frontend.response.Response;
import frontend.response.ServerStatusResponse;
import interfaces.AccountService;
import org.eclipse.jetty.server.session.HashSessionIdManager;

import javax.servlet.http.HttpServletRequest;

@AdminRightsRequired
public class ServerStatusServlet extends AbstractServlet {
    private HashSessionIdManager sessionIdManager;

    public ServerStatusServlet(AccountService accountService, HashSessionIdManager sessionIdManager) {
        super(accountService);
        this.sessionIdManager = sessionIdManager;
    }

    public Response onGet(HttpServletRequest request) {
        long userCount = accountService.getUserCount();
        long sessionCount = sessionIdManager.getSessions().size();
        return new ServerStatusResponse(userCount, sessionCount);
    }
}
