package com.management.building.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Space {

    @Id
    String id;
    int blockNumber;
    int floorNumber;
    int positionNumber;
    String status;
    double area;
    double length;
    double width;
    double height;

    @OneToOne
    SpaceType spaceType;
}
