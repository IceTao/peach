/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container;

public interface Ioc {

    Object getBean(String name);

    <T> T getBean(String name, Class<T> requiredType);

    <T> T getBean(Class<T> requiredType);

    boolean containsBean(String name);

}
