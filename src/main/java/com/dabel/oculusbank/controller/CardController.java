package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.CardHelper;
import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.service.delegate.DelegateCardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CardController implements PageTitleConfig {

    @Autowired
    DelegateCardService delegateCardService;

    @GetMapping(value = Endpoint.Cards.ROOT)
    public String dashboard(Model model) {

        setPageTitle(model, "Cards", null);
        return "cards";
    }

    @GetMapping(value = Endpoint.Cards.ROOT + "/{cardId}")
    public String cardDetails(@PathVariable int cardId, Model model) {

        CardDTO card = delegateCardService.finById(cardId);
        setPageTitle(model, "Card Details", "Cards");
        model.addAttribute("card", card);
        return "cards-details";
    }

    @GetMapping(value = Endpoint.Cards.ADD)
    public String addNewCard(Model model, CardDTO cardDTO) {

        setPageTitle(model,"Add New Card", "Cards");
        return "cards-add";
    }

    @PostMapping(value = Endpoint.Cards.ADD)
    public String addNewCardOnAccount(Model model, @Valid CardDTO cardDTO, BindingResult binding,
                                      @RequestParam String cardExpiryMonth,
                                      @RequestParam String cardExpiryYear,
                                      RedirectAttributes redirect) {

        setPageTitle(model,"Add New Card", "Cards");

        if(binding.hasErrors() || !CardHelper.isValidMonth(cardExpiryMonth) || !CardHelper.isValidYear(cardExpiryYear)) {
            model.addAttribute(MessageTag.ERROR, "Invalid card information !");
            return "cards-add";
        }

        //we set the expiration date before saving
        cardDTO.setExpirationDate(CardHelper.setExpirationDate(cardExpiryMonth, cardExpiryYear));
        delegateCardService.add(cardDTO);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Card added successfully !");

        return "redirect:" + Endpoint.Cards.ADD;
    }

    @GetMapping(value = Endpoint.Cards.ACTIVATE + "/{cardId}")
    public String approveCard(@PathVariable int cardId, RedirectAttributes redirect) {

        delegateCardService.activate(cardId);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Card successfully activated !");

        return "redirect:" + Endpoint.Cards.ROOT + "/" + cardId;
    }

    @PostMapping(value = Endpoint.Cards.DEACTIVATE + "/{cardId}")
    public String rejectCard(@PathVariable int cardId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Deactivate reason is mandatory !");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Card successfully deactivated!");
            delegateCardService.deactivate(cardId, rejectReason);
        }

        return "redirect:" + Endpoint.Cards.ROOT + "/" + cardId;
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.CARDS);
    }
}
