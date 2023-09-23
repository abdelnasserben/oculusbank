package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.CurrencyExchanger;
import com.dabel.oculusbank.app.OperationAcknowledgment;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DelegateExchangeService implements OperationAcknowledgment<ExchangeDTO> {

    @Autowired
    ExchangeService exchangeService;

    public ExchangeDTO exchange(ExchangeDTO exchangeDTO) {

        String purchaseCurrency = exchangeDTO.getPurchaseCurrency();
        String saleCurrency = exchangeDTO.getSaleCurrency();

        if(purchaseCurrency.equalsIgnoreCase(saleCurrency))
            throw new IllegalOperationException("Currencies must be different");

        //TODO: convert amount and set status of exchange before saving
        double saleAmount = CurrencyExchanger.exchange(purchaseCurrency, saleCurrency, exchangeDTO.getPurchaseAmount());
        exchangeDTO.setSaleAmount(saleAmount);
        exchangeDTO.setStatus(Status.Pending.code());
        return exchangeService.save(exchangeDTO);
    }

    @Override
    public ExchangeDTO approve(int operationId) {

        ExchangeDTO exchange = exchangeService.findById(operationId);

        if(!exchange.getStatus().equals(Status.Pending.name()))
            throw new IllegalOperationException("Cannot approve this exchange");

        exchange.setStatus(Status.Approved.code());
        exchange.setUpdatedBy("Administrator");
        exchange.setUpdatedAt(LocalDateTime.now());

        return exchangeService.save(exchange);
    }

    @Override
    public ExchangeDTO reject(int operationId, String remarks) {

        ExchangeDTO exchange = exchangeService.findById(operationId);

        if(!exchange.getStatus().equals(Status.Pending.name()))
            throw new IllegalOperationException("Cannot reject this exchange");

        exchange.setStatus(Status.Rejected.code());
        exchange.setFailureReason(remarks);
        exchange.setUpdatedBy("Administrator");
        exchange.setUpdatedAt(LocalDateTime.now());

        return exchangeService.save(exchange);
    }

    public List<ExchangeDTO> findAll() {
        return exchangeService.findAll();
    }

    public ExchangeDTO findById(int exchangeId) {
        return exchangeService.findById(exchangeId);
    }

    public List<ExchangeDTO> findAllByCustomerIdentity(String customerIdentity) {
        return exchangeService.findAllByCustomerIdentity(customerIdentity);
    }
}
