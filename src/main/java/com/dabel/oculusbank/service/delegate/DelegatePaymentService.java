package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.AccountChecker;
import com.dabel.oculusbank.app.CurrencyExchanger;
import com.dabel.oculusbank.app.Fee;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Fees;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountOperationService;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.FeeService;
import com.dabel.oculusbank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DelegatePaymentService implements OperationAcknowledgment<PaymentDTO> {

    @Autowired
    PaymentService paymentService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountOperationService accountOperationService;
    @Autowired
    FeeService feeService;

    public PaymentDTO pay(PaymentDTO paymentDTO) {

        AccountDTO debitAccount = accountService.findByNumber(paymentDTO.getDebitAccountNumber());
        AccountDTO creditAccount = accountService.findByNumber(paymentDTO.getCreditAccountNumber());

        if(!AccountChecker.isActive(debitAccount) || !AccountChecker.isActive(creditAccount)) {

            //TODO: save failed payment
            paymentDTO.setDebitAccountId(debitAccount.getAccountId());
            paymentDTO.setCreditAccountId(creditAccount.getAccountId());
            paymentDTO.setCurrency(debitAccount.getCurrency());
            paymentDTO.setStatus(Status.Failed.code());
            paymentDTO.setFailureReason("Debit and credit account must be active");

            paymentService.save(paymentDTO);
            throw new IllegalOperationException("Debit and credit account must be active");
        }

        if(debitAccount.getBalance() < paymentDTO.getAmount() + Fees.PAYMENT) {

            //TODO: save failed payment
            paymentDTO.setDebitAccountId(debitAccount.getAccountId());
            paymentDTO.setCreditAccountId(creditAccount.getAccountId());
            paymentDTO.setCurrency(debitAccount.getCurrency());
            paymentDTO.setStatus(Status.Failed.code());
            paymentDTO.setFailureReason("Account balance is insufficient");

            paymentService.save(paymentDTO);
            throw new BalanceInsufficientException();
        }

        //TODO: update payment info before saving
        paymentDTO.setDebitAccountId(debitAccount.getAccountId());
        paymentDTO.setCreditAccountId(creditAccount.getAccountId());
        paymentDTO.setCurrency(debitAccount.getCurrency());
        paymentDTO.setStatus(Status.Pending.code());

        return paymentService.save(paymentDTO);
    }

    @Override
    public PaymentDTO approve(int operationId) {

        PaymentDTO payment = paymentService.findById(operationId);
        AccountDTO debitAccount = accountService.findByNumber(payment.getDebitAccountNumber());
        AccountDTO creditAccount = accountService.findByNumber(payment.getCreditAccountNumber());

        //TODO: convert amount and fees when credit and debit account are different currencies
        double amount = CurrencyExchanger.exchange(debitAccount.getCurrency(), creditAccount.getCurrency(), payment.getAmount());
        double feesAmount = CurrencyExchanger.exchange(debitAccount.getCurrency(), Currency.KMF.name(), Fees.PAYMENT);

        accountOperationService.debit(debitAccount, payment.getAmount());
        accountOperationService.credit(creditAccount, amount);

        //TODO: apply fees on debit account
        feeService.apply(debitAccount, new Fee(feesAmount, "Payment"));

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
