package frontend.annotation;

import frontend.AbstractServlet;
import frontend.AbstractServlet.HttpMethod;

import java.lang.annotation.*;

/**
 * nickolay, 28.02.15.
 */

@Inherited
@Target(value=ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ApiMethod {
    HttpMethod method() default HttpMethod.GET;
}