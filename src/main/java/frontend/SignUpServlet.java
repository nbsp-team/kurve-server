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

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Map<String, Object> responseData = new HashMap<>();

        // Быдлокод
        if(username == null || email == null || password == null) {
            responseData.put("error", ResponseCodes.ERROR);
            responseData.put("message", "Invalid request");

        } else if (!accountService.addUser(username, new UserProfile(username, password, email))) {
            responseData.put("error", ResponseCodes.ERROR);
            responseData.put("message", "User with name: " + username + " already exists");
        } else {

            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("email", username);

            responseData.put("error", ResponseCodes.OK);
            responseData.put("data", userData);
        }

        response.getWriter().println(JsonGenerator.toJson(responseData));
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
