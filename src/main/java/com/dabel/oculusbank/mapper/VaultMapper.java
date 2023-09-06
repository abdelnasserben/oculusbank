package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.VaultDTO;
import com.dabel.oculusbank.model.Vault;
import com.dabel.oculusbank.model.VaultView;
import org.modelmapper.ModelMapper;

public class VaultMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Vault toEntity(VaultDTO vaultDTO) {
        return mapper.map(vaultDTO, Vault.class);
    }

    public static VaultDTO toDTO(VaultView vaultView) {
        return mapper.map(vaultView, VaultDTO.class);
    }
}
