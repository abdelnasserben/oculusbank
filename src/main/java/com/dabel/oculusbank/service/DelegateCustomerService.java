package com.dabel.oculusbank.service;

import com.dabel.oculusbank.app.Generator;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelegateCustomerService {

    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;

    public CustomerDTO createWithOwnAccountsAtOnce(CustomerDTO customerDTO) {

        customerDTO.setStatus(Status.Active.code());
        CustomerDTO savedCustomer = customerService.save(customerDTO);
        //TODO: save trunk KMF
        TrunkDTO trunkDTO = TrunkDTO.builder()
                .customerId(savedCustomer.getCustomerId())
                .accountName(savedCustomer.getFirstName() + " " + savedCustomer.getLastName())
                .accountNumber(Generator.generateAccountNumber())
                .accountType(AccountType.Current.name())
                .accountProfile(AccountProfile.Personal.name())
                .currency(Currency.KMF.name())
                .balance(0.0)
                .status(Status.Active.code())
                .build();
        accountService.saveTrunk(trunkDTO);

        return savedCustomer;
    }
}
