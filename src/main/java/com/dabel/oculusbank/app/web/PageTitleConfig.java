package com.dabel.oculusbank.app.web;

import org.springframework.ui.Model;

public interface PageTitleConfig {
    void setPageTitle(Model model, String pageTitle, String breadcrumb);
}
