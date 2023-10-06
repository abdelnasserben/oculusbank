package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.util.account.AccountChecker;
import com.dabel.oculusbank.app.util.CustomerChecker;
import com.dabel.oculusbank.constant.AccountMemberShip;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DelegateAccountService {

    @Autowired
    AccountService accountService;
    @Autowired
    CustomerService customerService;

    public void addJoint(String accountNumber, String identityNumber) {

        TrunkDTO account = accountService.findTrunkByNumber(accountNumber);
        if(!AccountChecker.isActive(account) || AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible for this operation");

        CustomerDTO customer = customerService.findByIdentityNumber(identityNumber);
        if(!CustomerChecker.isActive(customer))
            throw new IllegalOperationException("Customer must be active");

        //TODO: create new trunk for this account
        accountService.saveTrunk(account.getAccountId(), customer.getCustomerId(), AccountMemberShip.Jointed.name());

        //TODO: update the account profile to joint if it's not yet
        if(!AccountChecker.isJoint(account)) {
            account.setStatus(Status.Active.code());
            account.setAccountProfile(AccountProfile.Joint.name());
            accountService.save(account);
        }

    }

    public void addAssociate(String accountNumber, String identityNumber) {

        TrunkDTO account = accountService.findTrunkByNumber(accountNumber);
        if(!AccountChecker.isActive(account) || !AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible for this operation");

        CustomerDTO customer = customerService.findByIdentityNumber(identityNumber);
        if(!CustomerChecker.isActive(customer))
            throw new IllegalOperationException("Customer must be active");

        //TODO: create new trunk for this account
        accountService.saveTrunk(account.getAccountId(), customer.getCustomerId(), AccountMemberShip.Associated.name());

        //TODO: update the status of account is changed by the findTrunkByNumber method
        account.setStatus(Status.Active.code());
        accountService.save(account);
    }

    public List<TrunkDTO> findCustomerAccountsByCustomerId(int customerId) {
        return  accountService.findAllTrunksByCustomerId(customerId);
    }
}
