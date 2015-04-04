package frontend.response;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * nickolay, 25.02.15.
 */
public class ErrorResponse extends Response {
    public enum ErrorResponseCode {
        ERROR_SIGNIN_FAILED("Ошибка авторизации пользователя"),
        ERROR_SINGUP_FAILED("Ошибка при регистрации пользователя"),
        ERROR_EMPTY_RESPONSE("Сервер возвратил пустой ответ"),
        ERROR_INVALID_PARAMS("Неверные параметры"),
        ERROR_PERMISSION_DENIED("Ошибка доступа"),
        ERROR_INTERNAL_SERVER("Ошибка сервера");

        private ErrorResponseCode(String description){
            this.description = description;
        }

        private final String description;

        public String getDescription(){return description;}
    }
    private ErrorResponseCode errorCode;
    private String additionalDescription;

    public ErrorResponse(ErrorResponseCode errorCode) {
        this(errorCode, null);
    }

    public ErrorResponse(ErrorResponseCode errorCode, String additionalDescription) {
        this.errorCode = errorCode;
        this.additionalDescription = additionalDescription;
    }

    public ErrorResponseCode getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        if (additionalDescription != null) {
            return errorCode.getDescription() + " " + additionalDescription;
        }
        return errorCode.getDescription();
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
