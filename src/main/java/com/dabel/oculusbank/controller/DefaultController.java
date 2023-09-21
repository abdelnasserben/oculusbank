package com.dabel.oculusbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {

    @GetMapping("/404")
    public String pageNotFound() {
        return "404";
    }
}
