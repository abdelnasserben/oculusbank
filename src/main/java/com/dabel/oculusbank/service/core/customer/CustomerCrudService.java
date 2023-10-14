package com.dabel.oculusbank.service.core.customer;

import com.dabel.oculusbank.dto.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerCrudService {

    CustomerDTO save(CustomerDTO customerDTO);

    List<CustomerDTO> findAll();

    CustomerDTO findByIdentityNumber(String identityNumber);

    CustomerDTO findById(int customerId);
}
