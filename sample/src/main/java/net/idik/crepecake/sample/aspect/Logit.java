package net.idik.crepecake.sample.aspect;

import net.idik.crepecake.annotations.Aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by linshuaibin on 2018/1/2.
 */

@Aspect(LogitAspect.class)
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Logit {
}
