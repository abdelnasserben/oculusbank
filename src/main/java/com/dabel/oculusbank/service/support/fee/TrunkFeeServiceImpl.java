package com.dabel.oculusbank.service.support.fee;

import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.service.core.fee.FeeTrunkService;
import org.springframework.stereotype.Service;

@Service
public class TrunkFeeServiceImpl implements FeeTrunkService {

    @Override
    public void apply(AccountDTO account, Fee fee) {

    }
}
