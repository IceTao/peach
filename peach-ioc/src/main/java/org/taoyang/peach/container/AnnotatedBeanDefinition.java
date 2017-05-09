/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

public interface AnnotatedBeanDefinition extends BeanDefinition {

    /**
     * Obtain the annotation metadata (as well as basic class metadata)
     * for this bean definition's bean class.
     * @return the annotation metadata object (never {@code null})
     */
    AnnotationMetadata getMetadata();

    /**
     * Obtain metadata for this bean definition's factory method, if any.
     * @return the factory method metadata, or {@code null} if none
     * @since 4.1.1
     */
    MethodMetadata getFactoryMethodMetadata();

}
