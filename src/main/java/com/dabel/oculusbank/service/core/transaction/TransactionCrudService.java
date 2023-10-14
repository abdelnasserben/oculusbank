package com.dabel.oculusbank.service.core.transaction;

import com.dabel.oculusbank.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionCrudService {

    TransactionDTO save(TransactionDTO transactionDTO);

    List<TransactionDTO> findAll();

    TransactionDTO findById(int transactionId);

    List<TransactionDTO> findAllByAccountId(int accountId);
}
