package com.dabel.oculusbank.constant;

public enum Status {
    Pending(0), Active(1), Failed(2), Approved(3), Rejected(4);

    private final int code;
    Status(int code) {
        this.code = code;
    }

    public String code() {
        return String.valueOf(this.code);
    }

    public static String nameOf(String code) {
        int resolveCode = Integer.parseInt(code);
        for(Status status: values()) {
            if(status.code == resolveCode)
                return status.name();
        }
        return Pending.name();
    }

    public static String codeOf(String name) {
        for(Status status: values()) {
            if(status.name().equals(name))
                return status.code();
        }
        return Pending.code();
    }
}
