package com.dabel.oculusbank.mapper;

import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.model.Customer;
import org.modelmapper.ModelMapper;

public class CustomerMapper {
    private static final ModelMapper mapper = new ModelMapper();

    public static Customer toEntity(CustomerDTO customerDTO) {
        return mapper.map(customerDTO, Customer.class);
    }

    public static CustomerDTO toDTO(Customer customer) {
        return mapper.map(customer, CustomerDTO.class);
    }
}
