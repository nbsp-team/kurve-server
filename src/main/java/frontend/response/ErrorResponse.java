package frontend.response;

import frontend.response.Response;
import org.json.simple.JSONObject;

/**
 * nickolay, 25.02.15.
 */
public class ErrorResponse extends Response {
    public static final int ERROR_SIGNIN_FAILED = 0;
    public static final int ERROR_SINGUP_FAILED = 1;
    public static final int ERROR_EMPTY_RESPONSE = 2;
    public static final int ERROR_INVALID_PARAMS = 3;
    public static final int ERROR_PERMISSION_DENIED = 4;
    public static final int ERROR_INTERNAL_SERVER = 5;

    private static final String[] DEFAULT_ERROR_DESCRIPTIONS = new String[]{
            "Ошибка авторизации пользователя",
            "Ошибка при регистрации пользователя",
            "Сервер возвратил пустой ответ",
            "Неверные параметры",
            "Ошибка доступа",
            "Ошибка сервера"
    };

    public ErrorResponse(int errorCode, String message) {
        JSONObject errorJsonObject = new JSONObject();
        errorJsonObject.put("code", errorCode);
        errorJsonObject.put("description", message);

        rootJson.put("error", errorJsonObject);
    }

    public ErrorResponse(int errorCode) {
        this(errorCode, DEFAULT_ERROR_DESCRIPTIONS[errorCode]);
    }
}
