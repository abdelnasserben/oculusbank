package com.dabel.oculusbank.service.core.transaction;

import com.dabel.oculusbank.app.util.AccountChecker;
import com.dabel.oculusbank.app.util.CurrencyExchanger;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.constant.TransactionType;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Deposit extends Transaction {

    @Override
    public void init(TransactionDTO transactionDTO) {
        AccountDTO account = basicAccountCrudService.findByNumber(transactionDTO.getAccountNumber());

        if(!AccountChecker.isActive(account))
            throw new IllegalOperationException("Account must be active");

        //transactionDTO.setTransactionType(TransactionType.Deposit.name());
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setStatus(Status.PENDING.code());

        transactionCrudService.save(transactionDTO);
    }

    @Override
    void approve(TransactionDTO transaction) {

        AccountDTO account = basicAccountCrudService.findByNumber(transaction.getAccountNumber());

        if(!transaction.getStatus().equals(Status.PENDING.code()))
            throw new IllegalOperationException("Cannot approve this transaction");

        //TODO: exchange amount in given currency
        double amount = CurrencyExchanger.exchange(transaction.getCurrency(), account.getCurrency(), transaction.getAmount());
        accountOperationService.credit(account, amount);

        //TODO: update transaction info and save it
        transaction.setStatus(Status.APPROVED.code());
        transaction.setUpdatedBy("Administrator");
        transaction.setUpdatedAt(LocalDateTime.now());

        transactionCrudService.save(transaction);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }
}
