package com.management.building.entity.space;

import java.util.LinkedHashSet;
import java.util.Set;

import com.management.building.enums.space.SpaceStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "space", indexes = {
                @Index(columnList = "parent_space_id, level, name"),
                @Index(columnList = "space_type_id, status, level"),
                @Index(columnList = "parent_space_id"),
                @Index(columnList = "name")
})
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
        Integer capacity;

        @DecimalMin(value = "0.0", inclusive = false)
        Double area;

        @DecimalMin(value = "0.0", inclusive = true)
        Double length;

        @DecimalMin(value = "0.0", inclusive = true)
        Double width;

        @DecimalMin(value = "0.0", inclusive = true)
        Double height;

        @Min(value = 0)
        @Column(name = "level", nullable = false)
        Integer level;

        @Column(name = "parent_space_id")
        Long parentSpaceId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_space_id", insertable = false, updatable = false)
        @ToString.Exclude
        Space parentSpace;

        @OneToMany(mappedBy = "parentSpace", cascade = {
                        CascadeType.PERSIST,
                        CascadeType.MERGE, }, fetch = FetchType.LAZY)
        @Builder.Default
        @ToString.Exclude
        Set<Space> childSpaces = new LinkedHashSet<>();

        @Column(name = "type_name", nullable = false)
        String typeName;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "type_name", insertable = false, updatable = false)
        @ToString.Exclude
        SpaceType type;
}
