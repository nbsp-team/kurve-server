package frontend.response;

import com.google.gson.GsonBuilder;
import frontend.AbstractJsonMessage;
import frontend.response.serializer.*;
import game.Player;
import game.Room;
import model.UserProfile;
import websocket.message.serializer.PlayerSerializer;

/**
 * nickolay, 25.02.15.
 */
public class Response  extends AbstractJsonMessage{
    Response() {
        gson = new GsonBuilder()
                .registerTypeAdapter(ErrorResponse.class, new ErrorResponseSerializer())
                .registerTypeAdapter(PermissionDeniedErrorResponse.class, new ErrorResponseSerializer())
                .registerTypeAdapter(AuthErrorResponse.class, new ErrorResponseSerializer())
                .registerTypeAdapter(GetUserResponse.class, new GetUserResponseSerializer())
                .registerTypeAdapter(RatingResponse.class, new RatingResponseSerializer())
                .registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponseSerializer())
                .registerTypeAdapter(SuccessResponse.class, new SuccessResponseSerializer())
                .registerTypeAdapter(GetMobileUrlResponse.class, new GetMobileUrlResponseSerializer())
                .registerTypeAdapter(RoomsResponse.class, new RoomsSerializer())
                .registerTypeAdapter(CreatedRoomResponse.class, new CreatedRoomResponseSerializer())

                .registerTypeAdapter(UserProfile.class, new UserProfileSerializer())
                .registerTypeAdapter(Player.class, new PlayerSerializer())
                .registerTypeAdapter(Room.class, new RoomSerializer())

                .serializeNulls()
                .create();
    }
}
