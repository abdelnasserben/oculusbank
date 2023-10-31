package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.app.web.View;
import com.dabel.oculusbank.constant.SourceType;
import com.dabel.oculusbank.constant.TransactionType;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.service.delegate.DelegateTransactionService;
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
public class TransactionController implements PageTitleConfig {

    @Autowired
    DelegateTransactionService delegateTransactionService;

    @GetMapping(value = Endpoint.Transaction.ROOT)
    public String dashboard(Model model) {

        List<TransactionDTO> transactions = delegateTransactionService.findAll();

        setPageTitle(model, "Transactions", null);
        model.addAttribute("transactions", transactions);
        return View.Transaction.ROOT;
    }

    @GetMapping(value = Endpoint.Transaction.INIT)
    public String initBasicTransaction(Model model, TransactionDTO transactionDTO) {

        setPageTitle(model, "Transaction Init", "Transactions");
        return View.Transaction.INIT;
    }

    @PostMapping(value = Endpoint.Transaction.INIT)
    public String initBasicTransaction(Model model, @Valid TransactionDTO transactionDTO, BindingResult binding, RedirectAttributes redirect) {

        if(binding.hasErrors()) {
            setPageTitle(model, "Transaction Init", "Transactions");
            model.addAttribute(MessageTag.ERROR, "Invalid information!");
            return View.Transaction.INIT;
        }

        //We're online so we set the source
        transactionDTO.setSourceType(SourceType.Online.name());
        transactionDTO.setSourceValue("Branch Moroni HQ"); //we'll update this by the current user branch

        if(transactionDTO.getTransactionType().equals(TransactionType.Deposit.name()))
            delegateTransactionService.deposit(transactionDTO);
        else
            delegateTransactionService.withdraw(transactionDTO);

        redirect.addFlashAttribute(MessageTag.SUCCESS, transactionDTO.getTransactionType() + " successfully initiated.");
        return "redirect:" + Endpoint.Transaction.INIT;
    }

    @GetMapping(value = Endpoint.Transaction.ROOT + "/{transactionId}")
    public String transactionDetails(@PathVariable int transactionId, Model model) {

        TransactionDTO transactionDTO = delegateTransactionService.findById(transactionId);

        model.addAttribute("transaction", transactionDTO);
        setPageTitle(model, "Transaction Details", "Transactions");
        return View.Transaction.DETAILS;
    }

    @GetMapping(value = Endpoint.Transaction.APPROVE + "/{transactionId}")
    public String approveTransaction(@PathVariable int transactionId, RedirectAttributes redirect) {

        delegateTransactionService.approve(transactionId);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Transaction successfully approved!");

        return "redirect:" + Endpoint.Transaction.ROOT + "/" + transactionId;
    }

    @PostMapping(value = Endpoint.Transaction.REJECT + "/{transactionId}")
    public String rejectTransaction(@PathVariable int transactionId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Reject reason is mandatory!");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Transaction successfully rejected!");
            delegateTransactionService.reject(transactionId, rejectReason);
        }

        return "redirect:" + Endpoint.Transaction.ROOT + "/" + transactionId;
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.TRANSACTIONS);
    }
}
