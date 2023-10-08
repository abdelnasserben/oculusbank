package com.dabel.oculusbank.service.support.account;

import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.mapper.AccountMapper;
import com.dabel.oculusbank.model.Account;
import com.dabel.oculusbank.repository.AccountRepository;
import com.dabel.oculusbank.service.core.account.AccountCruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleAccountService implements AccountCruService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountDTO save(AccountDTO accountDTO) {

        Account account = accountRepository.save(AccountMapper.toEntity(accountDTO));
        return AccountMapper.toDTO(account);
    }

    @Override
    public AccountDTO findByNumber(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);
        return AccountMapper.toDTO(account);
    }
}
