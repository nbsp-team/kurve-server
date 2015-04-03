package configuration;

/**
 * Created by Dimorinny on 03.04.15.
 */
public class ApplicationConfig {

    private int port;
    private String host;

    public ApplicationConfig(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
