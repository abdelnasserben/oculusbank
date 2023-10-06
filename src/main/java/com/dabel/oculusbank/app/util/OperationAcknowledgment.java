package com.dabel.oculusbank.app.util;

public interface OperationAcknowledgment<T> {
    T approve(int operationId);
    T reject(int operationId, String remarks);
}
