package utils;

import com.github.mirreck.FakeFactory;
import com.github.mirreck.domain.Gender;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import main.Main;
import model.UserProfile;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

/**
 * nickolay, 24.05.15.
 */
public class SocialAuthHelper {

    public static final String GUEST_AVATAR_URL = "http://api.adorable.io/avatars/200/%s";
    public static final String OAUTH_REDIRECT_URL = "http://%s/api/v1/auth/social?type=%d";

    public static final String VK_APP_ID = "4930885";
    public static final String FB_APP_ID = "925250904206070";
    public static final String VK_SECRET = "5aBqPx1rZivFZnBw4McT";
    public static final String FB_SECRET = "1710860008ea04bce6214ef5fb893170";

    public static final String GET_VK_USER_API_URL = "https://api.vk.com/method/users.get";
    public static final String GET_FB_USER_API_URL = "https://graph.facebook.com/v2.3/me";
    public static final String GET_FB_USER_PHOTO_API_URL = "https://graph.facebook.com/v2.3/me/picture";

    public static final String GET_VK_ACCESS_TOKEN_API_URL = "https://oauth.vk.com/access_token";
    public static final String GET_FB_ACCESS_TOKEN_API_URL = "https://graph.facebook.com/oauth/access_token";

    public enum AuthProvider {
        AUTH_PROVIDER_VK,
        AUTH_PROVIDER_FB,
        AUTH_PROVIDER_GUEST
    }

    public static UserProfile auth(AuthProvider authProvider, String code) {
        UserProfile user = null;

        switch (authProvider) {
            case AUTH_PROVIDER_VK:
                user = getVkUser(code);
                break;
            case AUTH_PROVIDER_FB:
                user = getFbUser(code);
                break;
            case AUTH_PROVIDER_GUEST:
                user = getGuestUser(code);
                break;
        }

        return user;
    }

    private static UserProfile getVkUser(String code) {
        try {
            String redirectUri = String.format(
                    OAUTH_REDIRECT_URL,
                    Main.networkConfig.domain,
                    AuthProvider.AUTH_PROVIDER_VK.ordinal()
            );

            HttpResponse<String> accessTokenResponse = Unirest.post(GET_VK_ACCESS_TOKEN_API_URL)
                    .field("client_id", VK_APP_ID)
                    .field("client_secret", VK_SECRET)
                    .field("code", code)
                    .field("redirect_uri", redirectUri)
                    .asString();

            String accessTokenResponseString = accessTokenResponse.getBody();
            JsonObject accessTokenJson = new JsonParser().parse(accessTokenResponseString).getAsJsonObject();
            String accessToken = accessTokenJson.getAsJsonPrimitive("access_token").getAsString();

            HttpResponse<String> userInfoResponse = Unirest.post(GET_VK_USER_API_URL)
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
            String redirectUri = String.format(
                    OAUTH_REDIRECT_URL,
                    Main.networkConfig.domain,
                    AuthProvider.AUTH_PROVIDER_FB.ordinal()
            );

            HttpResponse<String> accessTokenResponse = Unirest.post(GET_FB_ACCESS_TOKEN_API_URL)
                    .field("client_id", FB_APP_ID)
                    .field("client_secret", FB_SECRET)
                    .field("code", code)
                    .field("redirect_uri", redirectUri)
                    .asString();

            String accessTokenResponseString = accessTokenResponse.getBody();

            String accessToken = null;
            List<NameValuePair> params = URLEncodedUtils.parse(accessTokenResponseString, Charset.defaultCharset());
            for (NameValuePair pair : params) {
                if (pair.getName().equals("access_token")) {
                    accessToken = pair.getValue();
                    break;
                }
            }

            if (accessToken == null) {
                return null;
            }

            HttpResponse<String> userInfoResponse = Unirest.get(GET_FB_USER_API_URL)
                    .queryString("access_token", accessToken)
                    .asString();

            String userInfoResponseString = userInfoResponse.getBody();

            JsonObject userInfo = new JsonParser()
                    .parse(userInfoResponseString)
                    .getAsJsonObject();

            HttpResponse<String> avatarResponse = Unirest.get(GET_FB_USER_PHOTO_API_URL)
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
                    userInfo.getAsJsonPrimitive("first_name").getAsString(),
                    userInfo.getAsJsonPrimitive("last_name").getAsString(),
                    avatar,
                    AuthProvider.AUTH_PROVIDER_FB.ordinal(),
                    userInfo.getAsJsonPrimitive("id").getAsString()
            );
        } catch (Exception e) {
            return null;
        }
    }

    private static UserProfile getGuestUser(String code) {
        FakeFactory factory = new FakeFactory();
        String id = UUID.randomUUID().toString();

        return new UserProfile(
                factory.firstName(Gender.M),
                factory.lastName(),
                String.format(GUEST_AVATAR_URL, id),
                AuthProvider.AUTH_PROVIDER_GUEST.ordinal(),
                UUID.randomUUID().toString()
        );
    }
}
