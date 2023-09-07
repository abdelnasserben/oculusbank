package com.dabel.oculusbank.service;

import com.dabel.oculusbank.app.CurrencyExchanger;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentOperationService implements OperationAcknowledgment<PaymentDTO> {

    @Autowired
    PaymentService paymentService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountOperationService accountOperationService;

    public PaymentDTO pay(String debitAccountNumber, double amount, String creditAccountNumber, String reason) {

        AccountDTO debitAccount = accountService.findByNumber(debitAccountNumber);
        AccountDTO creditAccount = accountService.findByNumber(creditAccountNumber);

        if(debitAccount.getBalance() < amount) {
            PaymentDTO payment = PaymentDTO.builder()
                    .debitAccountId(debitAccount.getAccountId())
                    .creditAccountId(creditAccount.getAccountId())
                    .currency(debitAccount.getCurrency())
                    .amount(amount)
                    .reason(reason)
                    .status(Status.Failed.code())
                    .failureReason("Balance is insufficient")
                    .build();
            paymentService.save(payment);

            throw new BalanceInsufficientException();
        }

        PaymentDTO payment = PaymentDTO.builder()
                .debitAccountId(debitAccount.getAccountId())
                .creditAccountId(creditAccount.getAccountId())
                .currency(debitAccount.getCurrency())
                .amount(amount)
                .reason(reason)
                .status(Status.Pending.code())
                .build();

        return paymentService.save(payment);
    }

    @Override
    public PaymentDTO approve(int operationId) {

        PaymentDTO payment = paymentService.findById(operationId);
        AccountDTO debitAccount = accountService.findByNumber(payment.getDebitAccountNumber());
        AccountDTO creditAccount = accountService.findByNumber(payment.getCreditAccountNumber());

        //TODO: convert amount when credit and debit account currencies are different
        double amount = CurrencyExchanger.exchange(debitAccount.getCurrency(),creditAccount.getCurrency(), payment.getAmount());
        accountOperationService.debit(debitAccount, payment.getAmount());
        accountOperationService.credit(creditAccount, amount);

        payment.setStatus(Status.Approved.code());
        payment.setUpdatedBy("Administrator");
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentService.save(payment);
    }

    @Override
    public PaymentDTO reject(int operationId, String remarks) {

        PaymentDTO payment = paymentService.findById(operationId);

        payment.setStatus(Status.Rejected.code());
        payment.setFailureReason(remarks);
        payment.setUpdatedBy("Administrator");
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentService.save(payment);
    }
}
