package com.dabel.oculusbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/404")
    public String pageNotFound() {
        return "404";
    }
}
