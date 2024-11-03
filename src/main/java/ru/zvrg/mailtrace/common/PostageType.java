package ru.zvrg.mailtrace.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostageType {

    LETTER("Письмо"),
    PACKAGE("Посылка"),
    PARCEL("Бандероль"),
    POSTCARD("Открытка");

    private final String postageTypeDescription;
}
