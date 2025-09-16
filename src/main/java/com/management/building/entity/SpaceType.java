package com.management.building.entity;

import java.util.HashSet;
import java.util.Set;

import com.management.building.enums.SpaceFunction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SpaceType {

    @Id
    String name;

    @Column(length = 1000)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "function_type")
    @ToString.Exclude
    Set<SpaceFunction> functions;

    @Column(length = 500)
    String specifications;

    @Builder.Default
    boolean requiresSpecialAccess = false;

    @Min(value = 0)
    int maxCapacity;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Space> spaces;
}
