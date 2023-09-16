package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BranchController implements PageTitleConfig {

    @GetMapping("/branches")
    public String dashboard(Model model) {

        setPageTitle(model, "Branches");
        return "branches";
    }

    @Override
    public void setPageTitle(Model model, String pageTitle) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", "Settings / Branches");
        model.addAttribute("currentPage", CurrentPageTitle.SETTINGS);
    }
}
