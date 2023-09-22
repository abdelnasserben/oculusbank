package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.AccountChecker;
import com.dabel.oculusbank.app.CurrencyExchanger;
import com.dabel.oculusbank.app.Fee;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountOperationService;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.FeeService;
import com.dabel.oculusbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DelegateTransactionService implements OperationAcknowledgment<TransactionDTO> {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountOperationService accountOperationService;
    @Autowired
    FeeService feeService;

    public TransactionDTO deposit(TransactionDTO transactionDTO) {

        AccountDTO account = accountService.findByNumber(transactionDTO.getAccountNumber());

        if(!AccountChecker.isActive(account))
            throw new IllegalOperationException("Account must be active");

        transactionDTO.setTransactionType(TransactionType.Deposit.name());
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setStatus(Status.Pending.code());

        return transactionService.save(transactionDTO);
    }

    public TransactionDTO withdraw(TransactionDTO transactionDTO) {

        AccountDTO account = accountService.findByNumber(transactionDTO.getAccountNumber());

        if(!AccountChecker.isActive(account))
            throw new IllegalOperationException("Account must be active");

        transactionDTO.setTransactionType(TransactionType.Withdraw.name());
        transactionDTO.setAccountId(account.getAccountId());
        transactionDTO.setCurrency(Currency.KMF.name());

        if((transactionDTO.getSourceType().equals(SourceType.Visa.name()) && account.getBalance() < transactionDTO.getAmount() + Fees.WITHDRAW_ON_ATM)
            || (!transactionDTO.getSourceType().equals(SourceType.Visa.name()) && account.getBalance() < transactionDTO.getAmount() + Fees.WITHDRAW_IN_AGENCY)) {

            transactionDTO.setStatus(Status.Failed.code());
            transactionDTO.setFailureReason("Insufficient balance");
            transactionService.save(transactionDTO);

            throw new BalanceInsufficientException();
        }

        transactionDTO.setStatus(Status.Pending.code());
        return transactionService.save(transactionDTO);
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
        else{
            accountOperationService.debit(account, transaction.getAmount());

            if(transaction.getSourceType().equals(SourceType.Visa.name()))
                feeService.apply(account, new Fee(Fees.WITHDRAW_ON_ATM, "Withdraw"), transaction.getSourceValue());
            else
                feeService.apply(account, new Fee(Fees.WITHDRAW_IN_AGENCY, "Withdraw"), transaction.getSourceValue());
        }

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

    public List<TransactionDTO> findAll() {
        return transactionService.findAll();
    }

    public TransactionDTO findById(int transactionId) {
        return transactionService.findById(transactionId);
    }

}
