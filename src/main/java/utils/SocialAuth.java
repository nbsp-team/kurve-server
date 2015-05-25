package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import interfaces.SocialAccountService;
import model.UserProfile;

/**
 * nickolay, 24.05.15.
 */
public class SocialAuth {

    public static final String VK_SECRET = "5aBqPx1rZivFZnBw4McT";

    public enum AuthProvider {
        AUTH_PROVIDER_VK,
        AUTH_PROVIDER_FB
    }

    public static final String VK_ACCESS_TOKEN_URL = "https://oauth.vk.com/access_token";

    public static UserProfile auth(AuthProvider authProvider, String code) {
        UserProfile user = null;

        switch (authProvider) {
            case AUTH_PROVIDER_VK:
                user = getVkUser(code);
                break;
            case AUTH_PROVIDER_FB:
                user = getFbUser(code);
                break;
        }

        return user;
    }

    private static UserProfile getVkUser(String code) {
        try {
            HttpResponse<String> accessTokenResponse = Unirest.post(VK_ACCESS_TOKEN_URL)
                    .field("client_id", "4930885")
                    .field("client_secret", "5aBqPx1rZivFZnBw4McT")
                    .field("code", code)
                    .field("redirect_uri", "http://kurve.ml/api/v1/auth/social?type=0")
                    .asString();

            String accessTokenResponseString = accessTokenResponse.getBody();
            JsonObject accessTokenJson = new JsonParser().parse(accessTokenResponseString).getAsJsonObject();
            String accessToken = accessTokenJson.getAsJsonPrimitive("access_token").getAsString();

            HttpResponse<String> userInfoResponse = Unirest.post("https://api.vk.com/method/users.get")
                    .field("fields", "photo_100")
                    .field("access_token", accessToken)
                    .asString();

            String userInfoResponseString = userInfoResponse.getBody();

            JsonObject userInfo = new JsonParser()
                    .parse(userInfoResponseString)
                    .getAsJsonObject()
                    .getAsJsonArray("response")
                    .get(0)
                    .getAsJsonObject();

            return new UserProfile(
                    "",
                    userInfo.getAsJsonPrimitive("first_name").getAsString(),
                    userInfo.getAsJsonPrimitive("last_name").getAsString(),
                    userInfo.getAsJsonPrimitive("photo_100").getAsString(),
                    AuthProvider.AUTH_PROVIDER_VK.ordinal(),
                    userInfo.getAsJsonPrimitive("uid").getAsString()
            );
        } catch (Exception e) {
            return null;
        }
    }

    private static UserProfile getFbUser(String sessionData) {
        return null;
    }
}
