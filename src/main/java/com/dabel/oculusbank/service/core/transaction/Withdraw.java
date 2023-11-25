package com.dabel.oculusbank.service.core.transaction;

import com.dabel.oculusbank.app.util.AccountChecker;
import com.dabel.oculusbank.app.util.AmountFormatter;
import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.core.fee.FeeTrunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Withdraw extends Transaction {

    @Autowired
    FeeTrunkService feeTrunkService;

    @Override
    public void init(TransactionDTO transactionDTO) {

        AccountDTO account = basicAccountCrudService.findByNumber(transactionDTO.getAccountNumber());

        if(!AccountChecker.isActive(account))
            throw new IllegalOperationException("Account must be active");

        //transactionDTO.setTransactionType(TransactionType.Withdraw.name());
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setCurrency(Currency.KMF.name());

        if((!transactionDTO.getSourceType().equals(SourceType.ONLINE.name()) && account.getBalance() < transactionDTO.getAmount() + Fees.WITHDRAW_ON_ATM)
                || (transactionDTO.getSourceType().equals(SourceType.ONLINE.name()) && account.getBalance() < transactionDTO.getAmount() + Fees.WITHDRAW_IN_AGENCY)) {

            transactionDTO.setStatus(Status.FAILED.code());
            transactionDTO.setFailureReason("Insufficient balance");
            transactionCrudService.save(transactionDTO);

            throw new BalanceInsufficientException();
        }

        transactionDTO.setStatus(Status.PENDING.code());
        transactionCrudService.save(transactionDTO);
    }

    @Override
    void approve(TransactionDTO transaction) {

        AccountDTO account = basicAccountCrudService.findByNumber(transaction.getAccountNumber());

        if(!transaction.getStatus().equals(Status.PENDING.code()))
            throw new IllegalOperationException("Cannot approve this transaction");

        accountOperationService.debit(account, AmountFormatter.format(transaction.getAmount()));

        if(!transaction.getSourceType().equals(SourceType.ONLINE.name()))
            feeTrunkService.apply(account, new Fee(Fees.WITHDRAW_ON_ATM, "Withdraw"));
        else
            feeTrunkService.apply(account, new Fee(Fees.WITHDRAW_IN_AGENCY, "Withdraw"));

        //TODO: update transaction info and save it
        transaction.setStatus(Status.APPROVED.code());
        transaction.setUpdatedBy("Administrator");
        transaction.setUpdatedAt(LocalDateTime.now());

        transactionCrudService.save(transaction);
    }


    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAW;
    }
}
