package com.dabel.oculusbank.service.support.account;

import com.dabel.oculusbank.dto.AccountDTO;

public interface AccountReadService<T extends AccountDTO> {
    T findByNumber(String accountNumber);
}
