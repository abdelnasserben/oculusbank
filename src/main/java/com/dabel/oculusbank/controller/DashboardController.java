package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController implements PageTitleConfig {

    @GetMapping
    public String dashboard(Model model) {

        setPageTitle(model, "Dashboard");
        return "dashboard";
    }

    @Override
    public void setPageTitle(Model model, String pageTitle) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", "Dashboard");
        model.addAttribute("currentPage", CurrentPageTitle.DASHBOARD);
    }
}
