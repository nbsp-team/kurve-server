package frontend.response;

/**
 * nickolay, 28.02.15.
 */
public class RoomIdResponse extends SuccessResponse {
    private final String roomId;

    public RoomIdResponse(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}