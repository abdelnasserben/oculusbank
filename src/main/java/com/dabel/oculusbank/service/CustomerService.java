package com.dabel.oculusbank.service;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.CustomerDTO;
import com.dabel.oculusbank.exception.CustomerNotFoundException;
import com.dabel.oculusbank.mapper.CustomerMapper;
import com.dabel.oculusbank.model.Customer;
import com.dabel.oculusbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = customerRepository.save(CustomerMapper.toEntity(customerDTO));
        return CustomerMapper.toDTO(customer);
    }

    public List<CustomerDTO> findAll() {

        return customerRepository.findAll().stream()
                .map(CustomerService::formatStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO findByIdentityNumber(String identityNumber) {
        Customer customer = customerRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(CustomerNotFoundException::new);

        return formatStatusToNameAndGetDTO(customer);
    }

    public CustomerDTO findById(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);

        return formatStatusToNameAndGetDTO(customer);
    }

    private static CustomerDTO formatStatusToNameAndGetDTO(Customer customer) {
        customer.setStatus(Status.nameOf(customer.getStatus()));
        return CustomerMapper.toDTO(customer);
    }
}
