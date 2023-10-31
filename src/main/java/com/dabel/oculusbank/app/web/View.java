package com.dabel.oculusbank.app.web;

public class View {

    public interface Branch {
        String ROOT = "branches";
    }

    public interface Card {
        String ROOT = "cards";
        String ADD = String.format("%s-add", ROOT);
        String DETAILS = String.format("%s-details", ROOT);
        String APPLICATION = String.format("%s-applications", ROOT);
        String APPLICATION_DETAILS = String.format("%s-details", APPLICATION);
    }

    public interface Cheque {
        String ROOT = "cheques";
    }

    public interface Common {
        String PAGE_404 = "404";
    }

    public interface Customer {
        String ROOT = "customers";
        String ADD = String.format("%s-add", ROOT);
        String DETAILS = String.format("%s-details", ROOT);
    }

    public interface Dashboard {
        String ROOT = "dashboard";
    }

    public interface Exchange {
        String ROOT = "exchanges";
        String INIT = String.format("%s-init", ROOT);
        String DETAILS = String.format("%s-details", ROOT);
    }

    public interface Loan {
        String ROOT = "loans";
        String DETAILS = String.format("%s-details", ROOT);
    }

    public interface Payment {
        String ROOT = "payments";
        String INIT = String.format("%s-init", ROOT);
        String DETAILS = String.format("%s-details", ROOT);
    }

    public interface Transaction {
        String ROOT = "transactions";
        String INIT = String.format("%s-init", ROOT);
        String DETAILS = String.format("%s-details", ROOT);
    }
}
