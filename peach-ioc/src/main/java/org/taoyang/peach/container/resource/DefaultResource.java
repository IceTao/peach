/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-09
 */
package org.taoyang.peach.container.resource;

import org.taoyang.peach.container.annotation.Component;

@Component
public class DefaultResource implements Resource {
    @Override
    public String value() {
        return "test";
    }
}
