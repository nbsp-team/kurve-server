package frontend.response.serializer;

import com.google.gson.*;
import com.sun.management.OperatingSystemMXBean;
import frontend.response.ServerStatusResponse;
import game.Player;
import game.Room;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Type;
import java.text.NumberFormat;

/**
 * nickolay, 18.04.15.
 */

public class ServerStatusResponseSerializer implements JsonSerializer<ServerStatusResponse> {
    public JsonElement serialize(ServerStatusResponse src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("error", JsonNull.INSTANCE);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("userCount", src.getUserCount());
        responseObject.addProperty("sessionCount", src.getSessionCount());
        responseObject.addProperty("roomCount", src.getRoomCount());

        JsonArray rooms = new JsonArray();
        for(Room room : src.getGameService().getRooms()) {
            JsonObject roomInfo = new JsonObject();

            roomInfo.addProperty("creationDate", room.getCreationDate());

            JsonArray players = new JsonArray();
            for(Player player : room.getPlayers()) {
                players.add(context.serialize(player));
            }

            roomInfo.add("players", players);

            rooms.add(roomInfo);
        }

        responseObject.add("rooms", rooms);

        addMemoryData(responseObject);
        addCpuData(responseObject);

        jsonObject.add("response", responseObject);

        return jsonObject;
    }

    private void addMemoryData(JsonObject responseObject) {
        Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory() / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024;
        long freeMemory = runtime.freeMemory() / 1024;
        long totalFreeMemory = (freeMemory + (maxMemory - allocatedMemory)) / 1024;

        responseObject.addProperty("maxMemory", maxMemory);
        responseObject.addProperty("freeMemory", freeMemory);
        responseObject.addProperty("allocatedMemory", allocatedMemory);
        responseObject.addProperty("totalFreeMemory", totalFreeMemory);
    }

    private void addCpuData(JsonObject responseObject) {
        OperatingSystemMXBean mbean = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        double load = mbean.getProcessCpuLoad();
        responseObject.addProperty("cpuLoad", load);
    }
}