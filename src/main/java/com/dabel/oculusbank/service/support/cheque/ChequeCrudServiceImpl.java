package com.dabel.oculusbank.service.support.cheque;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ChequeDTO;
import com.dabel.oculusbank.exception.ChequeNotFountException;
import com.dabel.oculusbank.mapper.ChequeMapper;
import com.dabel.oculusbank.model.Cheque;
import com.dabel.oculusbank.model.ChequeView;
import com.dabel.oculusbank.repository.ChequeRepository;
import com.dabel.oculusbank.repository.ChequeViewRepository;
import com.dabel.oculusbank.service.core.cheque.ChequeCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChequeCrudServiceImpl implements ChequeCrudService {

    @Autowired
    ChequeRepository chequeRepository;
    @Autowired
    ChequeViewRepository chequeViewRepository;

    @Override
    public ChequeDTO save(ChequeDTO chequeDTO) {

        chequeDTO.setStatus(Status.ACTIVE.code());
        Cheque cheque = chequeRepository.save(ChequeMapper.toEntity(chequeDTO));
        return ChequeMapper.toDTO(cheque);
    }

    @Override
    public ChequeDTO findByNumber(String chequeNumber) {

        ChequeView chequeView = chequeViewRepository.findByChequeNumber(chequeNumber).
                orElseThrow(ChequeNotFountException::new);
        chequeView.setStatus(Status.nameOf(chequeView.getStatus()));
        return ChequeMapper.viewToDTO(chequeView);
    }

    @Override
    public boolean exists(String chequeNumber) {

        return chequeViewRepository.findByChequeNumber(chequeNumber).isPresent();
    }
}
