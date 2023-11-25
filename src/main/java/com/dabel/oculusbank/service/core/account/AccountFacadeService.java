package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.constant.AccountPartnership;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.service.core.customer.CustomerCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountFacadeService {

    @Autowired
    TrunkService trunkService;
    @Autowired
    CustomerCrudService customerCrudService;
    @Autowired
    AccountPartnershipContext accountPartnershipContext;

    public void addMemberOn(String accountNumber, AccountPartnership subscriptionType, String identityNumber) {

        TrunkDTO account = trunkService.findByNumber(accountNumber);
        accountPartnershipContext.setContext(account.getAccountType()).add(account, subscriptionType, identityNumber);

        /*
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
        }*/

    }

    public void removeMemberOn(String accountNumber, String identityNumber) {

        TrunkDTO account = trunkService.findByNumber(accountNumber);
        accountPartnershipContext.setContext(account.getAccountType()).remove(account, identityNumber);
        /*
        if(!AccountChecker.isActive(account) || !AccountChecker.isAssociative(account))
            throw new IllegalOperationException("The account is not eligible for this operation");

        CustomerDTO customer = customerService.findByIdentityNumber(identityNumber);
        if(!CustomerChecker.isActive(customer))
            throw new IllegalOperationException("Customer must be active");

        //TODO: create new trunk for this account
        accountService.saveTrunk(account.getAccountId(), customer.getCustomerId(), AccountMemberShip.Associated.name());

        //TODO: update the status of account is changed by the findTrunkByNumber method
        account.setStatus(Status.Active.code());
        accountService.save(account);*/
    }

    public List<TrunkDTO> findAllTrunksByCustomerId(int customerId) {
        return  trunkService.findAllByCustomerId(customerId);
    }
}
