package uk.ac.ebi.intact.graphdb.ws.controller.intercepter;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerInterceptor extends HandlerInterceptorAdapter {

    /*The below two method is needed so that heavy memory, being used by the end point, is released*/

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.gc();
    }
}
