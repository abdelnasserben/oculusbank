package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.dto.AccountDTO;

public interface ReadAccountService<T extends AccountDTO> {
    T findByNumber(String accountNumber);
}
