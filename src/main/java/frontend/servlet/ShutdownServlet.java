package frontend.servlet;

import frontend.AbstractServlet;
import frontend.annotation.AdminRightsRequired;
import frontend.response.Response;
import frontend.response.SuccessResponse;
import auth.SocialAccountService;
import service.ServiceManager;

import javax.servlet.http.HttpServletRequest;

@AdminRightsRequired
public class ShutdownServlet extends AbstractServlet {
    public ShutdownServlet(ServiceManager serviceManager, SocialAccountService socialAccountService) {
        super(serviceManager, socialAccountService);
    }

    public Response onPost(HttpServletRequest request) {
        String timeString = request.getParameter("time");
        if (timeString != null) {
            int timeMS = Integer.valueOf(timeString);

            (new Thread(() -> {
                TimeHelper.sleep(timeMS);
                System.exit(0);
            })).start();
        }

        return new SuccessResponse();
    }

    public static class TimeHelper {
        public static void sleep(int period) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
