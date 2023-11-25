package com.dabel.oculusbank.service.core.customer;

import com.dabel.oculusbank.app.util.AccountNumberGenerator;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.service.core.account.BasicAccountCrudService;
import com.dabel.oculusbank.service.core.account.TrunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerFacadeService {

    @Autowired
    CustomerCrudService customerCrudService;
    @Autowired
    BasicAccountCrudService basicAccountCrudService;
    @Autowired
    TrunkService trunkService;

    public void create(CustomerDTO customerDTO, String accountType, String accountProfile, String accountMembership) {

        customerDTO.setStatus(Status.ACTIVE.code());
        CustomerDTO savedCustomer = customerCrudService.save(customerDTO);

        //TODO: save trunk KMF
        AccountDTO savedAccount = basicAccountCrudService.save(
                AccountDTO.builder()
                        .accountName(savedCustomer.getFirstName() + " " + savedCustomer.getLastName())
                        .accountNumber(AccountNumberGenerator.generate())
                        .accountType(accountType)
                        .accountProfile(accountProfile)
                        .currency(Currency.KMF.name())
                        .balance(0.0)
                        .status(Status.ACTIVE.code())
                        .build());

        trunkService.save(savedAccount.getAccountId(), savedCustomer.getCustomerId(), accountMembership);
    }

    public void update(CustomerDTO customerDTO) {

        CustomerDTO savedCustomer = customerCrudService.findById(customerDTO.getCustomerId());
        customerDTO.setBranchId(savedCustomer.getBranchId());

        customerCrudService.save(customerDTO);
    }

    public List<CustomerDTO> findAll() {
        return customerCrudService.findAll();
    }

    public CustomerDTO findById(int customerId) {
        return customerCrudService.findById(customerId);
    }

}
