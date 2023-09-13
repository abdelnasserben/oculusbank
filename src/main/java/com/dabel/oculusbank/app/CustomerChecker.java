package com.dabel.oculusbank.app;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.CustomerDTO;

public class CustomerChecker {

    public static boolean isActive(CustomerDTO customerDTO) {
        return !customerDTO.getStatus().equals(Status.Active.name()) && !customerDTO.getStatus().equals(Status.Active.code());
    }
}
