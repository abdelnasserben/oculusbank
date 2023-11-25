package com.dabel.oculusbank.service.core.payment;

import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.app.util.AccountChecker;
import com.dabel.oculusbank.constant.Fees;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.core.account.AccountOperationService;
import com.dabel.oculusbank.service.core.account.BasicAccountCrudService;
import com.dabel.oculusbank.service.core.account.TrunkService;
import com.dabel.oculusbank.service.core.fee.FeeTrunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentFacadeService {

    @Autowired
    PaymentCrudService paymentCrudService;
    @Autowired
    BasicAccountCrudService accountService;
    @Autowired
    TrunkService trunkService;
    @Autowired
    AccountOperationService accountOperationService;
    @Autowired
    FeeTrunkService feeTrunkService;

    public PaymentDTO pay(PaymentDTO paymentDTO) {

        AccountDTO debitAccount = accountService.findByNumber(paymentDTO.getDebitAccountNumber());
        AccountDTO creditAccount = accountService.findByNumber(paymentDTO.getCreditAccountNumber());

        if(debitAccount.getAccountNumber().equals(creditAccount.getAccountNumber()))
            throw new IllegalOperationException("Self-payment is not possible");

        if(!debitAccount.getCurrency().equals(creditAccount.getCurrency()))
            throw new IllegalOperationException("Accounts must have the same currency");

        if(!AccountChecker.isActive(debitAccount) || !AccountChecker.isActive(creditAccount)) {

            //TODO: save failed payment
            paymentDTO.setDebitAccountId(debitAccount.getAccountId());
            paymentDTO.setCreditAccountId(creditAccount.getAccountId());
            paymentDTO.setCurrency(debitAccount.getCurrency());
            paymentDTO.setStatus(Status.FAILED.code());
            paymentDTO.setFailureReason("Debit and credit account must be active");

            paymentCrudService.save(paymentDTO);
            throw new IllegalOperationException("Debit and credit account must be active");
        }

        if(debitAccount.getBalance() < paymentDTO.getAmount() + Fees.PAYMENT) {

            //TODO: save failed payment
            paymentDTO.setDebitAccountId(debitAccount.getAccountId());
            paymentDTO.setCreditAccountId(creditAccount.getAccountId());
            paymentDTO.setCurrency(debitAccount.getCurrency());
            paymentDTO.setStatus(Status.FAILED.code());
            paymentDTO.setFailureReason("Account balance is insufficient");

            paymentCrudService.save(paymentDTO);
            throw new BalanceInsufficientException();
        }

        //TODO: update payment info before saving
        paymentDTO.setDebitAccountId(debitAccount.getAccountId());
        paymentDTO.setCreditAccountId(creditAccount.getAccountId());
        paymentDTO.setCurrency(debitAccount.getCurrency());
        paymentDTO.setStatus(Status.PENDING.code());

        return paymentCrudService.save(paymentDTO);
    }

    public PaymentDTO approve(int operationId) {

        PaymentDTO payment = paymentCrudService.findById(operationId);
        AccountDTO debitAccount = accountService.findByNumber(payment.getDebitAccountNumber());
        AccountDTO creditAccount = accountService.findByNumber(payment.getCreditAccountNumber());

        accountOperationService.debit(debitAccount, payment.getAmount());
        accountOperationService.credit(creditAccount, payment.getAmount());

        //TODO: apply fees on debit account
        feeTrunkService.apply(debitAccount, new Fee(Fees.PAYMENT, "Payment"));

        payment.setStatus(Status.APPROVED.code());
        payment.setUpdatedBy("Administrator");
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentCrudService.save(payment);
    }

    public PaymentDTO reject(int operationId, String remarks) {

        PaymentDTO payment = paymentCrudService.findById(operationId);

        payment.setStatus(Status.REJECTED.code());
        payment.setFailureReason(remarks);
        payment.setUpdatedBy("Administrator");
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentCrudService.save(payment);
    }

    public List<PaymentDTO> findAll() {
        return paymentCrudService.findAll();
    }

    public PaymentDTO findById(int paymentId) {
        return paymentCrudService.findById(paymentId);
    }

    public List<PaymentDTO> findAllByCustomerId(int customerId) {

        List<PaymentDTO> payments = new ArrayList<>();
        List<TrunkDTO> customerAccounts = trunkService.findAllByCustomerId(customerId);

        customerAccounts.stream()
                .map(trunkDTO -> paymentCrudService.findAllByAccountId(trunkDTO.getAccountId()))
                .forEach(payments::addAll);

        return payments;
    }
}
