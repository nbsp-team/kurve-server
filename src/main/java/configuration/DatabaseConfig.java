package configuration;

import frontend.annotation.xml.FieldElement;
import frontend.annotation.xml.RootElement;

/**
 * nickolay, 03.04.15.
 */

@RootElement(name = "db")
public class DatabaseConfig {

    @FieldElement(name = "port")
    public String port;

    @FieldElement(name = "host")
    public String host;

    @FieldElement(name = "dbname")
    public String name;

    @FieldElement(name = "username")
    public String username;

    @FieldElement(name = "password")
    public String password;

    @FieldElement(name = "offline")
    public String offline;
}
