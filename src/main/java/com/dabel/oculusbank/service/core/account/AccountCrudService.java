package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.service.support.account.AccountReadService;
import org.springframework.stereotype.Service;

@Service
public interface AccountCrudService extends AccountReadService<AccountDTO> {

    AccountDTO save(AccountDTO accountDTO);
}
