package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.dto.AccountDTO;
import org.springframework.stereotype.Service;

@Service
public interface BasicAccountCrudService {

    AccountDTO save(AccountDTO accountDTO);
    AccountDTO findByNumber(String accountNumber);
}
