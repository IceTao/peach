/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-09
 */
package org.taoyang.peach.container.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {}
