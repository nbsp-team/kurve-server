package websocket;

import game.GameManager;
import game.Player;
import game.Room;
import model.Bonus.Bonus;
import model.Snake.Snake;
import model.UserProfile;
import org.junit.BeforeClass;
import org.junit.Test;
import websocket.message.*;
import websocket.message.serializer.ControlMessageSerializer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static utils.TestUtils.assertEqualsJSON;

/**
 * nickolay, 26.05.15.
 */
public class WebsocketMessageTests {
    private static Player player;
    private static Room room;

    @BeforeClass
    public static void init() {
        player = new Player("9999999999", "#000000",
                new UserProfile(
                        "123",
                        "Test",
                        "Abc",
                        "http://sdf/",
                        0,
                        "123123"
                ));

        room = new Room(mock(GameManager.class));
        room.onNewPlayer(player);
        room.onNewPlayer(player);
        room.onNewPlayer(player);
    }

    @Test
    public void testConnectedPlayerMessage() {
        Message message = new ConnectedPlayerMessage(
                player, 0
        );

        String expectedResult = "{\"code\":1,\"player\":{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"" + player.getId() + "\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testControlMessage() {
        Message message = new ControlMessage(
                true, false, 0
        );

        String expectedResult = "{\"code\":7,\"sender\":0,\"isLeft\":true,\"isUp\":false}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testDisconnectedPlayerMessage() {
        Message message = new DisconnectedPlayerMessage(player);

        String expectedResult = "{\"code\":2,\"player\":{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"" + player.getId() + "\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testEatBonusMessage() {
        Snake eater = mock(Snake.class);
        when(eater.getId()).thenReturn(234);

        Message message = new EatBonusMessage(0, eater);

        String expectedResult = "{\"code\":10,\"bonus_id\":0,\"eater_id\":234}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testGameOverMessage() {
        Message message = new GameOverMessage(room);

        String expectedResult = "{\"code\":12,\"results\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"points\":0,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"points\":0,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"points\":0,\"color\":\"#000000\"}]}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testNewBonusMessage() {
        Message message = new NewBonusMessage(new Bonus(0, 0, Bonus.Kind.BIG_HOLE_SELF));

        String expectedResult = "{\"code\":9,\"bonus\":{\"kind\":3,\"x\":0.0,\"y\":0.0,\"id\":9}}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testReadyMessage() {
        Message message = new ReadyMessage(player, true);

        String expectedResult = "{\"code\":4,\"ready\":true,\"player_id\":\"9999999999\"}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testRoomPlayersMessage() {
        Message message = new RoomPlayersMessage(room);

        String expectedResult = "{\"code\":0,\"players\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}]}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testSnakePatchMessage() {
        List<SnakeUpdateMessage> updateMessages = new ArrayList<>();
        updateMessages.add(new SnakeUpdateMessage(new Snake(
                0, 0, 0,
                new SnakeUpdatesManager(room),
                0,
                0
        ), 0));

        Message message = new SnakePatchMessage(updateMessages);

        String expectedResult = "{\"code\":16,\"updates\":[{\"code\":14,\"snake\":{\"lines\":[{\"id\":0,\"x1\":-1.3125,\"y1\":0.0,\"x2\":-1.3125,\"y2\":0.0,\"lineRadius\":4.0}],\"arcs\":[],\"id\":0,\"x\":0.0,\"y\":0.0,\"angle\":0.0,\"angleV\":0.02916666666666667,\"v\":1.2833333333333334,\"nlines\":1,\"narcs\":0,\"radius\":4,\"distance\":0.0,\"alive\":true,\"turnRadius\":44.0,\"partStopper\":0},\"id\":0}]}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testSnakeUpdateMessage() {
        Message message = new SnakeUpdateMessage(new Snake(
                0, 0, 0,
                new SnakeUpdatesManager(room),
                0
        ), 0);

        String expectedResult = "{\"code\":14,\"snake\":{\"lines\":[{\"id\":0,\"x1\":-1.3125,\"y1\":0.0,\"x2\":-1.3125,\"y2\":0.0,\"lineRadius\":4.0}],\"arcs\":[],\"id\":0,\"x\":0.0,\"y\":0.0,\"angle\":0.0,\"angleV\":0.02916666666666667,\"v\":1.2833333333333334,\"nlines\":1,\"narcs\":0,\"radius\":4,\"distance\":0.0,\"alive\":true,\"turnRadius\":44.0,\"partStopper\":236},\"id\":0}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testStartGameMessage() {
        Message message = new StartGameMessage(room, 0);

        String expectedResult = "{\"code\":5,\"FPS\":60,\"width\":1200,\"height\":600,\"speed\":77,\"angleSpeed\":50.13380707394703,\"holeLength\":20,\"myId\":0,\"countdown\":3,\"players\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}]}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }

    @Test
    public void testStartRoundMessage() {
        Message message = new StartRoundMessage(room, 0);

        String expectedResult = "{\"code\":17,\"FPS\":60,\"width\":1200,\"height\":600,\"speed\":77,\"angleSpeed\":50.13380707394703,\"holeLength\":20,\"myId\":0,\"countdown\":3,\"players\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}]}";
        String actualResult = message.getBody();

        assertEqualsJSON(actualResult, expectedResult);
    }
}
