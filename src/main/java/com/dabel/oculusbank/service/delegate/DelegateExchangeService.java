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


@Service
public class DelegateExchangeService implements OperationAcknowledgment<ExchangeDTO> {

    @Autowired
    ExchangeService exchangeService;

    public ExchangeDTO exchange(ExchangeDTO exchangeDTO) {

        String buyCurrency = exchangeDTO.getBuyCurrency();
        String saleCurrency = exchangeDTO.getSaleCurrency();

        if(buyCurrency.equalsIgnoreCase(saleCurrency))
            throw new IllegalOperationException("Currencies must be different");

        //TODO: convert amount and set status of exchange before saving
        double amount = CurrencyExchanger.exchange(buyCurrency, saleCurrency, exchangeDTO.getAmount());
        exchangeDTO.setAmount(amount);
        exchangeDTO.setStatus(Status.Pending.code());
        return exchangeService.save(exchangeDTO);
    }

    @Override
    public ExchangeDTO approve(int operationId) {

        ExchangeDTO exchange = exchangeService.findById(operationId);
        exchange.setStatus(Status.Approved.code());
        exchange.setUpdatedBy("Administrator");
        exchange.setUpdatedAt(LocalDateTime.now());

        return exchangeService.save(exchange);
    }

    @Override
    public ExchangeDTO reject(int operationId, String remarks) {
        ExchangeDTO exchange = exchangeService.findById(operationId);
        exchange.setStatus(Status.Rejected.code());
        exchange.setFailureReason(remarks);
        exchange.setUpdatedBy("Administrator");
        exchange.setUpdatedAt(LocalDateTime.now());

        return exchangeService.save(exchange);
    }
}
