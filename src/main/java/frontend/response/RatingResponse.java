package frontend.response;

import model.UserProfile;

import java.util.List;

/**
 * nickolay, 28.02.15.
 */
public class RatingResponse extends SuccessResponse {
    private final List<UserProfile> ratingItemList;

    public RatingResponse(List<UserProfile> ratingItemList) {
        this.ratingItemList = ratingItemList;
    }

    public List<UserProfile> getRatings() {
        return ratingItemList;
    }
}
