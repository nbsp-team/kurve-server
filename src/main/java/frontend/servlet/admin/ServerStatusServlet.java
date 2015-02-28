package frontend.servlet.admin;

import frontend.AbstractAdminServlet;
import frontend.response.Response;
import frontend.response.admin.ServerStatusResponse;
import main.AccountService;
import model.UserProfile;
import org.eclipse.jetty.server.session.HashSessionIdManager;

import javax.servlet.http.HttpServletRequest;

public class ServerStatusServlet extends AbstractAdminServlet {
    private AccountService accountService;
    private HashSessionIdManager sessionIdManager;

    public ServerStatusServlet(AccountService accountService, HashSessionIdManager sessionIdManager) {
        super(accountService);
        this.accountService = accountService;
        this.sessionIdManager = sessionIdManager;
    }

    public Response onGet(HttpServletRequest request, UserProfile userProfile) {
        int userCount = accountService.getUserCount();
        int sessionCount = sessionIdManager.getSessions().size();
        return new ServerStatusResponse(userCount, sessionCount);
    }
}
