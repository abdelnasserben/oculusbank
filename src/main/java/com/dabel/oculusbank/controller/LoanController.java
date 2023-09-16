package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoanController implements PageTitleConfig {

    @GetMapping("/loans")
    public String dashboard(Model model) {

        setPageTitle(model, "Loans");
        return "loans";
    }

    @Override
    public void setPageTitle(Model model, String pageTitle) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", "Loans");
        model.addAttribute("currentPage", CurrentPageTitle.LOANS);
    }
}
