package com.dabel.oculusbank.app;

import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;

public final class AccountChecker {

    public static boolean isActive(AccountDTO accountDTO) {
        return accountDTO.getStatus().equals(Status.Active.name()) || accountDTO.getStatus().equals(Status.Active.code());
    }

    public static boolean isJoint(AccountDTO accountDTO) {
        return accountDTO.getAccountProfile().equals(AccountProfile.Joint.name());
    }

    public static boolean isAssociative(AccountDTO accountDTO) {
        return accountDTO.getAccountProfile().equals(AccountProfile.Associative.name());
    }
}
