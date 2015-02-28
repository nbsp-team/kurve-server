package frontend.servlet.admin;

import frontend.AbstractAdminServlet;
import frontend.response.Response;
import frontend.response.SuccessResponse;
import main.AccountService;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

public class ShutdownServlet extends AbstractAdminServlet {
    private AccountService accountService;

    public ShutdownServlet(AccountService accountService) {
        super(accountService);
        this.accountService = accountService;
    }

    public Response onPost(HttpServletRequest request, UserProfile userProfile) {
        String timeString = request.getParameter("time");
        if (timeString != null) {
            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.print("\nShutdown.");
            System.exit(0);
        }

        return new SuccessResponse();
    }

    public static class TimeHelper {
        public static void sleep(int period){
            try{
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
