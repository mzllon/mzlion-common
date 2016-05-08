package com.mzlion.okhttp.web.spring;

import com.mzlion.okhttp.web.spring.config.ApplicationContextConfig;
import com.mzlion.okhttp.web.spring.config.WebContextConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 2016-05-07 DispatcherServlet config.{@linkplain SpringWebApplicationInitializer} class looks like as same as <code>web.xml</code>
 * </p>
 *
 * @author mzlion
 */
public class SpringWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ApplicationContextConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebContextConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true);
        return new Filter[]{characterEncodingFilter};
    }
}
