/*
 * Author:  taoyang <ice@taoyang.org>
 * Created: 2017-05-08
 */
package org.taoyang.peach.container.resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public interface ClassLoaderFile {

    URL getURL() throws IOException;

    URI getURI() throws IOException;

    File getFile() throws IOException;

    long contentLength() throws IOException;

    long lastModified() throws IOException;

    String getFilename();

    String getDescription();
}
