package com.management.building.entity;

import java.util.HashSet;
import java.util.Set;

import com.management.building.enums.SpaceStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    SpaceStatus status;

    @Min(value = 0)
    int capacity;

    @DecimalMin(value = "0.0", inclusive = false)
    double area;

    @DecimalMin(value = "0.0", inclusive = true)
    Double length;

    @DecimalMin(value = "0.0", inclusive = true)
    Double width;

    @DecimalMin(value = "0.0", inclusive = true)
    Double height;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_space_id")
    @ToString.Exclude
    Space parentSpace;

    @OneToMany(mappedBy = "parentSpace", cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    Set<Space> childSpaces = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_type_id")
    @NotNull(message = "Space type is required")
    @ToString.Exclude
    SpaceType type;
}
