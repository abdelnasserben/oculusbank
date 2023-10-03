package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.CardHelper;
import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.CardAppRequestDTO;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.service.delegate.DelegateCardAppRequestService;
import com.dabel.oculusbank.service.delegate.DelegateCardService;
import jakarta.persistence.EntityManager;
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
    @Autowired
    DelegateCardAppRequestService delegateCardAppRequestService;
    @Autowired
    EntityManager entityManager;

    @GetMapping(value = Endpoint.Cards.APP_REQUEST)
    public String applicationRequests(Model model, CardAppRequestDTO cardAppRequestDTO) {

        setPageTitle(model, "Cards Applications", "Cards");
        model.addAttribute("cardApplications", delegateCardAppRequestService.findAll());
        return "cards-applications";
    }

    @PostMapping(value = Endpoint.Cards.APP_REQUEST)
    public String sendNewApplicationRequests(Model model, @Valid CardAppRequestDTO cardAppRequestDTO, BindingResult binding, RedirectAttributes redirect) {

        setPageTitle(model, "Cards", null);
        model.addAttribute("cardApplications", delegateCardAppRequestService.findAll());

        if(binding.hasErrors()) {
            model.addAttribute(MessageTag.ERROR, "Invalid request application information !");
            return "cards-applications";
        }

        delegateCardAppRequestService.sendRequest(cardAppRequestDTO);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Card application sent successfully !");

        return "redirect:" + Endpoint.Cards.APP_REQUEST;
    }

    @GetMapping(value = Endpoint.Cards.APP_REQUEST + "/{requestId}")
    public String applicationRequestsDetails(Model model, @PathVariable int requestId, CardDTO cardDTO) {

        CardAppRequestDTO requestDTO = delegateCardAppRequestService.findById(requestId);

        setPageTitle(model, "Request Details", "Cards / Cards Applications");
        model.addAttribute("requestDTO", requestDTO);
        return "cards-applications-details";
    }

    @PostMapping(value = Endpoint.Cards.APP_REQUEST_APPROVE + "/{requestId}")
    public String approveApplicationRequest(Model model, @PathVariable int requestId, @Valid CardDTO cardDTO, BindingResult binding,
                                            @RequestParam String cardExpiryMonth,
                                            @RequestParam String cardExpiryYear,
                                            RedirectAttributes redirect) {

        CardAppRequestDTO requestDTO = delegateCardAppRequestService.findById(requestId);


        if(binding.hasErrors() || !requestDTO.getCardType().equals(cardDTO.getCardType()) || !requestDTO.getAccountNumber().equals(cardDTO.getAccountNumber())
                || !CardHelper.isValidMonth(cardExpiryMonth) || !CardHelper.isValidYear(cardExpiryYear))
            redirect.addFlashAttribute(MessageTag.ERROR, "Invalid card information !");
        else {

            //Clear cache
            entityManager.clear();

            //TODO: save the card
            //we set the expiration date before saving the card
            cardDTO.setExpirationDate(CardHelper.setExpirationDate(cardExpiryMonth, cardExpiryYear));
            delegateCardService.add(cardDTO);

            //TODO: approve the request application
            delegateCardAppRequestService.approve(requestId);
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Application approved successfully !");
        }

        return "redirect:" + Endpoint.Cards.APP_REQUEST + "/" + requestId;
    }

    @PostMapping(value = Endpoint.Cards.APP_REQUEST_REJECT + "/{requestId}")
    public String rejectApplicationRequest(@PathVariable int requestId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Reject reason reason is mandatory !");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Application request successfully rejected!");
            delegateCardAppRequestService.reject(requestId, rejectReason);
        }

        return "redirect:" + Endpoint.Cards.APP_REQUEST + "/" + requestId;
    }


    @GetMapping(value = Endpoint.Cards.ROOT + "/{cardId}")
    public String cardDetails(@PathVariable int cardId, Model model) {

        CardDTO card = delegateCardService.finById(cardId);
        setPageTitle(model, "Card Details", "Cards");
        model.addAttribute("card", card);
        return "cards-details";
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
