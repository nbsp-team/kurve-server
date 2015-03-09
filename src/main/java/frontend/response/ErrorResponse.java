package frontend.response;

import com.google.gson.*;

import java.lang.reflect.Type;

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

    private int errorCode;
    private String description;

    public ErrorResponse(int errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public ErrorResponse(int errorCode) {
        this(errorCode, DEFAULT_ERROR_DESCRIPTIONS[errorCode]);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return description;
    }

    public static class serializer implements JsonSerializer<ErrorResponse> {
        public JsonElement serialize(ErrorResponse src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject errorObject = new JsonObject();
            errorObject.add("code", new JsonPrimitive(src.getErrorCode()));
            errorObject.add("description", new JsonPrimitive(src.getErrorDescription()));

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("error", errorObject);
            return jsonObject;
        }
    }
}
