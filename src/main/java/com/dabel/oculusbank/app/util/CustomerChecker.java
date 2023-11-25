package com.dabel.oculusbank.app.util;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.CustomerDTO;

public class CustomerChecker {

    public static boolean isActive(CustomerDTO customerDTO) {
        return customerDTO.getStatus().equals(Status.ACTIVE.name()) || customerDTO.getStatus().equals(Status.ACTIVE.code());
    }
}
