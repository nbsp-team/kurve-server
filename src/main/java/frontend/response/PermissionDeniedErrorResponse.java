package frontend.response;

/**
 * nickolay, 25.02.15.
 */
public class PermissionDeniedErrorResponse extends ErrorResponse {
    public PermissionDeniedErrorResponse() {
        super(ERROR_PERMISSION_DENIED);
    }

    public PermissionDeniedErrorResponse(String message) {
        super(ERROR_PERMISSION_DENIED, message);
    }
}
