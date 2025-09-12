package com.management.building.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String status;
    int blockNumber;
    int floorNumber;
    int positionNumber;
    int capacity;
    double area;
    double length;
    double width;
    double height;

    @OneToOne
    SpaceType type;
}
