package com.dabel.oculusbank.app.web;

import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import org.springframework.ui.Model;

public interface CardSubPageTitleConfig {

    default void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        breadcrumb = breadcrumb != null ? breadcrumb : "";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", "Cards" + breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.CARDS);
    }
}
