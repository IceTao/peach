/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container.resource;

public interface ResourcesLoader {

    Resource getResource(String location);

    Resource[] getResources(String location);

    ClassLoader classloader();

}
