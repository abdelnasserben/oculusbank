package com.dabel.oculusbank.service.core.fee;

import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.dto.AccountDTO;
import org.springframework.stereotype.Service;

@Service
public interface FeeTrunkService {
    void apply(AccountDTO account, Fee fee);
}
