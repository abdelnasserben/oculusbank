package com.dabel.oculusbank.app.config;

import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AdviceHandler {

    @ExceptionHandler(value = {
            AccountNotFoundException.class,
            BalanceInsufficientException.class,
            BranchNotFoundException.class,
            CardAppNotFoundException.class,
            CardNotFoundException.class,
            ChequeNotFountException.class,
            CustomerNotFoundException.class,
            IllegalOperationException.class,
            LoanNotFoundException.class,
            TransactionNotFoundException.class})
    public String accountNotFoundHandler(HttpServletRequest request, Exception ex, RedirectAttributes redirect) {


        /*
        String view = request.getRequestURI();
        List<String> infiniteRedirectionViews = List.of(
                View.Customer.DETAILS,
                View.Card.DETAILS,
                View.Card.APPLICATION_DETAILS,
                View.Exchange.DETAILS,
                View.Loan.DETAILS,
                View.Payment.DETAILS,
                View.Transaction.DETAILS);

        if(infiniteRedirectionViews.contains(view)) {
            //do something
        }*/


        return this.redirection(redirect, request.getRequestURI(), ex.getMessage());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public String dataIntegrityViolationHandler(HttpServletRequest request, Exception ex, RedirectAttributes redirect) {

        return this.redirection(redirect, request.getRequestURI(), ex.getMessage().split("(for key)")[0].trim() + "]");
    }

    private String redirection(RedirectAttributes redirect, String url, String message) {
        redirect.addFlashAttribute(MessageTag.ERROR, message);
        return "redirect:" + url;
    }
}
