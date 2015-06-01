package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import model.Score;
import model.UserProfile;
import org.bson.types.ObjectId;
import utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * nickolay, 05.05.15.
 */
public class ScoresDao {
    private static final String USERS_COLLECTION = UsersDao.USERS_COLLECTION;

    public static final String SCORES_FIELD_NAME = "scores";
    public static final String POINTS_FIELD_NAME = "points";

    private static final String DATE_FIELD_NAME = "dt";

    public static final String TOTAL_POINTS_FIELD_NAME = "total_points";
    public static final int TOP_INTERVAL_DAYS = 7;

    private final DBCollection users;

    public ScoresDao(DB db) {
        this.users = db.getCollection(USERS_COLLECTION);
    }

    public void insert(Score score) {
        DBObject newScore = new BasicDBObject();
        newScore.put(POINTS_FIELD_NAME, score.getPoints());
        newScore.put(DATE_FIELD_NAME, score.getDate());

        BasicDBObject pushNewScore = new BasicDBObject("$push", new BasicDBObject(SCORES_FIELD_NAME, newScore));

        users.update(new BasicDBObject("_id", new ObjectId(score.getUserId())), pushNewScore);
    }

    public List<UserProfile> getTop(int topSize) {
        List<DBObject> pipeline = new ArrayList<>();

        pipeline.add(new BasicDBObject("$unwind", "$" + SCORES_FIELD_NAME));

        pipeline.add(getScoreDateMatch());

        DBObject group = new BasicDBObject();
        group.put("_id", "$" + UsersDao.USER_ID_FIELD_NAME);
        group.put(TOTAL_POINTS_FIELD_NAME, new BasicDBObject("$sum", "$" + SCORES_FIELD_NAME + "." + POINTS_FIELD_NAME));
        group.put(UsersDao.SOCIAL_ID_FIELD_NAME, new BasicDBObject("$first", "$" + UsersDao.SOCIAL_ID_FIELD_NAME));
        group.put(UsersDao.AUTH_PROVIDER_FIELD_NAME, new BasicDBObject("$first", "$" + UsersDao.AUTH_PROVIDER_FIELD_NAME));
        group.put(UsersDao.AVATAR_URL_FIELD_NAME, new BasicDBObject("$first", "$" + UsersDao.AVATAR_URL_FIELD_NAME));
        group.put(UsersDao.LAST_NAME_FIELD_NAME, new BasicDBObject("$first", "$" + UsersDao.LAST_NAME_FIELD_NAME));
        group.put(UsersDao.FIRST_NAME_FIELD_NAME, new BasicDBObject("$first", "$" + UsersDao.FIRST_NAME_FIELD_NAME));

        pipeline.add(new BasicDBObject("$group", group));

        pipeline.add(new BasicDBObject("$sort", new BasicDBObject(TOTAL_POINTS_FIELD_NAME, -1)));

        pipeline.add(new BasicDBObject("$limit", topSize));

        List<UserProfile> rating = new ArrayList<>();
        for(DBObject ratingDbItem : users.aggregate(pipeline).results()) {
            UserProfile user = new UserProfile(
                    ratingDbItem.get(UsersDao.USER_ID_FIELD_NAME).toString(),
                    ratingDbItem.get(UsersDao.FIRST_NAME_FIELD_NAME).toString(),
                    ratingDbItem.get(UsersDao.LAST_NAME_FIELD_NAME).toString(),
                    ratingDbItem.get(UsersDao.AVATAR_URL_FIELD_NAME).toString(),
                    (int) ratingDbItem.get(UsersDao.AUTH_PROVIDER_FIELD_NAME),
                    ratingDbItem.get(UsersDao.SOCIAL_ID_FIELD_NAME).toString(),
                    (int) ratingDbItem.get(ScoresDao.TOTAL_POINTS_FIELD_NAME)
            );
            rating.add(user);
        }

        return rating;
    }

    public int getPoints(String userId) {
        List<DBObject> pipeline = new ArrayList<>();

        pipeline.add(
                new BasicDBObject("$match",
                        new BasicDBObject(
                                UsersDao.USER_ID_FIELD_NAME,
                                new ObjectId(userId)
                        )
                )
        );

        pipeline.add(new BasicDBObject("$unwind", "$" + SCORES_FIELD_NAME));

        pipeline.add(getScoreDateMatch());

        DBObject group = new BasicDBObject();
        group.put("_id", null);
        group.put(TOTAL_POINTS_FIELD_NAME, new BasicDBObject("$sum", "$" + SCORES_FIELD_NAME + "." + POINTS_FIELD_NAME));

        pipeline.add(new BasicDBObject("$group", group));

        try {
            DBObject result = users.aggregate(pipeline).results().iterator().next();
            return (int) result.get(TOTAL_POINTS_FIELD_NAME);
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public static DBObject getScoreDateMatch() {
        BasicDBObject filter = new BasicDBObject(SCORES_FIELD_NAME + "." + DATE_FIELD_NAME,
                new BasicDBObject("$gte", DateUtils.offsetDays(new Date(), -TOP_INTERVAL_DAYS))
        );

        return new BasicDBObject("$match", filter);
    }
}
