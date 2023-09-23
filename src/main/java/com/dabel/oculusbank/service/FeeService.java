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

    public void apply(AccountDTO account, Fee fee) {

        transactionService.save(
                TransactionDTO.builder()
                .accountId(account.getAccountId())
                .transactionType(TransactionType.Fee.name())
                .currency(Currency.KMF.name())
                .amount(fee.value())
                .sourceType(SourceType.Online.name())
                .sourceValue("System")
                .reason("ATS@" + fee.description() + " fee")
                .customerFullName("OS Operator")
                .customerIdentity("123456789")
                .status(Status.Approved.code())
                .initiatedBy("OS Operator")
                .build());
        accountOperationService.debit(account, fee.value());
    }
}
