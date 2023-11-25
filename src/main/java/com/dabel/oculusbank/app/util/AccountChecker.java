package com.dabel.oculusbank.app.util;

import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;

public final class AccountChecker {

    public static boolean isActive(AccountDTO accountDTO) {
        return accountDTO.getStatus().equals(Status.ACTIVE.name()) || accountDTO.getStatus().equals(Status.ACTIVE.code());
    }

    public static boolean isJoint(AccountDTO accountDTO) {
        return accountDTO.getAccountProfile().equals(AccountProfile.JOINT.name());
    }

    public static boolean isAssociative(AccountDTO accountDTO) {
        return accountDTO.getAccountProfile().equals(AccountProfile.ASSOCIATIVE.name());
    }
}
