package com.dabel.oculusbank.app.web;

public class Endpoint {

    private static final String APPROVE = "/approve";
    private static final String REJECT = "/reject";

    public interface Transactions {
        public String ROOT = "/transactions";
        public String INIT = ROOT + "/init";
        public String APPROVE = ROOT + Endpoint.APPROVE;
        public String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Payments {
        public String ROOT = Transactions.ROOT + "/payments";
        public String INIT = ROOT + "/init";
        public String APPROVE = ROOT + Endpoint.APPROVE;
        public String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Exchanges {
        public String ROOT = Transactions.ROOT + "/exchanges";
        public String INIT = ROOT + "/init";
        public String APPROVE = ROOT + Endpoint.APPROVE;
        public String REJECT = ROOT + Endpoint.REJECT;
    }

    public interface Customers {
        public String ROOT = "/customers";
        public String ADD = ROOT + "/add";
    }

    public interface Cards {
        public String ROOT = "/cards";
        public String APP_REQUEST = ROOT + "/application-requests";
        public String APP_REQUEST_APPROVE = APP_REQUEST + "/approve";
        public String APP_REQUEST_REJECT = APP_REQUEST + "/reject";
        public String ACTIVATE = ROOT + "/activate";
        public String DEACTIVATE = ROOT + "/deactivate";
    }

}
