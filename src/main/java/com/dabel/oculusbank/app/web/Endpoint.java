package com.dabel.oculusbank.app.web;

public class Endpoint {

    private static final String APPROVE = "/approve";
    private static final String REJECT = "/reject";

    public interface Branch {
        String ROOT = "/branches";
    }

    public interface Card {
        String ROOT = "/cards";
        String APPLICATION = ROOT + "/application-requests";
        String APPLICATION_APPROVE = APPLICATION + "/approve";
        String APPLICATION_REJECT = APPLICATION + "/reject";
        String ACTIVATE = ROOT + "/activate";
        String DEACTIVATE = ROOT + "/deactivate";
    }

    public interface Cheque {
        String ROOT = "/cheques";
    }

    public interface Common {
        String PAGE_404 = "/404";
    }

    public interface Customer {
        String ROOT = "/customers";
        String ADD = ROOT + "/add";
    }

    public interface Exchange {
        String ROOT = Transaction.ROOT + "/exchanges";
        String INIT = ROOT + "/init";
        String APPROVE = ROOT + Endpoint.APPROVE;
        String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Loan {
        String ROOT = "/loans";
        String APPROVE = ROOT + Endpoint.APPROVE;
        String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Payment {
        String ROOT = Transaction.ROOT + "/payments";
        String INIT = ROOT + "/init";
        String APPROVE = ROOT + Endpoint.APPROVE;
        String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Transaction {
        String ROOT = "/transactions";
        String INIT = ROOT + "/init";
        String APPROVE = ROOT + Endpoint.APPROVE;
        String REJECT = ROOT + Endpoint.REJECT;
    }
}
