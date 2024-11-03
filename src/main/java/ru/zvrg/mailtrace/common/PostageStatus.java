package ru.zvrg.mailtrace.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostageStatus {

    RETURNED("Возвращено"),
    IN_TRANSIT("В пути"),
    AWAITING_PICK_UP("Ожидание получения"),
    DELIVERED("Доставлено");

    private final String postageStatusDescription;
}
