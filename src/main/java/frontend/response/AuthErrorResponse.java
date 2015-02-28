package frontend.response;

/**
 * nickolay, 25.02.15.
 */
public class AuthErrorResponse extends ErrorResponse {
    public AuthErrorResponse() {
        super(ERROR_SIGNIN_FAILED);
    }

    public AuthErrorResponse(String message) {
        super(ERROR_SIGNIN_FAILED, message);
    }
}
