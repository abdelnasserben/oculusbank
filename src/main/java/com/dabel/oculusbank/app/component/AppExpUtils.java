package com.dabel.oculusbank.app.component;

import com.dabel.oculusbank.constant.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AppExpUtils {

    public String statusColor(String status) {

        if(status.equals(Status.PENDING.name()))
            return "warning";

        return List.of(Status.ACTIVE.name(), Status.APPROVED.name()).contains(status) ? "success" : "danger";
    }

    public Object[] countries() {
        return Arrays.stream(Country.values())
                .map(Country::getName)
                .toArray();
    }

    public Object[] currencies() {
        return Arrays.stream(Currency.values())
                .map(Enum::name)
                .toArray();
    }

    public Object[] cardTypes() {
        return Arrays.stream(CardType.values())
                .map(Enum::name)
                .toArray();
    }

    public Object[] loanTypes() {
        return Arrays.stream(LoanType.values())
                .map(Enum::name)
                .toArray();
    }

    public Object[] transactionTypes() {
        return Arrays.stream(TransactionType.values())
                .map(Enum::name)
                .toArray();
    }
}
