package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.dto.TrunkDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrunkService extends ReadAccountService<TrunkDTO> {

    TrunkDTO save(int accountId, int customerId, String membership);

    TrunkDTO findByCustomerId(int customerId);

    List<TrunkDTO> findAllByCustomerId(int customerId);

    TrunkDTO findByNumberAndCustomerIdentity(String accountNumber, String customerIdentity);
}
