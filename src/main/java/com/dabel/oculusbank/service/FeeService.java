package com.dabel.oculusbank.service;

import com.dabel.oculusbank.app.Fee;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.SourceType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.constant.TransactionType;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeeService {
    @Autowired
    AccountOperationService accountOperationService;
    @Autowired
    TransactionService transactionService;

    public void apply(AccountDTO account, Fee fee, String source) {

        transactionService.save(
                TransactionDTO.builder()
                .accountId(account.getAccountId())
                .transactionType(TransactionType.Fee.name())
                .currency(Currency.KMF.name())
                .amount(fee.value())
                .sourceType(SourceType.Online.name())
                .sourceValue(source)
                .reason("ATS@" + fee.description() + " fee")
                .status(Status.Approved.code())
                .build());
        accountOperationService.debit(account, fee.value());
    }
}
