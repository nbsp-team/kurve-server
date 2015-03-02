package frontend.annotation;

import main.AccountService;

import java.lang.annotation.*;

/**
 * nickolay, 28.02.15.
 */

@Inherited
@Target(value=ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface AdminRightsRequired {
}
