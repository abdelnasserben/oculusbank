package com.dabel.oculusbank.service.core.transaction;

import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.service.core.account.TrunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionFacadeService {

    @Autowired
    TransactionCrudService transactionCrudService;
    @Autowired
    TransactionContext transactionContext;
    @Autowired
    TrunkService trunkService;

    public void init(TransactionDTO transactionDTO) {

        transactionContext.setContext(transactionDTO.getTransactionType()).init(transactionDTO);
    }

    public void approve(int transactionId) {

        TransactionDTO transactionDTO = transactionCrudService.findById(transactionId);
        transactionContext.setContext(transactionDTO.getTransactionType()).approve(transactionDTO);

    }

    public void reject(int transactionId, String remarks) {
        TransactionDTO transactionDTO = transactionCrudService.findById(transactionId);
        transactionContext.setContext(transactionDTO.getTransactionType()).reject(transactionDTO, remarks);
    }

    public List<TransactionDTO> findAll() {
        return transactionCrudService.findAll();
    }

    public TransactionDTO findById(int transactionId) {
        return transactionCrudService.findById(transactionId);
    }

    public List<TransactionDTO> findAllByCustomerId(int customerId) {

        List<TransactionDTO> transactions = new ArrayList<>();

        trunkService.findAllByCustomerId(customerId).stream()
                .map(trunkDTO -> transactionCrudService.findAllByAccountId(trunkDTO.getAccountId()))
                .forEach(transactions::addAll);

        return transactions;
    }

}
