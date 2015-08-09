package frontend.response;

import game.Room;

import java.util.Collection;

/**
 * nickolay, 28.02.15.
 */
public class RoomsResponse extends SuccessResponse {
    private final Collection<Room> rooms;

    public RoomsResponse(Collection<Room> rooms) {
        this.rooms = rooms;
    }

    public Collection<Room> getRooms() {
        return rooms;
    }
}
