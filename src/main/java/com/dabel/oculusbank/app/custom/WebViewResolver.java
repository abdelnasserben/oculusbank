package com.dabel.oculusbank.app.custom;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
public class WebViewResolver implements ErrorViewResolver {
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        if(status == HttpStatus.NOT_FOUND || status == HttpStatus.FORBIDDEN)
            return new ModelAndView("redirect:/404");
        return null;
    }
}
