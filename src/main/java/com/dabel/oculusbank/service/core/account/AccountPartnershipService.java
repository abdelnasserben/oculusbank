package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.constant.AccountPartnership;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.dto.AccountDTO;

public interface AccountPartnershipService {

    void add(AccountDTO accountDTO, AccountPartnership partnership, String identityNumber);

    void remove(AccountDTO accountDTO, String identityNumber);

    AccountProfile getType();
}
