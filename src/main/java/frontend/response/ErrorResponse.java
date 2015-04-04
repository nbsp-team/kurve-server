package frontend.response;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * nickolay, 25.02.15.
 */
public class ErrorResponse extends Response {
    public enum ErrorResponseCode {
        ERROR_SIGNIN_FAILED,
        ERROR_SINGUP_FAILED,
        ERROR_EMPTY_RESPONSE,
        ERROR_INVALID_PARAMS,
        ERROR_PERMISSION_DENIED,
        ERROR_INTERNAL_SERVER
    }

    private static final String[] DEFAULT_ERROR_DESCRIPTIONS = new String[]{
            "Ошибка авторизации пользователя",
            "Ошибка при регистрации пользователя",
            "Сервер возвратил пустой ответ",
            "Неверные параметры",
            "Ошибка доступа",
            "Ошибка сервера"
    };

    private ErrorResponseCode errorCode;
    private String description;

    public ErrorResponse(ErrorResponseCode errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public ErrorResponse(ErrorResponseCode errorCode) {
        this(errorCode, DEFAULT_ERROR_DESCRIPTIONS[errorCode.ordinal()]);
    }

    public ErrorResponseCode getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return description;
    }

    public static class serializer implements JsonSerializer<ErrorResponse> {
        public JsonElement serialize(ErrorResponse src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject errorObject = new JsonObject();
            errorObject.add("code", new JsonPrimitive(src.getErrorCode().ordinal()));
            errorObject.add("description", new JsonPrimitive(src.getErrorDescription()));

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("error", errorObject);
            return jsonObject;
        }
    }
}
