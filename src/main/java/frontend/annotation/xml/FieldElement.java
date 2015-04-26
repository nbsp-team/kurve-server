package frontend.annotation.xml;

import java.lang.annotation.*;
import java.lang.reflect.Type;


/**
 * Created by Dimorinny on 07.04.15.
 */

@Inherited
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface FieldElement {
    public String name();
}
