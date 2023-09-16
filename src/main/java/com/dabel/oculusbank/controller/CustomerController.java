package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController implements PageTitleConfig {

    @GetMapping("/customers")
    public String listOfCustomers(Model model) {

        setPageTitle(model, "Customers");
        return "customers";
    }

    @Override
    public void setPageTitle(Model model, String pageTitle) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", "Customers");
        model.addAttribute("currentPage", CurrentPageTitle.CUSTOMERS);
    }
}
