package com.dabel.oculusbank.app.util;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.StatedObject;

import java.util.List;

public class StatedObjectFormatter {

    public static <T extends StatedObject> T format(T t) {
        t.setStatus(Status.nameOf(t.getStatus()));
        return t;
    }

    public static <T extends StatedObject>List<T> format(List<T> list) {
        return list.stream()
                .peek(t -> t.setStatus(Status.nameOf(t.getStatus())))
                .toList();
    }
}
