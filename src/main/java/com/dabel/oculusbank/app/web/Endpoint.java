package com.dabel.oculusbank.app.web;

public class Endpoint {

    private static final String APPROVE = "/approve";
    private static final String REJECT = "/reject";

    public interface Transactions {
        String ROOT = "/transactions";
        String INIT = ROOT + "/init";
        String APPROVE = ROOT + Endpoint.APPROVE;
        String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Payments {
        String ROOT = Transactions.ROOT + "/payments";
        String INIT = ROOT + "/init";
        String APPROVE = ROOT + Endpoint.APPROVE;
        String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Exchanges {
        String ROOT = Transactions.ROOT + "/exchanges";
        String INIT = ROOT + "/init";
        String APPROVE = ROOT + Endpoint.APPROVE;
        String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Customers {
        String ROOT = "/customers";
        String ADD = ROOT + "/add";
    }

    public interface Cards {
        String ROOT = "/cards";
        String APP_REQUEST = ROOT + "/application-requests";
        String APP_REQUEST_APPROVE = APP_REQUEST + "/approve";
        String APP_REQUEST_REJECT = APP_REQUEST + "/reject";
        String ACTIVATE = ROOT + "/activate";
        String DEACTIVATE = ROOT + "/deactivate";
    }

}
