package frontend.annotation;

import java.lang.annotation.*;

/**
 * nickolay, 28.02.15.
 */

@Inherited
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface AuthenticationRequired {
}
