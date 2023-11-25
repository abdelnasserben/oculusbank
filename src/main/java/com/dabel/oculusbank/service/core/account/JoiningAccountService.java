package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.constant.AccountPartnership;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.dto.AccountDTO;
import org.springframework.stereotype.Service;

@Service
public class JoiningAccountService implements AccountPartnershipService {

    @Override
    public void add(AccountDTO accountDTO, AccountPartnership partnership, String identityNumber) {

    }

    @Override
    public void remove(AccountDTO accountDTO, String identityNumber) {

    }

    @Override
    public AccountProfile getType() {
        return AccountProfile.JOINT;
    }
}
