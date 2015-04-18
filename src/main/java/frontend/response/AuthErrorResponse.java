package frontend.response;

/**
 * nickolay, 25.02.15.
 */
public class AuthErrorResponse extends ErrorResponse {
    public AuthErrorResponse() {
        super(ErrorResponse.ErrorResponseCode.ERROR_SIGNIN_FAILED);
    }

    public AuthErrorResponse(String message) {
        super(ErrorResponse.ErrorResponseCode.ERROR_SIGNIN_FAILED, message);
    }
}
