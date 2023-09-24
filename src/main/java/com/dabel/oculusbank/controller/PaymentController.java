package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.TransactionSubPageTitleConfig;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.service.delegate.DelegatePaymentService;
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
public class PaymentController implements TransactionSubPageTitleConfig {

    private static final String USEFUL_BREADCRUMB = " / Payments";

    @Autowired
    DelegatePaymentService delegatePaymentService;

    @GetMapping(value = Endpoint.Payments.ROOT)
    public String paymentsListing(Model model) {

        setPageTitle(model, "Payments", null);

        List<PaymentDTO> payments = delegatePaymentService.findAll();
        model.addAttribute("payments", payments);
        return "payments";
    }

    @GetMapping(value = Endpoint.Payments.INIT)
    public String initPayment(Model model, PaymentDTO paymentDTO) {

        setPageTitle(model, "Payment Init", "Init");
        return "payments-init";
    }

    @PostMapping(value = Endpoint.Payments.INIT)
    public String initExchange(Model model, @Valid PaymentDTO paymentDTO, BindingResult binding, RedirectAttributes redirect) {

        setPageTitle(model, "Payments", USEFUL_BREADCRUMB);

        if(binding.hasErrors()) {
            return "payments-init";
        }

        delegatePaymentService.pay(paymentDTO);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Payment successfully initiated");
        return "redirect:" + Endpoint.Payments.INIT;
    }

    @GetMapping(value = Endpoint.Payments.ROOT + "/{paymentId}")
    public String paymentDetails(@PathVariable int paymentId, Model model) {

        PaymentDTO payment = delegatePaymentService.findById(paymentId);

        setPageTitle(model, "Payment Details", USEFUL_BREADCRUMB);
        model.addAttribute("payment", payment);

        return "payments-details";
    }

    @GetMapping(value = Endpoint.Payments.APPROVE + "/{paymentId}")
    public String approvePayment(@PathVariable int paymentId, RedirectAttributes redirect) {

        delegatePaymentService.approve(paymentId);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Payment successfully approved!");

        return "redirect:" + Endpoint.Payments.ROOT + "/" + paymentId;
    }

    @PostMapping(value = Endpoint.Payments.REJECT + "/{paymentId}")
    public String rejectPayment(@PathVariable int paymentId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Reject reason is mandatory!");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Payment successfully rejected!");
            delegatePaymentService.reject(paymentId, rejectReason);
        }

        return "redirect:" + Endpoint.Payments.ROOT + "/" + paymentId;
    }

}
