package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.model.Trunk;
import com.dabel.oculusbank.model.TrunkView;
import org.modelmapper.ModelMapper;

public class TrunkMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Trunk toEntity(TrunkDTO trunkDTO) {
        return mapper.map(trunkDTO, Trunk.class);
    }

    public static TrunkDTO toDTO(TrunkView trunkView) {
        return mapper.map(trunkView, TrunkDTO.class);
    }
}
