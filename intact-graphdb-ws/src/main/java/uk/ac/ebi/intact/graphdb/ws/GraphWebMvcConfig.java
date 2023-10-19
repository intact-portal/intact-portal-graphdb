package uk.ac.ebi.intact.graphdb.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.ac.ebi.intact.graphdb.ws.controller.intercepter.ControllerInterceptor;

@Configuration
public class GraphWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*TODO... Remove addPathPatterns later when you see it works well with  /network/**
           so that all controller methods have their resources cleaned after rest endpoint is served*/
        registry.addInterceptor(new ControllerInterceptor()).addPathPatterns("/network/**");
    }
}
