/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";


    String getScope();

    void setScope(String scope);

    /**
     * Return whether this a <b>Singleton</b>, with a single, shared instance
     * returned on all calls.
     */
    boolean isSingleton();

    /**
     * Return whether this a <b>Prototype</b>, with an independent instance
     * returned for each call.
     */
    boolean isPrototype();

    Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException;

    Class<?> getBeanClass() throws IllegalStateException;

    String getBeanClassName();

    void setBeanClassName(String beanClassName);

    boolean hasBeanClass();

    String getAlias();

    void setAlias(String alias);

}
