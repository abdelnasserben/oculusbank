package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.app.web.View;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChequeController implements PageTitleConfig {

    @GetMapping(value = Endpoint.Cheque.ROOT)
    public String dashboard(Model model) {

        setPageTitle(model, "Cheques", null);
        return View.Cheque.ROOT;
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.CHEQUES);
    }
}
