package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.TransactionSubPageTitleConfig;
import com.dabel.oculusbank.app.web.View;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.service.delegate.DelegateExchangeService;
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

import java.util.List;

@Controller
public class ExchangeController implements TransactionSubPageTitleConfig {

    private static final String USEFUL_BREADCRUMB = " / Exchanges";

    @Autowired
    DelegateExchangeService delegateExchangeService;

    @GetMapping(value = Endpoint.Exchange.ROOT)
    public String exchangesListing(Model model) {

        setPageTitle(model, "Exchanges", null);

        List<ExchangeDTO> exchanges = delegateExchangeService.findAll();
        model.addAttribute("exchanges", exchanges);

        return View.Exchange.ROOT;
    }

    @GetMapping(value = Endpoint.Exchange.INIT)
    public String initExchange(Model model, ExchangeDTO exchangeDTO) {
        setPageTitle(model, "Exchange init", USEFUL_BREADCRUMB);
        return View.Exchange.INIT;
    }

    @PostMapping(value = Endpoint.Exchange.INIT)
    public String initExchange(Model model, @Valid ExchangeDTO exchangeDTO, BindingResult binding, RedirectAttributes redirect) {

        if(binding.hasErrors()) {
            setPageTitle(model, "Exchange init", USEFUL_BREADCRUMB);
            return View.Exchange.INIT;
        }

        delegateExchangeService.exchange(exchangeDTO);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Exchange successfully initiated");
        return "redirect:" + Endpoint.Exchange.INIT;
    }

    @GetMapping(value = Endpoint.Exchange.ROOT + "/{exchangeId}")
    public String exchangeDetails(@PathVariable int exchangeId, Model model) {

        ExchangeDTO exchange = delegateExchangeService.findById(exchangeId);

        setPageTitle(model, "Exchange Details", USEFUL_BREADCRUMB);
        model.addAttribute("exchange", exchange);
        return View.Exchange.DETAILS;
    }

    @GetMapping(value = Endpoint.Exchange.APPROVE + "/{exchangeId}")
    public String approveExchange(@PathVariable int exchangeId, RedirectAttributes redirect) {

        delegateExchangeService.approve(exchangeId);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Exchange successfully approved!");

        return "redirect:" + Endpoint.Exchange.ROOT + "/" + exchangeId;
    }

    @PostMapping(value = Endpoint.Exchange.REJECT + "/{exchangeId}")
    public String rejectExchange(@PathVariable int exchangeId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Reject reason is mandatory!");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Exchange successfully rejected!");
            delegateExchangeService.reject(exchangeId, rejectReason);
        }

        return "redirect:" + Endpoint.Exchange.ROOT + "/" + exchangeId;
    }

}
