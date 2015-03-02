package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AdminRightsRequired;
import frontend.response.Response;
import frontend.response.SuccessResponse;
import main.AccountService;

import javax.servlet.http.HttpServletRequest;

@AdminRightsRequired
public class ShutdownServlet extends AbstractServlet {
    public ShutdownServlet(AccountService accountService) {
        super(accountService);
    }

    public Response onPost(HttpServletRequest request) {
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
