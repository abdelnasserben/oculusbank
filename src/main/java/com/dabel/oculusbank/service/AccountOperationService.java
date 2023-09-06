package com.dabel.oculusbank.service;

import com.dabel.oculusbank.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountOperationService {

    @Autowired
    AccountService accountService;

    public void debit(AccountDTO account, double amount) {
        account.setBalance(account.getBalance() - amount);
        accountService.save(account);
    }

    public void credit(AccountDTO account, double amount) {
        account.setBalance(account.getBalance() + amount);
        accountService.save(account);
    }
}
