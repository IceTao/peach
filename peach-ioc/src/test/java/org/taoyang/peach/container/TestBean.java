/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-09
 */
package org.taoyang.peach.container;

import org.taoyang.peach.container.annotation.Component;
import org.taoyang.peach.container.annotation.Inject;
import org.taoyang.peach.container.resource.Resource;

@Component
public class TestBean {

    private Resource resource;

    @Inject
    public TestBean(Resource resource) {
        this.resource = resource;
    }

    public void show() {
        System.out.println(resource.value());
    }

}
