package com.dabel.oculusbank.service.core.transaction;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.constant.TransactionType;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.core.account.AccountOperationService;
import com.dabel.oculusbank.service.core.account.BasicAccountCrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public abstract class Transaction {

    @Autowired
    protected TransactionCrudService transactionCrudService;
    @Autowired
    protected BasicAccountCrudService basicAccountCrudService;
    @Autowired
    protected AccountOperationService accountOperationService;
    abstract void init(TransactionDTO transactionDTO);
    abstract void approve(TransactionDTO transactionDTO);
    abstract TransactionType getType();

    public TransactionDTO reject(TransactionDTO transactionDTO, String remarks) {

        if(!transactionDTO.getStatus().equals(Status.PENDING.code()))
            throw new IllegalOperationException("Cannot reject this transactionDTO");

        transactionDTO.setStatus(Status.REJECTED.code());
        transactionDTO.setFailureReason(remarks);
        transactionDTO.setUpdatedBy("Administrator");
        transactionDTO.setUpdatedAt(LocalDateTime.now());

        return transactionCrudService.save(transactionDTO);
    }
}
