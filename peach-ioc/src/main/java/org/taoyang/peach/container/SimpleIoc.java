/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-09
 */
package org.taoyang.peach.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.taoyang.peach.container.annotation.Inject;
import org.taoyang.peach.container.annotation.Named;

public class SimpleIoc implements Ioc, Registry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final Map<String, Object> beans = new ConcurrentHashMap<>();

    private ClassLoader classLoader;

    public SimpleIoc() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public SimpleIoc(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Object getBean(String name) {
        return beans.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return requiredType.cast(beans.get(name));
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return getBean(requiredType.getCanonicalName(), requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return beans.containsKey(name);
    }

    @Override
    public void registerBeanDefinition (String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }

    @Override
    public BeanDefinition removeBeanDefinition (String name) {
        return beanDefinitionMap.remove(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitionMap.get(name);
    }

    public BeanDefinition getBeanDefinition(Class<?> requiredType) {
        BeanDefinition definition = beanDefinitionMap.get(requiredType.getCanonicalName());
        if (definition != null) {
            return definition;
        }

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            Class<?> clazz;
            if (!beanDefinition.hasBeanClass()) {
                try {
                    clazz = beanDefinition.resolveBeanClass(classLoader);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            } else {
                clazz = beanDefinition.getBeanClass();
            }

            for (Class<?> iface : clazz.getInterfaces()) {
                if (iface.equals(requiredType)) {
                    return beanDefinition;
                }
            }
        }

        return null;
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public boolean isNameInUse(String name) {
        return false;
    }

    public void initBeans() throws Exception {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            createBean(beanDefinition);
        }
    }

    private Object createBean(BeanDefinition beanDefinition)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (beanDefinition == null) {
            throw new RuntimeException("beanDefinition is not exist");
        }

        Class<?> clazz;
        if (!beanDefinition.hasBeanClass()) {
            try {
                clazz = beanDefinition.resolveBeanClass(classLoader);
            } catch (ClassNotFoundException e) {
                return null;
            }
        } else {
            clazz = beanDefinition.getBeanClass();
        }

        Constructor[] constructors = clazz.getConstructors();

        Constructor constructor = null;
        for (Constructor con : constructors) {
            if (constructor != null) {
                throw new RuntimeException("Unsupported too many inject constructs");
            }
            if (con.getAnnotation(Inject.class) != null) {
                if (con.getParameterTypes().length > 0) {
                    constructor = con;
                }
            }
        }

        Object bean = null;
        if (constructor != null) {
            int index = 0;

            Class<?>[] parameters = constructor.getParameterTypes();
            int length = parameters.length;

            Object[] args = new Object[length];
            Annotation[][] annotations = constructor.getParameterAnnotations();

            for (; index < length; length ++) {
                Object value = null;
                if (annotations[index].length > 0) {
                    for (Annotation annotation : annotations[index]) {
                        if (annotation instanceof Named) {
                            Named names = (Named) annotation;
                            value = getBean(names.value());
                        }
                    }
                }

                if (value == null){
                    value = getBean(parameters[index]);
                }

                if (value == null) {
                    value = createBean(getBeanDefinition(parameters[index].getCanonicalName()));
                }

                if (value == null) {
                    throw new RuntimeException("Error to Inject" + clazz.getCanonicalName() +
                            " with " + parameters[index] + ", Bean is not exist");
                }

                args[index] = value;
            }

            bean = constructor.newInstance(args);

        } else {
            bean = clazz.newInstance();
        }

        putBean(beanDefinition, bean);

        return bean;
    }

    private void putBean(BeanDefinition beanDefinition, Object value) {
        if (beanDefinition.getAlias() != null) {
            beans.put(beanDefinition.getAlias(), value);
        } else {
            Class<?>[] interfaces = beanDefinition.getBeanClass().getInterfaces();
            if (interfaces.length > 0) {
                for (Class<?> interfaceClazz : interfaces) {
                    if (beans.containsKey(interfaceClazz.getCanonicalName())) {
                        throw new RuntimeException("Unsupported multiple realization for one interface "
                                + interfaceClazz.getCanonicalName());
                    }
                    beans.put(interfaceClazz.getCanonicalName(), value);
                }
            } else {
                beans.put(beanDefinition.getBeanClassName(), value);
            }
        }
    }
}
