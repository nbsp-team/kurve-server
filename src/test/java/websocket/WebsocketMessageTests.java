package websocket;

import frontend.ResponseTests;
import game.GameService;
import game.Player;
import game.Room;
import model.Bonus.Bonus;
import model.Snake.Snake;
import model.UserProfile;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.MathUtilsTests;
import utils.TestHelper;
import websocket.message.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        room = new Room(mock(GameService.class));
        room.onNewPlayer(player);
        room.onNewPlayer(player);
        room.onNewPlayer(player);
    }

    

    @Test
    public void testConnectedPlayerMessage() {
        
        TestHelper.testMessage(
                new ConnectedPlayerMessage(
                        player, 0
                ),
                "{\"code\":1,\"player\":{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"" + player.getId() + "\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}}"
        );
    }

    @Test
    public void testControlMessage() {
        TestHelper.testMessage(
                new ControlMessage(
                        true, false, 0
                ),
                "{\"code\":7,\"sender\":0,\"isLeft\":true,\"isUp\":false}"
        );

    }

    @Test
    public void testDisconnectedPlayerMessage() {
        TestHelper.testMessage(new DisconnectedPlayerMessage(player),
                "{\"code\":2,\"player\":{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"" + player.getId() + "\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}}"
        );

    }

    @Test
    public void testEatBonusMessage() {
        Snake eater = mock(Snake.class);
        when(eater.getId()).thenReturn(234);

        TestHelper.testMessage(new EatBonusMessage(0, eater),
                "{\"code\":10,\"bonus_id\":0,\"eater_id\":234}"
        );

    }

    @Test
    public void testGameOverMessage() {
        TestHelper.testMessage(new GameOverMessage(room),
                "{\"code\":12,\"results\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"points\":0,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"points\":0,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"points\":0,\"color\":\"#000000\"}]}"
        );

    }

    @Test
    public void testNewBonusMessage() {
        TestHelper.testMessage(new NewBonusMessage(new Bonus(0, 0, Bonus.Kind.BIG_HOLE_SELF)),
                "{\"code\":9,\"bonus\":{\"kind\":3,\"x\":0.0,\"y\":0.0,\"id\":9}}"
        );

    }

    @Test
    public void testReadyMessage() {
        TestHelper.testMessage(new ReadyMessage(player, true),
                "{\"code\":4,\"ready\":true,\"player_id\":\"9999999999\"}"
        );

    }

    @Test
    public void testRoomPlayersMessage() {
        TestHelper.testMessage(new RoomPlayersMessage(room),

                "{\"code\":0,\"players\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}]}"
        );

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

        TestHelper.testMessage(new SnakePatchMessage(updateMessages),
                "{\"code\":16,\"updates\":[{\"code\":14,\"snake\":{\"lines\":[{\"id\":0,\"x1\":-1.3125,\"y1\":0.0,\"x2\":-1.3125,\"y2\":0.0,\"lineRadius\":4.0}],\"arcs\":[],\"id\":0,\"x\":0.0,\"y\":0.0,\"angle\":0.0,\"angleV\":0.02916666666666667,\"v\":1.2833333333333334,\"nlines\":1,\"narcs\":0,\"radius\":4,\"distance\":0.0,\"alive\":true,\"turnRadius\":44.0,\"partStopper\":0},\"id\":0}]}"
        );

    }

    @Test
    public void testSnakeUpdateMessage() {
        Snake snake = new Snake(
                0, 0, 0,
                new SnakeUpdatesManager(room),
                0);

        TestHelper.testMessage(new SnakeUpdateMessage(snake, 0),
                "{\"code\":14,\"snake\":{\"lines\":[{\"id\":0,\"x1\":-1.3125,\"y1\":0.0,\"x2\":-1.3125,\"y2\":0.0,\"lineRadius\":4.0}],\"arcs\":[],\"id\":0,\"x\":0.0,\"y\":0.0,\"angle\":0.0,\"angleV\":0.02916666666666667,\"v\":1.2833333333333334,\"nlines\":1,\"narcs\":0,\"radius\":4,\"distance\":0.0,\"alive\":true,\"turnRadius\":44.0,\"partStopper\":236},\"id\":0}"
        );
    }

    @Test
    public void testStartGameMessage() {
        TestHelper.testMessage(new StartGameMessage(room, 0),
                "{\"code\":5,\"FPS\":60,\"width\":1200,\"height\":600,\"speed\":77,\"angleSpeed\":50.13380707394703,\"holeLength\":20,\"myId\":0,\"countdown\":3,\"players\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}]}"
        );

    }

    @Test
    public void testStartRoundMessage() {
        TestHelper.testMessage(new StartRoundMessage(room, 0),
                "{\"code\":17,\"FPS\":60,\"width\":1200,\"height\":600,\"speed\":77,\"angleSpeed\":50.13380707394703,\"holeLength\":20,\"myId\":0,\"countdown\":3,\"players\":[{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"},{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"player_id\":\"9999999999\",\"global_rating\":0,\"is_ready\":false,\"color\":\"#000000\"}]}"
        );
    }
}
