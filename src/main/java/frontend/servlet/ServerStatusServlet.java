package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AdminRightsRequired;
import frontend.annotation.ApiMethod;
import frontend.response.Response;
import frontend.response.ServerStatusResponse;
import main.AccountService;
import org.eclipse.jetty.server.session.HashSessionIdManager;

import javax.servlet.http.HttpServletRequest;

@AdminRightsRequired
public class ServerStatusServlet extends AbstractServlet {
    private HashSessionIdManager sessionIdManager;

    public ServerStatusServlet(AccountService accountService, HashSessionIdManager sessionIdManager) {
        super(accountService);
        this.sessionIdManager = sessionIdManager;
    }

    @ApiMethod
    public Response getServerStatus(HttpServletRequest request) {
        int userCount = accountService.getUserCount();
        int sessionCount = sessionIdManager.getSessions().size();
        return new ServerStatusResponse(userCount, sessionCount);
    }
}
