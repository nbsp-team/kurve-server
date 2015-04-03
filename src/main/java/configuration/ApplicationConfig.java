package configuration;

/**
 * Created by Dimorinny on 03.04.15.
 */
public class ApplicationConfig {

    private int port;
    private String host;

    private int minPlayerNumber;
    private int maxPlayerNumber;

    public ApplicationConfig(int port, String host, int minPlayers, int maxPlayers) {
        this.port = port;
        this.host = host;
        this.minPlayerNumber = minPlayers;
        this.maxPlayerNumber = maxPlayers;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public int getMinPlayerNumber() {
        return minPlayerNumber;
    }

    public int getMaxPlayerNumber() {
        return maxPlayerNumber;
    }
}
