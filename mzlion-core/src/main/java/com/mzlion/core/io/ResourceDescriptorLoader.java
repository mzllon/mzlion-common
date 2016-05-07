package com.mzlion.core.io;

/**
 * Created by mzlion on 2016/5/6.
 */
public interface ResourceDescriptorLoader {

    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    ResourceDescriptor getResourceDescriptor(String location);

    ClassLoader getClassLoader();

}
