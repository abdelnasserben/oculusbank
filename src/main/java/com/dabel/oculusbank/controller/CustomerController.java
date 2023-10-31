package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.util.card.CardNumberFormatter;
import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.AccountMemberShip;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.constant.web.Countries;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.*;
import com.dabel.oculusbank.service.LoanService;
import com.dabel.oculusbank.service.delegate.*;
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

import java.util.List;

@Controller
public class CustomerController implements PageTitleConfig {

    @Autowired
    DelegateCustomerService delegateCustomerService;
    @Autowired
    DelegateAccountService delegateAccountService;
    @Autowired
    DelegateCardService delegateCardService;
    @Autowired
    DelegateTransactionService delegateTransactionService;
    @Autowired
    DelegatePaymentService delegatePaymentService;
    @Autowired
    DelegateExchangeService delegateExchangeService;
    @Autowired
    LoanService loanService;
    @Autowired
    EntityManager entityManager;

    @GetMapping(value = Endpoint.Customer.ROOT)
    public String customers(Model model) {

        setPageTitle(model, "Customers", null);
        model.addAttribute("customers", delegateCustomerService.findAll());
        return "customers";
    }

    @GetMapping(value = Endpoint.Customer.ADD)
    public String addNewCustomer(Model model, CustomerDTO customerDTO) {

        setPageTitle(model, "Add Customer", "Customers");
        model.addAttribute("countries", Countries.getNames());
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
            model.addAttribute("countries", Countries.getNames());
            model.addAttribute(MessageTag.ERROR, "Invalid information !");
            return "customers-add";
        }

        customerDTO.setBranchId(1); //We'll replace this automatically by user authenticated
        String accountMembership = switch(accountProfile){
            case "Associative" -> AccountMemberShip.Associated.name();
            case "Joint" -> AccountMemberShip.Jointed.name();
            default -> AccountMemberShip.Owner.name();
        };
        delegateCustomerService.create(customerDTO, accountType, accountProfile, accountMembership);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Customer added successfully !");
        return "redirect:" + Endpoint.Customer.ADD;
    }

    @GetMapping(value = Endpoint.Customer.ROOT + "/{customerId}")
    public String customerDetails(@PathVariable int customerId, Model model) {

        CustomerDTO customer = delegateCustomerService.findById(customerId);

        List<TrunkDTO> customerAccounts = delegateAccountService.findCustomerAccountsByCustomerId(customerId);
        double totalBalance = customerAccounts.stream()
                        .mapToDouble(AccountDTO::getBalance)
                        .sum();

        //clearing the entity manager empties its associated cache
        // because the state held by the cache doesn't reflect what is in the database because
        // in this case: accounts in cache are status active name but no the status code
        entityManager.clear();

        List<CardDTO> customerCards = delegateCardService.findAllByCustomerId(customerId)
                .stream()
                .peek(c -> c.setCardNumber(CardNumberFormatter.hide(c.getCardNumber())))
                .toList();
        boolean notifyNoActiveCreditCards = customerCards.stream()
                        .anyMatch(c -> c.getStatus().equals(Status.Active.name()));

        //clear cache again
        entityManager.clear();

        List<TransactionDTO> lastTenCustomerTransactions = delegateTransactionService.findAllByCustomerId(customerId).stream()
                .limit(10)
                .toList();

        //clear cache again
        entityManager.clear();

        List<PaymentDTO> lastTenCustomerPayments = delegatePaymentService.findAllByCustomerId(customerId).stream()
                .limit(10)
                .toList();

        List<ExchangeDTO> lastTenCustomerExchanges = delegateExchangeService.findAllByCustomerIdentity(customer.getIdentityNumber()).stream()
                .limit(10)
                .toList();

        List<LoanDTO> customerLoans = loanService.findAllByCustomerIdentityNumber(customer.getIdentityNumber()).stream()
                .filter(l -> l.getStatus().equals(Status.Active.name()))
                .toList();

        setPageTitle(model, "Customer Details", "Customers");
        model.addAttribute("customer", customer);
        model.addAttribute("accounts", customerAccounts);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("cards", customerCards);
        model.addAttribute("countries", Countries.getNames());
        model.addAttribute("notifyNoActiveCreditCards", notifyNoActiveCreditCards);
        model.addAttribute("transactions", lastTenCustomerTransactions);
        model.addAttribute("payments", lastTenCustomerPayments);
        model.addAttribute("exchanges", lastTenCustomerExchanges);
        model.addAttribute("loans", customerLoans);

        return "customers-details";
    }

    @PostMapping(value = Endpoint.Customer.ROOT + "/{customerId}")
    public String updateCustomerGeneralInfo(Model model, @Valid CustomerDTO customer, BindingResult binding, RedirectAttributes redirect) {

        if(binding.hasErrors()) {
            setPageTitle(model, "Customer Details", "Customers");
            model.addAttribute("countries", Countries.getNames());
            model.addAttribute("customer", customer);
            model.addAttribute(MessageTag.ERROR, "Invalid information !");
            return "customers-details";
        }

        delegateCustomerService.update(customer);
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
