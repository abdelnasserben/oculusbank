package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.util.StatedObjectFormatter;
import com.dabel.oculusbank.app.util.CardNumberFormatter;
import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.AccountMembership;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.*;
import com.dabel.oculusbank.service.core.account.AccountFacadeService;
import com.dabel.oculusbank.service.core.card.CardFacadeService;
import com.dabel.oculusbank.service.core.customer.CustomerFacadeService;
import com.dabel.oculusbank.service.core.exchange.ExchangeFacadeService;
import com.dabel.oculusbank.service.core.loan.LoanFacadeService;
import com.dabel.oculusbank.service.core.payment.PaymentFacadeService;
import com.dabel.oculusbank.service.core.transaction.TransactionFacadeService;
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
public class CustomerController implements PageTitleConfig {

    @Autowired
    CustomerFacadeService customerFacadeService;
    @Autowired
    AccountFacadeService accountFacadeService;
    @Autowired
    CardFacadeService cardFacadeService;
    @Autowired
    TransactionFacadeService transactionFacadeService;
    @Autowired
    PaymentFacadeService paymentFacadeService;
    @Autowired
    ExchangeFacadeService exchangeFacadeService;
    @Autowired
    LoanFacadeService loanFacadeService;

    @GetMapping(value = Endpoint.Customer.ROOT)
    public String customers(Model model) {

        setPageTitle(model, "Customers", null);
        model.addAttribute("customers", StatedObjectFormatter.format(customerFacadeService.findAll()));
        return "customers";
    }

    @GetMapping(value = Endpoint.Customer.ADD)
    public String addNewCustomer(Model model, CustomerDTO customerDTO) {

        setPageTitle(model, "Add Customer", "Customers");
        return "customers-add";
    }

    @PostMapping(value = Endpoint.Customer.ADD)
    public String addNewCustomer(Model model, @Valid CustomerDTO customerDTO, BindingResult binding,
                                          @RequestParam(required = false) String accountName,
                                          @RequestParam(defaultValue = "Saving") String accountType,
                                          @RequestParam(required = false) String accountProfile,
                                          RedirectAttributes redirect) {

        if(binding.hasErrors()) {
            setPageTitle(model, "Add Customer", "Customers");
            model.addAttribute(MessageTag.ERROR, "Invalid information !");
            return "customers-add";
        }

        customerDTO.setBranchId(1); //We'll replace this automatically by user authenticated
        String accountMembership = switch(accountProfile){
            case "Associative" -> AccountMembership.ASSOCIATED.name();
            case "Joint" -> AccountMembership.JOINTED.name();
            default -> AccountMembership.OWNER.name();
        };
        customerFacadeService.create(customerDTO, accountType, accountProfile, accountMembership);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Customer added successfully !");
        return "redirect:" + Endpoint.Customer.ADD;
    }

    @GetMapping(value = Endpoint.Customer.ROOT + "/{customerId}")
    public String customerDetails(@PathVariable int customerId, Model model) {

        CustomerDTO customer = customerFacadeService.findById(customerId);

        List<TrunkDTO> customerAccounts = accountFacadeService.findAllTrunksByCustomerId(customerId);
        double totalBalance = customerAccounts.stream()
                        .mapToDouble(AccountDTO::getBalance)
                        .sum();

        List<CardDTO> customerCards = cardFacadeService.findAllByCustomerId(customerId)
                .stream()
                .peek(c -> c.setCardNumber(CardNumberFormatter.hide(c.getCardNumber())))
                .toList();
        boolean notifyNoActiveCreditCards = customerCards.stream()
                        .anyMatch(c -> c.getStatus().equals(Status.ACTIVE.code()));

        List<TransactionDTO> lastTenCustomerTransactions = transactionFacadeService.findAllByCustomerId(customerId).stream()
                .limit(10)
                .toList();

        List<PaymentDTO> lastTenCustomerPayments = paymentFacadeService.findAllByCustomerId(customerId).stream()
                .limit(10)
                .toList();

        List<ExchangeDTO> lastTenCustomerExchanges = exchangeFacadeService.findAllByCustomerIdentity(customer.getIdentityNumber()).stream()
                .limit(10)
                .toList();

        List<LoanDTO> customerLoans = loanFacadeService.findAllByCustomerIdentityNumber(customer.getIdentityNumber()).stream()
                .filter(l -> l.getStatus().equals(Status.ACTIVE.code()))
                .toList();

        double totalLoan = customerLoans.stream()
                        .mapToDouble(LoanDTO::getTotalAmount)
                        .sum();

        setPageTitle(model, "Customer Details", "Customers");
        model.addAttribute("customer", StatedObjectFormatter.format(customer));
        model.addAttribute("accounts", StatedObjectFormatter.format(customerAccounts));
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("cards", StatedObjectFormatter.format(customerCards));
        model.addAttribute("notifyNoActiveCreditCards", notifyNoActiveCreditCards);
        model.addAttribute("transactions", StatedObjectFormatter.format(lastTenCustomerTransactions));
        model.addAttribute("payments", StatedObjectFormatter.format(lastTenCustomerPayments));
        model.addAttribute("exchanges", StatedObjectFormatter.format(lastTenCustomerExchanges));
        model.addAttribute("loans", StatedObjectFormatter.format(customerLoans));
        model.addAttribute("totalLoan", totalLoan);

        return "customers-details";
    }

    @PostMapping(value = Endpoint.Customer.ROOT + "/{customerId}")
    public String updateCustomerGeneralInfo(Model model, @Valid CustomerDTO customer, BindingResult binding, RedirectAttributes redirect) {

        if(binding.hasErrors()) {
            setPageTitle(model, "Customer Details", "Customers");
            model.addAttribute("customer", customer);
            model.addAttribute(MessageTag.ERROR, "Invalid information !");
            return "customers-details";
        }

        customerFacadeService.update(customer);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "Customer information updated successfully !");
        return "redirect:" + Endpoint.Customer.ROOT + "/" + customer.getCustomerId();
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.CUSTOMERS);
    }
}
