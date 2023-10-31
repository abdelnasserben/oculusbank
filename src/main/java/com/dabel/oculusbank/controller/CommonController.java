package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.View;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

    @GetMapping(value = Endpoint.Common.PAGE_404)
    public String pageNotFound() {
        return View.Common.PAGE_404;
    }
}
