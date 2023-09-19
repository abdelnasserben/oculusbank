package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.Generator;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

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

    public CustomerDTO findById(int customerId) {
        return customerService.findById(customerId);
    }

    public CustomerDTO update(CustomerDTO customerDTO) {

        CustomerDTO savedCustomer = customerService.findById(customerDTO.getCustomerId());
        customerDTO.setBranchId(savedCustomer.getBranchId());
        customerDTO.setStatus(Status.codeOf(savedCustomer.getStatus()));

        return customerService.save(customerDTO);
    }

    public List<CustomerDTO> findAll() {
        return customerService.findAll();
    }
}
