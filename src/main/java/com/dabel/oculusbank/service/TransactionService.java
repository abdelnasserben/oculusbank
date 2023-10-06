package com.dabel.oculusbank.service;

import com.dabel.oculusbank.app.util.card.CardNumberFormatter;
import com.dabel.oculusbank.constant.SourceType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import com.dabel.oculusbank.mapper.TransactionMapper;
import com.dabel.oculusbank.model.Transaction;
import com.dabel.oculusbank.model.TransactionView;
import com.dabel.oculusbank.repository.TransactionRepository;
import com.dabel.oculusbank.repository.TransactionViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionViewRepository transactionViewRepository;

    public TransactionDTO save(TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.save(TransactionMapper.toEntity(transactionDTO));
        return TransactionMapper.toDTO(transaction);
    }

    public List<TransactionDTO> findAll() {
        return transactionViewRepository.findAll().stream()
                .map(TransactionService::formatStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO findById(int transactionId) {
        TransactionView transactionView = transactionViewRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        return formatStatusToNameAndGetDTO(transactionView);
    }

    public List<TransactionDTO> findAllByAccountId(int accountId) {
        return transactionViewRepository.findAllByAccountId(accountId).stream()
                .map(TransactionService::formatStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    private static TransactionDTO formatStatusToNameAndGetDTO(TransactionView transactionView) {
        transactionView.setStatus(Status.nameOf(transactionView.getStatus()));
        if(!transactionView.getSourceType().equals(SourceType.Online.name()))
            transactionView.setSourceValue(CardNumberFormatter.hide(transactionView.getSourceValue()));

        return TransactionMapper.viewToDTO(transactionView);
    }
}
