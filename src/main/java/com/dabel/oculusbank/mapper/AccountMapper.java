package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.model.Account;
import org.modelmapper.ModelMapper;

public class AccountMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Account toEntity(AccountDTO accountDTO) {
        return mapper.map(accountDTO, Account.class);
    }

    public static AccountDTO toDTO(Account account) {
        return mapper.map(account, AccountDTO.class);
    }

}
