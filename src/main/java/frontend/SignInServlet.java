package frontend;

import main.AccountService;
import model.UserProfile;
import templater.JsonGenerator;
import templater.PageGenerator;
import utils.ResponseCodes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("username");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> responseData = new HashMap<>();
        UserProfile profile = accountService.getUser(name);

        if (profile != null && profile.getPassword().equals(password)) {
            Map<String, Object> userData = new HashMap<>();

            userData.put("username", profile.getLogin());
            userData.put("email", profile.getEmail());

            responseData.put("error", ResponseCodes.OK);
            responseData.put("data", userData);

        } else {
            responseData.put("error", ResponseCodes.ERROR);
            responseData.put("message", "Wrong login/password");
        }

        response.getWriter().println(JsonGenerator.toJson(responseData));
    }
}
