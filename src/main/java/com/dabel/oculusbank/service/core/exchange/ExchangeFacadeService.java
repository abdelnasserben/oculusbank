package com.dabel.oculusbank.service.core.exchange;

import com.dabel.oculusbank.app.util.CurrencyExchanger;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ExchangeFacadeService {

    @Autowired
    ExchangeCrudService exchangeCrudService;

    public ExchangeDTO exchange(ExchangeDTO exchangeDTO) {

        String purchaseCurrency = exchangeDTO.getPurchaseCurrency();
        String saleCurrency = exchangeDTO.getSaleCurrency();

        if(purchaseCurrency.equalsIgnoreCase(saleCurrency))
            throw new IllegalOperationException("Currencies must be different");

        //TODO: convert amount and set status of exchange before saving
        double saleAmount = CurrencyExchanger.exchange(purchaseCurrency, saleCurrency, exchangeDTO.getPurchaseAmount());
        exchangeDTO.setSaleAmount(saleAmount);
        exchangeDTO.setStatus(Status.PENDING.code());
        return exchangeCrudService.save(exchangeDTO);
    }

    public ExchangeDTO approve(int operationId) {

        ExchangeDTO exchange = exchangeCrudService.findById(operationId);

        if(!exchange.getStatus().equals(Status.PENDING.code()))
            throw new IllegalOperationException("Cannot approve this exchange");

        exchange.setStatus(Status.APPROVED.code());
        exchange.setUpdatedBy("Administrator");
        exchange.setUpdatedAt(LocalDateTime.now());

        return exchangeCrudService.save(exchange);
    }

    public ExchangeDTO reject(int operationId, String remarks) {

        ExchangeDTO exchange = exchangeCrudService.findById(operationId);

        if(!exchange.getStatus().equals(Status.PENDING.code()))
            throw new IllegalOperationException("Cannot reject this exchange");

        exchange.setStatus(Status.REJECTED.code());
        exchange.setFailureReason(remarks);
        exchange.setUpdatedBy("Administrator");
        exchange.setUpdatedAt(LocalDateTime.now());

        return exchangeCrudService.save(exchange);
    }

    public List<ExchangeDTO> findAll() {
        return exchangeCrudService.findAll();
    }

    public ExchangeDTO findById(int exchangeId) {
        return exchangeCrudService.findById(exchangeId);
    }

    public List<ExchangeDTO> findAllByCustomerIdentity(String customerIdentity) {
        return exchangeCrudService.findAllByCustomerIdentity(customerIdentity);
    }
}
