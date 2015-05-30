package frontend.response;

/**
 * nickolay, 28.02.15.
 */
public class GetMobileUrlResponse extends SuccessResponse {
    private final String session;

    public GetMobileUrlResponse(String session) {
        this.session = session;
    }

    public String getSession() {
        return session;
    }
}