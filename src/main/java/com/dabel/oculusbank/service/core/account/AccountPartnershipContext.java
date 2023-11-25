package com.dabel.oculusbank.service.core.account;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountPartnershipContext {

    private final Map<String, AccountPartnershipService> partnershipMap = new HashMap<>();

    public AccountPartnershipContext(Set<AccountPartnershipService> accountPartnershipServices) {
        accountPartnershipServices.forEach(p -> partnershipMap.put(p.getType().name(), p));
    }

    public AccountPartnershipService setContext(String accountType) {
        return Optional.ofNullable(partnershipMap.get(accountType)).orElseThrow();
    }
}
