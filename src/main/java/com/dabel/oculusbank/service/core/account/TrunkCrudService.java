package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.service.support.account.AccountReadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrunkCrudService extends AccountReadService<TrunkDTO> {

    TrunkDTO save(int accountId, int customerId, String membership);

    TrunkDTO findByCustomerId(int customerId);

    List<TrunkDTO> findAllByCustomerId(int customerId);

    TrunkDTO findByNumberAndCustomerIdentity(String accountNumber, String customerIdentity);
}
