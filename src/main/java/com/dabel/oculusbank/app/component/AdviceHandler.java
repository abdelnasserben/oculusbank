package com.dabel.oculusbank.app.component;

import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.exception.CustomerNotFoundException;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AdviceHandler {

    @ExceptionHandler(value = {CustomerNotFoundException.class, TransactionNotFoundException.class})
    public String defaultHandler(HttpServletRequest request, Exception ex) {

        return "redirect:/404";
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public String accountNotFoundHandler(HttpServletRequest request, Exception ex, RedirectAttributes redirect) {

        String url = request.getRequestURI();

        if(url.contains("/customers/addCard/")) {
            String[] params = url.split("/");
            url = "/customers/" + params[params.length - 1];
        }

        redirect.addFlashAttribute(MessageTag.ERROR, ex.getMessage());
        return "redirect:" + url;
    }
}
