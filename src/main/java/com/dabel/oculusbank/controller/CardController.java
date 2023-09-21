package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.service.delegate.DelegateCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CardController implements PageTitleConfig {

    @Autowired
    DelegateCardService delegateCardService;

    @GetMapping("/cards")
    public String dashboard(Model model) {

        setPageTitle(model, "Cards", null);
        return "cards";
    }

    @GetMapping("/cards/{cardId}")
    public String cardDetails(@PathVariable int cardId, Model model) {

        CardDTO card = delegateCardService.finById(cardId);
        setPageTitle(model, "Card Details", "Cards");
        model.addAttribute("card", card);
        return "cards-details";
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.CARDS);
    }
}
