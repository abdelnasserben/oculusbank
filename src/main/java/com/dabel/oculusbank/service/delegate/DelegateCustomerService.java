package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.Generator;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelegateCustomerService {

    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;

    public CustomerDTO create(CustomerDTO customerDTO) {

        customerDTO.setStatus(Status.Active.code());
        return customerService.save(customerDTO);
    }

    public CustomerDTO create(CustomerDTO customerDTO, String accountType, String accountProfile, String accountMembership) {

        customerDTO.setStatus(Status.Active.code());
        CustomerDTO savedCustomer = customerService.save(customerDTO);

        //TODO: save trunk KMF
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName(savedCustomer.getFirstName() + " " + savedCustomer.getLastName())
                .accountNumber(Generator.generateAccountNumber())
                .accountType(accountType)
                .accountProfile(accountProfile)
                .currency(Currency.KMF.name())
                .balance(0.0)
                .status(Status.Active.code())
                .build());

        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), accountMembership);

        return savedCustomer;
    }
}
