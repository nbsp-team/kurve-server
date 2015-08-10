package frontend.servlet;

import auth.SocialAccountService;
import frontend.AbstractServlet;
import frontend.annotation.AuthenticationRequired;
import frontend.response.ErrorResponse;
import frontend.response.Response;
import frontend.response.RoomIdResponse;
import game.GameService;
import game.Room;
import model.UserProfile;

import javax.servlet.http.HttpServletRequest;

@AuthenticationRequired
public class RandomRoomServlet extends AbstractServlet {

    private final GameService gameService;

    public RandomRoomServlet(SocialAccountService socialAccountService, GameService gameService) {
        super(socialAccountService);
        this.gameService = gameService;
    }

    public Response onGet(HttpServletRequest request) {
        Room room = gameService.getRoomManager().findFreePublicRoom(getUser(request));

        if (room != null) {
            return new RoomIdResponse(room.getId());
        } else {
            return new RoomIdResponse(null);
        }
    }
}