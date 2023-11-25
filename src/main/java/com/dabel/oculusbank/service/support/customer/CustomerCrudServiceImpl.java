package com.dabel.oculusbank.service.support.customer;

import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.exception.CustomerNotFoundException;
import com.dabel.oculusbank.mapper.CustomerMapper;
import com.dabel.oculusbank.model.Customer;
import com.dabel.oculusbank.repository.CustomerRepository;
import com.dabel.oculusbank.service.core.customer.CustomerCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCrudServiceImpl implements CustomerCrudService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {

        Customer customer = customerRepository.save(CustomerMapper.toEntity(customerDTO));
        return CustomerMapper.toDTO(customer);
    }

    @Override
    public List<CustomerDTO> findAll() {

        return customerRepository.findAll().stream()
                .map(CustomerMapper::toDTO)
                .toList();
    }

    @Override
    public CustomerDTO findByIdentityNumber(String identityNumber) {
        Customer customer = customerRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(CustomerNotFoundException::new);

        return CustomerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO findById(int customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);

        return CustomerMapper.toDTO(customer);
    }
}
