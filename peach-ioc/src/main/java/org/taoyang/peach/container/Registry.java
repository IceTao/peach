/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

public interface Registry {

    void registerBeanDefinition(String name, BeanDefinition beanDefinition);

    BeanDefinition removeBeanDefinition(String name);

    BeanDefinition getBeanDefinition(String name);

    boolean containsBeanDefinition(String name);

    int count();

    boolean isNameInUse(String name);

}
