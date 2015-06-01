package frontend.servlet;

import auth.SocialAccountService;
import dao.ScoresDao;
import frontend.AbstractServlet;
import frontend.response.RatingResponse;
import frontend.response.Response;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class RatingServlet extends AbstractServlet {
    public static final int TOP_SIZE = 15;
    private final ScoresDao scoresDao;

    public RatingServlet(SocialAccountService socialAccountService, ScoresDao scoresDao) {
        super(socialAccountService);
        this.scoresDao = scoresDao;
    }

    public Response onGet(HttpServletRequest request) {
        List<UserProfile> ratings = scoresDao.getTop(TOP_SIZE);
        return new RatingResponse(ratings);
    }
}