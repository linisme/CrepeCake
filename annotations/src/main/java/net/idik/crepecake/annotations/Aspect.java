package net.idik.crepecake.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by linshuaibin on 2017/12/28.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Aspect {
    Class value();
}
