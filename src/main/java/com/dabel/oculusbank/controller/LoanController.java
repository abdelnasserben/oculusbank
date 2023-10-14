package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.LoanDTO;
import com.dabel.oculusbank.service.LoanService;
import com.dabel.oculusbank.service.delegate.DelegateLoanService;
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
public class LoanController implements PageTitleConfig {

    @Autowired
    DelegateLoanService delegateLoanService;
    @Autowired
    LoanService loanService;
    @Autowired
    EntityManager entityManager;

    @GetMapping(value = Endpoint.Loans.ROOT)
    public String loansListing(Model model, LoanDTO loanDTO) {

        setPageTitle(model, "Loans", null);
        model.addAttribute("loans", loanService.findAll());
        return "loans";
    }

    @PostMapping(value = Endpoint.Loans.ROOT)
    public String initLoan(Model model, @Valid LoanDTO loanDTO, BindingResult binding, RedirectAttributes redirect) {

        if(binding.hasErrors()) {
            setPageTitle(model, "Loans", null);
            model.addAttribute(MessageTag.ERROR, "Invalid information!");
            return "loans";
        }

        entityManager.clear();
        delegateLoanService.loan(loanDTO);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Loan successfully initiated.");
        return "redirect:" + Endpoint.Loans.ROOT;
    }

    @GetMapping(value = Endpoint.Loans.ROOT + "/{loanId}")
    public String loanDetails(Model model, @PathVariable int loanId) {

        LoanDTO loanDTO = loanService.findLoanById(loanId);
        setPageTitle(model, "Loan Details", "Loans");
        model.addAttribute("loan", loanDTO);
        return "loans-details";
    }

    @GetMapping(value = Endpoint.Loans.APPROVE + "/{loanId}")
    public String approveELoan(@PathVariable int loanId, RedirectAttributes redirect) {

        delegateLoanService.approve(loanId);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Loan successfully approved!");

        return "redirect:" + Endpoint.Loans.ROOT + "/" + loanId;
    }

    @PostMapping(value = Endpoint.Loans.REJECT + "/{loanId}")
    public String rejectExchange(@PathVariable int loanId, @RequestParam String rejectReason, RedirectAttributes redirect) {

        if(rejectReason.isBlank())
            redirect.addFlashAttribute(MessageTag.ERROR, "Reject reason is mandatory!");
        else {
            redirect.addFlashAttribute(MessageTag.SUCCESS, "Loan successfully rejected!");
            delegateLoanService.reject(loanId, rejectReason);
        }

        return "redirect:" + Endpoint.Loans.ROOT + "/" + loanId;
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.LOANS);
    }
}
