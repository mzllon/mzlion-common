package com.mzlion.core.io.resource;

import com.mzlion.core.lang.Assert;
import com.mzlion.core.lang.ClassLoaderUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mzlion on 2016/5/6.
 */
public class DefaultResourceDescriptorLoader implements ResourceDescriptorLoader {

    private ClassLoader classLoader;

    public DefaultResourceDescriptorLoader() {
        this(ClassLoaderUtils.getDefaultClassLoader());
    }

    public DefaultResourceDescriptorLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ResourceDescriptor getResourceDescriptor(String location) {
        Assert.assertHasLength(location, "Location must not be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResourceDescriptor(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
        } else {
            try {
                // Try to parse the location as a URL...
                URL url = new URL(location);
                return new URLResourceDescriptor(url);
            } catch (MalformedURLException ex) {
                return new FileSystemResourceDescriptor(location);
            }
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : ClassLoaderUtils.getDefaultClassLoader());
    }
}
