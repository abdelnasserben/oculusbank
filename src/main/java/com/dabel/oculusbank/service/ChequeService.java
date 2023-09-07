package com.dabel.oculusbank.service;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ChequeDTO;
import com.dabel.oculusbank.exception.ChequeNotFountException;
import com.dabel.oculusbank.mapper.ChequeMapper;
import com.dabel.oculusbank.model.Cheque;
import com.dabel.oculusbank.model.ChequeView;
import com.dabel.oculusbank.repository.ChequeRepository;
import com.dabel.oculusbank.repository.ChequeViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChequeService {

    @Autowired
    ChequeRepository chequeRepository;
    @Autowired
    ChequeViewRepository chequeViewRepository;

    public ChequeDTO save(ChequeDTO chequeDTO) {

        chequeDTO.setStatus(Status.Active.code());
        Cheque cheque = chequeRepository.save(ChequeMapper.toEntity(chequeDTO));
        return ChequeMapper.toDTO(cheque);
    }

    public ChequeDTO findByNumber(String chequeNumber) {
        ChequeView chequeView = chequeViewRepository.findByChequeNumber(chequeNumber).
                orElseThrow(ChequeNotFountException::new);
        chequeView.setStatus(Status.nameOf(chequeView.getStatus()));
        return ChequeMapper.viewToDTO(chequeView);
    }

    public boolean exists(String chequeNumber) {
        return chequeViewRepository.findByChequeNumber(chequeNumber).isPresent();
    }
}
