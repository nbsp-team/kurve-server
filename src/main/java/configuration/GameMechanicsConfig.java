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

    @FieldElement(name = "defaultSnakeSpeed")
    public String snakeDefaultSpeed;

    @FieldElement(name = "defaultSnakeAngleSpeed")
    public String snakeDefaultAngleSpeed;

    @FieldElement(name = "FPS")
    public String FPS;

    @FieldElement(name = "gameFieldWidth")
    public String gameFieldWidth;

    @FieldElement(name = "gameFieldHeight")
    public String gameFieldHeight;

    @FieldElement(name = "defaultSnakeWidth")
    public String defaultSnakeWidth;
}
