package com.dabel.oculusbank.service.support.fee;

import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.SourceType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.constant.TransactionType;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.service.core.account.AccountOperationService;
import com.dabel.oculusbank.service.core.fee.FeeTrunkService;
import com.dabel.oculusbank.service.core.transaction.TransactionCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrunkFeeServiceImpl implements FeeTrunkService {
    @Autowired
    AccountOperationService accountOperationService;
    @Autowired
    TransactionCrudService transactionCrudService;

    @Override
    public void apply(AccountDTO account, Fee fee) {

        transactionCrudService.save(
                TransactionDTO.builder()
                        .accountId(account.getAccountId())
                        .transactionType(TransactionType.FEE.name())
                        .currency(Currency.KMF.name())
                        .amount(fee.value())
                        .sourceType(SourceType.ONLINE.name())
                        .sourceValue("System")
                        .reason("ATS@" + fee.description() + " fee")
                        .customerFullName("OS Operator")
                        .customerIdentity("123456789")
                        .status(Status.APPROVED.code())
                        .initiatedBy("OS Operator")
                        .build());
        accountOperationService.debit(account, fee.value());
    }
}
