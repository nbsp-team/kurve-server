package frontend.annotation.xml;

import java.lang.annotation.*;

/**
 * Created by egor on 20.04.15.
 */

@Inherited
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ElementGroup {
    public String name();
}
