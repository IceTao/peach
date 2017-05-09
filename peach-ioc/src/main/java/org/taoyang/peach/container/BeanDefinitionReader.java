/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

public interface BeanDefinitionReader {

    int load(String pkgName);

    int load(String... pkgNames);

    ClassLoader classLoader();

    Registry registry();

}
