package websocket.message.serializer;

import com.google.gson.*;
import game.MathHelper;
import model.Snake.Snake;
import model.Snake.SnakePartArc;
import model.Snake.SnakePartLine;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by egor on 06.05.15.
 */
public class SnakeUpdatesSerializer implements JsonSerializer<Snake> {
    public JsonElement serialize(Snake src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();


        JsonArray arcsToSend = new JsonArray();
        List<SnakePartArc> snakeArcs= src.getSnakeArcs();
        List<SnakePartLine> snakeLines = src.getSnakeLines();
        for (int i = Math.max(0, src.getArcsSent()); i < snakeArcs.size(); i++) {
            arcsToSend.add(context.serialize(snakeArcs.get(i)));
        }
        JsonArray linesToSend = new JsonArray();
        for (int i = Math.max(0, src.getLinesSent()); i < snakeLines.size(); i++) {
            linesToSend.add(context.serialize(snakeLines.get(i)));
        }

        jsonObject.add("lines", linesToSend);
        jsonObject.add("arcs", arcsToSend);

        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("x", src.getX());
        jsonObject.addProperty("y", src.getY());
        jsonObject.addProperty("angle", src.getAngle());
        jsonObject.addProperty("angleV", src.getAngleSpeed());
        jsonObject.addProperty("v", src.getSpeed());
        jsonObject.addProperty("nlines", snakeLines.size());
        jsonObject.addProperty("narcs", snakeArcs.size());
        jsonObject.addProperty("radius", src.getRadius());
        jsonObject.addProperty("distance", src.getTravSinceLastHole());
        jsonObject.addProperty("alive", src.isAlive());
        jsonObject.addProperty("turnRadius", MathHelper.shortDouble(src.getTurnRadius()));
        jsonObject.addProperty("partStopper", src.getPartStopper());
        if (!src.isTurning()) {
            src.setArcsSent(Math.max(0, snakeArcs.size() - 1));
            src.setLinesSent(snakeLines.size());
        } else {
            src.setArcsSent(snakeArcs.size());
            src.setLinesSent(Math.max(0, snakeLines.size() - 1));
        }

        return jsonObject;
    }
}