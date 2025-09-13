package com.management.building.entity;

import java.util.HashSet;
import java.util.Set;

import com.management.building.enums.SpaceFunction;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class SpaceType {

    @Id
    @NotBlank(message = "Space type name is required")
    @Column(nullable = false)
    String name;

    @Column(length = 1000)
    String description;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "function_type")
    @Builder.Default
    Set<SpaceFunction> functions = new HashSet<>();

    @Column(length = 500)
    String specifications;

    @Column(length = 200)
    String targetAudience;

    @Builder.Default
    boolean requiresSpecialAccess = false;

    @Min(value = 0, message = "Maximum capacity must be non-negative")
    int maxCapacity;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    Set<Space> spaces;
}
