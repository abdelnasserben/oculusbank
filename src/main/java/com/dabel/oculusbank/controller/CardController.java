package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.CardSubPageTitleConfig;
import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.View;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.service.delegate.DelegateCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CardController implements CardSubPageTitleConfig {

    @Autowired
    DelegateCardService delegateCardService;

    @GetMapping(value = Endpoint.Card.ROOT + "/{cardId}")
    public String cardDetails(@PathVariable int cardId, Model model) {

        CardDTO card = delegateCardService.finById(cardId);
        setPageTitle(model, "Card Details", null);
        model.addAttribute("card", card);
        return View.Card.DETAILS;
    }

    @GetMapping(value = Endpoint.Card.ACTIVATE + "/{cardId}")
    public String approveCard(@PathVariable int cardId, RedirectAttributes redirect) {

        delegateCardService.activate(cardId);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Card successfully activated !");

        return "redirect:" + Endpoint.Card.ROOT + "/" + cardId;
    }

    @PostMapping(value = Endpoint.Card.DEACTIVATE + "/{cardId}")
    public String rejectCard(@PathVariable int cardId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Deactivate reason is mandatory !");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Card successfully deactivated!");
            delegateCardService.deactivate(cardId, rejectReason);
        }

        return "redirect:" + Endpoint.Card.ROOT + "/" + cardId;
    }

}
