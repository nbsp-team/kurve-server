package frontend.servlet;

import auth.SocialAccountService;
import dao.ScoresDao;
import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.RatingResponse;
import frontend.response.Response;
import frontend.response.RoomsResponse;
import game.GameService;
import game.Room;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@AuthenticationRequired
public class RoomsServlet extends AbstractServlet {

    private final GameService gameService;

    public RoomsServlet(SocialAccountService socialAccountService, GameService gameService) {
        super(socialAccountService);
        this.gameService = gameService;
    }

    public Response onGet(HttpServletRequest request) {
        Collection<Room> rooms = gameService.getRooms();
        return new RoomsResponse(rooms);
    }
}