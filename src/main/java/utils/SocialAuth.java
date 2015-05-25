package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import interfaces.SocialAccountService;
import model.UserProfile;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.Charset;
import java.util.List;

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
    public static final String FB_ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token";

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

    private static UserProfile getFbUser(String code) {
        try {
            HttpResponse<String> accessTokenResponse = Unirest.post(FB_ACCESS_TOKEN_URL)
                    .field("client_id", "925250904206070")
                    .field("client_secret", "1710860008ea04bce6214ef5fb893170")
                    .field("code", code)
                    .field("redirect_uri", "http://kurve.ml/api/v1/auth/social?type=1")
                    .asString();

            String accessTokenResponseString = accessTokenResponse.getBody();

            String accessToken = null;
            List<NameValuePair> params = URLEncodedUtils.parse(accessTokenResponseString, Charset.defaultCharset());
            for(NameValuePair pair : params) {
                if (pair.getName().equals("access_token")) {
                    accessToken = pair.getValue();
                    break;
                }
            }

            if (accessToken == null) {
                return null;
            }

            HttpResponse<String> userInfoResponse = Unirest.get("https://graph.facebook.com/v2.3/me")
                    .queryString("access_token", accessToken)
                    .asString();

            String userInfoResponseString = userInfoResponse.getBody();

            JsonObject userInfo = new JsonParser()
                    .parse(userInfoResponseString)
                    .getAsJsonObject();

            HttpResponse<String> avatarResponse = Unirest.get("https://graph.facebook.com/v2.3/me/picture")
                    .queryString("access_token", accessToken)
                    .queryString("width", 100)
                    .queryString("height", 100)
                    .queryString("redirect", "false")
                    .asString();

            String avatarResponseString = avatarResponse.getBody();
            String avatar = new JsonParser()
                    .parse(avatarResponseString)
                    .getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonPrimitive("url")
                    .getAsString();

            return new UserProfile(
                    "",
                    userInfo.getAsJsonPrimitive("first_name").getAsString(),
                    userInfo.getAsJsonPrimitive("last_name").getAsString(),
                    avatar,
                    AuthProvider.AUTH_PROVIDER_VK.ordinal(),
                    userInfo.getAsJsonPrimitive("id").getAsString()
            );
        } catch (Exception e) {
            return null;
        }
    }
}
