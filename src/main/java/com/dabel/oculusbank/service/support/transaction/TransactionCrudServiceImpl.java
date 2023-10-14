package com.dabel.oculusbank.service.support.transaction;

import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import com.dabel.oculusbank.mapper.TransactionMapper;
import com.dabel.oculusbank.model.Transaction;
import com.dabel.oculusbank.model.TransactionView;
import com.dabel.oculusbank.repository.TransactionRepository;
import com.dabel.oculusbank.repository.TransactionViewRepository;
import com.dabel.oculusbank.service.core.transaction.TransactionCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionCrudServiceImpl implements TransactionCrudService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionViewRepository transactionViewRepository;


    @Override
    public TransactionDTO save(TransactionDTO transactionDTO) {

        Transaction transaction = transactionRepository.save(TransactionMapper.toEntity(transactionDTO));
        return TransactionMapper.toDTO(transaction);
    }

    @Override
    public List<TransactionDTO> findAll() {

        return transactionViewRepository.findAll().stream()
                .map(TransactionMapper::viewToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO findById(int transactionId) {

        TransactionView transactionView = transactionViewRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        return TransactionMapper.viewToDTO(transactionView);
    }

    @Override
    public List<TransactionDTO> findAllByAccountId(int accountId) {

        return transactionViewRepository.findAllByAccountId(accountId).stream()
                .map(TransactionMapper::viewToDTO)
                .collect(Collectors.toList());
    }
}
