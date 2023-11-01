package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.util.StatedObjectFormatter;
import com.dabel.oculusbank.app.util.card.CardExpirationDateSettlement;
import com.dabel.oculusbank.app.web.CardSubPageTitleConfig;
import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.View;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.CardApplicationDTO;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.service.delegate.DelegateCardApplicationService;
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
public class CardAppRequestController implements CardSubPageTitleConfig {

    @Autowired
    DelegateCardService delegateCardService;
    @Autowired
    DelegateCardApplicationService delegateCardApplicationService;
    @Autowired
    EntityManager entityManager;

    private static final String USEFUL_TITLE = "Card Application Requests";

    @GetMapping(value = Endpoint.Card.APPLICATION)
    public String applicationRequests(Model model, CardApplicationDTO cardApplicationDTO) {

        setTitleAndAddListOfAllApplicationRequestsAttribute(model);
        return View.Card.APPLICATION;
    }


    @PostMapping(value = Endpoint.Card.APPLICATION)
    public String sendNewApplicationRequests(Model model, @Valid CardApplicationDTO cardApplicationDTO, BindingResult binding, RedirectAttributes redirect) {

        if(binding.hasErrors()) {
            setTitleAndAddListOfAllApplicationRequestsAttribute(model);
            model.addAttribute(MessageTag.ERROR, "Invalid request application information !");
            return View.Card.APPLICATION;
        }

        delegateCardApplicationService.sendRequest(cardApplicationDTO);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Card application sent successfully !");

        return "redirect:" + Endpoint.Card.APPLICATION;
    }

    @GetMapping(value = Endpoint.Card.APPLICATION + "/{requestId}")
    public String applicationRequestsDetails(Model model, @PathVariable int requestId, CardDTO cardDTO) {

        CardApplicationDTO cardApplicationDTO = delegateCardApplicationService.findById(requestId);

        setPageTitle(model, "Request Details", " / Application Requests");
        model.addAttribute("requestDTO", StatedObjectFormatter.format(cardApplicationDTO));
        return View.Card.APPLICATION_DETAILS;
    }

    @PostMapping(value = Endpoint.Card.APPLICATION_APPROVE + "/{requestId}")
    public String approveApplicationRequest(Model model, @PathVariable int requestId, @Valid CardDTO cardDTO, BindingResult binding,
                                            @RequestParam String cardExpiryMonth,
                                            @RequestParam String cardExpiryYear,
                                            RedirectAttributes redirect) {

        CardApplicationDTO requestDTO = delegateCardApplicationService.findById(requestId);

        if(binding.hasErrors() || !requestDTO.getCardType().equals(cardDTO.getCardType()) || !requestDTO.getAccountNumber().equals(cardDTO.getAccountNumber())
                || !CardExpirationDateSettlement.isValidMonth(cardExpiryMonth) || !CardExpirationDateSettlement.isValidYear(cardExpiryYear))
            redirect.addFlashAttribute(MessageTag.ERROR, "Invalid card information !");
        else {

            //Clear cache
            entityManager.clear();

            //TODO: save the card
            //we set the expiration date before saving the card
            cardDTO.setExpirationDate(CardExpirationDateSettlement.setDate(cardExpiryMonth, cardExpiryYear));
            delegateCardService.add(cardDTO);

            //TODO: approve the request application
            delegateCardApplicationService.approve(requestId);
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Application approved successfully !");
        }

        return "redirect:" + Endpoint.Card.APPLICATION + "/" + requestId;
    }

    @PostMapping(value = Endpoint.Card.APPLICATION_REJECT + "/{requestId}")
    public String rejectApplicationRequest(@PathVariable int requestId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Reject reason reason is mandatory !");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Application request successfully rejected!");
            delegateCardApplicationService.reject(requestId, rejectReason);
        }

        return "redirect:" + Endpoint.Card.APPLICATION + "/" + requestId;
    }

    private void setTitleAndAddListOfAllApplicationRequestsAttribute(Model model) {
        setPageTitle(model, USEFUL_TITLE, null);
        model.addAttribute("cardApplications", StatedObjectFormatter.format(delegateCardApplicationService.findAll()));
    }
}
