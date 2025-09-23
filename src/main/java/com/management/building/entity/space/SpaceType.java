package com.management.building.entity.space;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {
                @Index(name = "ID_level", columnList = "level"),
                @Index(name = "ID_name_level", columnList = "name, level"),
                @Index(name = "ID_level_name", columnList = "level, name"),
                @Index(name = "ID_name_parentName_level", columnList = "name, parent_name, level"),
                @Index(name = "ID_parentName_name_level", columnList = "parent_name, name, level"),
})
public class SpaceType {

        @Id
        @Column(length = 150)
        String name;

        @Column(nullable = false)
        Boolean requiresSpecialAccess;

        @Min(value = 0)
        @Column(name = "level", nullable = false)
        Integer level;

        @Column(length = 1000)
        String description;

        @Column(length = 500)
        String specifications;

        @Min(value = 0)
        Integer maxCapacity;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_name", nullable = true)
        SpaceType parent;

        @OneToMany(mappedBy = "parent", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
        Set<SpaceType> children;

        @OneToMany(mappedBy = "type", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
        Set<Space> spaces;
}
