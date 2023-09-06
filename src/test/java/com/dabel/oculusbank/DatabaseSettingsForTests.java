package com.dabel.oculusbank;

import com.dabel.oculusbank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSettingsForTests {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    ExchangeRepository exchangeRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TrunkRepository trunkRepository;
    @Autowired
    VaultRepository vaultRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BranchRepository branchRepository;

    public void truncate() {
        paymentRepository.deleteAll();
        exchangeRepository.deleteAll();
        transactionRepository.deleteAll();
        trunkRepository.deleteAll();
        vaultRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        branchRepository.deleteAll();
    }
}
