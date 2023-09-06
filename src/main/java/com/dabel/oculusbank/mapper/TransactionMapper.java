package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.model.Transaction;
import com.dabel.oculusbank.model.TransactionView;
import org.modelmapper.ModelMapper;

public class TransactionMapper {
    private static final ModelMapper mapper = new ModelMapper();

    public static Transaction toEntity(TransactionDTO transactionDTO) {
        return mapper.map(transactionDTO, Transaction.class);
    }

    public static TransactionDTO toDTO(Transaction transaction) {
        return mapper.map(transaction, TransactionDTO.class);
    }

    public static TransactionDTO viewToDTO(TransactionView transactionView) {
        return mapper.map(transactionView, TransactionDTO.class);
    }
}
