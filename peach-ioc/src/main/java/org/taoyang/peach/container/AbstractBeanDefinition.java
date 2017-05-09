/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

import org.springframework.core.io.Resource;

public class AbstractBeanDefinition implements BeanDefinition {

    private String scope = SCOPE_SINGLETON;

    private volatile Object beanClass;

    private Resource resource;

    private String alias;

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException(
                    "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClassObject;
    }

    /**
     * Determine the class of the wrapped bean, resolving it from a
     * specified class name if necessary. Will also reload a specified
     * Class from its name when called with the bean class already resolved.
     * @param classLoader the ClassLoader to use for resolving a (potential) class name
     * @return the resolved bean class
     * @throws ClassNotFoundException if the class name could be resolved
     */
    @Override
    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = classLoader.loadClass(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class) {
            return ((Class<?>) beanClassObject).getName();
        }
        else {
            return (String) beanClassObject;
        }
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }

    /**
     * Return whether this definition specifies a bean class.
     */
    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Set the resource that this bean definition came from
     * (for the purpose of showing context in case of errors).
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Return the resource that this bean definition came from.
     */
    public Resource getResource() {
        return this.resource;
    }
}
