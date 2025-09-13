package com.management.building.entity;

import java.util.HashSet;
import java.util.Set;

import com.management.building.enums.SpaceStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Space name is required")
    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Status is required")
    SpaceStatus status;

    @Min(value = 0, message = "Position number must be non-negative")
    int positionNumber;

    @Min(value = 0, message = "Capacity must be non-negative")
    int capacity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Area must be positive")
    double area;

    @DecimalMin(value = "0.0", inclusive = true, message = "Length must be non-negative")
    Double length;

    @DecimalMin(value = "0.0", inclusive = true, message = "Width must be non-negative")
    Double width;

    @DecimalMin(value = "0.0", inclusive = true, message = "Height must be non-negative")
    Double height;

    @ManyToOne
    @JoinColumn(name = "parent_space_id")
    Space parentSpace;

    @OneToMany(mappedBy = "parentSpace", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @Builder.Default
    Set<Space> childSpaces = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "space_type_id")
    @NotNull(message = "Space type is required")
    SpaceType type;
}
