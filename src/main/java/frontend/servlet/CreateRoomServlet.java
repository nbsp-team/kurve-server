package frontend.servlet;

import auth.SocialAccountService;
import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.CreatedRoomResponse;
import frontend.response.Response;
import frontend.response.RoomsResponse;
import game.GameService;
import game.Room;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@AuthenticationRequired
public class CreateRoomServlet extends AbstractServlet {

    private final GameService gameService;

    public CreateRoomServlet(SocialAccountService socialAccountService, GameService gameService) {
        super(socialAccountService);
        this.gameService = gameService;
    }

    public Response onGet(HttpServletRequest request) {
        boolean isPrivate = false;

        String type = request.getParameter("type");
        if (type != null && type.equals("private")) {
            isPrivate = true;
        }

        UserProfile user = getUser(request);
        Room room = gameService.getRoomManager().createRoom(gameService, user, isPrivate);

        return new CreatedRoomResponse(room.getId());
    }
}