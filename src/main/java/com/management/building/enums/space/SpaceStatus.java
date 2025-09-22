package com.management.building.enums.space;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceStatus {
    AVAILABLE,
    OCCUPIED,
    RESERVED,
    BOOKED,
    MAINTENANCE,
    OUT_OF_ORDER,
    CLEANING,
    DISABLED,
    BLOCKED,
    UNDER_CONSTRUCTION;
}