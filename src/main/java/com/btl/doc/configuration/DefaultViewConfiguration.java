package com.btl.doc.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DefaultViewConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/swagger").setViewName("forward:/swagger/index.html");
        registry.addViewController("/swagger/").setViewName("forward:/swagger/index.html");
        registry.addViewController("/doc").setViewName("forward:/doc/index.html");
        registry.addViewController("/doc/").setViewName("forward:/doc/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
}