package frontend.annotation.xml;

import java.lang.annotation.*;

/**
 * Created by Dimorinny on 07.04.15.
 */

@Inherited
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RootElement {
    public String name();
}
