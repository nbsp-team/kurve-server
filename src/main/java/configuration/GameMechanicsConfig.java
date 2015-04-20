package configuration;

import frontend.annotation.xml.ArrayElement;
import frontend.annotation.xml.FieldElement;
import frontend.annotation.xml.ElementGroup;
import frontend.annotation.xml.RootElement;

import java.util.List;

/**
 * Created by Dimorinny on 07.04.15.
 */

@RootElement(name = "mechanics")
public class GameMechanicsConfig {

    @FieldElement(name = "minPlayerNumber")
    public String minPlayerNumber;

    @FieldElement(name = "maxPlayerNumber")
    public String maxPlayerNumber;

    @ElementGroup(name = "Snake")
    @FieldElement(name = "defaultSpeed")
    public String snakeDefaultSpeed;

    @ElementGroup(name = "Snake")
    @FieldElement(name = "defaultAngleSpeed")
    public String snakeDefaultAngleSpeed;

    @ElementGroup(name = "Snake")
    @FieldElement(name = "defaultWidth")
    public String defaultSnakeWidth;

    @FieldElement(name = "FPS")
    public String FPS;

    @ElementGroup(name = "GameField")
    @FieldElement(name = "width")
    public String gameFieldWidth;

    @ElementGroup(name = "GameField")
    @FieldElement(name = "height")
    public String gameFieldHeight;

    @ElementGroup(name = "Snake")
    @ArrayElement
    @FieldElement(name = "color")
    public List<String> colors;


}
