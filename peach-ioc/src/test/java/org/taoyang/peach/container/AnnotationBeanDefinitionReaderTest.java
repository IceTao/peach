/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

import java.util.Set;

public class AnnotationBeanDefinitionReaderTest {

    @org.junit.Test
    public void testFilter() throws Exception {
        SimpleIoc ioc = new SimpleIoc();

        AnnotationBeanDefinitionReader reader = new AnnotationBeanDefinitionReader(ioc);

        Set<BeanDefinition> v =  reader.findComponents("org.taoyang.peach.container");

        for (BeanDefinition vs : v) {
            DefaultBeanDefinition definition = (DefaultBeanDefinition) vs;

            Class<?> clazz = definition.resolveBeanClass(Thread.currentThread().getContextClassLoader());
            System.out.println(clazz.getCanonicalName());
        }

        System.out.println(reader.load("org.taoyang.peach.container"));

        ioc.initBeans();

        TestBean bean = ioc.getBean(TestBean.class);

        bean.show();

    }

}
