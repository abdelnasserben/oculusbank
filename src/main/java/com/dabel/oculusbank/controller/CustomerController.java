package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.constant.AccountMemberShip;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.service.delegate.DelegateCustomerService;
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
public class CustomerController implements PageTitleConfig {

    @Autowired
    DelegateCustomerService delegateCustomerService;

    @GetMapping("/customers")
    public String customers(Model model) {

        setPageTitle(model, "Customers", null);
        model.addAttribute("customers", delegateCustomerService.findAll());
        return "customers";
    }

    @GetMapping("/customers/add")
    public String addNewCustomer(Model model, CustomerDTO customerDTO) {
        setPageTitle(model, "Add Customer", "Customers");
        return "customers-add";
    }

    @PostMapping("/customers/add")
    public String addNewCustomerPostProcess(Model model, @Valid CustomerDTO customerDTO, BindingResult binding,
                                          @RequestParam(required = false) String accountName,
                                          @RequestParam(defaultValue = "Saving") String accountType,
                                          @RequestParam(required = false) boolean accountProfile,
                                          RedirectAttributes redirect) {

        setPageTitle(model, "Add Customer", "Customers");

        if(binding.hasErrors()) {
            model.addAttribute(MessageTag.ERROR, "Invalid information !");
            return "customers-add";
        }

        customerDTO.setBranchId(1); //We'll replace this automatically by user authenticated
        String accountProfileValue = accountProfile ? AccountProfile.Associative.name() : AccountProfile.Personal.name();
        String accountMembership = accountProfile ? AccountMemberShip.Associated.name() : AccountMemberShip.Owner.name();
        delegateCustomerService.create(customerDTO, accountType, accountProfileValue, accountMembership);

        redirect.addFlashAttribute(MessageTag.SUCCESS, "Customer added successfully !");
        return "redirect:/customers/add";
    }

    @GetMapping("/customers/{customerId}")
    public String customerDetails(@PathVariable int customerId, Model model) {

        CustomerDTO customer = delegateCustomerService.findById(customerId);
        setPageTitle(model, "Customer Details", "Customers");
        model.addAttribute("customer", customer);

        return "customers-details";
    }

    @PostMapping("/customers/{customerId}")
    public String updateCustomerInfo(Model model, @Valid CustomerDTO customer, BindingResult binding) {

        setPageTitle(model, "Customer Details", "Customers");

        if(binding.hasErrors()) {
            model.addAttribute("customer", customer);
            model.addAttribute(MessageTag.ERROR, "Invalid information !");
            return "customers-details";
        }

        customer = delegateCustomerService.update(customer);
        model.addAttribute("customer", customer);
        model.addAttribute(MessageTag.SUCCESS, "We'll update later !");
        return "customers-details";
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.CUSTOMERS);
    }
}
