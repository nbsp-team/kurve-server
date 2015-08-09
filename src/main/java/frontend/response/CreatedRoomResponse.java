package frontend.response;

/**
 * nickolay, 28.02.15.
 */
public class CreatedRoomResponse extends SuccessResponse {
    private final String roomId;

    public CreatedRoomResponse(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}