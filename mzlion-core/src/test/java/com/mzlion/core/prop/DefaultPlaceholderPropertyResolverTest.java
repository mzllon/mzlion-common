package com.mzlion.core.prop;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by mzlion on 2016/4/11.
 */
public class DefaultPlaceholderPropertyResolverTest {

    @Test
    public void constructorTest() throws Exception {
        PropertyResolver propertyResolver = new DefaultPlaceholderPropertyResolver.Builder()
                .path("classpath:system.properties").build();
        Map<String, String> allProperties = propertyResolver.getAllProperties();
        System.out.println(allProperties);
    }

    @Test
    public void containsProperty() throws Exception {
        PropertyResolver propertyResolver = new DefaultPlaceholderPropertyResolver.Builder()
                .path("classpath:system.properties").build();
        boolean contains = propertyResolver.containsProperty("admin");
        Assert.assertTrue(contains);
    }

    @Test
    public void getProperty() throws Exception {
        PropertyResolver propertyResolver = new DefaultPlaceholderPropertyResolver.Builder()
                .path("classpath:system.properties").build();
        String port = propertyResolver.getProperty("port");
        Assert.assertEquals("3306", port);
    }

    @Test
    public void getProperty1() throws Exception {

    }

    @Test
    public void resolvePlaceholders() throws Exception {

    }

}