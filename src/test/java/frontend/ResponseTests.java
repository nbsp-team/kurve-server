package frontend;

import frontend.response.*;
import model.UserProfile;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TestHelper;

/**
 * nickolay, 26.05.15.
 */
public class ResponseTests {
    private static UserProfile userProfile;

    @BeforeClass
    public static void init() {
        userProfile = new UserProfile(
                "123",
                "Test",
                "Abc",
                "http://sdf/",
                0,
                "123123"
        );
    }

    @Test
    public void testAuthErrorResponse() {
        TestHelper.testMessage(new AuthErrorResponse("test"),
                "{\"error\":{\"code\":0,\"description\":\"Ошибка авторизации пользователя test\"}}"
        );


    }

    @Test
    public void testGetUserResponse() {
        TestHelper.testMessage(new GetUserResponse(userProfile),
                "{\"error\":null,\"response\":{\"user\":{\"user_id\":\"123\",\"first_name\":\"Test\",\"last_name\":\"Abc\",\"avatar\":\"http://sdf/\",\"global_rating\":0}}}"
        );
    }

    @Test
    public void testRatingResponse() {
        TestHelper.testMessage(new RatingResponse(ratingItemList),
                "{\"error\":null,\"response\":{\"users\":[{\"username\":\"user0\",\"global_rating\":100},{\"username\":\"user1\",\"global_rating\":105},{\"username\":\"user2\",\"global_rating\":110},{\"username\":\"user3\",\"global_rating\":115},{\"username\":\"user4\",\"global_rating\":120},{\"username\":\"user5\",\"global_rating\":125},{\"username\":\"user6\",\"global_rating\":130},{\"username\":\"user7\",\"global_rating\":135},{\"username\":\"user8\",\"global_rating\":140},{\"username\":\"user9\",\"global_rating\":145},{\"username\":\"user10\",\"global_rating\":150},{\"username\":\"user11\",\"global_rating\":155},{\"username\":\"user12\",\"global_rating\":160},{\"username\":\"user13\",\"global_rating\":165},{\"username\":\"user14\",\"global_rating\":170},{\"username\":\"user15\",\"global_rating\":175},{\"username\":\"user16\",\"global_rating\":180},{\"username\":\"user17\",\"global_rating\":185},{\"username\":\"user18\",\"global_rating\":190},{\"username\":\"user19\",\"global_rating\":195}]}}"
        );
    }

    @Test
    public void testServerStatusResponse() {
        TestHelper.testMessage(new ServerStatusResponse(1, 12),
                "{\"error\":null,\"response\":{\"userCount\":1,\"sessionCount\":12}}"
        );
    }
}
