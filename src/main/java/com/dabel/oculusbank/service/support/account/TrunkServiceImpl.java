package com.dabel.oculusbank.service.support.account;

import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.mapper.TrunkMapper;
import com.dabel.oculusbank.model.Trunk;
import com.dabel.oculusbank.model.TrunkView;
import com.dabel.oculusbank.repository.TrunkRepository;
import com.dabel.oculusbank.repository.TrunkViewRepository;
import com.dabel.oculusbank.service.core.account.TrunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrunkServiceImpl implements TrunkService {

    @Autowired
    TrunkRepository trunkRepository;
    @Autowired
    TrunkViewRepository trunkViewRepository;

    @Override
    public TrunkDTO save(int accountId, int customerId, String membership) {

        TrunkDTO trunkDTO = TrunkDTO.builder()
                .accountId(accountId)
                .customerId(customerId)
                .membership(membership)
                .build();
        Trunk savedTrunk = trunkRepository.save(TrunkMapper.toEntity(trunkDTO));
        trunkDTO.setTrunkId(savedTrunk.getTrunkId());

        return trunkDTO;
    }

    @Override
    public TrunkDTO findByCustomerId(int customerId) {

        TrunkView trunkView = trunkViewRepository.findByCustomerId(customerId)
                .orElseThrow(AccountNotFoundException::new);

        return TrunkMapper.toDTO(trunkView);
    }

    @Override
    public List<TrunkDTO> findAllByCustomerId(int customerId) {

        List<TrunkView> trunks = trunkViewRepository.findAllByCustomerId(customerId);
        return trunks.stream()
                .map(TrunkMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TrunkDTO findByNumberAndCustomerIdentity(String accountNumber, String customerIdentity) {

        TrunkView trunkView = trunkViewRepository.findByAccountNumberAndCustomerIdentityNumber(accountNumber, customerIdentity)
                .orElseThrow(AccountNotFoundException::new);

        return TrunkMapper.toDTO(trunkView);
    }

    @Override
    public TrunkDTO findByNumber(String accountNumber) {

        TrunkView trunkView = trunkViewRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);

        return TrunkMapper.toDTO(trunkView);
    }
}
