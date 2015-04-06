package configuration;

import frontend.annotation.xml.FieldElement;
import frontend.annotation.xml.RootElement;

/**
 * Created by Dimorinny on 07.04.15.
 */

@RootElement(name = "mechanics")
public class GameMechanicsConfig {

    @FieldElement(name = "minPlayerNumber")
    public String minPlayerNumber;

    @FieldElement(name = "maxPlayerNumber")
    public String maxPlayerNumber;
}
