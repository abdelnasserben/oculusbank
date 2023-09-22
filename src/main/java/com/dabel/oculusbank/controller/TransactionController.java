package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.PageTitleConfig;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TransactionController implements PageTitleConfig {

    @Autowired
    DelegateTransactionService delegateTransactionService;

    @GetMapping("/transactions")
    public String dashboard(Model model) {

        List<TransactionDTO> transactions = delegateTransactionService.findAll();

        setPageTitle(model, "Transactions", null);
        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/transactions/init")
    public String initBasicTransaction(Model model, TransactionDTO transactionDTO) {

        setPageTitle(model, "Transaction Init", "Transactions");
        return "transactions-init";
    }

    @GetMapping("/transactions/{transactionId}")
    public String transactionDetails(@PathVariable int transactionId, Model model) {

        TransactionDTO transactionDTO = delegateTransactionService.findById(transactionId);

        model.addAttribute("transaction", transactionDTO);
        setPageTitle(model, "Transaction Details", "Transactions");
        return "transactions-details";
    }

    @PostMapping("/transactions/init")
    public String initBasicTransaction(Model model, @Valid TransactionDTO transactionDTO, BindingResult binding, RedirectAttributes redirect) {

        setPageTitle(model, "Transaction Init", "Transactions");

        if(binding.hasErrors()) {
            model.addAttribute(MessageTag.ERROR, "Invalid information!");
            return "transactions-init";
        }

        //We're online so we set the source
        transactionDTO.setSourceType(SourceType.Online.name());
        transactionDTO.setSourceValue("Branch Moroni HQ"); //we'll update this by the current user branch

        if(transactionDTO.getTransactionType().equals(TransactionType.Deposit.name()))
            delegateTransactionService.deposit(transactionDTO);
        else
            delegateTransactionService.withdraw(transactionDTO);

        redirect.addFlashAttribute(MessageTag.SUCCESS, transactionDTO.getTransactionType() + " successfully initiated.");
        return "redirect:/transactions/init";
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.TRANSACTIONS);
    }
}
