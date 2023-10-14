package com.dabel.oculusbank.service.core.account;

public interface MemberOnAccountOperator {

    void addJoint(String accountNumber, String identityNumber);

    void addAssociate(String accountNumber, String identityNumber);

    void removeJoint(String accountNumber, String identityNumber);

    void removeAssociate(String accountNumber, String identityNumber);
}
