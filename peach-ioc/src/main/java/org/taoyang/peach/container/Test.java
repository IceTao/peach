/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.taoyang.peach.container.annotation.Component;
import org.taoyang.peach.container.annotation.Inject;

@Component
public class Test {

    private Resource resource;

    private Ioc ioc;

    public Test(@Component("xxx") Resource resource, Ioc ioc) {
        this.resource = resource;
        this.ioc = ioc;
    }

    public static void main(String[] args) {

        Constructor[] constructors = Test.class.getConstructors();

        System.out.println(constructors.length);

        Constructor constructor = constructors[0];

        Class[] p = constructor.getParameterTypes();
        for (Class ep : p) {
            System.out.println(ep.getCanonicalName());
            System.out.println(ep.isAnnotationPresent(Component.class));
        }

        constructor.getTypeParameters();

        Annotation[][] annotations = constructor.getParameterAnnotations();
        Annotation[] annotation = annotations[0];

        Annotation a = annotation[0];

        System.out.println(a instanceof Component);

        Component inject = (Component) a;
        System.out.println(inject.value());

        System.out.println(a.annotationType());


        System.out.println(annotation.length);

        Annotation[] annotation2 = annotations[1];

        System.out.println(annotation2.length);



    }

}
