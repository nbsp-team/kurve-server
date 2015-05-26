package configuration;

import frontend.annotation.xml.FieldElement;
import frontend.annotation.xml.RootElement;

/**
 * Created by Dimorinny on 03.04.15.
 */

@RootElement(name = "network")
public class NetworkConfig {

    @FieldElement(name = "port")
    public String port;

    @FieldElement(name = "host")
    public String host;

    @FieldElement(name = "domain")
    public String domain;
}
