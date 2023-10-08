package com.dabel.oculusbank.service.core.cheque;

import com.dabel.oculusbank.dto.ChequeDTO;
import org.springframework.stereotype.Service;

@Service
public interface ChequeCruService {

    ChequeDTO save(ChequeDTO chequeDTO);

    ChequeDTO findByNumber(String chequeNumber);

    boolean exists(String chequeNumber);
}
