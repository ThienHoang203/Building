package com.management.building.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum SpaceStatus {
    AVAILABLE("Space is available for use", 100),
    OCCUPIED("Space is currently occupied", 200),
    RESERVED("Space is reserved", 300),
    BOOKED("Space is booked in advance", 400),
    MAINTENANCE("Space is under maintenance", 500),
    OUT_OF_ORDER("Space is out of order", 600),
    CLEANING("Space is being cleaned", 700),
    DISABLED("Space is permanently disabled", 800),
    BLOCKED("Space is temporarily blocked", 900),
    UNDER_CONSTRUCTION("Space is under construction", 1000);

    String description;
    int code;
}