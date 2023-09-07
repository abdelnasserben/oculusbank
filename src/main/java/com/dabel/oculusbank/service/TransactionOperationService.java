package com.dabel.oculusbank.service;

import com.dabel.oculusbank.app.CurrencyExchanger;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.constant.TransactionType;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionOperationService implements OperationAcknowledgment<TransactionDTO> {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountOperationService accountOperationService;

    public TransactionDTO deposit(String accountNumber, double amount, String currency, String sourceType, String sourceValue, String reason) {

        AccountDTO account = accountService.findByNumber(accountNumber);

        TransactionDTO transaction = TransactionDTO.builder()
                .transactionType(TransactionType.Deposit.name())
                .accountId(account.getAccountId())
                .currency(currency)
                .amount(amount)
                .sourceType(sourceType)
                .sourceValue(sourceValue)
                .reason(reason)
                .status(Status.Pending.code())
                .build();

        return transactionService.save(transaction);
    }

    public TransactionDTO withdraw(String accountNumber, double amount, String sourceType, String sourceValue, String reason) throws AccountNotFoundException, BalanceInsufficientException, IllegalTransactionException {

        AccountDTO account = accountService.findByNumber(accountNumber);
        TransactionDTO transaction = TransactionDTO.builder()
                .transactionType(TransactionType.Withdraw.name())
                .accountId(account.getAccountId())
                .currency(Currency.KMF.name())
                .amount(amount)
                .sourceType(sourceType)
                .sourceValue(sourceValue)
                .reason(reason)
                .build();

        if(account.getBalance() < amount) {

            transaction.setStatus(Status.Failed.code());
            transaction.setFailureReason("Insufficient balance");
            transactionService.save(transaction);

            throw new BalanceInsufficientException();
        }

        transaction.setStatus(Status.Pending.code());
        return transactionService.save(transaction);
    }

    @Override
    public TransactionDTO approve(int operationId) {

        TransactionDTO transaction = transactionService.findById(operationId);
        AccountDTO account = accountService.findByNumber(transaction.getAccountNumber());

        if(transaction.getTransactionType().equals(TransactionType.Deposit.name())) {
            //TODO: exchange amount in given currency
            double amount = CurrencyExchanger.exchange(transaction.getCurrency(), account.getCurrency(), transaction.getAmount());
            accountOperationService.credit(account, amount);
        }
        else
            accountOperationService.debit(account, transaction.getAmount());

        //TODO: update transaction info and save it
        transaction.setStatus(Status.Approved.code());
        transaction.setUpdatedBy("Administrator");
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionService.save(transaction);
    }

    @Override
    public TransactionDTO reject(int operationId, String remarks) {

        TransactionDTO transaction = transactionService.findById(operationId);
        transaction.setStatus(Status.Rejected.code());
        transaction.setFailureReason(remarks);
        transaction.setUpdatedBy("Administrator");
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionService.save(transaction);
    }
}
